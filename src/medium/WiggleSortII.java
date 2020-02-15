package medium;

import helper.Printer;

import java.util.Arrays;

/**
 * [324] Wiggle Sort II
 *
 * Description:
 * Given an unsorted array nums, reorder it such that nums[0] < nums[1] > nums[2] < nums[3]....
 *
 * Example 1:
 * Input: nums = [1, 5, 1, 1, 6, 4]
 * Output: One possible answer is [1, 4, 1, 5, 1, 6].
 *
 * Example 2:
 * Input: nums = [1, 3, 2, 2, 3, 1]
 * Output: One possible answer is [2, 3, 1, 3, 1, 2].
 *
 * Note:
 * You may assume all input has valid answer.
 *
 * Follow Up:
 * Can you do it in O(n) time and/or in-place with O(1) extra space?
 */
public class WiggleSortII {
    /**
     * Solution 1: Quick Sort
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     */
    class Solution1 {
        public void wiggleSort(int[] nums) {
            if (nums.length < 2) {
                return;
            }
            int median = findMedian(nums);
            threeWayPartition(nums, median);
            int lowerIdx = (nums.length - 1) >> 1, higherIdx = nums.length - 1;

            boolean isEven = true;
            int[] copy = Arrays.copyOf(nums, nums.length);
            for (int i = 0; i < nums.length; i++) {
                if (isEven) {
                    nums[i] = copy[lowerIdx--];
                } else {
                    nums[i] = copy[higherIdx--];
                }
                isEven = !isEven;
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
        private void threeWayPartition(int[] nums, int pivot) {
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
     * Solution 2: Quick Select + Index mapping + Two pointers (Color Sort)
     * Explanation:
     *      1. Quick select: Locate the median (at M) with Quick Select,
     *         whilst partition the array into two sections, with nums[0]
     *         ~ num[M] larger than the median, and nums[M+1] ~ nums[N-1]
     *         smaller than the median. Note that M = (n-floor[(n+1)/2]-1),
     *         as the lower half must NOT contain less numbers than the
     *         higher half
     *      2. Index Mapping:
     *      3. Two Pointers: We use a pointer L to identify where the next
     *         larger-than-median number will be inserted, and R to identify
     *         where the next smaller-than-median number will be inserted.
     *         Every time we map to a new index i, we compare the number at
     *         i with the median. Depending on whether the number is smaller
     *         or larger than median, we swap the number to either R or L
     *         accordingly.
     */
    class Solution2 {
        public void wiggleSort(int[] nums) {
            int n = nums.length;
            int median = nums[findKthLargest(nums, ((n + 1) >> 1))];
            int curr = 0, left = 0, right = n - 1;
            while (curr <= right) {
                if (nums[mapIndex(curr, n)] > median) {
                    // If current number is larger than the median, we
                    // swap this number to the next available LEFT index
                    // position, as indicated by the left pointer
                    swap(nums, mapIndex(left++, n), mapIndex(curr++, n));
                }
                else if (nums[mapIndex(curr, n)] < median) {
                    // Current number is smaller than the median, so we
                    // swap this number to the next available RIGHT index
                    swap(nums, mapIndex(right--, n), mapIndex(curr, n));
                }
                else {
                    // When we change L/R pointer, it means that we are
                    // confident that the number at (mapped) index L/R
                    // can never be swapped to the other side, i.e. it's
                    // definitely larger or smaller than the median.
                    // Therefore, Neither left or right pointer is changed
                    // in this case
                    curr++;
                }
            }
        }
        public int mapIndex(int index, int n) {
            return (1 + 2 * index) % (n | 1);
        }
        public int findKthLargest(int[] nums, int k) {
            int lo = 0, hi = nums.length - 1;
            k -= 1;
            while (lo < hi) {
                int pivotIndex = partition(nums, lo, hi);
                if (k <= pivotIndex) {
                    hi = pivotIndex;
                } else {
                    lo = pivotIndex + 1;
                }
            }
            return hi;
        }
        private int partition(int[] nums, int lo, int hi) {
            int pivot = nums[(lo + ((hi - lo) >> 1))];
            int i = lo - 1;
            int j = hi + 1;
            while (true) {
                do {
                    i++;
                } while (nums[i] < pivot);
                do {
                    j--;
                } while (nums[j] > pivot);

                if (i >= j) {
                    return j;
                }
                swap(nums, i, j);
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
    public static void main(String[] args) {
        int[] nums = new int[]{1,5,1,1,6,4};
        new WiggleSortII().new Solution2().wiggleSort(nums);
        Printer.printArray(nums);
        nums = new int[]{1,3,2,2,3,1};
        new WiggleSortII().new Solution2().wiggleSort(nums);
        Printer.printArray(nums);
        nums = new int[]{4,5,5,6};
        new WiggleSortII().new Solution2().wiggleSort(nums);
        Printer.printArray(nums);
    }
}
