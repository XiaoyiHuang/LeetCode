package medium;

import helper.Printer;

import java.util.Arrays;

/**
 * [377] Combination Sum IV
 *
 * Description:
 * Given an integer array with all positive numbers and no duplicates,
 * find the number of possible combinations that add up to a positive integer target.
 *
 * Example:
 * nums = [1, 2, 3]
 * target = 4
 * The possible combination ways are:
 * (1, 1, 1, 1)
 * (1, 1, 2)
 * (1, 2, 1)
 * (1, 3)
 * (2, 1, 1)
 * (2, 2)
 * (3, 1)
 * Note that different sequences are counted as different combinations.
 * Therefore the output is 7.
 *
 * Follow up:
 * What if negative numbers are allowed in the given array?
 * How does it change the problem?
 * What limitation we need to add to the question to allow negative numbers?
 */
public class CombinationSumIV {
    /**
     * Solution 1: Brute Force
     * Time Complexity: O(N*K) (K denotes target)
     * Space Complexity: O(1)
     * Result: TLE
     */
    class Solution1 {
        public int combinationSum4(int[] nums, int target) {
            Arrays.sort(nums);
            int combCount = 0;
            for (int i = 0; i < nums.length; i++) {
                combCount = helper(nums, target - nums[i], combCount);
            }
            return combCount;
        }
        private int helper(int[] nums, int target, int combCount) {
            if (target == 0) {
                return combCount + 1;
            }
            for (int i = 0; i < nums.length; i++) {
                if (nums[i] > target) {
                    return combCount;
                }
                combCount = helper(nums, target - nums[i], combCount);
            }
            return combCount;
        }
    }

    /**
     * Solution 2: Dynamic Programming
     * Time Complexity: O(N*K) (K denotes target)
     * Space Complexity: O(K)
     * Result: Accepted (1ms)
     */
    class Solution2 {
        public int combinationSum4(int[] nums, int target) {
            int[] dp = new int[target + 1];
            Arrays.fill(dp, -1);
            dp[0] = 1;
            return helper(nums, target, dp);
        }
        private int helper(int[] nums, int target, int[] dp) {
            if (dp[target] != -1) {
                return dp[target];
            }
            int res = 0;
            for (int i = 0; i < nums.length; i++) {
                if (target >= nums[i]) {
                    res += helper(nums, target - nums[i], dp);
                }
            }
            dp[target] = res;
            return res;
        }
    }
    public static void main(String[] args) {
        Printer.printNum(new CombinationSumIV().new Solution2().combinationSum4(new int[]{1,2,3}, 4));
    }
}
