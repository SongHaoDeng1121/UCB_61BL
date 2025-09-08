package core;

import tileengine.TETile;
import tileengine.Tileset;
import java.util.Random;

/*
Updated Player class that can walk over trap tiles
*/
public class Player {
    private Position position;
    private TETile avatarTile;
    private TETile previousTile; // Store the tile that was under the player
    private Random rand;
    private int lives = 4;
    private int maxLives = 5;

    // Constructor creates a player with the avatar tile.
    public Player(long seed) {
        this.avatarTile = Tileset.AVATAR; // @ symbol
        this.previousTile = Tileset.FLOOR; // Default floor under player
        this.rand = new Random(seed);
    }



    // Sets the player's position. Used when loading a game.
    public void setPosition(Position newPos) {
        this.position = newPos;
    }

    // Sets the tile the player was standing on. Used when loading a game.
    public void setPreviousTile(TETile tile) {
        this.previousTile = tile;
    }

    // Places the player at a deterministic starting position in the world.
    public boolean placeInWorld(TETile[][] world, int width, int height) {
        // Find the first floor tile in a deterministic order (bottom-left to top-right)
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (world[x][y] == Tileset.FLOOR || world[x][y] == Tileset.GRASS) {
                    this.position = new Position(x, y);
                    this.previousTile = world[x][y]; // Store what was there before
                    world[x][y] = avatarTile; // Place the player
                    System.out.println("Player placed at position: " + position);
                    return true;
                }
            }
        }

        System.err.println("ERROR: Could not find a valid starting position for player!");
        return false;
    }

    // Attempts to move the player in the specified direction.
    public boolean move(TETile[][] world, char direction, int width, int height) {
        // Calculate new position based on direction
        Position newPos = calculateNewPosition(direction);

        // Validate the new position
        if (!isValidMove(world, newPos, width, height)) {
            return false;
        }

        // Perform the move
        executeMove(world, newPos);
        return true;
    }

    // Calculates the new position based on movement direction.
    private Position calculateNewPosition(char direction) {
        int newX = position.x;
        int newY = position.y;

        switch (direction) {
            case 'w': // Move up
                newY++;
                break;
            case 'a': // Move left
                newX--;
                break;
            case 's': // Move down
                newY--;
                break;
            case 'd': // Move right
                newX++;
                break;
            default:
                // Invalid direction, return current position
                System.out.println("Invalid direction: " + direction);
                return position;
        }

        return new Position(newX, newY);
    }

    // Checks if a move to the new position is valid.
    private boolean isValidMove(TETile[][] world, Position newPos, int width, int height) {
        // Check bounds
        if (newPos.x < 0 || newPos.x >= width || newPos.y < 0 || newPos.y >= height) {
            return false;
        }

        // Check if destination is walkable (not a wall)
        TETile destinationTile = world[newPos.x][newPos.y];
        return isWalkableTile(destinationTile);
    }

    // Determines if a tile is walkable by the player.
    private boolean isWalkableTile(TETile tile) {
        // Player can walk on floor, grass, keys, AND ALL TRAP TILES
        return tile == Tileset.FLOOR ||
                tile == Tileset.GRASS ||
                tile == Tileset.LOCKED_DOOR ||
                isTrapTile(tile);
    }

    // Helper method to check if a tile is a trap tile
    private boolean isTrapTile(TETile tile) {
        if (tile == null) return false;

        // Check trap tiles by their description or character
        String description = tile.description().toLowerCase();
        char character = tile.character();

        return description.contains("trap") ||
                description.contains("bomb") ||
                description.contains("fire") ||
                description.contains("health potion") ||
                character == '→' || character == '↑' || character == '←' || character == '↓' ||
                character == '●' || character == 'Ψ' || character == '♥';
    }

    // Executes the move by updating the world state and player position.
    private void executeMove(TETile[][] world, Position newPos) {
        // Store what's at the new position BEFORE placing the player
        TETile tileAtNewPosition = world[newPos.x][newPos.y];

        // Restore the previous tile where the player was
        world[position.x][position.y] = previousTile;

        // If the new position has a trap, we need to remember the underlying tile
        // For traps, the underlying tile is typically FLOOR or GRASS
        if (isTrapTile(tileAtNewPosition)) {
            // For traps, assume the underlying tile is FLOOR (traps remember their own underlying tiles)
            previousTile = Tileset.FLOOR;
            System.out.println("DEBUG: Player stepping on trap: " + tileAtNewPosition.description());
        } else {
            // Store what's at the new position
            previousTile = tileAtNewPosition;
        }

        // Place the player at the new position
        world[newPos.x][newPos.y] = avatarTile;

        // Update player position
        position = newPos;

    }

    // Gets the current player position.
    public Position getPosition() {
        return position;
    }

    // Gets the tile that was under the player before the last move.
    public TETile getPreviousTile() {
        return previousTile;
    }

    // Gets the avatar tile used for the player.
    public TETile getAvatarTile() {
        return avatarTile;
    }


    // Update the tile after the keys have been collected
    public void updatePreviousTile(TETile newPreviousTile) {
        this.previousTile = newPreviousTile;
        System.out.println("DEBUG: Player previousTile updated to: " + newPreviousTile.description());
    }

}