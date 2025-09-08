package core;

import tileengine.TETile;

import java.util.Random;

/**
 * Fire trap - moves randomly
 */
public class FireTrap extends TrapItem {
    private int moveCounter;
    private static final int MOVE_FREQUENCY = 4; // Move every 4 updates

    public FireTrap(Position position, Random random) {
        super(position, TrapTileset.FIRE, true, random);
        this.moveCounter = 0;
    }

    @Override
    public boolean update(TETile[][] world, int width, int height) {
        if (!isActive || !isMovable) return false;

        moveCounter++;
        if (moveCounter < MOVE_FREQUENCY) return false;

        moveCounter = 0;

        // Try random directions
        for (int attempt = 0; attempt < 4; attempt++) {
            int direction = random.nextInt(4);
            Position newPos = calculatePositionInDirection(direction);

            if (canMoveTo(world, newPos, width, height)) {
                moveTo(world, newPos);
                return true;
            }
        }
        return false; // Couldn't move anywhere
    }

    private Position calculatePositionInDirection(int direction) {
        int newX = position.x;
        int newY = position.y;

        switch (direction) {
            case 0: newX++; break; // right
            case 1: newY++; break; // up
            case 2: newX--; break; // left
            case 3: newY--; break; // down
        }
        return new Position(newX, newY);
    }

    @Override
    public int interactWithPlayer(Player player, HealthManager healthManager) {
        if (isCollidingWithPlayer(player)) {
            System.out.println("🔥 Burned by fire!");
            return -DAMAGE_AMOUNT;
        }
        return 0;
    }
}