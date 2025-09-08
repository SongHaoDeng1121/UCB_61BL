package core;

import tileengine.TETile;

import java.util.Random;


public class HealthPotionTrap extends TrapItem {
    public HealthPotionTrap(Position position, Random random) {
        super(position, TrapTileset.HEALTH_POTION, false, random);
    }

    @Override
    public boolean update(TETile[][] world, int width, int height) {
        // Health potions are stationary
        return false;
    }

    @Override
    public int interactWithPlayer(Player player, HealthManager healthManager) {
        if (isCollidingWithPlayer(player)) {
            System.out.println("💚 Found a health potion!");
            // Remove the potion after use
            setActive(false);
            return HEAL_AMOUNT;
        }
        return 0;
    }
}