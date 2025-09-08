package core;

import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.util.List;
import java.util.Random;

public class GameController {
    private TERenderer renderer;
    private TETile[][] gameWorld;
    private Player player;
    private long currentSeed;
    private boolean gameRunning;
    private AdversaryManager adversaryManager;
    private KeyManager keyManager;
    private WorldBuilder worldBuilder;
    private HealthManager healthManager;
    private TrapManager trapManager;
    private GameState gameState; // present only when loading

    private final WorldConfig config;
    private static final int HUD_HEIGHT = 1;
    private int updateCounter = 0;
    private static final int TRAP_UPDATE_FREQUENCY = 5;

    public GameController(WorldConfig config, long seed) {
        this(config, seed, false);
    }

    public GameController(WorldConfig config, long seed, boolean fromLoad) {
        this.config = config;
        this.currentSeed = seed;
        this.gameRunning = true;
        this.renderer = new TERenderer();
        renderer.initialize(config.width, config.height + HUD_HEIGHT);

        if (!fromLoad) {
            // Fresh game setup
            this.healthManager = new HealthManager();
            this.trapManager = new TrapManager(seed);

            generateWorld(seed);
            initializePlayer(seed);
            initializeKeySystem(seed);
            initializeTrapSystem(seed);

            this.adversaryManager = new AdversaryManager(this);
        }
    }

    public static GameController loadGameState(WorldConfig config) {
        GameState state = SaveGameManager.loadGame();
        if (state == null) return null;

        GameController gc = new GameController(config, state.seed, true);
        gc.gameState = state;
        gc.generateWorld(state.seed);

        // Restore player
        gc.player = new Player(state.seed);
        gc.player.setPosition(state.playerPos);
        gc.player.setPreviousTile(state.previousTile);
        gc.gameWorld[state.playerPos.x][state.playerPos.y] = gc.player.getAvatarTile();
        // Restore keys
        gc.keyManager = new KeyManager(new Random(state.seed));
        gc.keyManager.placeKeysAtPositions(state.keyPositions);
        gc.keyManager.setKeysCollected(state.keysCollected);
        gc.placeKeysInWorld();
        // Restore traps
        gc.trapManager = new TrapManager(state.seed);
        gc.trapManager.setTrapPositions(
                state.trapPositions,
                gc.gameWorld,
                gc.gameWorld.length,
                gc.gameWorld[0].length
        );
        // Restore health
        gc.healthManager = new HealthManager();
        gc.healthManager.setMaxHealth(state.maxLives);
        gc.healthManager.setCurrentHealth(state.lives);
        // Restore adversaries using saved positions
        gc.adversaryManager = new AdversaryManager(gc);
        gc.renderGameState();
        return gc;
    }

    private void generateWorld(long seed) {
        worldBuilder = new WorldBuilder(config, seed);
        worldBuilder.roomGenerator();
        worldBuilder.connectRoomsWithHallways();
        worldBuilder.addWallsToWorld();

        TETile[][] baseWorld = worldBuilder.getWorld();
        gameWorld = new TETile[config.width][config.height + HUD_HEIGHT];

        for (int x = 0; x < config.width; x++) {
            for (int y = 0; y < config.height; y++) {
                gameWorld[x][y] = baseWorld[x][y];
            }
            gameWorld[x][config.height] = Tileset.NOTHING; // HUD row
        }
    }

    private void initializeKeySystem(long seed) {
        Random random = new Random(seed);
        keyManager = new KeyManager(random);

        if (worldBuilder != null && worldBuilder.getRooms() != null) {
            keyManager.generateKeys(worldBuilder.getRooms());
            placeKeysInWorld();
        }
    }

    private void initializeTrapSystem(long seed) {
        if (worldBuilder != null && worldBuilder.getRooms() != null) {
            trapManager.generateTraps(worldBuilder.getRooms(), gameWorld, config.width, config.height);
        }
    }

    private void placeKeysInWorld() {
        for (Room room : keyManager.getKeyRooms()) {
            if (room.hasKey() && room.getKeyPosition() != null) {
                Position keyPos = room.getKeyPosition();
                gameWorld[keyPos.x][keyPos.y] = Tileset.LOCKED_DOOR;
            }
        }
    }

    private void initializePlayer(long seed) {
        player = new Player(seed);
        boolean placed = player.placeInWorld(gameWorld, config.width, config.height);
        if (!placed) gameRunning = false;
    }

