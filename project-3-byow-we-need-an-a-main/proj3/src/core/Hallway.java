package core;

import tileengine.TETile;
import tileengine.Tileset;
import java.util.*;

public class Hallway {
    // Tile used for hallway floors (injected via constructor)
    private final TETile hallwayTile;

    // Reference to the world grid for hallway placement
    private TETile[][] world;
    private int width;
    private int height;
    private Random rand;

    // Track all hallway positions for external access
    private Set<Position> hallwayPositions;

    // Track entrance points for each room (center points of each side)
    private Map<Room, List<Position>> roomSideEntrances;
    private Map<Room, Position> selectedEntrances;

    public Hallway(TETile[][] world, int width, int height, long seed, TETile hallwayTile) {
        this.world = world;
        this.width = width;
        this.height = height;
        this.rand = new Random(seed);
        this.hallwayPositions = new HashSet<>();
        this.roomSideEntrances = new HashMap<>();
        this.selectedEntrances = new HashMap<>();
        this.hallwayTile = hallwayTile; // <- from WorldConfig
    }

    public void connectAllRooms(List<Room> rooms) {
        if (rooms.size() < 2) return;

        // Clear any existing data from previous generations
        hallwayPositions.clear();
        roomSideEntrances.clear();
        selectedEntrances.clear();
        // Calculate center points of each room's sides
        calculateRoomSideEntrances(rooms);
        // Use minimum spanning tree to connect rooms optimally
        connectRoomsOptimally(rooms);
    }

