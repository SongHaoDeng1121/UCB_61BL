package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.awt.Color;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Handles saving and loading of the game state to/from a file.
 */
public class SaveGameManager {
    private static final String SAVE_FILE = "./save.txt";

    private static final TETile PLAYER_TILE =
            new TETile('@', Color.white, Color.black, "player", 0);
    private static final TETile LOCKED_DOOR_TILE =
            new TETile('+', Color.red, Color.black, "locked door", 1);
    private static final TETile UNLOCKED_DOOR_TILE =
            new TETile('/', Color.green, Color.black, "unlocked door", 2);
    private static final TETile KEY_TILE =
            new TETile('k', Color.yellow, Color.black, "key", 3);

    public static void saveGame(GameState state) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SAVE_FILE))) {
            writer.write(Long.toString(state.seed));
            writer.newLine();

            writer.write(state.playerPos.x + " " + state.playerPos.y);
            writer.newLine();

            writer.write(state.previousTile.description());
            writer.newLine();

            writer.write(Integer.toString(state.keysCollected));
            writer.newLine();

            // Save key positions
            writer.write(Integer.toString(state.keyPositions.size()));
            writer.newLine();
            for (Position p : state.keyPositions) {
                writer.write(p.x + " " + p.y);
                writer.newLine();
            }

            // Save trap positions
            writer.write(Integer.toString(state.trapPositions.size()));
            writer.newLine();
            for (Position p : state.trapPositions) {
                writer.write(p.x + " " + p.y);
                writer.newLine();
            }

            // Save lives and maxLives
            writer.write(state.lives + " " + state.maxLives);
            writer.newLine();

            // Save adversary positions
            writer.write(Integer.toString(state.adversaryPositions.size()));
            writer.newLine();
            for (Position p : state.adversaryPositions) {
                writer.write(p.x + " " + p.y);
                writer.newLine();
            }

        } catch (IOException e) {
            System.err.println("Failed to save game: " + e.getMessage());
        }
    }

    public static boolean hasSaveFile() {
        File file = new File(SAVE_FILE);
        return file.exists() && file.isFile();
    }

    public static GameState loadGame() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SAVE_FILE))) {
            long seed = Long.parseLong(reader.readLine());

            String[] posParts = reader.readLine().split(" ");
            Position playerPos = new Position(Integer.parseInt(posParts[0]), Integer.parseInt(posParts[1]));

            String tileDesc = reader.readLine();
            TETile previousTile = tileFromDescription(tileDesc);

            int keysCollected = Integer.parseInt(reader.readLine());

            // Load key positions
            int keyCount = Integer.parseInt(reader.readLine());
            List<Position> keyPositions = new ArrayList<>();
            for (int i = 0; i < keyCount; i++) {
                String[] coords = reader.readLine().split(" ");
                keyPositions.add(new Position(Integer.parseInt(coords[0]), Integer.parseInt(coords[1])));
            }

            // Load trap positions
            int trapCount = Integer.parseInt(reader.readLine());
            List<Position> trapPositions = new ArrayList<>();
            for (int i = 0; i < trapCount; i++) {
                String[] coords = reader.readLine().split(" ");
                trapPositions.add(new Position(Integer.parseInt(coords[0]), Integer.parseInt(coords[1])));
            }

            // Load lives and maxLives
            String[] lifeParts = reader.readLine().split(" ");
            int lives = Integer.parseInt(lifeParts[0]);
            int maxLives = Integer.parseInt(lifeParts[1]);

            // Load adversary positions
            int adversaryCount = Integer.parseInt(reader.readLine());
            List<Position> adversaryPositions = new ArrayList<>();
            for (int i = 0; i < adversaryCount; i++) {
                String[] coords = reader.readLine().split(" ");
                adversaryPositions.add(new Position(Integer.parseInt(coords[0]), Integer.parseInt(coords[1])));
            }

            return new GameState(
                    seed,
                    playerPos,
                    previousTile,
                    keyPositions,
                    keysCollected,
                    trapPositions,
                    lives,
                    maxLives,
                    adversaryPositions
            );

        } catch (IOException e) {
            System.err.println("Failed to load game: " + e.getMessage());
            return null;
        }
    }

    private static TETile tileFromDescription(String desc) {
        switch (desc) {
            case "floor": return Tileset.FLOOR;
            case "wall": return Tileset.WALL;
            case "nothing": return Tileset.NOTHING;
            case "player": return PLAYER_TILE;
            case "locked door": return LOCKED_DOOR_TILE;
            case "unlocked door": return UNLOCKED_DOOR_TILE;
            case "key": return KEY_TILE;
            case "grass": return Tileset.GRASS;
            default: throw new IllegalArgumentException("Unknown tile description: " + desc);
        }
    }
}