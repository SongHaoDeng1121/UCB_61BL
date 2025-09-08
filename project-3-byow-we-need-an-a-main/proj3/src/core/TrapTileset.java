package core;

import tileengine.TETile;
import java.awt.Color;

/**
 * Custom tiles for traps (separate from main Tileset to avoid conflicts)
 */
public class TrapTileset {
    public static final TETile ARROW = new TETile('→', Color.RED, Color.BLACK, "arrow trap", 20);
    public static final TETile BOMB = new TETile('●', Color.ORANGE, Color.BLACK, "bomb", 21);
    public static final TETile FIRE = new TETile('Ψ', Color.RED, Color.YELLOW, "fire", 22);
    public static final TETile HEALTH_POTION = new TETile('♥', Color.GREEN, Color.BLACK, "health potion", 23);
}