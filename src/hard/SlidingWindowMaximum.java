package hard;

import helper.Printer;

import java.util.LinkedList;

/**
 * [239] Sliding Window Maximum
 *
 * Description:
 * Given an array nums, there is a sliding window of size k which is
 * moving from the very left of the array to the very right.
 * You can only see the k numbers in the window.
 * Each time the sliding window moves right by one position.
 * Return the max sliding window.
 *
 * Example:
 * Input: nums = [1,3,-1,-3,5,3,6,7], and k = 3
 * Output: [3,3,5,5,6,7]
 *
 * Explanation:
 * Window position                Max
 * ---------------               -----
 * [1  3  -1] -3  5  3  6  7       3
 *  1 [3  -1  -3] 5  3  6  7       3
 *  1  3 [-1  -3  5] 3  6  7       5
 *  1  3  -1 [-3  5  3] 6  7       5
 *  1  3  -1  -3 [5  3  6] 7       6
 *  1  3  -1  -3  5 [3  6  7]      7
 *
 * Note:
 * You may assume k is always valid, 1 ≤ k ≤ input array's size for non-empty array.
 *
 * Follow up:
 * Could you solve it in linear time?
 */
public class SlidingWindowMaximum {
    /**
     * Solution 1: Monotonic Deque
     *
     * Explanation:
     *      Each element of the monoQueue contains two values.
     *      The first value is the actual number, while the second
     *      one denotes the number of SMALLER numbers that are pushed
     *      into the monoQueue BEFORE this number.
     *
     * Time Complexity: O(N)
     * Space Complexity: O(K) (K denotes window size)
     * Result: Accepted (4ms)
     */
    class Solution1 {
        LinkedList<int[]> monoQueue = new LinkedList<>();
        public void push(int num) {
            int polledNumberCount = 0;
            while (!monoQueue.isEmpty() && monoQueue.peekLast()[0] < num) {
                polledNumberCount += (monoQueue.pollLast()[1] + 1);
            }
            monoQueue.offerLast(new int[]{num, polledNumberCount});
        }
        public int pop() {
            int[] windowMax = monoQueue.peek();
            if (--windowMax[1] < 0) {
                monoQueue.pollFirst();
            }
            return windowMax[0];
        }
        public int[] maxSlidingWindow(int[] nums, int k) {
            if (nums.length == 0) {
                return nums;
            }
            int[] windowMax = new int[nums.length - k + 1];
            for (int i = 0; i < k - 1; i++) {
                push(nums[i]);
            }
            int windowMaxIdx = 0;
            for (int i = k - 1; i < nums.length; i++) {
                push(nums[i]);
                windowMax[windowMaxIdx++] = pop();
            }
            return windowMax;
        }
    }
    /**
     * Solution 2: Monotonic Deque (Simplified)
     *
     * Explanation:
     *      The monoQueue stores the indices of numbers that
     *      are largest among the numbers pushed into the
     *      monoQueue earlier.
     *
     * Time Complexity: O(N)
     * Space Complexity: O(K) (K denotes window size)
     * Result: Accepted (5ms)
     */
    class Solution2 {
        LinkedList<Integer> monoQueue = new LinkedList<>();
        public void push(int[] nums, int index) {
            while (!monoQueue.isEmpty() && nums[monoQueue.peekLast()] < nums[index]) {
                monoQueue.pollLast();
            }
            monoQueue.offerLast(index);
        }
        public int pop(int[] nums, int windowLowerIdx) {
            int maxNumIdx = monoQueue.peekFirst();
            if (maxNumIdx <= windowLowerIdx) {
                monoQueue.pollFirst();
            }
            return nums[maxNumIdx];
        }
        public int[] maxSlidingWindow(int[] nums, int k) {
            int[] windowMax = new int[nums.length - k + 1];
            for (int i = 0; i < k - 1; i++) {
                push(nums, i);
            }
            int windowMaxIdx = 0;
            for (int i = k - 1; i < nums.length; i++) {
                push(nums, i);
                windowMax[windowMaxIdx++] = pop(nums, i - k + 1);
            }
            return windowMax;
        }
    }

    /**
     * Solution 3: Two-pass
     *
     * Explanation:
     *      0. Suppose the input array is [1,3,-1,-3,5,3,6,7], and the window size k equals 3
     *      1. Divide the input sequence into segments of size k
     *        (the last segment might contain less elements)
     *                  1, 3, -1 | -3, 5, 3 | 6, 7
     *
     *      2. All sliding windows can be separated into two sections, as illustrated below:
     *                    (i)     (i+k-1)
     *                  1, 3, -1 | -3, 5, 3 | 6, 7
     *                    |<-window->|
     *                    |<-R->|<-L->|
     *
     *         With this, if we know the maximum number in R and L, we can obtain the window
     *         maximum through {max(maxR, maxL}.
     *
     *      3. Now the question is simplified to the calculation of maxL and maxR. This can
     *         be done with classic dynamic programming. For maxL, we scan the input array
     *         from LEFT to RIGHT to record the maximum value discovered so far. As we only
     *         care about the maximum within the k-sized segments, we should reset the maximum
     *         value according when we traverse to a new segment.
     *
     *         For maxR, we can do the exact opposite by scanning from RIGHT to LEFT, only to
     *         reset the recorded maximum value at segment boundaries.
     *
     *      NOTE: This approach seems to violate the 'only see the k numbers in the window'
     *            requirement of the question. The other two solutions should work better if
     *            we are dealing with a stream-like flow of input numbers.
     *
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     * Result: Accepted (3ms)
     */
    class Solution3 {
        public int[] maxSlidingWindow(int[] nums, int k) {
            int N = nums.length;
            if (N == 0) {
                return nums;
            }
            int[] leftMax = new int[N];
            int[] rightMax = new int[N];
            for (int i = 0; i < nums.length; i++) {
                leftMax[i] = (i % k == 0) ? nums[i] : Math.max(nums[i], leftMax[i - 1]);
                int rightIdx = N - i - 1;
                rightMax[rightIdx] = (rightIdx == N - 1 || rightIdx % k == k - 1) ? nums[rightIdx] :
                        Math.max(nums[rightIdx], rightMax[rightIdx + 1]);
            }
            int[] windowMax = new int[N - k + 1];
            for (int i = 0; i < N - k + 1; i++) {
                windowMax[i] = Math.max(rightMax[i], leftMax[i + k - 1]);
            }
            return windowMax;
        }
    }
    public static void main(String[] args) {
        Printer.printArray(new SlidingWindowMaximum()
                .new Solution1().maxSlidingWindow(new int[]{1,3,-1,-3,5,3,6,7}, 3));
        Printer.printArray(new SlidingWindowMaximum()
                .new Solution2().maxSlidingWindow(new int[]{1,3,-1,-3,5,3,6,7}, 3));
        Printer.printArray(new SlidingWindowMaximum()
                .new Solution3().maxSlidingWindow(new int[]{1,3,-1,-3,5,3,6,7}, 3));
    }
}
