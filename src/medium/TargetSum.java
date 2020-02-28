package medium;

import helper.Printer;

import java.util.Arrays;

/**
 * [494] Target Sum
 *
 * Description:
 * You are given a list of non-negative integers, a1, a2, ..., an, and a target, S.
 * Now you have 2 symbols + and -. For each integer, you should choose one from + and - as its new symbol.
 *
 * Find out how many ways to assign symbols to make sum of integers equal to target S.
 *
 * Example 1:
 * Input: nums is [1, 1, 1, 1, 1], S is 3.
 * Output: 5
 * Explanation:
 *      -1+1+1+1+1 = 3
 *      +1-1+1+1+1 = 3
 *      +1+1-1+1+1 = 3
 *      +1+1+1-1+1 = 3
 *      +1+1+1+1-1 = 3
 * There are 5 ways to assign symbols to make the sum of nums be target 3.
 *
 * Note:
 *      1. The length of the given array is positive and will not exceed 20.
 *      2. The sum of elements in the given array will not exceed 1000.
 *      3. Your output answer is guaranteed to be fitted in a 32-bit integer.
 */
public class TargetSum {
    /**
     * Solution 1: Dynamic Programming
     * Time Complexity: O(N * s) (s = sum(nums) * 2 + 1)
     * Space Complexity: O(N * s)
     * Result: Accepted (16ms)
     */
    class Solution1 {
        public int findTargetSumWays(int[] nums, int S) {
            int maxSum = 0;
            for (int num : nums) {
                maxSum += num;
            }
            if (S > maxSum || S < -maxSum) {
                return 0;
            }
            int sumRange = (maxSum << 1);
            int[][] dp = new int[nums.length + 1][sumRange + 1];
            dp[0][maxSum] = 1;
            for (int i = 1; i <= nums.length; i++) {
                for (int j = 0; j <= sumRange; j++) {
                    if (j <= sumRange - nums[i - 1]) {
                        // If using symbol '-'
                        dp[i][j] += dp[i - 1][j + nums[i - 1]];
                    }
                    if (j >= nums[i - 1]) {
                        // If using symbol '+'
                        dp[i][j] += dp[i - 1][j - nums[i - 1]];
                    }
                }
            }
            return dp[nums.length][S + maxSum];
        }
    }
    /**
     * Solution 2: Dynamic Programming
     * Time Complexity: O(N * s) (s = sum(nums) * 2 + 1)
     * Space Complexity: O(s)
     * Result: Accepted (8ms)
     */
    class Solution2 {
        public int findTargetSumWays(int[] nums, int S) {
            int maxSum = 0;
            for (int num : nums) {
                maxSum += num;
            }
            if (S > maxSum || S < -maxSum) {
                return 0;
            }
            int sumRange = (maxSum << 1);
            int[] dp = new int[sumRange + 1];
            dp[maxSum] = 1;
            for (int num : nums) {
                int[] tmp = new int[sumRange + 1];
                for (int j = 0; j <= sumRange; j++) {
                    if (j <= sumRange - num) {
                        // If using symbol '-'
                        tmp[j] += dp[j + num];
                    }
                    if (j >= num) {
                        // If using symbol '+'
                        tmp[j] += dp[j - num];
                    }
                }
                dp = tmp;
            }
            return dp[S + maxSum];
        }
    }

    /**
     * Solution 3: DFS + Memoization
     * Time Complexity: O(N * s) (s = sum(nums) * 2 + 1)
     * Space Complexity: O(N * s)
     * Result: Accepted (8ms)
     */
    class Solution3 {
        public int findTargetSumWays(int[] nums, int S) {
            int maxSum = 0;
            for (int num : nums) {
                maxSum += num;
            }
            if (S > maxSum || S < -maxSum) {
                return 0;
            }
            int[][] memo = new int[nums.length][(maxSum << 1) + 1];
            for (int[] m : memo) {
                Arrays.fill(m, -1);
            }
            return find(nums, S + maxSum, maxSum, nums.length - 1, memo);
        }
        private int find(int[] nums, int target, int maxSum, int index, int[][] memo) {
            if (target >= memo[0].length || target < 0) {
                return 0;
            }
            if (index < 0) {
                return target == maxSum ? 1 : 0;
            }
            if (memo[index][target] >= 0) {
                return memo[index][target];
            }
            int pos = find(nums, target - nums[index], maxSum, index - 1, memo);
            int neg = find(nums, target + nums[index], maxSum, index - 1, memo);
            return memo[index][target] = (pos + neg);
        }
    }

    /**
     * Solution 4: Dynamic Programming
     * Explanation: The problem can be interpreted as grouping the numbers into two subsets:
     *              the positive subset(P), and the negative subset(N). It can be transformed
     *              into a subset problem with the following derivations:
     *                      P + N + (P - N) = S + sum(nums) = 2P
     *              With this, the final subset problem would be: finding a subset of numbers
     *              that sum up to (S + sum(nums)) / 2. Turns out this conversion greatly
     *              improves both the runtime and memory performance.
     * Time Complexity: O(N * s) (s = (S + sum(nums)) / 2, see explanation above)
     * Space Complexity: O(s)
     * Result: Accepted (1ms)
     */
    class Solution4 {
        public int findTargetSumWays(int[] nums, int S) {
            int maxSum = 0;
            for (int num : nums) {
                maxSum += num;
            }
            if (S > maxSum || S < -maxSum || ((S + maxSum) & 1) == 1) {
                return 0;
            }
            int positiveSum = (S + maxSum) >> 1;
            int[] positiveSet = new int[positiveSum + 1];
            positiveSet[0] = 1;
            for (int num : nums) {
                for (int j = positiveSum; j >= num; j--) {
                    positiveSet[j] += positiveSet[j - num];
                }
            }
            return positiveSet[positiveSum];
        }
    }
    public static void main(String[] args) {
        Printer.printNum(new TargetSum().new Solution1().findTargetSumWays(new int[]{1,1,1,1,1}, 3));
        Printer.printNum(new TargetSum().new Solution2().findTargetSumWays(new int[]{1,1,1,1,1}, 3));
        Printer.printNum(new TargetSum().new Solution3().findTargetSumWays(new int[]{1,1,1,1,1}, 3));
        Printer.printNum(new TargetSum().new Solution4().findTargetSumWays(new int[]{1,1,1,1,1}, 3));
        Printer.printNum(new TargetSum().new Solution1().findTargetSumWays(new int[]{1}, 1));
        Printer.printNum(new TargetSum().new Solution2().findTargetSumWays(new int[]{1}, 1));
        Printer.printNum(new TargetSum().new Solution3().findTargetSumWays(new int[]{1}, 1));
        Printer.printNum(new TargetSum().new Solution4().findTargetSumWays(new int[]{1}, 1));
    }
}
