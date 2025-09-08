package core;

import tileengine.TETile;
import java.awt.Color;
import java.util.Random;


public class ArrowTrap extends TrapItem {
    private int direction; // 0=right, 1=up, 2=left, 3=down
    private int moveCounter;
    private static final int MOVE_FREQUENCY = 3; // Move every 3 updates

    public ArrowTrap(Position position, Random random) {
        super(position, TrapTileset.ARROW, true, random);
        this.direction = random.nextInt(4);
        this.moveCounter = 0;
        updateArrowDirection();
    }

    private void updateArrowDirection() {
        switch (direction) {
            case 0: tile = new TETile('→', Color.RED, Color.BLACK, "arrow trap", 20); break;
            case 1: tile = new TETile('↑', Color.RED, Color.BLACK, "arrow trap", 20); break;
            case 2: tile = new TETile('←', Color.RED, Color.BLACK, "arrow trap", 20); break;
            case 3: tile = new TETile('↓', Color.RED, Color.BLACK, "arrow trap", 20); break;
        }
    }

    @Override
    public boolean update(TETile[][] world, int width, int height) {
        if (!isActive || !isMovable) return false;

        moveCounter++;
        if (moveCounter < MOVE_FREQUENCY) return false;
        moveCounter = 0;
        Position newPos = calculateNextPosition();
        if (canMoveTo(world, newPos, width, height)) {
            moveTo(world, newPos);
            return true;
        } else {
            // Bounce off walls by reversing direction
            direction = (direction + 2) % 4;
            updateArrowDirection();
            return false;
        }
    }

    private Position calculateNextPosition() {
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
            System.out.println("🏹 Hit by arrow trap!");
            return -DAMAGE_AMOUNT;
        }
        return 0;
    }
}