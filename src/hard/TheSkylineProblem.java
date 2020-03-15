package hard;

import helper.Printer;

import java.util.*;

/**
 * [218] The Skyline Problem
 *
 * Description:
 * A city's skyline is the outer contour of the silhouette formed by all the buildings in that city
 * when viewed from a distance. Now suppose you are given the locations and height of all the buildings
 * as shown on a cityscape photo (Figure A), write a program to output the skyline formed by these
 * buildings collectively (Figure B).
 *
 * [Figure A Link: https://assets.leetcode.com/uploads/2018/10/22/skyline1.png]
 * [Figure B Link: https://assets.leetcode.com/uploads/2018/10/22/skyline2.png]
 *
 * The geometric information of each building is represented by a triplet of integers [Li, Ri, Hi],
 * where Li and Ri are the x coordinates of the left and right edge of the ith building, respectively,
 * and Hi is its height. It is guaranteed that 0 ≤ Li, Ri ≤ INT_MAX, 0 < Hi ≤ INT_MAX, and Ri - Li > 0.
 * You may assume all buildings are perfect rectangles grounded on an absolutely flat surface at height 0.
 *
 * For instance, the dimensions of all buildings in Figure A are recorded as:
 * [ [2 9 10], [3 7 15], [5 12 12], [15 20 10], [19 24 8] ] .
 *
 * The output is a list of "key points" (red dots in Figure B) in the format of [ [x1,y1], [x2, y2], [x3, y3], ... ]
 * that uniquely defines a skyline.
 *
 * A key point is the left endpoint of a horizontal line segment. Note that the last key point, where the
 * rightmost building ends, is merely used to mark the termination of the skyline, and always has zero height.
 * Also, the ground in between any two adjacent buildings should be considered part of the skyline contour.
 *
 * For instance, the skyline in Figure B should be represented as:
 * [ [2 10], [3 15], [7 12], [12 0], [15 10], [20 8], [24, 0] ].
 *
 * Notes:
 * [1] The number of buildings in any input list is guaranteed to be in the range [0, 10000].
 * [2] The input list is already sorted in ascending order by the left x position Li.
 * [3] The output list must be sorted by the x position.
 * [4] There must be no consecutive horizontal lines of equal height in the output skyline.
 *     For instance, [...[2 3], [4 5], [7 5], [11 5], [12 7]...] is not acceptable;
 *     the three lines of height 5 should be merged into one in the final output as such:
 *     [...[2 3], [4 5], [12 7], ...]
 *
 * [NOTE] A great reference for this problem: https://briangordon.github.io/2014/08/the-skyline-problem.html
 */
public class TheSkylineProblem {
    /**
     * Solution 1: Segment Tree
     * Time Complexity: O(NlogN)
     * Space Complexity: O(N) (See [Largest Rectangle in History] for analysis of space complexity)
     * Result: Accepted (510ms)
     */
    class Solution1 {
        class SegTree {
            SegTree left;
            SegTree right;
            int from;
            int to;
            int contourHeight;
            SegTree(int from, int to) {
                this.from = from;
                this.to = to;
            }
        }
        public List<List<Integer>> getSkyline(int[][] buildings) {
            int N = buildings.length;
            if (N == 0) {
                return new ArrayList<>();
            }

            Set<Integer> pointSet = new HashSet<>();
            for (int[] building : buildings) {
                pointSet.add(building[0]);
                pointSet.add(building[1]);
            }
            List<Integer> points = new ArrayList<>(pointSet);
            Collections.sort(points);

            Map<Integer, Integer> pointToIndex = new HashMap<>();
            int[] indexToPoint = new int[points.size()];
            for (int i = 0; i < points.size(); i++) {
                int coord = points.get(i);
                pointToIndex.put(coord, i);
                indexToPoint[i] = coord;
            }

            SegTree root = new SegTree(0, pointSet.size() - 1);
            for (int[] building : buildings) {
                updateTree(root, pointToIndex.get(building[0]), pointToIndex.get(building[1]) - 1, building[2]);
            }

            List<List<Integer>> lines = new ArrayList<>();
            drawSkyline(root, indexToPoint, lines);
            return lines;
        }
        private void updateTree(SegTree node, int updateFrom, int updateTo, int height) {
            if (node.from == node.to) {
                node.contourHeight = Math.max(node.contourHeight, height);
                return;
            }
            int mid = node.from + ((node.to - node.from) >> 1);
            if (node.left == null) {
                node.left = new SegTree(node.from, mid);
            }
            if (node.right == null) {
                node.right = new SegTree(mid + 1, node.to);
            }
            if (updateFrom <= mid) {
                updateTree(node.left, updateFrom, Math.min(updateTo, mid), height);
            }
            if (updateTo > mid) {
                updateTree(node.right, Math.max(mid + 1, updateFrom), updateTo, height);
            }
            int lHeight = (node.left == null) ? Integer.MIN_VALUE : node.left.contourHeight;
            int rHeight = (node.right == null) ? Integer.MIN_VALUE : node.right.contourHeight;
            node.contourHeight = Math.max(node.contourHeight, Math.max(lHeight, rHeight));
        }
        private void drawSkyline(SegTree node, int[] indexToCoord, List<List<Integer>> lines) {
            if (node.left == null && node.right == null) {
                if ((lines.isEmpty() || lines.get(lines.size() - 1).get(1) != node.contourHeight)) {
                    lines.add(Arrays.asList(indexToCoord[node.from], node.contourHeight));
                }
                return;
            }
            drawSkyline(node.left, indexToCoord, lines);
            drawSkyline(node.right, indexToCoord, lines);
        }
    }

