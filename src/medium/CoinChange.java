package medium;

import helper.Printer;

import java.util.Arrays;

/**
 * [322] Coin Change
 *
 * Description:
 * You are given coins of different denominations and a total amount of money amount.
 * Write a function to compute the fewest number of coins that you need to make up that amount.
 * If that amount of money cannot be made up by any combination of the coins, return -1.
 *
 * Example 1:
 * Input: coins = [1, 2, 5], amount = 11
 * Output: 3
 * Explanation: 11 = 5 + 5 + 1
 *
 * Example 2:
 * Input: coins = [2], amount = 3
 * Output: -1
 */
public class CoinChange {
    /**
     * Solution 2: Dynamic Programming (Iterative)
     * Time Complexity: O(N * k + NlogN) (k is the total amount)
     * Space Complexity: O(k)
     * Result: Accepted (12ms)
     */
    class Solution1 {
        public int coinChange(int[] coins, int amount) {
            if (amount < 0 || coins.length == 0) {
                return -1;
            }
            if (amount == 0) {
                return 0;
            }
            Arrays.sort(coins);
            int[] dp = new int[amount + 1];
            for (int coin : coins) {
                for (int j = 0; j <= amount - coin; j++) {
                    if (j > 0 && dp[j] == 0) {
                        continue;
                    }
                    int coinSum = j + coin;
                    if (dp[coinSum] == 0) {
                        dp[coinSum] = dp[j] + 1;
                    } else {
                        dp[coinSum] = Math.min(dp[coinSum], dp[j] + 1);
                    }
                }
            }
            return dp[amount] == 0 ? -1 : dp[amount];
        }
    }

    /**
     * Solution 2: Dynamic Programming (Iterative)
     * Time Complexity: O(N * k) (k is the total amount)
     * Space Complexity: O(k)
     * Result: Accepted (11ms)
     */
    class Solution2 {
        public int coinChange(int[] coins, int amount) {
            if (coins.length == 0 || amount < 0) {
                return -1;
            }
            int max = amount + 1;
            int[] dp = new int[amount + 1];
            Arrays.fill(dp, max);
            dp[0] = 0;
            for (int i = 1; i <= amount; i++) {
                for (int coin : coins) {
                    if (coin <= i) {
                        dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                    }
                }
            }
            return dp[amount] == max ? -1 : dp[amount];
        }
    }

    /**
     * Solution 3: Dynamic Programming (Recursive)
     * Time Complexity: O(N * k) (k is the total amount)
     * Space Complexity: O(k)
     * Result: Accepted (23ms)
     */
    class Solution3 {
        public int coinChange(int[] coins, int amount) {
            if (amount < 0 || coins.length == 0) {
                return -1;
            }
            return useCoin(coins, amount, new int[amount + 1]);
        }
        private int useCoin(int[] coins, int amount, int[] states) {
            if (amount < 0) {
                return -1;
            }
            if (amount == 0) {
                return 0;
            }
            if (states[amount] != 0) {
                return states[amount];
            }
            int minUseNum = Integer.MAX_VALUE;
            for (int coin : coins) {
                int useNum = useCoin(coins, amount - coin, states);
                if (useNum >= 0) {
                    minUseNum = Math.min(minUseNum, useNum + 1);
                }
            }
            states[amount] = (minUseNum == Integer.MAX_VALUE) ? -1 : minUseNum;
            return states[amount];
        }
    }

    /**
     * Solution 4: Sorting + DFS
     * Explanation: For certain amount, we always try with the coins with max possible denominations first,
     *              which turns out to be quite efficient, as it naturally eliminates many "impossible to happen"
     *              scenarios, hence significantly trimming the paths we need to cover.
     * Time Complexity: O(k ^ N) (k is the total amount)
     *                  (Since every coin denomination could have s/c(i) values, the total number of
     *                   possible combinations is:
     *                        [k/c(1)] * [k/c(2)] * ... * [k/c(N)] = (k^N) / [c(1)*c(2)*...*c(N)].
     *                   However, as our strategy starts with the largest possible coin denominations,
     *                   we make sure that the minimum result are retrieved "as soon as possible".
     *                   Therefore, many of the above combinations are in fact skipped in practice.)
     * Space Complexity: O(N)
     * Result: Accepted (1ms)
     */
    class Solution4 {
        int minCoinNum = Integer.MAX_VALUE;
        public int coinChange(int[] coins, int amount) {
            if (amount < 0 || coins.length == 0) {
                return -1;
            }
            if (amount == 0) {
                return 0;
            }
            Arrays.sort(coins);
            dfs(coins, amount, coins.length - 1, 0);
            return (minCoinNum == Integer.MAX_VALUE) ? -1 : minCoinNum;
        }
        private void dfs(int[] coins, int remain, int index, int coinNum) {
            if (remain < 0) {
                return;
            }
            for (int num = remain / coins[index]; num >= 0; num--) {
                int currRemain = remain - coins[index] * num;
                if (currRemain == 0) {
                    minCoinNum = Math.min(minCoinNum, coinNum + num);
                }
                if (coinNum + num + 1 < minCoinNum && index > 0) {
                    // Trim unnecessary paths
                    dfs(coins, currRemain, index - 1, coinNum + num);
                } else {
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        Printer.printNum(new CoinChange().new Solution1().coinChange(new int[]{1,2,5}, 11));
        Printer.printNum(new CoinChange().new Solution1().coinChange(new int[]{2}, 3));
        Printer.printNum(new CoinChange().new Solution1().coinChange(new int[]{1,2,5,8}, 18));
        Printer.printNum(new CoinChange().new Solution1().coinChange(new int[]{186,419,83,408}, 6249));
    }
}
