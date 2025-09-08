package core;

import tileengine.TETile;
import java.util.List;

// Represents the full game state to be saved and restored
public class GameState {
    public final long seed;
    public final Position playerPos;
    public final TETile previousTile;
    public final List<Position> keyPositions;
    public final int keysCollected;
    public final List<Position> trapPositions;
    public final int lives;
    public final int maxLives;
    public final List<Position> adversaryPositions;
    public final TETile[][] world;

    // Constructor for saving
    public GameState(long seed, Position playerPos, TETile previousTile,
                     List<Position> keyPositions, int keysCollected,
                     List<Position> trapPositions, int lives, int maxLives,
                     List<Position> adversaryPositions, TETile[][] world) {
        this.seed = seed;
        this.playerPos = playerPos;
        this.previousTile = previousTile;
        this.keyPositions = keyPositions;
        this.keysCollected = keysCollected;
        this.trapPositions = trapPositions;
        this.lives = lives;
        this.maxLives = maxLives;
        this.adversaryPositions = adversaryPositions;
        this.world = world;
    }

    // Constructor for loading (world not yet restored)
    public GameState(long seed, Position playerPos, TETile previousTile,
                     List<Position> keyPositions, int keysCollected,
                     List<Position> trapPositions, int lives, int maxLives,
                     List<Position> adversaryPositions) {
        this(seed, playerPos, previousTile, keyPositions, keysCollected,
                trapPositions, lives, maxLives, adversaryPositions, null);
    }
}