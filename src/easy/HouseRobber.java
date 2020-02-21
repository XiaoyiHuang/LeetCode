package easy;

import helper.Printer;

/**
 * [198] House Robber
 *
 * Description:
 * You are a professional robber planning to rob houses along a street.
 * Each house has a certain amount of money stashed, the only constraint stopping you
 * from robbing each of them is that adjacent houses have security system connected
 * and it will automatically contact the police if two adjacent houses were broken into on the same night.
 *
 * Given a list of non-negative integers representing the amount of money of each house,
 * determine the maximum amount of money you can rob tonight without alerting the police.
 *
 * Example 1:
 * Input: [1,2,3,1]
 * Output: 4
 * Explanation: Rob house 1 (money = 1) and then rob house 3 (money = 3).
 *              Total amount you can rob = 1 + 3 = 4.
 *
 * Example 2:
 * Input: [2,7,9,3,1]
 * Output: 12
 * Explanation: Rob house 1 (money = 2), rob house 3 (money = 9) and rob house 5 (money = 1).
 *              Total amount you can rob = 2 + 9 + 1 = 12.
 */
public class HouseRobber {
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
            dp[0] = nums[0];
            dp[1] = Math.max(nums[0], nums[1]);
            for (int i = 2; i < n; i++) {
                dp[i] = Math.max(dp[i - 2] + nums[i], dp[i - 1]);
            }
            return dp[n - 1];
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
            int rob = nums[0];
            int noRob = 0;
            for (int i = 1; i < n; i++) {
                int prevRob = rob;
                rob = noRob + nums[i];
                noRob = Math.max(prevRob, noRob);
            }
            return Math.max(rob, noRob);
        }
    }

    public static void main(String[] args) {
        Printer.printNum(new HouseRobber().new Solution1().rob(new int[]{1,2,3,1}));
        Printer.printNum(new HouseRobber().new Solution2().rob(new int[]{1,2,3,1}));

        Printer.printNum(new HouseRobber().new Solution1().rob(new int[]{2,7,9,3,1}));
        Printer.printNum(new HouseRobber().new Solution2().rob(new int[]{2,7,9,3,1}));
    }
}
