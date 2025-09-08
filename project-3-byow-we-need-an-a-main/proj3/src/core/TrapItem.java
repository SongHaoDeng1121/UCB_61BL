package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.util.Random;


public abstract class TrapItem {
    protected Position position;
    protected TETile tile;
    protected TETile previousTile;
    protected boolean isMovable;
    protected boolean isActive;
    protected Random random;

    // Effect constants
    public static final int DAMAGE_AMOUNT = 1;
    public static final int HEAL_AMOUNT = 1;

    public TrapItem(Position position, TETile tile, boolean isMovable, Random random) {
        this.position = new Position(position.x, position.y);
        this.tile = tile;
        this.previousTile = Tileset.FLOOR; // Default underlying tile
        this.isMovable = isMovable;
        this.isActive = true;
        this.random = random;
    }

    /**
     * Places the trap in the world
     */
    public void placeInWorld(TETile[][] world, int width, int height) {
        if (isValidPosition(world, position, width, height)) {
            previousTile = world[position.x][position.y];
            world[position.x][position.y] = tile;
        }
    }

    /**
     * Removes the trap from the world
     */
    public void removeFromWorld(TETile[][] world) {
        if (position != null && isActive) {
            world[position.x][position.y] = previousTile;
        }
        isActive = false;
    }


    public abstract boolean update(TETile[][] world, int width, int height);


    public abstract int interactWithPlayer(Player player, HealthManager healthManager);


    protected boolean canMoveTo(TETile[][] world, Position newPos, int width, int height) {
        if (!isValidPosition(world, newPos, width, height)) {
            return false;
        }

        TETile targetTile = world[newPos.x][newPos.y];

        // Can't move into walls
        if (targetTile == Tileset.WALL) {
            return false;
        }

        // CRITICAL FIX: Allow moving into player position!
        // This enables collision detection to work properly
        if (targetTile == Tileset.AVATAR) {
           return true; // Allow the move so collision can be detected
        }

        // Can move to floors, grass, and other walkable tiles
        return targetTile == Tileset.FLOOR ||
                targetTile == Tileset.GRASS ||
                targetTile == Tileset.LOCKED_DOOR ||
                isTrapTile(targetTile); // Can move through other traps if needed
    }

    /**
     * Helper method to identify trap tiles
     */
    private boolean isTrapTile(TETile tile) {
        if (tile == null) return false;
        String description = tile.description().toLowerCase();
        char character = tile.character();

        return description.contains("trap") ||
                description.contains("bomb") ||
                description.contains("fire") ||
                description.contains("health potion") ||
                character == '→' || character == '↑' || character == '←' || character == '↓' ||
                character == '●' || character == 'Ψ' || character == '♥';
    }


    protected boolean isValidPosition(TETile[][] world, Position pos, int width, int height) {
        return pos.x >= 0 && pos.x < width && pos.y >= 0 && pos.y < height;
    }


    protected void moveTo(TETile[][] world, Position newPosition) {
        if (!isMovable || newPosition.equals(position)) {
            return;
        }

        // Check if we're moving into player position
        boolean movingIntoPlayer = world[newPosition.x][newPosition.y] == Tileset.AVATAR;

        // Restore previous tile at old position
        world[position.x][position.y] = previousTile;

        if (movingIntoPlayer) {

            // The collision will be handled by the TrapManager
            position = newPosition;
            // Keep the previous tile as FLOOR since we can't see what was under the player
            previousTile = Tileset.FLOOR;
        } else {
            // Normal move - store what's at the new position
            previousTile = world[newPosition.x][newPosition.y];
            world[newPosition.x][newPosition.y] = tile;
            position = newPosition;

        }
    }


    public boolean isCollidingWithPlayer(Player player) {
        if (!isActive || player == null || player.getPosition() == null) {
            return false;
        }

        Position playerPos = player.getPosition();
        boolean colliding = position.equals(playerPos);


        return colliding;
    }

    // Getters
    public Position getPosition() {
        return position;
    }

    public TETile getTile() {
        return tile;
    }

    public boolean isMovable() {
        return isMovable;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public int getDamageAmount() {
        return DAMAGE_AMOUNT;
    }
}