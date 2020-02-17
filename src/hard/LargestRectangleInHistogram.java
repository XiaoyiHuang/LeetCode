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

    /**
     * Solution 3: Segment Tree + Divide and Conquer
     * Time Complexity: O(NlogN)
     * Space Complexity: O(2k - 1) (k = 2 ^ ⌈log2(n)⌉)
     *     Reasoning:
     *     (1) height of a segment tree: h = ⌈log2(n)⌉ + 1
     *     (2) size of a complete segment tree: 1 + 2 + 4 + 8 + ... + (2 ^ (h - 1))
     *                                        = 2 ^ h - 1
     *                                        = 2 * (2 ^ ⌈log2(n)⌉) - 1
     * Result: Accepted (12ms)
     */
    class Solution3 {
        class SegmentTree {
            SegmentTree left;
            SegmentTree right;
            int from;
            int to;
            int min = -1;
            SegmentTree(int from, int to) {
                this.from = from;
                this.to = to;
            }
        }
        public int largestRectangleArea(int[] heights) {
            SegmentTree root = new SegmentTree(0, heights.length - 1);
            for (int i = 0; i < heights.length; i++) {
                updateInterval(root, heights, i);
            }
            return getMaxArea(root, heights, 0, heights.length - 1);
        }
        private void updateInterval(SegmentTree node, int[] heights, int index) {
            if (node.from == node.to) {
                node.min = index;
                return;
            }
            int mid = node.from + ((node.to - node.from) >> 1);
            if (index <= mid) {
                if (node.left == null) {
                    node.left = new SegmentTree(node.from, mid);
                }
                updateInterval(node.left, heights, index);
            } else {
                if (node.right == null) {
                    node.right = new SegmentTree(mid + 1, node.to);
                }
                updateInterval(node.right, heights, index);
            }

            int minHeight = Integer.MAX_VALUE;
            if (node.left != null) {
                node.min = node.left.min;
                minHeight = heights[node.min];
            }
            if (node.right != null) {
                node.min = heights[node.right.min] < minHeight ? node.right.min : node.min;
            }
        }
        private int getIntervalMin(SegmentTree node, int[] heights, int from, int to) {
            if (from <= node.from && node.to <= to) {
                return node.min;
            }
            int mid = node.from + ((node.to - node.from) >> 1);
            int minHeight = Integer.MAX_VALUE, minIndex = -1;
            if (from <= mid) {
                int index = getIntervalMin(node.left, heights, from, to);
                minIndex = index;
                minHeight = heights[index];
            }
            if (to > mid) {
                int index = getIntervalMin(node.right, heights, from, to);
                minIndex = heights[index] <= minHeight ? index : minIndex;
            }
            return minIndex;
        }
        private int getMaxArea(SegmentTree root, int[] heights, int from, int to) {
            if (to < from) {
                return 0;
            }
            int minIndex = getIntervalMin(root, heights, from, to);
            int maxArea = heights[minIndex] * (to - from + 1);
            maxArea = Math.max(maxArea, getMaxArea(root, heights, from, minIndex - 1));
            maxArea = Math.max(maxArea, getMaxArea(root, heights, minIndex + 1, to));
            return maxArea;
        }
    }
    /**
     * Solution 4: Segment Tree (Array representation) + Divide and Conquer
     * Time Complexity: O(NlogN)
     * Space Complexity: O(4N)
     * Result: Accepted (10ms)
     */
    class Solution4 {
        public int largestRectangleArea(int[] heights) {
            int N = heights.length;
            int[] segTree = new int[N << 2];    // The max size of the segment tree is 2 * (2 ^ ⌈log2(N)⌉) - 1,
                                                // which is close to N << 2 in worst cases
            for (int i = 0; i < N; i++) {
                // Note that currIdx starts from 1, i.e. the root is at index 1
                updateInterval(segTree, heights, 0, N - 1, 1, i);
            }
            return getMaxArea(segTree, heights, 0, N - 1);
        }
        private void updateInterval(int[] segTree, int[] heights, int currFrom, int currTo, int segIdx, int heightIdx) {
            if (currFrom >= currTo) {
                segTree[segIdx] = heightIdx;
                return;
            }
            int mid = currFrom + ((currTo - currFrom) >> 1);
            if (heightIdx <= mid) {
                updateInterval(segTree, heights, currFrom, mid, segIdx << 1, heightIdx);
            } else {
                updateInterval(segTree, heights, mid + 1, currTo,segIdx << 1 | 1, heightIdx);
            }
            int lMin = segTree[segIdx << 1], rMin = segTree[segIdx << 1 | 1];
            segTree[segIdx] = heights[lMin] <= heights[rMin] ? lMin : rMin;
        }
        private int getIntervalMin(int[] segTree, int[] heights, int queryFrom, int queryTo,
                                   int currFrom, int currTo, int currIdx) {
            if (queryFrom <= currFrom && currTo <= queryTo) {
                return segTree[currIdx];
            }
            int mid = currFrom + ((currTo - currFrom) >> 1);
            int minHeight = Integer.MAX_VALUE, minIndex = -1;
            if (queryFrom <= mid) {
                int lMin = getIntervalMin(segTree, heights, queryFrom, queryTo, currFrom,
                        mid, currIdx << 1);
                minIndex = lMin;
                minHeight = heights[lMin];
            }
            if (queryTo > mid) {
                int rMin = getIntervalMin(segTree, heights, queryFrom, queryTo, mid + 1,
                        currTo, currIdx << 1 | 1);
                minIndex = heights[rMin] <= minHeight ? rMin : minIndex;
            }
            return minIndex;
        }
        private int getMaxArea(int[] segTree, int[] heights, int from, int to) {
            if (to < from) {
                return 0;
            }
            if (from == to) {
                return heights[from];
            }
            int minIndex = getIntervalMin(segTree, heights, from, to, 0, heights.length - 1, 1);
            int maxArea = heights[minIndex] * (to - from + 1);
            maxArea = Math.max(maxArea, getMaxArea(segTree, heights, from, minIndex - 1));
            maxArea = Math.max(maxArea, getMaxArea(segTree, heights, minIndex + 1, to));
            return maxArea;
        }
    }
    public static void main(String[] args) {
        Printer.printNum(new LargestRectangleInHistogram().new Solution1()
                .largestRectangleArea(new int[]{2,1,5,6,2,3}));
        Printer.printNum(new LargestRectangleInHistogram().new Solution2()
                .largestRectangleArea(new int[]{2,1,5,6,2,3}));
        Printer.printNum(new LargestRectangleInHistogram().new Solution3()
                .largestRectangleArea(new int[]{2,1,5,6,2,3}));
        Printer.printNum(new LargestRectangleInHistogram().new Solution4()
                .largestRectangleArea(new int[]{2,1,5,6,2,3}));

        Printer.printNum(new LargestRectangleInHistogram().new Solution1()
                .largestRectangleArea(new int[]{5,4,1,2}));
        Printer.printNum(new LargestRectangleInHistogram().new Solution2()
                .largestRectangleArea(new int[]{5,4,1,2}));
        Printer.printNum(new LargestRectangleInHistogram().new Solution3()
                .largestRectangleArea(new int[]{5,4,1,2}));
        Printer.printNum(new LargestRectangleInHistogram().new Solution4()
                .largestRectangleArea(new int[]{5,4,1,2}));

        Printer.printNum(new LargestRectangleInHistogram().new Solution3()
                .largestRectangleArea(new int[]{0,0,2147483647}));
        Printer.printNum(new LargestRectangleInHistogram().new Solution4()
                .largestRectangleArea(new int[]{0,0,2147483647}));
    }
}
