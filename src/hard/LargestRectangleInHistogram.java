package hard;

import helper.Printer;

import java.util.LinkedList;

/**
 * [84] Largest Rectangle in Histogram
 *
 * Description:
 * Given n non-negative integers representing the histogram's bar height
 * where the width of each bar is 1, find the area of largest rectangle in the histogram.
 *
 * Above is a histogram where width of each bar is 1, given height = [2,1,5,6,2,3].
 * The largest rectangle is shown in the shaded area, which has area = 10 unit.
 *
 * Example:
 * Input: [2,1,5,6,2,3]
 * Output: 10
 */
public class LargestRectangleInHistogram {
    /**
     * Solution 1: Monotonic Queue
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     * Result: Accepted (5ms)
     */
    class Solution1 {
        public int largestRectangleArea(int[] heights) {
            int N = heights.length;
            if (N <= 1) {
                return N == 0 ? 0 : heights[0];
            }
            // Only preserve the indices of 'trough' numbers in the sequence
            LinkedList<Integer> monoQueue = new LinkedList<>();
            int largestArea = 0;
            for (int i = 0; i < N; i++) {
                while (!monoQueue.isEmpty() && heights[monoQueue.peekLast()] > heights[i]) {
                    int currIdx = monoQueue.pollLast();
                    int prevIdx = monoQueue.isEmpty() ? 0 : monoQueue.peekLast() + 1;
                    largestArea = Math.max(largestArea, heights[currIdx] * (i - prevIdx));
                }
                monoQueue.offerLast(i);
            }
            while (!monoQueue.isEmpty()) {
                int currIdx = monoQueue.pollLast();
                int prevIdx = monoQueue.isEmpty() ? -1 : monoQueue.peekLast();
                largestArea = Math.max(largestArea, heights[currIdx] * (N - 1 - prevIdx));
            }
            return largestArea;
        }
    }

    /**
     * Solution 2: Dynamic Programming
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     * Result: Accepted (2ms)
     */
    class Solution2 {
        public int largestRectangleArea(int[] heights) {
            int N = heights.length;
            if (N <= 1) {
                return N == 0 ? 0 : heights[0];
            }
            int[] leftLessThan = new int[N];
            int[] rightLessThan = new int[N];
            leftLessThan[0] = -1;
            rightLessThan[N - 1] = N;
            for (int i = 1; i < N; i++) {
                int nextIdx = i - 1;
                while (nextIdx >= 0 && heights[nextIdx] >= heights[i]) {
                    nextIdx = leftLessThan[nextIdx];
                }
                leftLessThan[i] = nextIdx;
            }
            for (int i = N - 2; i >= 0; i--) {
                int nextIdx = i + 1;
                while (nextIdx < N && heights[nextIdx] >= heights[i]) {
                    nextIdx = rightLessThan[nextIdx];
                }
                rightLessThan[i] = nextIdx;
            }
            int largestArea = 0;
            for (int i = 0; i < N; i++) {
                largestArea = Math.max(largestArea, heights[i] * (rightLessThan[i] - leftLessThan[i] - 1));
            }
            return largestArea;
        }
    }
    class Solution3 {
        class IntervalTree {
            IntervalTree left;
            IntervalTree right;
            int from;
            int to;
            int minInRange = Integer.MAX_VALUE;
            IntervalTree(int from, int to) {
                this.from = from;
                this.to = to;
            }
        }
        public int largestRectangleArea(int[] heights) {
            IntervalTree root = new IntervalTree(0, heights.length - 1);
            for (int i = 0; i < heights.length; i++) {
                updateInterval(root, heights, i);
            }
        }
        private void updateInterval(IntervalTree node, int[] heights, int index) {
            if (node.from == node.to) {
                node.minInRange = heights[index];
                return;
            }
            int mid = node.from + ((node.from - node.to) >> 1);
            if (index <= mid) {
                if (node.left == null) {
                    node.left = new IntervalTree(node.from, mid);
                }
                updateInterval(node.left, heights, index);
            } else {
                if (node.right == null) {
                    node.right = new IntervalTree(mid + 1, node.to);
                }
                updateInterval(node.left, heights, index);
            }
            int leftMin = node.left == null ? Integer.MAX_VALUE : node.left.minInRange;
            int rightMin = node.right == null ? Integer.MAX_VALUE : node.right.minInRange;
            node.minInRange = Math.min(leftMin, rightMin);
        }
        private int getIntervalMin(IntervalTree node, int from, int to) {
            if (from <= node.from && node.to <= to) {
                return node.minInRange;
            }
            int mid = node.from + ((node.from - node.to) >> 1);
            int leftMin = Integer.MAX_VALUE, rightMin = Integer.MAX_VALUE;
            if (from <= mid) {
                leftMin = getIntervalMin(node.left, from, to);
            }
            if (to > mid) {
                rightMin = getIntervalMin(node.right, from, to);
            }
            return Math.min(leftMin, rightMin);
        }
        private int getMaxArea(IntervalTree root, int[] heights, int from, int to) {
            int minInRange = getIntervalMin(root, from, to);
            // Min should be index
        }
    }
    public static void main(String[] args) {
        Printer.printNum(new LargestRectangleInHistogram().new Solution1()
                .largestRectangleArea(new int[]{2,1,5,6,2,3}));
        Printer.printNum(new LargestRectangleInHistogram().new Solution2()
                .largestRectangleArea(new int[]{2,1,5,6,2,3}));
        Printer.printNum(new LargestRectangleInHistogram().new Solution1()
                .largestRectangleArea(new int[]{5,4,1,2}));
        Printer.printNum(new LargestRectangleInHistogram().new Solution2()
                .largestRectangleArea(new int[]{5,4,1,2}));
    }
}
