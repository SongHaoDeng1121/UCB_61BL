package core;

import tileengine.TETile;

import java.util.Random;


public class BombTrap extends TrapItem {
    public BombTrap(Position position, Random random) {
        super(position, TrapTileset.BOMB, false, random);
    }

    @Override
    public boolean update(TETile[][] world, int width, int height) {
        // Bombs are stationary
        return false;
    }

    @Override
    public int interactWithPlayer(Player player, HealthManager healthManager) {
        if (isCollidingWithPlayer(player)) {
            System.out.println("💥 Stepped on a bomb!");
            // Remove the bomb after explosion
            setActive(false);
            return -DAMAGE_AMOUNT;
        }
        return 0;
    }
}