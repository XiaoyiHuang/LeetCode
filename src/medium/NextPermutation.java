package medium;

import helper.Printer;

/**
 * [31] Next Permutation
 *
 * Description:
 * Implement next permutation, which rearranges numbers into the lexicographically
 * next greater permutation of numbers.
 *
 * If such arrangement is not possible, it must rearrange it as the lowest possible
 * order (ie, sorted in ascending order).
 *
 * The replacement must be in-place and use only constant extra memory.
 *
 * Here are some examples. Inputs are in the left-hand column and its corresponding
 * outputs are in the right-hand column.
 * 1,2,3 → 1,3,2
 * 3,2,1 → 1,2,3
 * 1,1,5 → 1,5,1
 *
 * Result: Accepted (1ms)
 *
 */
public class NextPermutation {
    public void nextPermutation(int[] nums) {
        if (nums.length < 2) {
            return;
        }
        int currIndex = 1, replaceIndex = -1;
        for (; currIndex < nums.length; currIndex++) {
            if (nums[currIndex] > nums[currIndex - 1]) {
                replaceIndex = currIndex - 1;
            }
        }
        if (replaceIndex < 0) {
            // Reverse the entire sequence
            reverse(nums, 0, nums.length -1);
            return;
        }

        // The part right to {replaceIndex} must be in reverse order
        // We use binary search to find the smallest number larger
        // than {nums[replaceIndex]}
        int lPtr = replaceIndex + 1, rPtr = nums.length - 1;
        while (lPtr < rPtr) {
            int mPtr = lPtr + ((rPtr - lPtr) >> 1);
            if (nums[mPtr] > nums[replaceIndex]) {
                lPtr = mPtr + 1;
            } else {
                rPtr = mPtr - 1;
            }
        }
        if (nums[lPtr] <= nums[replaceIndex]) {
            lPtr -= 1;
        }
        // Swap the two numbers
        swap(nums, replaceIndex, lPtr);

        // Finally, reverse the portion right to {replaceIndex}
        reverse(nums, replaceIndex + 1, nums.length - 1);
    }

    private void swap(int[] nums, int idx0, int idx1) {
        nums[idx0] ^= nums[idx1];
        nums[idx1] ^= nums[idx0];
        nums[idx0] ^= nums[idx1];
    }

    private void reverse(int[] nums, int revFrom, int revTo) {
        while (revFrom < revTo) {
            swap(nums, revFrom++, revTo--);
        }
    }

    public static void main(String[] args) {
        int[] nums = new int[]{1,2,3,6,3,2,1,2,4};
        new NextPermutation().nextPermutation(nums);
        Printer.printArray(nums);

        nums = new int[]{6,1,2,3,4};
        new NextPermutation().nextPermutation(nums);
        Printer.printArray(nums);

        nums = new int[]{3,2,1};
        new NextPermutation().nextPermutation(nums);
        Printer.printArray(nums);

        nums = new int[]{1,2,3,4,5,2};
        new NextPermutation().nextPermutation(nums);
        Printer.printArray(nums);
    }
}
