package medium;

import helper.Printer;

import java.util.Arrays;

/**
 * [64] Minimum Path Sum
 *
 * Description:
 * Given a m x n grid filled with non-negative numbers, find a path from top left to
 * bottom right which minimizes the sum of all numbers along its path.
 *
 * Note: You can only move either down or right at any point in time.
 *
 * Example:
 * Input:
 * [
 *   [1,3,1],
 *   [1,5,1],
 *   [4,2,1]
 * ]
 * Output: 7
 * Explanation: Because the path 1→3→1→1→1 minimizes the sum.
 */
public class MinimumPathSum {
    /**
     * Solution 1: Dynamic Programming (Top-down)
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     * Result: Accepted (1ms)
     */
    class Solution1 {
        public int minPathSum(int[][] grid) {
            if (grid.length == 0 || grid[0].length == 0) {
                return 0;
            }
            int M = grid.length;
            int N = grid[0].length;
            int[][] memo = new int[M][N];
            for (int[] row : memo) {
                Arrays.fill(row, -1);
            }
            memo[0][0] = grid[0][0];
            return find(grid, M - 1, N - 1, memo);
        }
        private int find(int[][] grid, int x, int y, int[][] memo) {
            if (x < 0 || y < 0) {
                return Integer.MAX_VALUE;
            }
            if (memo[x][y] >= 0) {
                return memo[x][y];
            }
            memo[x][y] = Math.min(find(grid, x - 1, y, memo), find(grid, x, y - 1, memo)) + grid[x][y];
            return memo[x][y];
        }
    }
    /**
     * Solution 1: Dynamic Programming (Bottom-up)
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     * Result: Accepted (2ms)
     */
    class Solution2 {
        public int minPathSum(int[][] grid) {
            int M = grid.length;
            int N = grid[0].length;
            int[][] dp = new int[M + 1][N + 1];
            for (int i = 2; i <= M; i++) {
                dp[i][0] = Integer.MAX_VALUE;
            }
            for (int j = 2; j <= N; j++) {
                dp[0][j] = Integer.MAX_VALUE;
            }
            for (int i = 1; i <= M; i++) {
                for (int j = 1; j <= N; j++) {
                    dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + grid[i - 1][j - 1];
                }
            }
            return dp[M][N];
        }
    }
    public static void main(String[] args) {
        int[][] grid = new int[][]{{1,3,1},{1,5,1},{4,2,1}};
        Printer.printNum(new MinimumPathSum().new Solution1().minPathSum(grid));
        Printer.printNum(new MinimumPathSum().new Solution2().minPathSum(grid));
    }
}