    /**
     * Solution 2: Segment Tree (Array representation)
     * Time Complexity: O(NlogN)
     * Space Complexity: O(N)
     * Result: Accepted (480ms)
     */
    class Solution2 {
        public List<List<Integer>> getSkyline(int[][] buildings) {
            if (buildings.length == 0) {
                return new ArrayList<>();
            }

            Set<Integer> pointSet = new HashSet<>();
            for (int[] building : buildings) {
                pointSet.add(building[0]);
                pointSet.add(building[1]);
            }
            List<Integer> points = new ArrayList<>(pointSet);
            Collections.sort(points);

            Map<Integer, Integer> pointToIndex = new HashMap<>();
            int[] indexToPoint = new int[points.size()];
            for (int i = 0; i < points.size(); i++) {
                int coord = points.get(i);
                pointToIndex.put(coord, i);
                indexToPoint[i] = coord;
            }

            int[] segTree = new int[pointSet.size() << 2];
            for (int[] building : buildings) {
                updateTree(segTree, 1, 0, pointSet.size() - 1, pointToIndex.get(building[0]),
                        pointToIndex.get(building[1]) - 1, building[2]);
            }

            List<List<Integer>> lines = new ArrayList<>();
            drawSkyline(segTree, 1, 0, pointSet.size() - 1, indexToPoint, lines);
            return lines;
        }
        private void updateTree(int[] segTree, int segIndex, int segFrom, int segTo, int updateFrom,
                                int updateTo, int height) {
            if (segFrom == segTo) {
                segTree[segIndex] = Math.max(segTree[segIndex], height);
                return;
            }
            int mid = segFrom + ((segTo - segFrom) >> 1);
            if (updateFrom <= mid) {
                updateTree(segTree, segIndex << 1, segFrom, mid, updateFrom,
                        Math.min(updateTo, mid), height);
            }
            if (updateTo > mid) {
                updateTree(segTree, segIndex << 1 | 1, mid + 1, segTo,
                        Math.max(mid + 1, updateFrom), updateTo, height);
            }
            segTree[segIndex] = Math.max(segTree[segIndex],
                    Math.max(segTree[segIndex << 1], segTree[segIndex << 1 | 1]));
        }
        private void drawSkyline(int[] segTree, int segIndex, int segFrom, int segTo, int[] indexToPoint,
                                 List<List<Integer>> lines) {
            if (segFrom == segTo) {
                if ((lines.isEmpty() || lines.get(lines.size() - 1).get(1) != segTree[segIndex])) {
                    lines.add(Arrays.asList(indexToPoint[segFrom], segTree[segIndex]));
                }
                return;
            }
            int mid = segFrom + ((segTo - segFrom) >> 1);
            drawSkyline(segTree, segIndex << 1, segFrom, mid, indexToPoint, lines);
            drawSkyline(segTree, segIndex << 1 | 1, mid + 1, segTo, indexToPoint, lines);
        }
    }

