package easy;

import helper.Printer;

/**
 * [53] Maximum Subarray
 *
 * Description:
 * Given an integer array nums, find the contiguous subarray (containing at least one number)
 * which has the largest sum and return its sum.
 *
 * Example:
 * Input: [-2,1,-3,4,-1,2,1,-5,4],
 * Output: 6
 * Explanation: [4,-1,2,1] has the largest sum = 6.
 *
 * Follow up:
 * If you have figured out the O(n) solution, try coding another solution using the
 * divide and conquer approach, which is more subtle.
 */
public class MaximumSubarray {
    /**
     * Solution 1: Dynamic Programming (Knapsack)
     * Time Complexity: O(N)
     * Space Complexity: O(1)
     * Result: Accepted (1ms)
     */
    class Solution1 {
        public int maxSubArray(int[] nums) {
            if (nums.length == 0) {
                return 0;
            }
            int extend = nums[0], restart = nums[0], maxSum = nums[0];
            for (int i = 1; i < nums.length; i++) {
                extend = Math.max(extend, restart) + nums[i];
                restart = nums[i];
                maxSum = Math.max(maxSum, Math.max(extend, restart));
            }
            return maxSum;
        }
    }
    /**
     * Solution 2: Dynamic Programming
     * Time Complexity: O(N)
     * Space Complexity: O(1)
     * Result: Accepted (1ms)
     */
    class Solution2 {
        public int maxSubArray(int[] nums) {
            if (nums.length == 0) {
                return 0;
            }
            int currSum = 0, maxSum = Integer.MIN_VALUE;
            for (int num : nums) {
                currSum += num;
                maxSum = Math.max(maxSum, currSum);
                currSum = Math.max(currSum, 0);
            }
            return maxSum;
        }
    }

    public static void main(String[] args) {
        Printer.printNum(new MaximumSubarray().new Solution1().maxSubArray(new int[]{-2,1,-3,4,-1,2,1,-5,4}));
        Printer.printNum(new MaximumSubarray().new Solution2().maxSubArray(new int[]{-2,1,-3,4,-1,2,1,-5,4}));
    }
}
