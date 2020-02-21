package medium;

import helper.Printer;

/**
 * [213] House Robber
 *
 * Description:
 * You are a professional robber planning to rob houses along a street.
 * Each house has a certain amount of money stashed. All houses at this place are arranged in a circle.
 * That means the first house is the neighbor of the last one.
 * Meanwhile, adjacent houses have security system connected and it will automatically
 * contact the police if two adjacent houses were broken into on the same night.
 *
 * Given a list of non-negative integers representing the amount of money of each house,
 * determine the maximum amount of money you can rob tonight without alerting the police.
 *
 * Example 1:
 * Input: [2,3,2]
 * Output: 3
 * Explanation: You cannot rob house 1 (money = 2) and then rob house 3 (money = 2),
 *              because they are adjacent houses.
 *
 * Example 2:
 * Input: [1,2,3,1]
 * Output: 4
 * Explanation: Rob house 1 (money = 1) and then rob house 3 (money = 3).
 *              Total amount you can rob = 1 + 3 = 4.
 */
public class HouseRobberII {
    /**
     * Solution 1: Dynamic Programming
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     * Result: Accepted (0ms)
     */
    class Solution1 {
        public int rob(int[] nums) {
            int n = nums.length;
            if (n < 2) {
                return n == 0 ? 0 : nums[0];
            }
            int[] dp = new int[n];

            // If decide to rob the first house
            dp[0] = nums[0];
            dp[1] = Math.max(nums[0], nums[1]);
            for (int i = 2; i < n - 1; i++) {
                dp[i] = Math.max(dp[i - 2] + nums[i], dp[i - 1]);
            }
            int maxIfRobFirst = dp[n - 2];

            // If skipping the first house
            dp[0] = 0;
            dp[1] = nums[1];
            for (int i = 2; i < n; i++) {
                dp[i] = Math.max(dp[i - 2] + nums[i], dp[i - 1]);
            }
            int maxIfNotRobFirst = dp[n - 1];

            return Math.max(maxIfRobFirst, maxIfNotRobFirst);
        }
    }

    /**
     * Solution 2: Dynamic Programming
     * Time Complexity: O(N)
     * Space Complexity: O(1)
     * Result: Accepted (0ms)
     */
    class Solution2 {
        public int rob(int[] nums) {
            int n = nums.length;
            if (n < 2) {
                return n == 0 ? 0 : nums[0];
            }
            // If decide to rob the first house
            int rob = nums[0], skip = 0;
            for (int i = 1; i < n; i++) {
                int prevRob = rob;
                rob = Math.max(skip + nums[i], rob);
                skip = Math.max(skip, prevRob);
            }
            int maxIfRobFirst = skip;
            // If skipping the first house
            rob = 0;
            skip = 0;
            for (int i = 1; i < n; i++) {
                int prevRob = rob;
                rob = Math.max(skip + nums[i], rob);
                skip = Math.max(skip, prevRob);
            }
            int maxIfNotRobFirst = Math.max(rob, skip);
            return Math.max(maxIfRobFirst, maxIfNotRobFirst);
        }
    }
    public static void main(String[] args) {
        Printer.printNum(new HouseRobberII().new Solution1().rob(new int[]{2,3,2}));
        Printer.printNum(new HouseRobberII().new Solution2().rob(new int[]{2,3,2}));
    }
}