    /**
     * Solution 3: Priority Queue
     * Explanation: The reason that the priority queue stores the height of the left edge as a negative number
     *              is two-fold:
     *              (1) If a right edge and a left edge shares the same x-coordinate, this makes sure the height
     *                  of the left edge is pushed into the queue before the right edge. For example, if the
     *                  provided buildings are {{1,2,1},{2,3,2}}, this guarantees that edge {2,2} is examined
     *                  before edge {2,1}. Otherwise, if edge {2,1} is examined first, the result set will
     *                  include both {2,0} and {2,2} points, which clearly is an overlap.
     *              (2) If the left edges of two adjacent buildings locate at the same x position, this makes
     *                  sure the edge with larger height is pushed into the queue first. Otherwise, the same
     *                  edge will be pushed into the queue twice.
     *
     *             Solution 4 provides an alternate version of solution with priority queue, in which the height
     *             of right edge is stored as negative. the major difference of solution 4 is that we need to
     *             manually rule out the duplicates when adding points to the result set, which turns out to be
     *             slightly more complicated.
     *
     *             Also, as remove() of PriorityQueue is a O(N) call, we use TreeMap here to speed up the remove
     *             operation (In fact, the runtime for using PriorityQueue is around 240ms).
     *
     * Time Complexity: O(NlogN)
     * Space Complexity: O(N)
     * Result: Accepted (19ms)
     */
    class Solution3 {
        public List<List<Integer>> getSkyline(int[][] buildings) {
            List<List<Integer>> contour = new ArrayList<>();
            List<int[]> edges = new ArrayList<>();
            for (int[] building : buildings) {
                edges.add(new int[]{building[0], -building[2]});
                edges.add(new int[]{building[1], building[2]});
            }
            edges.sort((e1, e2) -> e1[0] == e2[0] ? e1[1] - e2[1] : e1[0] - e2[0]);
            TreeMap<Integer, Integer> heightLayers = new TreeMap<>((h1, h2) -> (h2 - h1));
            heightLayers.put(0, 0);
            int prevHeight = 0;
            for (int[] edge : edges) {
                if (edge[1] < 0) {
                    heightLayers.put(-edge[1], heightLayers.getOrDefault(-edge[1], 0) + 1);
                } else {
                    int count;
                    if ((count = heightLayers.get(edge[1])) == 1) {
                        heightLayers.remove(edge[1]);
                    } else {
                        heightLayers.put(edge[1], count - 1);
                    }
                }
                int currMaxHeight = heightLayers.firstKey();
                if (currMaxHeight != prevHeight) {
                    contour.add(Arrays.asList(edge[0], currMaxHeight));
                    prevHeight = currMaxHeight;
                }
            }
            return contour;
        }
    }

    /**
     * Solution 4: Priority Queue (Alternate version)
     * Time Complexity: O(NlogN)
     * Space Complexity: O(N)
     * Result: Accepted (19ms)
     */
    class Solution4 {
        public List<List<Integer>> getSkyline(int[][] buildings) {
            List<List<Integer>> contour = new ArrayList<>();
            if (buildings.length == 0) {
                return contour;
            }
            TreeMap<Integer, Integer> heightLayers = new TreeMap<>((h1, h2) -> (h2 - h1));
            List<int[]> edges = new ArrayList<>();
            for (int[] building : buildings) {
                int[] leftEdge = new int[]{building[0], building[2]};
                int[] rightEdge = new int[]{building[1], -building[2]};
                edges.add(leftEdge);
                edges.add(rightEdge);
            }
            edges.sort((o1, o2) -> o1[0] == o2[0] ? o2[1] - o1[1] : o1[0] - o2[0]);
            int[] prevPoint = new int[]{-1, 0};
            heightLayers.put(0, 0);

            for (int[] edge : edges) {
                if (edge[1] >= 0) {
                    heightLayers.put(edge[1], heightLayers.getOrDefault(edge[1], 0) + 1);
                    int currMaxHeight = heightLayers.firstKey();
                    if (prevPoint[0] != edge[0] && currMaxHeight != prevPoint[1]) {
                        contour.add(Arrays.asList(edge[0], currMaxHeight));
                        prevPoint[0] = edge[0];
                        prevPoint[1] = currMaxHeight;
                    }
                } else {
                    int count;
                    if ((count = heightLayers.get(-edge[1])) == 1) {
                        heightLayers.remove(-edge[1]);
                    } else {
                        heightLayers.put(-edge[1], count - 1);
                    }
                    int currMaxHeight = heightLayers.firstKey();
                    if (prevPoint[0] != edge[0] && currMaxHeight != prevPoint[1]) {
                        contour.add(Arrays.asList(edge[0], currMaxHeight));
                        prevPoint[0] = edge[0];
                        prevPoint[1] = currMaxHeight;
                    }
                }
            }
            return contour;
        }
    }

