package core;

import tileengine.TETile;
import tileengine.Tileset;
import java.util.*;

public class Wall {
    // Tile type used for all walls in the world
    private static final TETile WALL_TILE = Tileset.WALL;

    // Reference to the world grid for wall placement
    private TETile[][] world;
    private int width;
    private int height;

    // Track all wall positions for analysis and debugging
    private Set<Position> wallPositions;

    public Wall(TETile[][] world, int width, int height) {
        this.world = world;
        this.width = width;
        this.height = height;
        this.wallPositions = new HashSet<>();
    }

    public void addWallsAroundRooms(List<Room> rooms) {
        for (Room room : rooms) {
            addWallsAroundSingleRoom(room);
        }
    }

    private void addWallsAroundSingleRoom(Room room) {
        // Create walls around the entire room perimeter including corners
        for (int dx = -1; dx <= room.width; dx++) {
            for (int dy = -1; dy <= room.height; dy++) {
                int x = room.x + dx;
                int y = room.y + dy;

                // Skip positions that are inside the room itself
                if (dx >= 0 && dx < room.width && dy >= 0 && dy < room.height) {
                    continue;
                }

                // Place wall if position is valid and currently empty
                // Don't place walls on hallway tiles (preserves entrances)
                if (isValidPosition(x, y) && world[x][y] == Tileset.NOTHING) {
                    placeWall(x, y);
                }
            }
        }
    }

    public void addWallsAroundHallways(Set<Position> hallwayPositions) {
        // Process each hallway position to add surrounding walls
        for (Position hallwayPos : hallwayPositions) {
            addWallsAroundPosition(hallwayPos.x, hallwayPos.y);
        }
    }

    private void addWallsAroundPosition(int centerX, int centerY) {
        // Check all 8 surrounding positions (including diagonals for complete enclosure)
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int x = centerX + dx;
                int y = centerY + dy;

                // Skip the center position itself
                if (dx == 0 && dy == 0) {
                    continue;
                }

                // Place wall if position is valid and currently empty
                if (isValidPosition(x, y) && world[x][y] == Tileset.NOTHING) {
                    placeWall(x, y);
                }
            }
        }
    }

    public void addWallsAroundAllFloorTiles() {
        // First pass: identify all floor tile positions
        Set<Position> floorPositions = new HashSet<>();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (isFloorTile(x, y)) {
                    floorPositions.add(new Position(x, y));
                }
            }
        }

        // Second pass: add walls around each floor tile
        for (Position floorPos : floorPositions) {
            addWallsAroundPosition(floorPos.x, floorPos.y);
        }
    }

    private void placeWall(int x, int y) {
        world[x][y] = WALL_TILE;
        wallPositions.add(new Position(x, y));
    }

    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    private boolean isFloorTile(int x, int y) {
        return world[x][y] == Tileset.GRASS || world[x][y] == Tileset.FLOOR;
    }

}