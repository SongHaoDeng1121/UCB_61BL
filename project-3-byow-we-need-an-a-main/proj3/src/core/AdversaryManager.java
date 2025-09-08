package core;

import tileengine.TETile;
import java.util.*;

// Manages adversaries: spawns them and updates their behavior every game tick
public class AdversaryManager {
    private final GameController controller;
    private final List<Adversary> adversaries;
    private final Random random;

    public AdversaryManager(GameController controller) {
        this.controller = controller;
        this.random = new Random();
        this.adversaries = new ArrayList<>();

        // If loading from save, restore positions; otherwise spawn fresh
        List<Position> savedPositions = controller.getRestoredAdversaryPositions();
        if (savedPositions != null && !savedPositions.isEmpty()) {
            setAdversaryPositions(savedPositions);
        } else {
            spawnInitialAdversaries();
        }
    }

    // Spawns a few adversaries at random valid positions
    private void spawnInitialAdversaries() {
        int count = 3;
        for (int i = 0; i < count; i++) {
            Position spawn = getRandomValidSpawnLocation();
            if (spawn != null) {
                adversaries.add(new Adversary(spawn, controller));
            }
        }
    }

    // Finds a valid floor tile that is not on HUD row, player, or another adversary
    private Position getRandomValidSpawnLocation() {
        TETile[][] world = controller.getGameWorld();
        int width = world.length;
        int height = world[0].length;
        Position playerPos = controller.getPlayerPosition();

        for (int attempts = 0; attempts < 1000; attempts++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height - 1); // exclude HUD row

            if (isWalkable(world[x][y]) && !isOccupied(x, y, playerPos)) {
                return new Position(x, y);
            }
        }
        return null;
    }

    private boolean isWalkable(TETile t) {
        WorldConfig cfg = controller.getConfig();
        return t == cfg.roomFloorTile || t == cfg.hallwayFloorTile;
    }

    // Checks if the position is occupied by player or any adversary
    private boolean isOccupied(int x, int y, Position playerPos) {
        if (x == playerPos.x && y == playerPos.y) {
            return true;
        }
        for (Adversary a : adversaries) {
            Position pos = a.getPosition();
            if (pos.x == x && pos.y == y) {
                return true;
            }
        }
        return false;
    }

    // Updates all adversaries each tick (safe removal during iteration)
    public void update() {
        for (Adversary a : new ArrayList<>(adversaries)) {
            a.takeTurn(); // may call controller.removeAdversary(this)
        }
    }

    // Returns list of all adversary positions for saving
    public List<Position> getAdversaryPositions() {
        List<Position> positions = new ArrayList<>();
        for (Adversary a : adversaries) {
            positions.add(a.getPosition());
        }
        return positions;
    }

    // ===== Loading helpers =====

    // Used when loading from saved state
    public void setAdversaryPositions(List<Position> positions) {
        adversaries.clear();
        TETile[][] world = controller.getGameWorld();
        int width = world.length;
        int height = world[0].length;

        for (Position pos : positions) {
            if (pos.x >= 0 && pos.x < width && pos.y >= 0 && pos.y < height) {
                if (isWalkable(world[pos.x][pos.y])) {
                    adversaries.add(new Adversary(pos, controller));
                }
            }
        }
    }

    // Removes an adversary from the game
    public void removeAdversary(Adversary adv) {
        adversaries.remove(adv);
    }
}