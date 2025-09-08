package game2048logic;

import game2048rendering.Side;

/**
 * @author  Josh Hug
 */
public class GameLogic {
    /** Moves the given tile up as far as possible, subject to the minR constraint.
     *
     * @param board the current state of the board
     * @param r     the row number of the tile to move up
     * @param c -   the column number of the tile to move up
     * @param minR  the minimum row number that the tile can land in, e.g.
     *              if minR is 2, the moving tile should move no higher than row 2.
     * @return      if there is a merge, returns the 1 + the row number where the merge occurred.
     *              if no merge occurs, then return minR.
     */
    public static int moveTileUpAsFarAsPossible(int[][] board, int r, int c, int minR) {
        for (int i = r; i > minR; i -= 1) {
            if (board[i - 1][c] == 0) {
                board[i - 1][c] = board[i][c];
                board[i][c] = 0;
            } else  if (board[i - 1][c] == board[i][c]) {
                board[i - 1][c] = 2 * board[i][c];
                board[i][c] = 0;
                return i;
            } else {
                break;
            }
        }



        return minR;
    }

    /**
     * Modifies the board to simulate the process of tilting column c
     * upwards.
     *
     * @param board     the current state of the board
     * @param c         the column to tilt up.
     */
    public static void tiltColumn(int[][] board, int c) {
        int minR = 0;
        for (int r = 0; r < board.length; r++) {
            minR = GameLogic.moveTileUpAsFarAsPossible(board, r, c, minR);
            GameLogic.moveTileUpAsFarAsPossible(board, r, c, minR);
        }
    }

    /**
     * Modifies the board to simulate tilting all columns upwards.
     *
     * @param board     the current state of the board.
     */
    public static void tiltUp(int[][] board) {
        for (int c = 0; c < board.length; c++) {
            GameLogic.tiltColumn(board, c);
        }
        return;
    }

    /**
     * Modifies the board to simulate tilting the entire board to
     * the given side.
     *
     * @param board the current state of the board
     * @param side  the direction to tilt
     */
    public static void tilt(int[][] board, Side side) {
        if (side == Side.NORTH) {
            GameLogic.tiltUp(board);
        } else if (side == Side.EAST) {
            game2048logic.MatrixUtils.rotateLeft(board);
            GameLogic.tiltUp(board);
            game2048logic.MatrixUtils.rotateRight(board);
        } else if (side == Side.SOUTH) {
            game2048logic.MatrixUtils.rotateLeft(board);
            game2048logic.MatrixUtils.rotateLeft(board);
            GameLogic.tiltUp(board);
            game2048logic.MatrixUtils.rotateRight(board);
            game2048logic.MatrixUtils.rotateRight(board);
        } else if (side == Side.WEST) {
            game2048logic.MatrixUtils.rotateRight(board);
            GameLogic.tiltUp(board);
            game2048logic.MatrixUtils.rotateLeft(board);
        } else {
            System.out.println("Invalid side specified");
        }
    }
}
