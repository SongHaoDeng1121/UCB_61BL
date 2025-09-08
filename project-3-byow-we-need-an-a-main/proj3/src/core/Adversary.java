package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.util.*;

// Represents an enemy that moves toward the player using A* pathfinding
public class Adversary {
    private Position position;
    private GameController controller;
    private TETile previousTile;
    private static final int UPDATE_FREQUENCY = 10;
    private int tickCount = 0;
    private boolean isDead = false;

    public Adversary(Position start, GameController controller) {
        this.position = start;
        this.controller = controller;
        TETile[][] world = controller.getGameWorld();
        this.previousTile = world[start.x][start.y];
        world[start.x][start.y] = Tileset.ADVERSARY;
    }

    public void takeTurn() {
        if (isDead) return;
        tickCount++;
        if (tickCount % UPDATE_FREQUENCY != 0) return;

        Position playerPos = controller.getPlayerPosition();
        Position next = getNextStepToward(playerPos);
        if (next == null) return;
        TETile[][] world = controller.getGameWorld();

        if (next.equals(playerPos)) {
            // restore the tile we were standing on
            world[position.x][position.y] = previousTile;

            // damage player + HUD
            controller.damagePlayer();
            HUD.updateHUDOnly(
                    controller.getGameWorld(),
                    controller.getPlayer(),
                    controller.getKeyManager(),
                    controller.getHealthManager()
            );
            // mark dead and REMOVE from manager so it doesn't get saved
            isDead = true;
            controller.removeAdversary(this);
            return;
        }

        TETile nextTile = world[next.x][next.y];
        if (!isValidMovementTile(nextTile)) return;

        world[position.x][position.y] = previousTile;
        previousTile = nextTile;
        position = next;
        world[position.x][position.y] = Tileset.ADVERSARY;
    }
    private Position getNextStepToward(Position target) {
        List<Position> path = AStarPathFinder.findPath(controller.getGameWorld(), position, target);
        if (path.size() >= 2) return path.get(1);
        return null;
    }

    private boolean isValidMovementTile(TETile tile) {
        return tile == Tileset.FLOOR || tile == Tileset.GRASS || tile == Tileset.AVATAR;
    }

    public Position getPosition() {
        return position;
    }

    public boolean isDead() {
        return isDead;
    }
}