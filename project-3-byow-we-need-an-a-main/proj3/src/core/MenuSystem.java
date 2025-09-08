package core;

import tileengine.TETile;
import tileengine.Tileset;

public class MenuSystem {
    private MenuState currentState;
    private StringBuilder seedInput;
    private long gameSeed;

    // World generation settings
    private static final int WORLD_WIDTH = 80;
    private static final int WORLD_HEIGHT = 50;
    private static final int MAX_ROOM_QUANTITY = 30;
    private static final int ROOM_MIN_SIZE = 4;
    private static final int ROOM_MAX_SIZE = 8;

    // Tile settings
    private static final TETile ROOM_FLOOR_TILE = Tileset.GRASS;
    private static final TETile HALLWAY_FLOOR_TILE = Tileset.GRASS;

    // Config object with all parameters
    public static final WorldConfig CONFIG = new WorldConfig(
            WORLD_WIDTH,
            WORLD_HEIGHT,
            MAX_ROOM_QUANTITY,
            ROOM_MIN_SIZE,
            ROOM_MAX_SIZE,
            ROOM_FLOOR_TILE,
            HALLWAY_FLOOR_TILE
    );

    public MenuSystem() {
        currentState = MenuState.MAIN_MENU;
        seedInput = new StringBuilder();
        gameSeed = 0;
    }

    public void start() {
        MenuRenderer.initializeCanvas();
        runMenuLoop();
    }

    private void runMenuLoop() {
        while (true) {
            switch (currentState) {
                case MAIN_MENU -> handleMainMenu();
                case SEED_INPUT -> handleSeedInput();
                case GAME_ACTIVE -> {
                    handleGameActive();
                    currentState = MenuState.MAIN_MENU;
                }
                case GAME_LOADING -> {
                    handleGameLoading();
                    currentState = MenuState.MAIN_MENU;
                }
            }
        }
    }

    private void handleMainMenu() {
        MenuRenderer.renderMainMenu();
        char key = InputHandler.getNextKeyPressed();

        switch (key) {
            case 'n' -> {
                System.out.println("New game selected");
                currentState = MenuState.SEED_INPUT;
                seedInput.setLength(0);
            }
            case 'l' -> {
                System.out.println("Load game selected");
                if (SaveGameManager.hasSaveFile()) {
                    GameController loaded = GameController.loadGameState(CONFIG);
                    if (loaded != null) {
                        loaded.runGame();
                        MenuRenderer.initializeCanvas();
                        currentState = MenuState.MAIN_MENU;
                    } else {
                        System.out.println("Failed to load saved game.");
                    }
                } else {
                    System.out.println("No save file found.");
                }
            }
            case 'q' -> {
                System.out.println("Quit selected");
                System.exit(0);
            }
            default -> System.out.println("Invalid input: " + key);
        }
    }

    private void handleSeedInput() {
        MenuRenderer.renderSeedInputScreen(seedInput.toString());
        char key = InputHandler.getNextKeyPressed();

        if (Character.isDigit(key)) {
            seedInput.append(key);
            System.out.println("Current seed: " + seedInput);
        } else if (key == 's') {
            if (seedInput.length() > 0) {
                try {
                    gameSeed = Long.parseLong(seedInput.toString());
                    System.out.println("Starting game with seed: " + gameSeed);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid seed format. Using current time.");
                    gameSeed = System.currentTimeMillis();
                }
            } else {
                gameSeed = System.currentTimeMillis();
                System.out.println("No seed entered. Using default: " + gameSeed);
            }
            currentState = MenuState.GAME_ACTIVE;
        }
    }

    private void handleGameActive() {
        System.out.println("Generating new world...");
        MenuRenderer.renderLoadingScreen("Generating World...");

        GameController game = new GameController(CONFIG, gameSeed);
        game.runGame();

        System.out.println("Game ended. Returning to menu...");
        MenuRenderer.initializeCanvas();
    }

    private void handleGameLoading() {
        System.out.println("Loading saved game...");
        MenuRenderer.renderLoadingScreen("Loading Saved Game...");

        GameController loaded = GameController.loadGameState(CONFIG);
        if (loaded != null) {
            loaded.runGame();
        } else {
            System.out.println("Failed to load saved game.");
        }

        MenuRenderer.initializeCanvas();
    }
}