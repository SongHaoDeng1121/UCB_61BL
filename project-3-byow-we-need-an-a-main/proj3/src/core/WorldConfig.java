package core;

import tileengine.TETile;

public class WorldConfig {
    public final int width;
    public final int height;
    public final int maxRoomQuantity;
    public final int roomMinSize;
    public final int roomMaxSize;

    // New: configurable tile types
    public final TETile roomFloorTile;
    public final TETile hallwayFloorTile;

    public WorldConfig(int width, int height, int maxRoomQuantity, int roomMinSize, int roomMaxSize,
                       TETile roomFloorTile, TETile hallwayFloorTile) {
        this.width = width;
        this.height = height;
        this.maxRoomQuantity = maxRoomQuantity;
        this.roomMinSize = roomMinSize;
        this.roomMaxSize = roomMaxSize;
        this.roomFloorTile = roomFloorTile;
        this.hallwayFloorTile = hallwayFloorTile;
    }
}