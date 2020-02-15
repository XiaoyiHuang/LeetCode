package medium;

import helper.Printer;

/**
 * [723] Candy Crush
 *
 * Description:
 * This question is about implementing a basic elimination algorithm for Candy Crush.
 *
 * Given a 2D integer array board representing the grid of candy, different positive
 * integers board[i][j] represent different types of candies. A value of board[i][j] = 0
 * represents that the cell at position (i, j) is empty. The given board represents
 * the state of the game following the player's move. Now, you need to restore the board
 * to a stable state by crushing candies according to the following rules:
 *
 * 1. If three or more candies of the same type are adjacent vertically or horizontally,
 * "crush" them all at the same time - these positions become empty.
 * 2. After crushing all candies simultaneously, if an empty space on the board has candies
 * on top of itself, then these candies will drop until they hit a candy or bottom at the
 * same time. (No new candies will drop outside the top boundary.)
 * 3. After the above steps, there may exist more candies that can be crushed. If so, you
 * need to repeat the above steps.
 * 4. If there does not exist more candies that can be crushed (ie. the board is stable),
 * then return the current board.
 * 5. You need to perform the above rules until the board becomes stable, then return
 * the current board.
 *
 * Example:
 * Input:
 * board = [[110,5,112,113,114],
 *          [210,211,5,213,214],
 *          [310,311,3,313,314],
 *          [410,411,412,5,414],
 *          [5,1,512,3,3],
 *          [610,4,1,613,614],
 *          [710,1,2,713,714],
 *          [810,1,2,1,1],
 *          [1,1,2,2,2],
 *          [4,1,4,4,1014]]
 *
 * Output: [[0,0,0,0,0],
 *          [0,0,0,0,0],
 *          [0,0,0,0,0],
 *          [110,0,0,0,114],
 *          [210,0,0,0,214],
 *          [310,0,0,113,314],
 *          [410,0,0,213,414],
 *          [610,211,112,313,614],
 *          [710,311,412,613,714],
 *          [810,411,512,713,1014]]
 *
 * Note:
 * The length of board will be in the range [3, 50].
 * The length of board[i] will be in the range [3, 50].
 * Each board[i][j] will initially start as an integer in the range [1, 2000].
 */
public class CandyCrush {
    public int[][] candyCrush(int[][] board) {
        int rows = board.length, cols = board[0].length;
        boolean hasChanged = true;
        while (hasChanged) {
            hasChanged = false;
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    int value = Math.abs(board[r][c]);
                    if (value == 0) {
                        continue;
                    }
                    // Check horizontally
                    if (c + 2 < cols && Math.abs(board[r][c + 1]) == value &&
                            Math.abs(board[r][c + 2]) == value) {
                        // Mark candies as drop-pending
                        board[r][c] = -value;
                        board[r][c + 1] = -value;
                        board[r][c + 2] = -value;
                        hasChanged = true;
                    }
                    // Check vertically
                    if (r + 2 < rows && Math.abs(board[r + 1][c]) == value &&
                            Math.abs(board[r + 2][c]) == value) {
                        // Mark candies as drop-pending
                        board[r][c] = -value;
                        board[r + 1][c] = -value;
                        board[r + 2][c] = -value;
                        hasChanged = true;
                    }
                }
            }
            if (hasChanged) {
                // Drop all marked candies
                for (int c = 0; c < cols; c++) {
                    int currRowIdx = rows - 1;
                    for (int r = rows - 1; r >= 0; r--) {
                        if (board[r][c] <= 0) {
                            continue;
                        }
                        board[currRowIdx][c] = board[r][c];
                        currRowIdx -= 1;
                    }
                    for (int r = currRowIdx; r >= 0; r--) {
                        board[r][c] = 0;
                    }
                }
            }
        }
        return board;
    }
    public static void main(String[] args) {
        int[][] board = new int[][] {
                {110,5,112,113,114},
                {210,211,5,213,214},
                {310,311,3,313,314},
                {410,411,412,5,414},
                {5,1,512,3,3},
                {610,4,1,613,614},
                {710,1,2,713,714},
                {810,1,2,1,1},
                {1,1,2,2,2},
                {4,1,4,4,1014}
        };
        Printer.printArray(new CandyCrush().candyCrush(board));
    }
}
