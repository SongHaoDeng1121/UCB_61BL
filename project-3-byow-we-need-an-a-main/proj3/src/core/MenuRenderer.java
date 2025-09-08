package core;

import edu.princeton.cs.algs4.StdDraw;
import java.awt.Color;
import java.awt.Font;


public class MenuRenderer {
    private static final int CANVAS_WIDTH = 800;
    private static final int CANVAS_HEIGHT = 600;

    // Set up canvas
    public static void initializeCanvas() {
        StdDraw.setCanvasSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        StdDraw.setXscale(0, CANVAS_WIDTH);
        StdDraw.setYscale(0, CANVAS_HEIGHT);
        StdDraw.enableDoubleBuffering(); // Prevents flickering
    }

    // Renders the main menu screen.
    public static void renderMainMenu() {
        // Clear the canvas with a dark background
        StdDraw.clear(new Color(20, 20, 20));

        // Set text color to white
        StdDraw.setPenColor(Color.WHITE);

        // Draw the main title
        StdDraw.setFont(new Font("Arial", Font.BOLD, 48));
        StdDraw.text(CANVAS_WIDTH / 2.0, CANVAS_HEIGHT * 0.75, "BYOW GAME");

        // Draw subtitle
        StdDraw.setFont(new Font("Arial", Font.PLAIN, 24));
        StdDraw.text(CANVAS_WIDTH / 2.0, CANVAS_HEIGHT * 0.65, "Build Your Own World");

        // Draw menu options
        StdDraw.setFont(new Font("Arial", Font.PLAIN, 18));
        StdDraw.text(CANVAS_WIDTH / 2.0, CANVAS_HEIGHT * 0.45, "Press N for New Game");
        StdDraw.text(CANVAS_WIDTH / 2.0, CANVAS_HEIGHT * 0.40, "Press L to Load Game");
        StdDraw.text(CANVAS_WIDTH / 2.0, CANVAS_HEIGHT * 0.35, "Press Q to Quit");

        // Add decorative elements
        StdDraw.setPenColor(new Color(100, 150, 255));
        StdDraw.setFont(new Font("Arial", Font.ITALIC, 14));
        StdDraw.text(CANVAS_WIDTH / 2.0, CANVAS_HEIGHT * 0.15, "Choose your adventure...");

        // Make changes visible
        StdDraw.show();
    }

    // Renders the seed input screen.
    public static void renderSeedInputScreen(String currentSeed) {
        // Clear the canvas with a dark blue background
        StdDraw.clear(new Color(30, 30, 60));

        // Set text color to white
        StdDraw.setPenColor(Color.WHITE);

        // Draw the header
        StdDraw.setFont(new Font("Arial", Font.BOLD, 36));
        StdDraw.text(CANVAS_WIDTH / 2.0, CANVAS_HEIGHT * 0.75, "Enter Seed");

        // Draw instructions
        StdDraw.setFont(new Font("Arial", Font.PLAIN, 18));
        StdDraw.text(CANVAS_WIDTH / 2.0, CANVAS_HEIGHT * 0.65, "Enter a number to generate your world");
        StdDraw.text(CANVAS_WIDTH / 2.0, CANVAS_HEIGHT * 0.60, "Press S when done to start the game");

        // Draw the seed input box background
        StdDraw.setPenColor(new Color(60, 60, 60));
        StdDraw.filledRectangle(CANVAS_WIDTH / 2.0, CANVAS_HEIGHT * 0.45, 200, 30);

        // Draw the seed input box border
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.rectangle(CANVAS_WIDTH / 2.0, CANVAS_HEIGHT * 0.45, 200, 30);

        // Draw the current seed
        StdDraw.setFont(new Font("Arial", Font.PLAIN, 24));
        if (currentSeed.isEmpty()) {
            // Show placeholder text if no seed entered
            StdDraw.setPenColor(new Color(150, 150, 150));
            StdDraw.text(CANVAS_WIDTH / 2.0, CANVAS_HEIGHT * 0.45, "Enter seed here...");
        } else {
            // Show the actual seed
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.text(CANVAS_WIDTH / 2.0, CANVAS_HEIGHT * 0.45, currentSeed);
        }

        // Draw additional instructions
        StdDraw.setPenColor(new Color(200, 200, 200));
        StdDraw.setFont(new Font("Arial", Font.PLAIN, 14));
        StdDraw.text(CANVAS_WIDTH / 2.0, CANVAS_HEIGHT * 0.25, "Only numbers (0-9) are allowed");
        StdDraw.text(CANVAS_WIDTH / 2.0, CANVAS_HEIGHT * 0.20, "Press S to start with seed: " +
                (currentSeed.isEmpty() ? "None" : currentSeed));

        // Make changes visible
        StdDraw.show();
    }

    // Renders a loading screen while the world is being generated.
    public static void renderLoadingScreen(String message) {
        // Clear with dark green background
        StdDraw.clear(new Color(20, 40, 20));

        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new Font("Arial", Font.BOLD, 24));
        StdDraw.text(CANVAS_WIDTH / 2.0, CANVAS_HEIGHT / 2.0, message);

        // Add a simple loading animation (dots)
        StdDraw.setFont(new Font("Arial", Font.PLAIN, 18));
        long time = System.currentTimeMillis();
        int dotCount = (int) ((time / 500) % 4); // Changes every 500ms
        String dots = "";
        for (int i = 0; i < dotCount; i++) {
            dots += ".";
        }
        StdDraw.text(CANVAS_WIDTH / 2.0, CANVAS_HEIGHT / 2.0 - 50, dots);

        StdDraw.show();
    }


}