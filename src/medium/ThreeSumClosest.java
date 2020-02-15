package medium;

import helper.Printer;

import java.util.Arrays;

/**
 * [16] 3Sum Closest
 *
 * Description:
 * Given an array nums of n integers and an integer target,
 * find three integers in nums such that the sum is closest to target.
 * Return the sum of the three integers.
 * You may assume that each input would have exactly one solution.
 *
 * Example:
 * Given array nums = [-1, 2, 1, -4], and target = 1.
 * The sum that is closest to the target is 2. (-1 + 2 + 1 = 2).
 *
 * Result: Accepted (3ms)
 */
public class ThreeSumClosest {
    public int threeSumClosest(int[] nums, int target) {
        if (nums.length < 3) {
            return 0;
        }
        Arrays.sort(nums);
        int minDiff = Integer.MAX_VALUE;
        int closestSum = Integer.MAX_VALUE;
        for (int i = 0; i < nums.length - 2; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            int threeSum = nums[i] + twoSumClosest(nums, target - nums[i], i + 1);
            int currDiff;
            if ((currDiff = Math.abs(threeSum - target)) < minDiff) {
                minDiff = currDiff;
                closestSum = threeSum;
            }
        }
        return closestSum;
    }
    public int twoSumClosest(int[] nums, int target, int baseIdx) {
        int lo = baseIdx, hi = nums.length - 1;
        int minDiff = Integer.MAX_VALUE;
        int closestSum = Integer.MAX_VALUE;
        while (lo < hi) {
            int twoSum = nums[lo] + nums[hi];
            int currDiff;
            if ((currDiff = Math.abs(twoSum - target)) < minDiff) {
                minDiff = currDiff;
                closestSum = twoSum;
            }
            if (twoSum < target) {
                lo += 1;
                while (lo < hi && nums[lo] == nums[lo - 1]) {
                    lo += 1;
                }
            } else if (twoSum > target) {
                hi -= 1;
                while (hi > lo && nums[hi] == nums[hi + 1]) {
                    hi -= 1;
                }
            } else {
                return nums[lo] + nums[hi];
            }
        }
        return closestSum;
    }
    public static void main(String[] args) {
        Printer.printNum(new ThreeSumClosest().threeSumClosest(new int[]{-1,2,1,-4}, 1));
    }
}
