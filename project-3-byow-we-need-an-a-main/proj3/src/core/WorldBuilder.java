package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// It builds the world grid, adds rooms, connects them, and prepares the world for rendering
public class WorldBuilder {
    private final int width;
    private final int height;
    private final int maxRoomQuantity;
    private final int roomMinSize;
    private final int roomMaxSize;

    private final TETile[][] world;
    private final List<Room> rooms;
    private final Random rand;

    private final Hallway hallwaySystem;
    private final Wall wallSystem;

    private final WorldConfig config; // keep reference to config

    // Constructor that uses WorldConfig
    public WorldBuilder(WorldConfig config, long seed) {
        this.config = config;
        this.width = config.width;
        this.height = config.height;
        this.maxRoomQuantity = config.maxRoomQuantity;
        this.roomMinSize = config.roomMinSize;
        this.roomMaxSize = config.roomMaxSize;

        this.world = new TETile[width][height];
        this.rooms = new ArrayList<>();
        this.rand = new Random(seed);

        // Pass hallway tile from config
        this.hallwaySystem = new Hallway(world, width, height, seed, config.hallwayFloorTile);
        this.wallSystem = new Wall(world, width, height);

        fillWithTiles();
    }

    // Fill the entire world with nothing tiles
    public void fillWithTiles() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }
    }

    // Generate random non-overlapping rooms
    public void roomGenerator() {
        int attempts = 0;
        int maxAttempts = maxRoomQuantity * 10;

        while (rooms.size() < maxRoomQuantity && attempts < maxAttempts) {
            int randWidth = rand.nextInt(roomMaxSize - roomMinSize + 1) + roomMinSize;
            int randHeight = rand.nextInt(roomMaxSize - roomMinSize + 1) + roomMinSize;
            int margin = 1;

            int randX = rand.nextInt(width - randWidth - 2 * margin) + margin;
            int randY = rand.nextInt(height - randHeight - 2 * margin) + margin;

            // Pass room floor tile from config
            Room newRoom = new Room(randX, randY, randHeight, randWidth, config.roomFloorTile);

            if (!roomOverlaps(newRoom)) {
                rooms.add(newRoom);
                newRoom.drawRoom(world);
            }

            attempts++;
        }
    }

    // Check if a room overlaps any existing room
    private boolean roomOverlaps(Room room) {
        for (Room r : rooms) {
            if (room.overlaps(r)) {
                return true;
            }
        }
        return false;
    }

    // Connect all rooms using the hallway system
    public void connectRoomsWithHallways() {
        if (rooms.size() > 1) {
            hallwaySystem.connectAllRooms(rooms);

            if (!hallwaySystem.validateConnectivity(rooms)) {
                System.err.println("Warning: World connectivity validation failed!");
            }
        }
    }

    // Add walls around rooms and hallways
    public void addWallsToWorld() {
        wallSystem.addWallsAroundRooms(rooms);
        wallSystem.addWallsAroundHallways(hallwaySystem.getHallwayPositions());
        wallSystem.addWallsAroundAllFloorTiles();
    }

    // Return the generated world
    public TETile[][] getWorld() {
        return world;
    }

    // Return the list of generated rooms
    public List<Room> getRooms() {
        return new ArrayList<>(rooms);
    }

}
