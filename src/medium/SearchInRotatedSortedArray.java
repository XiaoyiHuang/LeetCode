package medium;

import helper.Printer;

/**
 * [33] Search In Rotated Sorted Array
 *
 * Description:
 * Suppose an array sorted in ascending order is rotated at some
 * pivot unknown to you beforehand.
 *
 * (i.e., [0,1,2,4,5,6,7] might become [4,5,6,7,0,1,2]).
 *
 * You are given a target value to search. If found in the array
 * return its index, otherwise return -1.
 *
 * You may assume no duplicate exists in the array.
 *
 * Your algorithm's runtime complexity must be in the order
 * of O(log n).
 *
 * Example 1:
 * Input: nums = [4,5,6,7,0,1,2], target = 0
 * Output: 4
 *
 * Example 2:
 * Input: nums = [4,5,6,7,0,1,2], target = 3
 * Output: -1
 *
 * Result:
 * Solution 1: Accepted (0ms)
 * Solution 2: Accepted (0ms)
 */
public class SearchInRotatedSortedArray {
    /**
     * Solution 1: One-pass
     * Time Complexity: O(logN)
     * Space Complexity: O(1)
     */
    class Solution1 {
        // Scenario 1: m > l > r (pivot is in the right half)
        // Scenario 2: m < r < l (pivot is in the left half, or at m)
        // Scenario 3: l < m < r (pivot is in the left half)
        public int search(int[] nums, int target) {
            int lPtr = 0, rPtr = nums.length - 1;
            while (lPtr <= rPtr) {
                int mPtr = lPtr + ((rPtr - lPtr) >> 1);
                if (target == nums[mPtr]) {
                    return mPtr;
                }
                else if (target > nums[mPtr]) {
                    if (nums[mPtr] >= nums[lPtr]) {
                        lPtr = mPtr + 1;
                    } else if (target > nums[rPtr]) {
                        rPtr = mPtr - 1;
                    } else {
                        lPtr = mPtr + 1;
                    }
                } else {
                    if (nums[mPtr] >= nums[lPtr]) {
                        if (target >= nums[lPtr]) {
                            rPtr = mPtr - 1;
                        } else {
                            lPtr = mPtr + 1;
                        }
                    } else {
                        rPtr = mPtr - 1;
                    }
                }
            }
            return -1;
        }
    }

    /**
     * Solution 2: Two-pass
     * Time Complexity: O(logN)
     * Space Complexity: O(1)
     */
    class Solution2 {
        public int search(int[] nums, int target) {
            if (nums.length == 0) {
                return -1;
            }
            // Find min value of the sequence first
            int lPtr = 0, rPtr = nums.length - 1, minIndex;
            while (lPtr < rPtr) {
                int mPtr = lPtr + ((rPtr - lPtr) >> 1);
                if (nums[mPtr] >= nums[lPtr]) {
                    if (nums[mPtr] > nums[rPtr]) {
                        // Scenario 1
                        lPtr = mPtr + 1;
                    } else {
                        // Scenario 3
                        rPtr = mPtr - 1;
                    }
                } else {
                    // Scenario 2
                    rPtr = mPtr;
                }
            }
            minIndex = lPtr;
            // Then locate the target
            if (target > nums[nums.length - 1]) {
                rPtr = minIndex - 1;
                lPtr = 0;
            } else {
                lPtr = minIndex;
                rPtr = nums.length - 1;
            }
            while (lPtr <= rPtr) {
                int mPtr = lPtr + ((rPtr - lPtr) >> 1);
                if (nums[mPtr] == target) {
                    return mPtr;
                } else if (nums[mPtr] > target) {
                    rPtr = mPtr - 1;
                } else {
                    lPtr = mPtr + 1;
                }
            }

            return -1;
        }
    }
    public static void main(String[] args) {
        Printer.printNum(new SearchInRotatedSortedArray().new Solution1().search(new int[]{4,5,6,7,0,1,2}, 0));
        Printer.printNum(new SearchInRotatedSortedArray().new Solution2().search(new int[]{4,5,6,7,0,1,2}, 0));
        Printer.printNum(new SearchInRotatedSortedArray().new Solution1().search(new int[]{4,5,6,7,8,9,0,1}, 7));
        Printer.printNum(new SearchInRotatedSortedArray().new Solution2().search(new int[]{4,5,6,7,8,9,0,1}, 7));
    }
}
