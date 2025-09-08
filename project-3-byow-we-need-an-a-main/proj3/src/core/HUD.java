package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TETile;
import tileengine.Tileset;

import java.awt.*;

// Handles drawing the top Heads-Up Display (HUD) row above the game world
public class HUD {
    private static final double HUD_MARGIN = 1.5;
    private static final double HUD_BACKGROUND_HEIGHT = 0.6;
    private static final String HUD_FONT_NAME = "Monaco";
    private static final int HUD_FONT_SIZE = 15;

    private static final String ICON_POSITION = "\uD83D\uDCCD"; // 📍
    private static final String ICON_KEY = "\uD83D\uDD11";       // 🔑
    private static final String ICON_HEART_FULL = "\u2764\uFE0F"; // ❤️
    private static final String ICON_HEART_EMPTY = "♡";           // ♡
    private static final String ESCAPE_HINT = "ESC to return to menu";

    public static void updateHUDOnly(TETile[][] paddedWorld, Player player, KeyManager keyManager, HealthManager healthManager) {
        if (paddedWorld == null || paddedWorld.length == 0 || paddedWorld[0].length == 0) return;

        final int width = paddedWorld.length;
        final int height = paddedWorld[0].length;
        final double hudY = height - 1.5;

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.filledRectangle(width / 2.0, hudY, width / 2.0, HUD_BACKGROUND_HEIGHT);
        StdDraw.setPenColor(StdDraw.WHITE);

        double usableWidth = width - (2 * HUD_MARGIN);
        double spacing = usableWidth / 5;

        double posX = HUD_MARGIN + spacing * 0;
        double keyX = HUD_MARGIN + spacing * 1;
        double heartX = HUD_MARGIN + spacing * 2;
        double descX = HUD_MARGIN + spacing * 3;
        double statusX = HUD_MARGIN + spacing * 4;
        double escX = width - HUD_MARGIN;

        StdDraw.setFont(new Font(HUD_FONT_NAME, Font.PLAIN, HUD_FONT_SIZE));

        if (player != null && player.getPosition() != null) {
            int px = player.getPosition().x;
            int py = player.getPosition().y;
            StdDraw.text(posX, hudY, ICON_POSITION + " (" + px + ", " + py + ")");
        }

        if (keyManager != null) {
            String keys = ICON_KEY + " " + keyManager.getKeysCollected() + " / " + keyManager.getTotalKeys();
            StdDraw.text(keyX, hudY, keys);
        }

        // Lives display (hearts) — now uses HealthManager directly
        int lives = healthManager != null ? healthManager.getCurrentHealth() : 0;
        int totalLives = healthManager != null ? healthManager.getMaxHealth() : 0;
        StringBuilder hearts = new StringBuilder();
        for (int i = 0; i < lives; i++) hearts.append(ICON_HEART_FULL);
        for (int i = lives; i < totalLives; i++) hearts.append(ICON_HEART_EMPTY);

        StdDraw.setFont(new Font("Apple Color Emoji", Font.PLAIN, HUD_FONT_SIZE + 2));
        StdDraw.text(heartX, hudY, hearts.toString());

        StdDraw.setFont(new Font(HUD_FONT_NAME, Font.PLAIN, HUD_FONT_SIZE));

        int mx = (int) StdDraw.mouseX();
        int my = (int) StdDraw.mouseY();
        if (mx >= 0 && mx < width && my >= 0 && my < height) {
            TETile tile = paddedWorld[mx][my];
            if (tile != null) {
                StdDraw.text(descX, hudY, tile.description());
            }
        }

        if (healthManager != null) {
            String statusText = "";
            if (healthManager.isDead()) {
                StdDraw.setPenColor(StdDraw.RED);
                statusText = "DEAD";
            } else if (healthManager.getCurrentHealth() == 1) {
                StdDraw.setPenColor(Color.ORANGE);
                statusText = "CRITICAL";
            } else if (healthManager.isFullHealth()) {
                StdDraw.setPenColor(Color.GREEN);
                statusText = "HEALTHY";
            } else {
                StdDraw.setPenColor(StdDraw.WHITE);
                statusText = "INJURED";
            }
            if (!statusText.isEmpty()) {
                StdDraw.text(statusX, hudY, statusText);
            }
            StdDraw.setPenColor(StdDraw.WHITE);
        }
        StdDraw.textRight(escX, hudY, ESCAPE_HINT);
        StdDraw.show();
    }
}