    public void runGame() {
        renderGameState();
        boolean awaitingColon = false;

        while (gameRunning) {
            Character key = InputHandler.pollKeyOnce();
            boolean shouldRender = false;

            if (key != null) {
                if (awaitingColon) {
                    if (key == 'q' || key == 'Q') {
                        saveAndExit();; // return to the menu
                        break;
                    } else {
                        awaitingColon = false;
                    }
                } else if (key == ':') {
                    awaitingColon = true;
                } else {
                    boolean moved = handleInput(key);
                    if (!moved) break;

                    checkTrapCollisionsAfterPlayerMove();

                    if (checkGameEndConditions()) break;
                    shouldRender = true;
                }
            }

            // Update the enemy
            adversaryManager.update();

            updateCounter++;
            if (updateCounter >= TRAP_UPDATE_FREQUENCY) {
                updateCounter = 0;
                boolean trapsChanged = trapManager.updateTraps(gameWorld, config.width, config.height);
                if (trapsChanged) {
                    shouldRender = true;
                    checkTrapCollisionsAfterTrapMove();
                    if (healthManager.isDead()) {
                        handlePlayerDeath();
                        break;
                    }
                }
            }
            if (shouldRender) renderGameState();

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // clear the status
        InputHandler.prepareForMenu();
    }

    private void renderGameState() {
        renderer.renderFrame(gameWorld);
        HUD.updateHUDOnly(gameWorld, player, keyManager, healthManager);
    }

    private boolean handleInput(char key) {
        switch (key) {
            case 27: gameRunning = false; return false; // ESC
            case 'w': case 'a': case 's': case 'd':
                return handlePlayerMove(key);
            default:
                return true;
        }
    }

    private boolean handlePlayerMove(char direction) {
        if (healthManager.isDead()) return true;

        boolean moved = player.move(gameWorld, direction, config.width, config.height);
        if (!moved) return true;

        Position newPosition = player.getPosition();
        if (keyManager.checkKeyCollection(newPosition, gameWorld)) {
            TETile underlyingTile = determineUnderlyingTile(newPosition);
            player.updatePreviousTile(underlyingTile);
        }
        return true;
    }

    private void checkTrapCollisionsAfterPlayerMove() {
        trapManager.checkTrapCollisions(player, healthManager, gameWorld, keyManager);
    }

    private void checkTrapCollisionsAfterTrapMove() {
        trapManager.checkTrapCollisions(player, healthManager, gameWorld, keyManager);
    }

    private boolean checkGameEndConditions() {
        if (keyManager.isGameComplete()) {
            handleGameVictory();
            return true;
        }
        if (healthManager.isDead()) {
            handlePlayerDeath();
            return true;
        }
        return false;
    }

    public void damagePlayer() {
        healthManager.decreaseHealth();
        renderGameState();
        System.out.println("Player took damage!");
    }

    private void handlePlayerDeath() {
        System.out.println("Game Over");
        gameRunning = false;
    }

    private void handleGameVictory() {
        System.out.println("You Win");
        gameRunning = false;
    }

    // Updated to use config tiles
    private TETile determineUnderlyingTile(Position position) {
        for (Room room : worldBuilder.getRooms()) {
            if (isPositionInRoom(position, room)) {
                return config.roomFloorTile;
            }
        }
        return config.hallwayFloorTile;
    }

    private boolean isPositionInRoom(Position position, Room room) {
        return position.x >= room.x && position.x < room.x + room.width &&
                position.y >= room.y && position.y < room.y + room.height;
    }

    public void saveAndExit() {
        GameState state = new GameState(
                currentSeed,
                player.getPosition(),
                player.getPreviousTile(),
                keyManager.getKeyPositions(),
                keyManager.getKeysCollected(),
                trapManager.getTrapPositions(),
                healthManager.getCurrentHealth(),
                healthManager.getMaxHealth(),
                adversaryManager.getAdversaryPositions(),
                gameWorld
        );
        SaveGameManager.saveGame(state);
        System.exit(0);
    }

    public List<Position> getRestoredAdversaryPositions() {
        return (gameState != null) ? gameState.adversaryPositions : null;
    }

    public void removeAdversary(Adversary adv) {
        adversaryManager.removeAdversary(adv);
    }

    // Accessors
    public KeyManager getKeyManager() { return keyManager; }
    public HealthManager getHealthManager() { return healthManager; }
    public Player getPlayer() { return player; }
    public Position getPlayerPosition() { return player.getPosition(); }
    public TETile[][] getGameWorld() { return gameWorld; }
    public WorldConfig getConfig() { return config; } // NEW getter
}