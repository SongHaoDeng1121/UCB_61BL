
package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.util.*;

// Implements A* pathfinding for adversary movement
public class AStarPathFinder {
    // Internal class representing a node in the A* search
    private static class Node {
        Position pos;
        Node parent;
        int gCost; // Cost from start
        int fCost; // Total cost: g + h

        Node(Position pos, Node parent, int gCost, int fCost) {
            this.pos = pos;
            this.parent = parent;
            this.gCost = gCost;
            this.fCost = fCost;
        }
    }

    // Finds a path from start to goal using A* and returns a list of positions
    public static List<Position> findPath(TETile[][] world, Position start, Position goal) {
        PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingInt(n -> n.fCost));
        Map<Position, Node> openMap = new HashMap<>();
        Set<Position> closed = new HashSet<>();

        Node startNode = new Node(start, null, 0, heuristic(start, goal));
        open.add(startNode);
        openMap.put(start, startNode);

        while (!open.isEmpty()) {
            Node current = open.poll();
            openMap.remove(current.pos);

            if (current.pos.equals(goal)) {
                return reconstructPath(current);
            }

            closed.add(current.pos);

            for (Position neighbor : getNeighbors(world, current.pos)) {
                if (closed.contains(neighbor)) {
                    continue;
                }

                int tentativeG = current.gCost + 1;
                int tentativeF = tentativeG + heuristic(neighbor, goal);

                Node neighborNode = openMap.get(neighbor);
                if (neighborNode == null) {
                    neighborNode = new Node(neighbor, current, tentativeG, tentativeF);
                    open.add(neighborNode);
                    openMap.put(neighbor, neighborNode);
                } else if (tentativeG < neighborNode.gCost) {
                    open.remove(neighborNode);
                    neighborNode.parent = current;
                    neighborNode.gCost = tentativeG;
                    neighborNode.fCost = tentativeF;
                    open.add(neighborNode);
                }
            }
        }

        return new ArrayList<>(); // No path found
    }

    // Reconstructs the full path from the goal node to the start
    private static List<Position> reconstructPath(Node node) {
        List<Position> path = new ArrayList<>();
        while (node != null) {
            path.add(0, node.pos);
            node = node.parent;
        }
        return path;
    }

    // Manhattan distance heuristic
    private static int heuristic(Position a, Position b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    // Returns valid neighboring positions that can be walked through
    private static List<Position> getNeighbors(TETile[][] world, Position p) {
        List<Position> neighbors = new ArrayList<>();
        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] d : dirs) {
            int nx = p.x + d[0];
            int ny = p.y + d[1];
            if (inBounds(world, nx, ny) && isPassable(world[nx][ny])) {
                neighbors.add(new Position(nx, ny));
            }
        }
        return neighbors;
    }

    // Determines which tiles are passable by adversaries
    private static boolean isPassable(TETile tile) {
        return tile == Tileset.FLOOR ||
                tile == Tileset.GRASS ||
                tile == Tileset.AVATAR;
    }

    // Checks whether a position is within the world bounds
    private static boolean inBounds(TETile[][] world, int x, int y) {
        return x >= 0 && x < world.length && y >= 0 && y < world[0].length;
    }
}