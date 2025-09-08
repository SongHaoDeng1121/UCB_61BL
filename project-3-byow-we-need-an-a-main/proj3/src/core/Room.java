package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.io.Serializable;

// It defines the room shape and behavior
public class Room implements Serializable {
    // coordinates
    public int x;
    public int y;
    // room height and width
    public int width;
    public int height;

    // Floor tile for this room (configurable)
    private final TETile roomTile;

    private static final int TILE_BUFFER = 2;
    private boolean hasKey = false;
    private Position keyPosition = null;
    private static final TETile KEY_TILE = Tileset.LOCKED_DOOR;


    public Room(int x, int y, int height, int width) {
        this(x, y, height, width, Tileset.GRASS);
    }


    public Room(int x, int y, int height, int width, TETile roomTile) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.roomTile = roomTile;
    }

    // Draw the room into the world using roomTile
    public void drawRoom(TETile[][] world) {
        // the floor
        for (int dx = 0; dx < width; dx++) {
            for (int dy = 0; dy < height; dy++) {
                world[x + dx][y + dy] = roomTile;
            }
        }

        // the key
        if (hasKey && keyPosition != null) {
            world[keyPosition.x][keyPosition.y] = KEY_TILE;
        }
    }

    // helper method to get the center coords of the room
    public Position getCenter() {
        return new Position(x + width / 2, y + height / 2);
    }

    public boolean overlaps(Room other) {
        // this room is entirely to the right of the other room with TILE_BUFFER in between
        if (this.x - TILE_BUFFER >= other.x + other.width + TILE_BUFFER) {
            return false;
        }
        // this room is entirely to the left of the other room with TILE_BUFFER in between
        if (this.x + this.width + TILE_BUFFER <= other.x - TILE_BUFFER) {
            return false;
        }
        // this room is entirely above the other room with TILE_BUFFER in between
        if (this.y - TILE_BUFFER >= other.y + other.height + TILE_BUFFER) {
            return false;
        }
        // this room is entirely below the other room with TILE_BUFFER in between
        if (this.y + this.height + TILE_BUFFER <= other.y - TILE_BUFFER) {
            return false;
        }
        // Otherwise the other room must overlap. It has less than TILE_BUFFER in between
        return true;
    }

    //Key logic
    public void placeKey() {
        this.hasKey = true;
        this.keyPosition = getCenter();
    }

    public boolean hasKeyAt(Position pos) {
        return hasKey && keyPosition != null && keyPosition.equals(pos);
    }

    public boolean removeKey() {
        if (hasKey) {
            hasKey = false;
            keyPosition = null;
            return true;
        }
        return false;
    }

    public boolean hasKey() {
        return hasKey;
    }

    public Position getKeyPosition() {
        return keyPosition;
    }

    // Store the Key state
    public String getKeyState() {
        if (hasKey && keyPosition != null) {
            return keyPosition.x + "," + keyPosition.y;
        }
        return "null";
    }

}