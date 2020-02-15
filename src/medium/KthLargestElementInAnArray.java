package medium;

import helper.Printer;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * [215] Kth Largest Element in an Array
 *
 * Description:
 * Find the kth largest element in an unsorted array. Note that
 * it is the kth largest element in the sorted order, not the kth
 * distinct element.
 *
 * Example 1:
 * Input: [3,2,1,5,6,4] and k = 2
 * Output: 5
 *
 * Example 2:
 * Input: [3,2,3,1,2,4,5,5,6] and k = 4
 * Output: 4
 *
 * Note:
 * You may assume k is always valid, 1 ≤ k ≤ array's length.
 *
 * Result:
 * Solution 1: Accepted (2ms)
 * Solution 2: Accepted (4ms)
 * Solution 3: Accepted (1ms)
 */
public class KthLargestElementInAnArray {
    /**
     * Solution 1: Sort
     * Time Complexity: O(NlogN)
     * Space Complexity: O(1)
     */
    class Solution1 {
        public int findKthLargest(int[] nums, int k) {
            Arrays.sort(nums);
            return nums[nums.length - k];
        }
    }
    /**
     * Solution 2: Priory Queue
     * Time Complexity: O(NlogK)
     * Space Complexity: O(K)
     */
    class Solution2 {
        public int findKthLargest(int[] nums, int k) {
            PriorityQueue<Integer> window = new PriorityQueue<>();
            int currWindowSize = 0;
            for (int num : nums) {
                window.offer(num);
                currWindowSize++;
                if (currWindowSize > k) {
                    window.poll();
                    currWindowSize--;
                }
            }
            return window.peek();
        }
    }

    /**
     * Solution 3: Quick Select (with Hoare's partition scheme)
     * Time Complexity: O(N) (with worst case of O(N^2))
     * Space Complexity: O(1)
     */
    class Solution3 {
        public int findKthLargest(int[] nums, int k) {
            int lo = 0, hi = nums.length - 1;
            k = nums.length - k;
            while (true) {
                if (lo < hi) {
                    int pivot = partition(nums, lo, hi);
                    if (pivot >= k) {
                        hi = pivot;
                    } else {
                        lo = pivot + 1;
                    }
                } else {
                    return nums[lo];
                }
            }
        }
        private int partition(int[] nums, int lo, int hi) {
            int pivot = nums[lo + ((hi - lo) >> 1)];
            int i = lo - 1;
            int j = hi + 1;

            while (true) {
                do {
                    i += 1;
                } while (nums[i] < pivot);
                do {
                    j -= 1;
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
        Printer.printNum(new KthLargestElementInAnArray().new Solution1()
                .findKthLargest(new int[]{3,2,3,1,2,4,5,5,6}, 4));
        Printer.printNum(new KthLargestElementInAnArray().new Solution2()
                .findKthLargest(new int[]{3,2,3,1,2,4,5,5,6}, 4));
        Printer.printNum(new KthLargestElementInAnArray().new Solution3()
                .findKthLargest(new int[]{3,2,3,1,2,4,5,5,6}, 4));
    }
}