    private void calculateRoomSideEntrances(List<Room> rooms) {
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            List<Position> entrances = new ArrayList<>();

            // Top side center (above the room)
            int topCenterX = room.x + room.width / 2;
            int topCenterY = room.y + room.height;
            entrances.add(new Position(topCenterX, topCenterY));
            // Bottom side center (below the room)
            int bottomCenterX = room.x + room.width / 2;
            int bottomCenterY = room.y - 1;
            entrances.add(new Position(bottomCenterX, bottomCenterY));
            // Left side center (left of the room)
            int leftCenterX = room.x - 1;
            int leftCenterY = room.y + room.height / 2;
            entrances.add(new Position(leftCenterX, leftCenterY));
            // Right side center (right of the room)
            int rightCenterX = room.x + room.width;
            int rightCenterY = room.y + room.height / 2;
            entrances.add(new Position(rightCenterX, rightCenterY));
            // Filter valid positions
            List<Position> validEntrances = new ArrayList<>();
            for (Position pos : entrances) {
                boolean isValid = isValidHallwayPosition(pos.x, pos.y);
                boolean isInsideRoom = isInsideAnyRoom(pos.x, pos.y);
                if (isValid && !isInsideRoom) validEntrances.add(pos);
            }
            roomSideEntrances.put(room, validEntrances);
        }
    }

    private void connectRoomsOptimally(List<Room> rooms) {
        Set<Room> connected = new HashSet<>();
        Set<Room> unconnected = new HashSet<>(rooms);

        Room startRoom = rooms.get(rand.nextInt(rooms.size()));
        connected.add(startRoom);
        unconnected.remove(startRoom);

        while (!unconnected.isEmpty()) {
            Connection bestConnection = findBestConnection(connected, unconnected);
            if (bestConnection != null) {
                createOptimalHallway(bestConnection.from, bestConnection.to);
                selectedEntrances.put(bestConnection.fromRoom, bestConnection.from);
                selectedEntrances.put(bestConnection.toRoom, bestConnection.to);
                connected.add(bestConnection.toRoom);
                unconnected.remove(bestConnection.toRoom);
            } else {
                forceConnectRemainingRooms(connected, unconnected);
                break;
            }
        }
        ensureAllRoomsHaveEntrances(rooms);
    }

    private Connection findBestConnection(Set<Room> connected, Set<Room> unconnected) {
        Connection bestConnection = null;
        int shortestDistance = Integer.MAX_VALUE;

        for (Room connectedRoom : connected) {
            for (Room unconnectedRoom : unconnected) {
                List<Position> a = roomSideEntrances.get(connectedRoom);
                List<Position> b = roomSideEntrances.get(unconnectedRoom);
                if (a.isEmpty() || b.isEmpty()) continue;

                for (Position fromPos : a) {
                    for (Position toPos : b) {
                        if (!isValidHallwayPosition(fromPos.x, fromPos.y) ||
                                !isValidHallwayPosition(toPos.x, toPos.y)) {
                            continue;
                        }
                        int dist = fromPos.distanceTo(toPos);
                        if (dist < shortestDistance) {
                            shortestDistance = dist;
                            bestConnection = new Connection(connectedRoom, unconnectedRoom, fromPos, toPos, dist);
                        }
                    }
                }
            }
        }
        return bestConnection;
    }

    private void forceConnectRemainingRooms(Set<Room> connected, Set<Room> unconnected) {
        for (Room unconnectedRoom : new HashSet<>(unconnected)) {
            Room closest = null;
            int best = Integer.MAX_VALUE;

            Position uCenter = unconnectedRoom.getCenter();
            for (Room c : connected) {
                int d = c.getCenter().distanceTo(uCenter);
                if (d < best) { best = d; closest = c; }
            }
            if (closest != null) {
                createOptimalHallway(closest.getCenter(), uCenter);
                selectedEntrances.put(closest, closest.getCenter());
                selectedEntrances.put(unconnectedRoom, uCenter);
                connected.add(unconnectedRoom);
                unconnected.remove(unconnectedRoom);
            }
        }
    }

    private void ensureAllRoomsHaveEntrances(List<Room> rooms) {
        for (Room room : rooms) {
            if (!selectedEntrances.containsKey(room)) {
                List<Position> available = roomSideEntrances.get(room);
                if (!available.isEmpty()) {
                    Position entrance = available.get(0);
                    selectedEntrances.put(room, entrance);
                    if (isValidHallwayPosition(entrance.x, entrance.y)) {
                        world[entrance.x][entrance.y] = hallwayTile;
                        hallwayPositions.add(entrance);
                    }
                } else {
                    selectedEntrances.put(room, room.getCenter());
                }
            }
        }
    }

    private void createOptimalHallway(Position start, Position end) {
        if (isValidHallwayPosition(start.x, start.y)) {
            world[start.x][start.y] = hallwayTile;
            hallwayPositions.add(start);
        }
        if (isValidHallwayPosition(end.x, end.y)) {
            world[end.x][end.y] = hallwayTile;
            hallwayPositions.add(end);
        }

        if (canCreateStraightLine(start, end)) {
            createStraightLine(start, end);
        } else {
            boolean horizontalFirst = shouldGoHorizontalFirst(start, end);
            if (horizontalFirst) {
                createHorizontalSegment(start.x, end.x, start.y);
                createVerticalSegment(end.x, start.y, end.y);
            } else {
                createVerticalSegment(start.x, start.y, end.y);
                createHorizontalSegment(start.x, end.x, end.y);
            }
        }
    }

    private boolean canCreateStraightLine(Position start, Position end) {
        if (start.y == end.y) {
            int minX = Math.min(start.x, end.x);
            int maxX = Math.max(start.x, end.x);
            for (int x = minX; x <= maxX; x++) {
                if (!isValidHallwayPosition(x, start.y) ||
                        (isInsideAnyRoom(x, start.y) && x != start.x && x != end.x)) {
                    return false;
                }
            }
            return true;
        }
        if (start.x == end.x) {
            int minY = Math.min(start.y, end.y);
            int maxY = Math.max(start.y, end.y);
            for (int y = minY; y <= maxY; y++) {
                if (!isValidHallwayPosition(start.x, y) ||
                        (isInsideAnyRoom(start.x, y) && y != start.y && y != end.y)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private void createStraightLine(Position start, Position end) {
        if (start.y == end.y) {
            createHorizontalSegment(start.x, end.x, start.y);
        } else if (start.x == end.x) {
            createVerticalSegment(start.x, start.y, end.y);
        }
    }

    private boolean shouldGoHorizontalFirst(Position start, Position end) {
        int hd = Math.abs(end.x - start.x);
        int vd = Math.abs(end.y - start.y);
        if (hd > vd * 2) return true;
        if (vd > hd * 2) return false;
        return rand.nextBoolean();
    }

    private void createHorizontalSegment(int x1, int x2, int y) {
        int startX = Math.min(x1, x2);
        int endX = Math.max(x1, x2);
        for (int x = startX; x <= endX; x++) {
            if (isValidHallwayPosition(x, y) && !isInsideAnyRoom(x, y)) {
                world[x][y] = hallwayTile;
                hallwayPositions.add(new Position(x, y));
            }
        }
    }

    private void createVerticalSegment(int x, int y1, int y2) {
        int startY = Math.min(y1, y2);
        int endY = Math.max(y1, y2);
        for (int y = startY; y <= endY; y++) {
            if (isValidHallwayPosition(x, y) && !isInsideAnyRoom(x, y)) {
                world[x][y] = hallwayTile;
                hallwayPositions.add(new Position(x, y));
            }
        }
    }

    private void removeDeadEndHallways() {
        boolean foundDeadEnd = true;
        int iterations = 0;
        int maxIterations = 10;
        while (foundDeadEnd && iterations < maxIterations) {
            foundDeadEnd = false;
            Set<Position> toRemove = new HashSet<>();
            for (Position pos : hallwayPositions) {
                if (isDeadEnd(pos) && !isRoomEntrance(pos)) {
                    toRemove.add(pos);
                    foundDeadEnd = true;
                }
            }
            for (Position pos : toRemove) {
                world[pos.x][pos.y] = Tileset.NOTHING;
                hallwayPositions.remove(pos);
            }
            iterations++;
        }
    }

    private boolean isDeadEnd(Position pos) {
        int connections = 0;
        int[] dx = {0, 1, 0, -1};
        int[] dy = {1, 0, -1, 0};
        for (int i = 0; i < 4; i++) {
            Position adjacent = new Position(pos.x + dx[i], pos.y + dy[i]);
            if (hallwayPositions.contains(adjacent) || isRoomEntrance(adjacent)) {
                connections++;
            }
        }
        return connections <= 1;
    }

    private boolean isRoomEntrance(Position pos) {
        return selectedEntrances.containsValue(pos);
    }

    private boolean isInsideAnyRoom(int x, int y) {
        for (Room room : roomSideEntrances.keySet()) {
            if (x >= room.x && x < room.x + room.width &&
                    y >= room.y && y < room.y + room.height) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidHallwayPosition(int x, int y) {
        return x > 0 && x < width - 1 && y > 0 && y < height - 1;
    }

    public Set<Position> getHallwayPositions() {
        return new HashSet<>(hallwayPositions);
    }

    public Map<Room, Position> getSelectedEntrances() {
        return new HashMap<>(selectedEntrances);
    }

    public boolean validateConnectivity(List<Room> rooms) {
        if (rooms.isEmpty() || rooms.size() < 2) return true;

        Room startRoom = rooms.get(0);
        Position startPos = startRoom.getCenter();

        Set<Position> visited = new HashSet<>();
        Queue<Position> queue = new LinkedList<>();
        queue.offer(startPos);
        visited.add(startPos);

        while (!queue.isEmpty()) {
            Position current = queue.poll();
            int[] dx = {0, 1, 0, -1};
            int[] dy = {1, 0, -1, 0};

            for (int i = 0; i < 4; i++) {
                int newX = current.x + dx[i];
                int newY = current.y + dy[i];
                Position neighbor = new Position(newX, newY);

                if (visited.contains(neighbor) ||
                        newX < 0 || newX >= width || newY < 0 || newY >= height) {
                    continue;
                }

                // Walkable if it's a floor (room/hall) — keep your existing rule:
                TETile tile = world[newX][newY];
                boolean isWalkable = (tile == Tileset.GRASS || tile == Tileset.FLOOR);

                if (isWalkable) {
                    visited.add(neighbor);
                    queue.offer(neighbor);
                }
            }
        }

        int reachableRooms = 0;
        for (Room room : rooms) {
            Position roomCenter = room.getCenter();
            boolean canReachRoom = visited.contains(roomCenter);
            if (!canReachRoom) {
                for (int dx = 0; dx < room.width; dx++) {
                    for (int dy = 0; dy < room.height; dy++) {
                        Position roomPos = new Position(room.x + dx, room.y + dy);
                        if (visited.contains(roomPos)) {
                            canReachRoom = true;
                            break;
                        }
                    }
                    if (canReachRoom) break;
                }
            }
            if (canReachRoom) reachableRooms++;
        }
        return reachableRooms == rooms.size();
    }

    private static class Connection {
        final Room fromRoom;
        final Room toRoom;
        final Position from;
        final Position to;
        final int distance;

        Connection(Room fromRoom, Room toRoom, Position from, Position to, int distance) {
            this.fromRoom = fromRoom;
            this.toRoom = toRoom;
            this.from = from;
            this.to = to;
            this.distance = distance;
        }
    }
}