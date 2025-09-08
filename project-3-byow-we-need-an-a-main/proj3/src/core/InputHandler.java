package core;
import edu.princeton.cs.algs4.StdDraw;
import java.awt.event.KeyEvent;

public class InputHandler {
    private static boolean escPressed = false;

    // Gets the next key pressed, handling both typed characters and special keys.
    public static char getNextKeyPressed() {
        while (true) {
            // Check for ESC key first (special handling)
            if (StdDraw.isKeyPressed(KeyEvent.VK_ESCAPE)) {
                if (!escPressed) { // Prevent multiple rapid ESC detections
                    escPressed = true;
                    return 27; // Return ESC ASCII code
                }
            } else {
                escPressed = false; // Reset ESC flag when key is released
            }
            // Check for regular typed characters (WASD, etc.)
            if (StdDraw.hasNextKeyTyped()) {
                char key = Character.toLowerCase(StdDraw.nextKeyTyped());
                return key;
            }
            // Small delay to prevent excessive CPU usage
            StdDraw.pause(10);
        }
    }

    // The `pollKeyOnce()` method prevents flickering by checking each key press exactly once per tick,
    // combining both immediate and typed key detection to ensure smooth input handling without repeated processing.
    public static Character pollKeyOnce() {
        // ESC
        if (StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_ESCAPE)) return (char)27;
        // typed keys
        if (StdDraw.hasNextKeyTyped()) return Character.toLowerCase(StdDraw.nextKeyTyped());
        return null;
    }

    //Waits until all relevant keys are released to prevent accidental immediate input.
    public static void waitForAllKeysReleased() {
        while (StdDraw.isKeyPressed(KeyEvent.VK_W) ||
                StdDraw.isKeyPressed(KeyEvent.VK_A) ||
                StdDraw.isKeyPressed(KeyEvent.VK_S) ||
                StdDraw.isKeyPressed(KeyEvent.VK_D) ||
                StdDraw.isKeyPressed(KeyEvent.VK_ESCAPE)) {
            StdDraw.pause(10);
        }
    }

    public static void clearInputBuffer() {
        while (StdDraw.hasNextKeyTyped()) {
            StdDraw.nextKeyTyped();
        }
        escPressed = false;
        waitForAllKeysReleased();
        System.out.println("Input buffer cleared");
    }

    public static void prepareForMenu() {
        clearInputBuffer();
        StdDraw.pause(100);
    }
}