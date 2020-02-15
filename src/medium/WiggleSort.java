package medium;

import helper.Printer;

import java.util.Arrays;
import java.util.Random;

/**
 * [280] Wiggle Sort
 *
 * Description:
 * Given an unsorted array nums, reorder it in-place such that nums[0] <= nums[1] >= nums[2] <= nums[3]....
 * For example, given nums = [3, 5, 2, 1, 6, 4], one possible answer is [1, 6, 2, 5, 3, 4].
 */
public class WiggleSort {
    /**
     * Solution 1: In-place swap
     * Time Complexity: O(N)
     * Space Complexity: O(1)
     */
    class Solution1 {
        public void wiggleSort(int[] nums) {
            if (nums.length < 2) {
                return;
            }
            int index = 1;
            boolean revOrder = false;
            while (index < nums.length) {
                if ((revOrder && nums[index] > nums[index - 1]) ||
                        (!revOrder && nums[index] < nums[index - 1])) {
                    swap(nums, index, index - 1);
                }
                index += 1;
                revOrder = !revOrder;
            }
        }
        private void swap(int[] nums, int i, int j) {
            if (i != j) {
                nums[i] ^= nums[j];
                nums[j] ^= nums[i];
                nums[i] ^= nums[j];
            }
        }
    }

    /**
     * Solution 2: Quick sort (three-way partition)
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     */
    class Solution2 {
        public void wiggleSort(int[] nums) {
            if (nums.length == 0) {
                return;
            }
            int median = findMedian(nums);
            int[] tmpNums = Arrays.copyOf(nums, nums.length);
            int[] pivotRange = threeWayPartition(tmpNums, median);
            int lowerIdx = 0, equalIdx = pivotRange[0], higherIdx = pivotRange[1];
            boolean insertLowerNum = true;
            for (int i = 0; i < nums.length; i++) {
                if (insertLowerNum) {
                    if (lowerIdx < equalIdx) {
                        nums[i] = tmpNums[lowerIdx++];
                    } else {
                        nums[i] = tmpNums[equalIdx++];
                    }
                } else {
                    if (higherIdx < nums.length) {
                        nums[i] = tmpNums[higherIdx++];
                    } else {
                        nums[i] = tmpNums[equalIdx++];
                    }
                }
                insertLowerNum = !insertLowerNum;
            }
        }
        private int findMedian(int[] nums) {
            int medianIndex = nums.length >> 1;
            int lo = 0, hi = nums.length - 1;
            while (true) {
                if (lo >= hi) {
                    break;
                }
                int pivotIndex = partition(nums, lo, hi);
                if (pivotIndex > medianIndex) {
                    hi = pivotIndex - 1;
                } else if (pivotIndex < medianIndex) {
                    lo = pivotIndex + 1;
                } else {
                    lo = pivotIndex;
                    break;
                }
            }
            return nums[lo];
        }
        private int partition(int[] nums, int lo, int hi) {
            int pivot = nums[hi];
            int i = lo;
            for (int j = lo; j < hi; j++) {
                if (nums[j] < pivot) {
                    swap(nums, i, j);
                    i++;
                }
            }
            swap(nums, i, hi);
            return i;
        }
        private int[] threeWayPartition(int[] nums, int pivot) {
            int i = 0, j = 0, k = nums.length - 1;
            while (j < k) {
                if (nums[j] < pivot) {
                    swap(nums, i, j);
                    i += 1;
                    j += 1;
                } else if (nums[j] == pivot) {
                    j += 1;
                } else {
                    swap(nums, j, k);
                    k -= 1;
                }
            }
            return new int[]{i, j};
        }
        private void swap(int[] nums, int i, int j) {
            if (i != j) {
                nums[i] ^= nums[j];
                nums[j] ^= nums[i];
                nums[i] ^= nums[j];
            }
        }
    }

    /**
     * Simple validator to verify the correctness of solutions
     * @param rounds
     */
    public void validator(int rounds) {
        Random rand = new Random(System.currentTimeMillis());
        int maxNum = 101;
        for (int round = 0; round < rounds; round++) {
            // Generate test cases
            int[] nums = new int[rand.nextInt(101)];
            for (int i = 0; i < nums.length; i++) {
                nums[i] = rand.nextInt(maxNum);
            }

            // Perform operations
            int[] numsBeforeSort = Arrays.copyOf(nums, nums.length);
            new Solution2().wiggleSort(nums);

            // Verify results
            boolean isEven = true;
            for (int i = 1; i < nums.length; i++) {
                if ((isEven && nums[i] < nums[i - 1]) || (!isEven && nums[i] > nums[i - 1])) {
                    System.err.println("[ERROR VERIFY]: "
                            + "\nTEST CASE: " + Printer.getArrayString(numsBeforeSort)
                            + "\nRESULT: " + Printer.getArrayString(nums)
                            + "\nERROR OCCUR AT INDEX: " + i);
                    return;
                }
                isEven = !isEven;
            }
            System.out.println("TEST " + (round + 1) + " VERIFY COMPLETE!");
        }
        System.out.println("VERIFY COMPLETE!");
    }

    public static void main(String[] args) {
        int[] nums = new int[]{3,5,2,1,6,4};
        new WiggleSort().new Solution1().wiggleSort(nums);
        Printer.printArray(nums);
        nums = new int[]{2,2,2,2,2};
        new WiggleSort().new Solution1().wiggleSort(nums);
        Printer.printArray(nums);
    }
}
