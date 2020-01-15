package medium;

import helper.Printer;

/**
 * [34] Find First and Last Position of Element in Sorted Array
 *
 * Description:
 * Given an array of integers nums sorted in ascending order, find the
 * starting and ending position of a given target value.
 *
 * Your algorithm's runtime complexity must be in the order of O(log n).
 * If the target is not found in the array, return [-1, -1].
 *
 * Example 1:
 * Input: nums = [5,7,7,8,8,10], target = 8
 * Output: [3,4]
 *
 * Example 2:
 * Input: nums = [5,7,7,8,8,10], target = 6
 * Output: [-1,-1]
 *
 * Result: Accepted (0ms)
 */
public class FindFirstAndLastPositionOfElementInSortedArray {
    public int[] searchRange(int[] nums, int target) {
        if (nums.length == 0) {
            return new int[]{-1, -1};
        }
        int rangeFrom = -1, rangeTo = -1;

        // Locate the start of the range
        int lPtr = 0, rPtr = nums.length - 1;
        while (lPtr < rPtr) {
            int mPtr = lPtr + ((rPtr - lPtr) >> 1);
            if (nums[mPtr] < target) {
                lPtr = mPtr + 1;
            } else {
                rPtr = mPtr;
            }
        }
        if (lPtr < nums.length && nums[lPtr] == target) {
            rangeFrom = lPtr;
        } else {
            return new int[]{-1, -1};
        }

        // Locate the end of the range
        rPtr = nums.length - 1;
        while (lPtr < rPtr) {
            int mPtr = lPtr + ((rPtr - lPtr) >> 1);
            if (nums[mPtr] > target) {
                rPtr = mPtr - 1;
            } else {
                lPtr = mPtr + 1;
            }
        }
        if (lPtr < nums.length && nums[lPtr] == target) {
            rangeTo = lPtr;
        } else if (lPtr - 1 < nums.length && nums[lPtr - 1] == target) {
            rangeTo = lPtr - 1;
        }
        return new int[]{rangeFrom, rangeTo};
    }

    public static void main(String[] args) {
        Printer.printArray(new FindFirstAndLastPositionOfElementInSortedArray()
                .searchRange(new int[]{5,7,7,8,8,10}, 8));
        Printer.printArray(new FindFirstAndLastPositionOfElementInSortedArray()
                .searchRange(new int[]{5,7,7,8,8,10}, 6));
        Printer.printArray(new FindFirstAndLastPositionOfElementInSortedArray()
                .searchRange(new int[]{7,7,7}, 7));
        Printer.printArray(new FindFirstAndLastPositionOfElementInSortedArray()
                .searchRange(new int[]{1,2,3}, 2));
    }
}
