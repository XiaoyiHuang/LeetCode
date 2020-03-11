package hard;

import helper.Printer;

/**
 * [174] Dungeon Game
 *
 * Description:
 * The demons had captured the princess (P) and imprisoned her in the bottom-right corner of a dungeon.
 * The dungeon consists of M x N rooms laid out in a 2D grid. Our valiant knight (K) was initially
 * positioned in the top-left room and must fight his way through the dungeon to rescue the princess.
 *
 * The knight has an initial health point represented by a positive integer.
 * If at any point his health point drops to 0 or below, he dies immediately.
 *
 * Some of the rooms are guarded by demons, so the knight loses health (negative integers)
 * upon entering these rooms; other rooms are either empty (0's) or contain magic orbs that
 * increase the knight's health (positive integers).
 *
 * In order to reach the princess as quickly as possible, the knight decides to move only
 * rightward or downward in each step.
 *
 * Write a function to determine the knight's minimum initial health so that he is able to rescue the princess.
 *
 * For example, given the dungeon below, the initial health of the knight must be at least 7 if he
 * follows the optimal path RIGHT -> RIGHT -> DOWN -> DOWN.
 *
 *                                      -2 (K)  -3	    3
 *                                      -5	    -10	    1
 *                                      10	    30	    -5 (P)
 *
 *
 * Note:
 *   [1] The knight's health has no upper bound.
 *   [2] Any room can contain threats or power-ups, even the first room the knight enters
 *       and the bottom-right room where the princess is imprisoned.
 */
public class DungeonGame {
    /**
     * Solution 1: Dynamic Programming
     * Explanation:
     *              The following solution is somewhat rudimentary, and the state function is kind of hard
     *              to perceive. The second solution has a similar idea, but seems to be much better for
     *              understanding.
     *
     *              The HPSum in this solution is derived by traversing from (M - 1, N - 1) to the origin.
     *              Each value HPSum[i][j] denotes the minimum HP losses if we take a route from (M - 1, N - 1)
     *              to (i, j). As the knight only moves rightward or downward, HPSum[i][j] is only related to
     *              HPSum[i + 1][j] and HPSum[i][j + 1]. We take the value of the neighboring nodes, and
     *              simply add it with the room value at (i, j).
     *
     *              One thing worth mentioning is that when HPSum[i + 1][j] (or HPSum[i][j + 1]) is positive,
     *              while room value is negative, instead of doing HPSum[i + 1][j] + roomValue, we need to
     *              reset HPSum[i + 1][j] to the room value. If sum value at neighbor nodes are positive,
     *              it means that as long as the knight is alive (HP >= 1) when reaching (i + 1, j), he will
     *              definitely be able to rescue the princess. However, with a negative room value at (i, j),
     *              the HP requirement for (i, j) is no longer HP >= 1. It should be HP > -roomValue instead.
     *
     * Time Complexity: O(M * N)
     * Space Complexity: O(M * N)
     * Result: Accepted (3ms)
     */
    class Solution1 {
        public int calculateMinimumHP(int[][] dungeon) {
            int M = dungeon.length;
            int N = dungeon[0].length;
            int[][] HPSum = new int[M][N];
            HPSum[M - 1][N - 1] = dungeon[M - 1][N - 1];
            for (int i = M - 1; i >= 0; i--) {
                for (int j = N - 1; j >= 0; j--) {
                    int roomValue = dungeon[i][j];
                    if (i == M - 1 && j == N - 1) {
                        continue;
                    }
                    HPSum[i][j] = Integer.MIN_VALUE;
                    if (j < N - 1) {
                        int rightSum;
                        if (HPSum[i][j + 1] > 0 && roomValue < 0) {
                            rightSum = roomValue;                       // Reset sum to room value
                        } else {
                            rightSum = HPSum[i][j + 1] + roomValue;
                        }
                        HPSum[i][j] = Math.max(rightSum, HPSum[i][j]);
                    }
                    if (i < M - 1) {
                        int bottomSum;
                        if (HPSum[i + 1][j] > 0 && roomValue < 0) {
                            bottomSum = roomValue;                      // Reset sum to room value
                        } else {
                            bottomSum = HPSum[i + 1][j] + roomValue;
                        }
                        HPSum[i][j] = Math.max(bottomSum, HPSum[i][j]);
                    }
                }
            }
            return Math.max(1, 1 - HPSum[0][0]);
        }
    }

    /**
     * Solution 2: Dynamic Programming
     * Explanation: In this solution, minHP[i][j] represents the minimum health required to rescue the princess
     *              at dungeon (i, j)
     * Time Complexity: O(M * N)
     * Space Complexity: O(M * N)
     * Result: Accepted (3ms)
     */
    class Solution2 {
        public int calculateMinimumHP(int[][] dungeon) {
            int M = dungeon.length;
            int N = dungeon[0].length;
            int[][] minHP = new int[M + 1][N + 1];
            for (int i = 0; i < M; i++) {
                minHP[i][N] = Integer.MAX_VALUE;
            }
            for (int j = 0; j < N; j++) {
                minHP[M][j] = Integer.MAX_VALUE;
            }
            minHP[M][N - 1] = minHP[M - 1][N] = 1;

            for (int i = M - 1; i >= 0; i--) {
                for (int j = N - 1; j >= 0; j--) {
                    int min = Math.min(minHP[i + 1][j], minHP[i][j + 1]) - dungeon[i][j];
                    minHP[i][j] = Math.max(1, min);
                }
            }
            return minHP[0][0];
        }
    }
    public static void main(String[] args) {
        int[][] dungeon = new int[][]{{-2,-3,3},{-5,-10,1},{10,30,-5}};
        Printer.printNum(new DungeonGame().new Solution2().calculateMinimumHP(dungeon));
        dungeon = new int[][]{{1,-3,3},{0,-2,0},{-3,-3,-3}};
        Printer.printNum(new DungeonGame().new Solution2().calculateMinimumHP(dungeon));

    }
}
