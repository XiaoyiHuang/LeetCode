package medium;

import helper.Printer;

import java.util.PriorityQueue;

/**
 * [973] K Closest Points to Origin
 *
 * Description:
 * We have a list of points on the plane. Find the K closest points to the origin (0, 0).
 * (Here, the distance between two points on a plane is the Euclidean distance.)
 * You may return the answer in any order. The answer is guaranteed to be unique
 * (except for the order that it is in.)
 *
 * Example 1:
 * Input: points = [[1,3],[-2,2]], K = 1
 * Output: [[-2,2]]
 * Explanation:
 * The distance between (1, 3) and the origin is sqrt(10).
 * The distance between (-2, 2) and the origin is sqrt(8).
 * Since sqrt(8) < sqrt(10), (-2, 2) is closer to the origin.
 * We only want the closest K = 1 points from the origin, so the answer is just [[-2,2]].
 *
 * Example 2:
 * Input: points = [[3,3],[5,-1],[-2,4]], K = 2
 * Output: [[3,3],[-2,4]]
 * (The answer [[-2,4],[3,3]] would also be accepted.)
 *
 * Note:
 *      1. 1 <= K <= points.length <= 10000
 *      2. -10000 < points[i][0] < 10000
 *      3. -10000 < points[i][1] < 10000
 */
public class KClosestPointsToOrigin {
    /**
     * Solution 1: Heap
     * Time Complexity: O(KlogN)
     * Space Complexity: O(N)
     * Result: Accepted (21ms)
     */
    class Solution1 {
        public int[][] kClosest(int[][] points, int K) {
            if (K >= points.length) {
                return points;
            }
            PriorityQueue<int[]> pq = new PriorityQueue<>((p1, p2) -> (dist(p1) - dist(p2)));
            for (int[] point : points) {
                pq.offer(point);
            }
            int index = 0;
            int[][] ans = new int[K][2];
            for (int i = 0; i < Math.min(K, points.length); i++) {
                ans[index++] = pq.poll();
            }
            return ans;
        }
        private int dist(int[] point) {
            return point[0] * point[0] + point[1] * point[1];
        }
    }

    /**
     * Solution 2: Quick select
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     * Result: Accepted (5ms)
     */
    class Solution2 {
        public int[][] kClosest(int[][] points, int K) {
            if (K >= points.length) {
                return points;
            }
            int[][] ans = new int[K][2];
            int lo = 0, hi = points.length - 1;

            while (lo < hi) {
                int pivot = partition(points, lo, hi);
                if (K - 1 <= pivot) {
                    hi = pivot;
                } else {
                    lo = pivot + 1;
                }
            }
            for (int i = K - 1; i >= 0; i--) {
                ans[i] = points[i];
            }
            return ans;
        }
        private int partition(int[][] points, int lo, int hi) {
            int[] pivot = points[lo + ((hi - lo) >> 1)];
            int i = lo - 1;
            int j = hi + 1;
            while (true) {
                do {
                    i += 1;
                } while (closer(points[i], pivot));
                do {
                    j -= 1;
                } while (closer(pivot, points[j]));
                if (i >= j) {
                    return j;
                }
                swap(points, i, j);
            }
        }
        private boolean closer(int[] p0, int[] p1) {
            int dist0 = p0[0] * p0[0] + p0[1] * p0[1];
            int dist1 = p1[0] * p1[0] + p1[1] * p1[1];
            return dist0 < dist1 || ((dist0 == dist1) && p0[0] < p1[0]);
        }
        private void swap(int[][] points, int p0, int p1) {
            int[] tmp = points[p0];
            points[p0] = points[p1];
            points[p1] = tmp;
        }
    }
    public static void main(String[] args) {
        int[][] points = new int[][]{{3,3},{5,-1},{-2,4}};
        Printer.printArray(new KClosestPointsToOrigin().new Solution2().kClosest(points, 2));
        points = new int[][]{{1, 3}, {-2, 2}};
        Printer.printArray(new KClosestPointsToOrigin().new Solution2().kClosest(points, 1));
        points = new int[][]{{1, 3}, {-2, 2}};
        Printer.printArray(new KClosestPointsToOrigin().new Solution2().kClosest(points, 2));
        points = new int[][]{{1, 3}, {-2, 2}, {2, -2}};
        Printer.printArray(new KClosestPointsToOrigin().new Solution2().kClosest(points, 2));
    }
}
