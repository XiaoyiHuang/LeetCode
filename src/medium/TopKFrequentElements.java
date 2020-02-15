package medium;

import helper.Printer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * [347] Top K Frequent Elements
 *
 * Description:
 * Given a non-empty array of integers, return the k most frequent elements.
 *
 * Example 1:
 * Input: nums = [1,1,1,2,2,3], k = 2
 * Output: [1,2]
 *
 * Example 2:
 * Input: nums = [1], k = 1
 * Output: [1]
 *
 * Note:
 * You may assume k is always valid, 1 ≤ k ≤ number of unique elements.
 * Your algorithm's time complexity must be better than O(n log n),
 * where n is the array's size.
 *
 * Result:
 * Solution 1: Accepted (12ms)
 * Solution 2: Accepted (11ms)
 */
public class TopKFrequentElements {
    /**
     * Solution 1: Quick select
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     */
    class Solution1 {
        public List<Integer> topKFrequent(int[] nums, int k) {
            Map<Integer, Integer> numToFreq = new HashMap<>();
            for (int num : nums) {
                numToFreq.put(num, numToFreq.getOrDefault(num, 0) + 1);
            }

            // Record numbers with identical frequency
            Map<Integer, LinkedList<Integer>> freqToNums = new HashMap<>();
            int[] freqs = new int[numToFreq.size()];
            int index = 0;
            for (int num : numToFreq.keySet()) {
                int freq = numToFreq.get(num);
                freqToNums.computeIfAbsent(freq, r -> new LinkedList<>()).offer(num);
                freqs[index++] = freq;
            }

            // Use hiStack to keep track of all visited higher pointers
            LinkedList<Integer> hiStack = new LinkedList<>();
            LinkedList<Integer> topKs = new LinkedList<>();
            int lo = 0, hi = freqs.length - 1;
            hiStack.push(hi);

            for (k = freqs.length - k; k < freqs.length; k++) {
                while (true) {
                    if (lo < hi) {
                        int pivot = select(freqs, lo, hi);
                        if (pivot >= k) {
                            hi = pivot;
                            hiStack.push(hi);
                        } else {
                            lo = pivot + 1;
                        }
                    } else {
                        topKs.addFirst(freqToNums.get(freqs[lo]).poll());
                        hiStack.pop();
                        lo = lo + 1;
                        hi = hiStack.isEmpty() ? freqs.length - 1 : hiStack.peek();
                        break;
                    }
                }
            }
            return topKs;
        }
        public int select(int[] freqs, int lo, int hi) {
            int pivot = freqs[lo + ((hi - lo) >> 1)];
            int i = lo - 1;
            int j = hi + 1;
            while (true) {
                do {
                    i += 1;
                } while (freqs[i] < pivot);
                do {
                    j -= 1;
                } while (freqs[j] > pivot);
                if (i >= j) {
                    return j;
                }
                swap(freqs, i, j);
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
     * Solution 2: Heap
     * Time Complexity: O(NlogK)
     * Space Complexity: O(N)
     */
    class Solution2 {
        public List<Integer> topKFrequent(int[] nums, int k) {
            Map<Integer, Integer> numToFreq = new HashMap<>();
            for (int num : nums) {
                numToFreq.put(num, numToFreq.getOrDefault(num, 0) + 1);
            }

            // Record numbers with identical frequency
            Map<Integer, List<Integer>> freqToNums = new HashMap<>();
            for (int num : numToFreq.keySet()) {
                int freq = numToFreq.get(num);
                freqToNums.computeIfAbsent(freq, r -> new ArrayList<>()).add(num);
            }

            // Use heap to sort out the top K frequencies
            PriorityQueue<Integer> pq = new PriorityQueue<>();
            int numCount = 0;
            for (int freq : freqToNums.keySet()) {
                pq.offer(freq);
                numCount += freqToNums.get(freq).size();
                while (numCount > k) {
                    numCount -= freqToNums.get(pq.poll()).size();
                }
            }

            LinkedList<Integer> topKs = new LinkedList<>();
            while (topKs.size() < k) {
                int freq = pq.poll();
                for (int num : freqToNums.get(freq)) {
                    if (topKs.size() < k) {
                        topKs.addFirst(num);
                    } else {
                        break;
                    }
                }
            }
            return topKs;
        }
    }
    public static void main(String[] args) {
        int[] nums = new int[]{3,2,3,1,2,4,5,5,6,7,7,8,2,3,1,1,1,10,11,5,6,2,4,7,8,5,6};
        Printer.printList(new TopKFrequentElements().new Solution2().topKFrequent(nums, 10));
        nums = new int[]{4,1,-1,2,-1,2,3};
        Printer.printList(new TopKFrequentElements().new Solution2().topKFrequent(nums, 2));
        nums = new int[]{5,2,5,3,5,3,1,1,3};
        Printer.printList(new TopKFrequentElements().new Solution2().topKFrequent(nums, 2));
    }
}