    /**
     * Solution 5: Divide and Conquer + LinkedList
     * Time Complexity: O(NlogN)
     * Space Complexity: O(N)
     * Result: Accepted (2ms)
     */
    class Solution5 {
        class KeyPoint {
            int x;
            int h;
            KeyPoint next;
            KeyPoint(int x, int h) {
                this.x = x;
                this.h = h;
            }
        }
        public List<List<Integer>> getSkyline(int[][] buildings) {
            if (buildings.length == 0) {
                return new ArrayList<>();
            }
            KeyPoint points = divide(buildings, 0, buildings.length - 1);
            List<List<Integer>> keyPoints = new ArrayList<>();
            KeyPoint curr = points;
            while (curr != null) {
                keyPoints.add(Arrays.asList(curr.x, curr.h));
                curr = curr.next;
            }
            return keyPoints;
        }
        public KeyPoint divide(int[][] buildings, int from, int to) {
            if (from == to) {
                KeyPoint pt = new KeyPoint(buildings[from][0], buildings[from][2]);
                pt.next = new KeyPoint(buildings[from][1], 0);
                return pt;
            }
            int mid = from + ((to - from) >> 1);
            return merge(divide(buildings, from, mid), divide(buildings, mid + 1, to));
        }
        public KeyPoint merge(KeyPoint lPts, KeyPoint rPts) {
            KeyPoint mergedPts = new KeyPoint(-1, 0), mergedCur = mergedPts;
            KeyPoint lCur = lPts, rCur = rPts;
            int lHeight = 0, rHeight = 0;
            while (lCur != null && rCur != null) {
                int mergedX, mergedH;
                if (lCur.x < rCur.x) {
                    mergedX = lCur.x;
                    mergedH = Math.max(lCur.h, rHeight);
                    lHeight = lCur.h;
                    lCur = lCur.next;
                } else if (rCur.x < lCur.x) {
                    mergedX = rCur.x;
                    mergedH = Math.max(lHeight, rCur.h);
                    rHeight = rCur.h;
                    rCur = rCur.next;
                } else {
                    mergedX = lCur.x;
                    mergedH = Math.max(lCur.h, rCur.h);
                    lHeight = lCur.h;
                    rHeight = rCur.h;
                    lCur = lCur.next;
                    rCur = rCur.next;
                }
                if (mergedH != mergedCur.h) {
                    mergedCur.next = new KeyPoint(mergedX, mergedH);
                    mergedCur = mergedCur.next;
                }
            }
            while (lCur != null) {
                mergedCur.next = lCur;
                lCur = lCur.next;
                mergedCur = mergedCur.next;
            }
            while (rCur != null) {
                mergedCur.next = rCur;
                rCur = rCur.next;
                mergedCur = mergedCur.next;
            }
            return mergedPts.next;
        }
    }

    public static void validate(int[][] buildings) {
        Printer.printArray(buildings);
        Printer.printString("=====================================");
        Class<?>[] solutions = TheSkylineProblem.class.getDeclaredClasses();
        Object[] param = {buildings};
        List prevResult = null;
        try {
            Object baseInstance = TheSkylineProblem.class.newInstance();
            for (Class<?> solution : solutions) {
                Printer.printString(solution.getSimpleName());
                Object instance = solution.getDeclaredConstructor(TheSkylineProblem.class).newInstance(baseInstance);
                List result = (List) solution.getMethod("getSkyline", int[][].class).invoke(instance, param);
                if (prevResult != null) {
                    if (!result.equals(prevResult)) {
                        Printer.printString("[ERROR] UNMATCHED OUTPUT!");
                    }
                }
                Printer.printList(result);
                Printer.printString("=====================================");
                prevResult = result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        validate(new int[][]{{2,9,10},{3,7,15},{5,12,12},{15,20,10},{19,24,8}});
        validate(new int[][]{{1,4,1},{2,5,2},{3,6,1}});
        validate(new int[][]{{1,2,1},{1,2,2},{1,2,3}});
        validate(new int[][]{{1,2,3},{1,2,2},{1,2,1}});
        validate(new int[][]{{1,3,5},{3,5,7}});
        validate(new int[][]{{1,2,1},{2,3,1}});
        validate(new int[][]{{0,5,7},{5,10,7},{5,10,12},{10,15,7},{15,20,7},{15,20,12},{20,25,7}});
    }
}
