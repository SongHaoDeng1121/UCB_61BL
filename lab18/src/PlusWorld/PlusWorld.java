package PlusWorld;

import byowTools.TileEngine.TERenderer;
import byowTools.TileEngine.TETile;
import byowTools.TileEngine.Tileset;

/**
 * Draws a world consisting of plus shaped regions.
 */
public class PlusWorld {

    private static final int WIDTH = 60;
    private static final int HEIGHT = 40;

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // World Create
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        // Plus symbol
        addPlus(world, 15, 20, 5, Tileset.FLOWER);

        // render the world
        ter.renderFrame(world);
    }

    /**
     * Adds a plus shape to the world.
     * @param world the tile world
     * @param centerX x coordinate of the center
     * @param centerY y coordinate of the center
     * @param size the size of each arm of the plus
     * @param tile the tile to use for the plus
     */
    public static void addPlus(TETile[][] world, int centerX, int centerY, int size, TETile tile) {
        // Calculate offset to center the squares properly
        int offset = (size - 1) / 2;

        // Center square
        addSquare(world, centerX - offset, centerY - offset, size, tile);

        // Top square
        addSquare(world, centerX - offset, centerY + size - offset, size, tile);

        // Bottom square
        addSquare(world, centerX - offset, centerY - size - offset, size, tile);

        // Left square
        addSquare(world, centerX - size - offset, centerY - offset, size, tile);

        // Right square
        addSquare(world, centerX + size - offset, centerY - offset, size, tile);
    }

    /**
     * Adds a square of tiles to the world.
     * @param world the tile world
     * @param startX bottom-left x coordinate of the square
     * @param startY bottom-left y coordinate of the square
     * @param size the size of the square (size x size)
     * @param tile the tile to use
     */
    private static void addSquare(TETile[][] world, int startX, int startY, int size, TETile tile) {
        for (int x = startX; x < startX + size; x++) {
            for (int y = startY; y < startY + size; y++) {
                if (inBounds(world, x, y)) {
                    world[x][y] = tile;
                }
            }
        }
    }


    // Judge whether it is out of bound
    private static boolean inBounds(TETile[][] world, int x, int y) {
        return x >= 0 && x < world.length && y >= 0 && y < world[0].length;
    }
}
