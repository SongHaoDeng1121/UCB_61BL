package core;

import tileengine.TETile;

import utils.RandomUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KeyManager {
    private static final int TOTAL_KEYS = 3;
    private List<Room> keyRooms;
    private int keysCollected;
    private Random random;

    public KeyManager(Random random) {
        this.random = random;
        this.keyRooms = new ArrayList<>();
        this.keysCollected = 0;
    }

    // Constructor used when loading a saved game.
    // Takes saved key rooms and the number of keys collected.
    public KeyManager(List<Room> savedRooms, int savedCount) {
        this.keyRooms = new ArrayList<>(savedRooms);
        this.keysCollected = savedCount;
        this.random = new Random(); // not used after loading
    }

    // generate key in random room
    public void generateKeys(List<Room> rooms) {
        if (rooms.size() < TOTAL_KEYS) {
            throw new IllegalArgumentException("房间数量必须至少为 " + TOTAL_KEYS + " 个");
        }

        keyRooms.clear();
        keysCollected = 0;

        // choose three rooms to place key
        List<Room> availableRooms = new ArrayList<>(rooms);
        for (int i = 0; i < TOTAL_KEYS; i++) {
            int randomIndex = RandomUtils.uniform(random, availableRooms.size());
            Room selectedRoom = availableRooms.remove(randomIndex);
            selectedRoom.placeKey();
            keyRooms.add(selectedRoom);
        }
    }

    // Check whteher the player collects the keys
    public boolean checkKeyCollection(Position playerPos, TETile[][] world) {

        for (int i = 0; i < keyRooms.size(); i++) {
            Room room = keyRooms.get(i);

            if (room.hasKeyAt(playerPos)) {
                // Collecting the key
                boolean removed = room.removeKey();
                if (removed) {
                    keysCollected++;
                    return true;
                }
            }
        }

        return false;
    }

    //Check whether all the keys are collected
    public boolean isGameComplete() {
        return keysCollected >= TOTAL_KEYS;
    }

    // Get the keys have been collected
    public int getKeysCollected() {
        return keysCollected;
    }

    // Get the total counts of the key
    public int getTotalKeys() {
        return TOTAL_KEYS;
    }

    // Reset the keys
    public void reset() {
        for (Room room : keyRooms) {
            room.removeKey();
        }
        keyRooms.clear();
        keysCollected = 0;
    }

   // Get all the keys with the
    public List<Room> getKeyRooms() {
        return new ArrayList<>(keyRooms);
    }

    // Game Process
    public String getProgressString() {
        return "Collecting Process " + keysCollected + "/" + TOTAL_KEYS;
    }

    // Returns a list of all key positions that are currently active (not yet collected).
    public List<Position> getKeyPositions() {
        List<Position> positions = new ArrayList<>();
        for (Room room : keyRooms) {
            if (room.hasKey()) {
                positions.add(room.getKeyPosition());
            }
        }
        return positions;
    }

    // Replaces all keyRooms with keys placed at the provided positions
    public void placeKeysAtPositions(List<Position> positions) {
        keyRooms.clear();
        keysCollected = 0;

        for (Position pos : positions) {
            Room room = new Room(pos.x, pos.y, 1, 1); // fake 1x1 room
            room.placeKey();
            keyRooms.add(room);
        }
    }

    public void setKeysCollected(int count) {
        this.keysCollected = count;
    }
}