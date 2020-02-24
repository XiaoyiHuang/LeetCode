package medium;

import helper.Printer;

/**
 * [518] Coin Change 2
 *
 * Description:
 * You are given coins of different denominations and a total amount of money.
 * Write a function to compute the number of combinations that make up that amount.
 * You may assume that you have infinite number of each kind of coin.

 * Example 1:
 * Input: amount = 5, coins = [1, 2, 5]
 * Output: 4
 * Explanation: there are four ways to make up the amount:
 * 5=5
 * 5=2+2+1
 * 5=2+1+1+1
 * 5=1+1+1+1+1
 *
 * Example 2:
 * Input: amount = 3, coins = [2]
 * Output: 0
 * Explanation: the amount of 3 cannot be made up just with coins of 2.
 *
 * Example 3:
 * Input: amount = 10, coins = [10]
 * Output: 1
 *
 * Note:
 * You can assume that
 *      1. 0 <= amount <= 5000
 *      2. 1 <= coin <= 5000
 *      3. the number of coins is less than 500
 *      4. the answer is guaranteed to fit into signed 32-bit integer
 */
public class CoinChange2 {
    /**
     * Solution 1: Dynamic Programming
     * Time Complexity: O(N * k) (k is the the target amount)
     * Space Complexity: O(N * k)
     * Result: Accepted (8ms)
     */
    class Solution1 {
        public int change(int amount, int[] coins) {
            if (amount < 0) {
                return 0;
            }

            // The number of combinations that make up amount j using first i coins
            int[][] dp = new int[coins.length + 1][amount + 1];
            for (int i = 0; i <= coins.length; i++) {
                dp[i][0] = 1;
            }

            for (int i = 1; i <= coins.length; i++) {
                for (int j = 1; j <= amount; j++) {
                    if (j >= coins[i - 1]) {
                        dp[i][j] = dp[i - 1][j] + dp[i][j - coins[i - 1]];
                    } else {
                        dp[i][j] = dp[i - 1][j];
                    }
                }
            }
            return dp[coins.length][amount];
        }
    }
    /**
     * Solution 2: Dynamic Programming (Space optimization)
     * Explanation: The transition function in Solution 1 is:
     *                           dp[i][j] = dp[i - 1][j] + dp[i][j - coins[i]] (where j is the target amount)
     *
     *              We can see that the i'th dimension is only related to the (i-1)'th state.
     *              Therefore, we can compress the i'th dimension and optimize the space complexity
     *              to O(k).
     *
     *              To ensure that i remains the same while j is changed, the i'th dimension MUST be
     *              in the OUTER loop. With this, when changing j, the state dp[i - 1][j] is naturally
     *              'inherited' to dp[i][j]. This idea is frequently used in dynamic programming's
     *              space optimization.
     *
     * Time Complexity: O(N * k) (k is the the target amount)
     * Space Complexity: O(k)
     * Result: Accepted (2ms)
     */
    class Solution2 {
        public int change(int amount, int[] coins) {
            if (amount < 0) {
                return 0;
            }
            int[] dp = new int[amount + 1];
            dp[0] = 1;
            for (int coin : coins) {
                for (int i = coin; i <= amount; i++) {
                    dp[i] += dp[i - coin];
                }
            }
            return dp[amount];
        }
    }
    public static void main(String[] args) {
        Printer.printNum(new CoinChange2().new Solution2().change(5, new int[]{1,2,5}));
        Printer.printNum(new CoinChange2().new Solution1().change(5, new int[]{1,2,5}));
        Printer.printNum(new CoinChange2().new Solution2().change(3, new int[]{2}));
        Printer.printNum(new CoinChange2().new Solution1().change(3, new int[]{2}));
        Printer.printNum(new CoinChange2().new Solution2().change(10, new int[]{10}));
        Printer.printNum(new CoinChange2().new Solution1().change(10, new int[]{10}));
    }
}
