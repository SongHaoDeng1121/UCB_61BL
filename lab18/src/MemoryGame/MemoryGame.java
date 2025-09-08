package MemoryGame;

import byowTools.RandomUtils;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    /** The width of the window of this game. */
    private int width;
    /** The height of the window of this game. */
    private int height;
    /** The current round the user is on. */
    private int round;
    /** The Random object used to randomly generate Strings. */
    private Random rand;
    /** Whether or not the game is over. */
    private boolean gameOver;
    /** Whether or not it is the player's turn. */
    private boolean playerTurn;
    /** The characters we generate random Strings from. */
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    /** Encouraging phrases. */
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    private String encouragementMessage;

    public static void main(String[] args) {
        long seed;
        if (args.length < 1) {
            System.out.println("No seed provided. Using default seed 1234.");
            seed = 1234;
        } else {
            seed = Long.parseLong(args[0]);
        }

        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();

    }

    public MemoryGame(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        this.rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        //TODO: Generate random string of letters of length n
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            char c = CHARACTERS[rand.nextInt(CHARACTERS.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    public void drawFrame(String s) {
        drawFrame(s, true);
    }

    public void drawFrame(String s, boolean unusedShowHeader) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);

        // 中间文字（显示字符）
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(this.width / 2, this.height / 2, s);

        drawHeader();
        StdDraw.show();
    }


    private void drawHeader() {
        Font fontSmall = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(fontSmall);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.line(0, this.height - 2, this.width, this.height - 2);
        StdDraw.textLeft(0.5, this.height - 1, "Round: " + this.round);
        StdDraw.text(this.width / 2, this.height - 1, this.playerTurn ? "Type!" : "Watch!");
        StdDraw.textRight(this.width - 0.5, this.height - 1, this.encouragementMessage);
    }




    public void flashSequence(String letters) {
        //TODO: Display each character in letters, making sure to blank the screen between letters
        for (int i = 0; i < letters.length(); i++) {
            drawFrame(Character.toString(letters.charAt(i)));
            StdDraw.pause(1000);

            StdDraw.clear(Color.BLACK);
            drawHeader();
            StdDraw.show();
            StdDraw.pause(500);
        }
    }

    public String solicitNCharsInput(int n) {
        //TODO: Read n letters of player input
        StringBuilder input = new StringBuilder();
        while (input.length() < n) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                input.append(c);
                drawFrame(input.toString());
            }
        }
        StdDraw.pause(500);
        return input.toString();
    }

    public void startGame() {
        //TODO: Set any relevant variables before the game starts
        this.round = 1;
        this.gameOver = false;
        this.encouragementMessage = ENCOURAGEMENT[RandomUtils.uniform(this.rand, ENCOURAGEMENT.length)];


        //TODO: Establish Engine loop
        while (!gameOver) {
            this.playerTurn = false;
            String target = generateRandomString(round);
            flashSequence(target);

            this.playerTurn = true;
            String typed = solicitNCharsInput(round);

            if (!typed.equals(target)) {
                gameOver = true;
                break;
            }
            round++;
            drawFrame("Correct! Get ready for round " + round + "...");
            StdDraw.pause(1000);
        }

        drawFrame("Game Over! You made it to round: " + this.round);

    }

}
