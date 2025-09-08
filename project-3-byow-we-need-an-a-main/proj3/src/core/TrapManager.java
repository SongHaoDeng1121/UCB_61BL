package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Manages all traps in the game world.
 * Finalized to work with GameController.loadGameState calling:
 * trapManager.setTrapPositions(state.trapPositions, world, width, height);
 */
public class TrapManager {
    private List<TrapItem> traps;
    private Random random;

    // Trap generation constants
    private static final int TRAPS_PER_ROOM = 2;
    private static final double HEALTH_POTION_CHANCE = 0.15; // 15%
    private static final double ARROW_CHANCE = 0.4;          // 40%
    private static final double BOMB_CHANCE = 0.3;           // 30%
    private static final double FIRE_CHANCE = 0.15;          // 15%

    public TrapManager(long seed) {
        this.traps = new ArrayList<>();
        this.random = new Random(seed);
    }

    /** Optional: swap RNG if you ever need deterministic replay after load */
    public void setRandom(Random random) {
        this.random = random;
    }

    /** Generates traps in the given rooms (skips key rooms). */
    public void generateTraps(List<Room> rooms, TETile[][] world, int width, int height) {
        if (rooms == null || rooms.isEmpty()) return;

        int trapsGenerated = 0;

        for (Room room : rooms) {
            if (room.hasKey()) continue;

            for (int i = 0; i < TRAPS_PER_ROOM; i++) {
                Position pos = findValidTrapPosition(room, world, width, height);
                if (pos != null) {
                    TrapItem trap = createRandomTrap(pos);
                    if (trap != null) {
                        traps.add(trap);
                        trap.placeInWorld(world, width, height);
                        trapsGenerated++;
                    }
                }
            }
        }

        System.out.println("Total traps generated: " + trapsGenerated);
    }

    /** Finds a valid floor/grass position inside room and not too close to other traps. */
    private Position findValidTrapPosition(Room room, TETile[][] world, int width, int height) {
        int attempts = 0, maxAttempts = 50;

        while (attempts < maxAttempts) {
            int x = random.nextInt(Math.max(1, room.width - 2)) + room.x + 1; // avoid edges
            int y = random.nextInt(Math.max(1, room.height - 2)) + room.y + 1;
            Position pos = new Position(x, y);

            if (x >= 0 && x < width && y >= 0 && y < height) {
                TETile tile = world[x][y];
                if ((tile == Tileset.FLOOR || tile == Tileset.GRASS) && isPositionSafeFromOtherTraps(pos)) {
                    return pos;
                }
            }
            attempts++;
        }
        return null;
    }

    /** Keep at least Manhattan distance 2 from other traps. */
    private boolean isPositionSafeFromOtherTraps(Position pos) {
        for (TrapItem trap : traps) {
            if (!trap.isActive()) continue;
            Position t = trap.getPosition();
            int dist = Math.abs(pos.x - t.x) + Math.abs(pos.y - t.y);
            if (dist < 2) return false;
        }
        return true;
    }

    /** Weighted random trap factory. */
    private TrapItem createRandomTrap(Position position) {
        double r = random.nextDouble();

        if (r < HEALTH_POTION_CHANCE) {
            return new HealthPotionTrap(position, random);
        } else if (r < HEALTH_POTION_CHANCE + ARROW_CHANCE) {
            return new ArrowTrap(position, random);
        } else if (r < HEALTH_POTION_CHANCE + ARROW_CHANCE + BOMB_CHANCE) {
            return new BombTrap(position, random);
        } else {
            return new FireTrap(position, random);
        }
    }

    /** Per-tick trap updates (movement/animation). Returns true if world changed. */
    public boolean updateTraps(TETile[][] world, int width, int height) {
        boolean changed = false;
        for (TrapItem trap : traps) {
            if (!trap.isActive()) continue;
            if (trap.update(world, width, height)) {
                changed = true;
            }
        }
        return changed;
    }

    /**
     * Trap-player interactions (damage/heal) + cleanup inactive traps.
     * Returns true if player died this tick.
     */
    public boolean checkTrapCollisions(Player player, HealthManager healthManager,
                                       TETile[][] world, KeyManager keyManager) {
        Iterator<TrapItem> it = traps.iterator();
        boolean playerDied = false;

        while (it.hasNext()) {
            TrapItem trap = it.next();

            if (!trap.isActive()) {
                trap.removeFromWorld(world);
                it.remove();
                continue;
            }

            int effect = trap.interactWithPlayer(player, healthManager);
            if (effect != 0) {
                if (effect < 0) {
                    if (healthManager.takeDamage(-effect)) playerDied = true;
                } else {
                    healthManager.heal(effect);
                }

                HUD.updateHUDOnly(world, player, keyManager, healthManager);

                if (!trap.isActive()) {
                    trap.removeFromWorld(world);
                    it.remove();
                }
            }
        }
        return playerDied;
    }


    /**
     * Restore traps from saved positions only (types are re-randomized).
     * Matches GameController.loadGameState(...) call signature (no Random param).
     */
    public void setTrapPositions(List<Position> positions, TETile[][] world, int width, int height) {
        traps.clear();
        if (positions == null) return;

        for (Position pos : positions) {
            if (pos == null) continue;
            if (pos.x < 0 || pos.y < 0 || pos.x >= width || pos.y >= height) continue;

            TETile tile = world[pos.x][pos.y];
            if (tile != Tileset.FLOOR && tile != Tileset.GRASS) continue;

            TrapItem trap = createRandomTrap(pos);
            if (trap != null) {
                traps.add(trap);
                trap.placeInWorld(world, width, height);
            }
        }
    }

    /** Positions of all active traps for saving. */
    public List<Position> getTrapPositions() {
        List<Position> out = new ArrayList<>();
        for (TrapItem t : traps) {
            if (t.isActive()) out.add(t.getPosition());
        }
        return out;
    }
}
