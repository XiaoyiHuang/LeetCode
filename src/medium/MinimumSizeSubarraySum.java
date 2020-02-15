package medium;

import helper.Printer;

/**
 * [209] Minimum Size Subarray Sum
 *
 * Description:
 * Given an array of n positive integers and a positive integer s,
 * find the minimal length of a contiguous subarray of which the sum ≥ s.
 * If there isn't one, return 0 instead.
 *
 * Example:
 * Input: s = 7, nums = [2,3,1,2,4,3]
 * Output: 2
 * Explanation: the subarray [4,3] has the minimal length under the problem constraint.
 *
 * Follow up:
 * If you have figured out the O(n) solution, try coding another solution of
 * which the time complexity is O(n log n).
 */
public class MinimumSizeSubarraySum {
    /**
     * Solution 1: Sliding Window
     * Time Complexity: O(N)
     * Space Complexity: O(1)
     * Result: Accepted (1ms)
     */
    class Solution1 {
        public int minSubArrayLen(int s, int[] nums) {
            int windowSum = 0, windowFrom = 0, windowTo = -1, minLen = Integer.MAX_VALUE;
            for (int i = 0; i < nums.length; i++) {
                windowSum += nums[i];
                windowTo += 1;
                if (windowSum >= s) {
                    while (windowFrom <= windowTo && windowSum >= s) {
                        windowSum -= nums[windowFrom++];
                    }
                    minLen = Math.min(minLen, windowTo - windowFrom + 2);
                }
                if (minLen == 1) {
                    return minLen;
                }
            }
            return minLen == Integer.MAX_VALUE ? 0 : minLen;
        }
    }

    /**
     * Solution 2: Binary Search
     * Time Complexity: O(NlogN)
     * Space Complexity: O(N)
     * Result: Accepted (2ms)
     */
    class Solution2 {
        public int minSubArrayLen(int s, int[] nums) {
            if (nums.length == 0) {
                return 0;
            }
            int minLen = Integer.MAX_VALUE;
            int[] sums = new int[nums.length];
            sums[0] = nums[0];
            for (int i = 1; i < nums.length; i++) {
                sums[i] = sums[i - 1] + nums[i];
            }
            for (int i = 0; i < nums.length; i++) {
                int lo = i, hi = nums.length - 1;
                while (lo <= hi) {
                    int mid = lo + ((hi - lo) >> 1);
                    int rangeSum = sums[mid] - (i == 0 ? 0 : sums[i - 1]);
                    if (rangeSum < s) {
                        lo = mid + 1;
                    } else if (rangeSum > s) {
                        hi = mid - 1;
                        minLen = Math.min(minLen, mid - i + 1);
                    } else {
                        minLen = Math.min(minLen, mid - i + 1);
                        break;
                    }
                }
            }
            return minLen == Integer.MAX_VALUE ? 0 : minLen;
        }
    }
    public static void main(String[] args) {
        Printer.printNum(new MinimumSizeSubarraySum().new Solution1()
                .minSubArrayLen(7, new int[]{2,3,1,2,4,3}));
        Printer.printNum(new MinimumSizeSubarraySum().new Solution1()
                .minSubArrayLen(11, new int[]{1,2,3,4,5}));
    }
}
