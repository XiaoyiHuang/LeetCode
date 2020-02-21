package medium;

import helper.Printer;

import java.util.*;

/**
 * [310] Minimum Height Trees
 *
 * Description:
 * For an undirected graph with tree characteristics, we can choose any node as the root.
 * The result graph is then a rooted tree. Among all possible rooted trees,
 * those with minimum height are called minimum height trees (MHTs).
 * Given such a graph, write a function to find all the MHTs and return a list of their root labels.
 *
 * Format
 * The graph contains n nodes which are labeled from 0 to n - 1.
 * You will be given the number n and a list of undirected edges (each edge is a pair of labels).
 *
 * You can assume that no duplicate edges will appear in edges.
 * Since all edges are undirected, [0, 1] is the same as [1, 0] and thus will not appear together in edges.
 *
 * Example 1 :
 * Input: n = 4, edges = [[1, 0], [1, 2], [1, 3]]
 *
 *         0
 *         |
 *         1
 *        / \
 *       2   3
 *
 * Output: [1]
 *
 * Example 2 :
 * Input: n = 6, edges = [[0, 3], [1, 3], [2, 3], [4, 3], [5, 4]]
 *
 *      0  1  2
 *       \ | /
 *         3
 *         |
 *         4
 *         |
 *         5
 *
 * Output: [3, 4]
 *
 * Note:
 * (1) According to the definition of tree on Wikipedia: “a tree is an undirected graph in which any two vertices
 *     are connected by exactly one path. In other words, any connected graph without simple cycles is a tree.”
 *
 * (2) The height of a rooted tree is the number of edges on the longest downward path between the root and a leaf.
 */
public class MinimumHeightTrees {
    /**
     * Solution 1: Bi-directional BFS
     * Time Complexity: O(V ^ 2)
     * Space Complexity: O(V ^ 2)
     * Result: TLE
     */
    class Solution1 {
        public List<Integer> findMinHeightTrees(int n, int[][] edges) {
            int[][] minDistances = new int[n][n];
            for (int[] edge : edges) {
                minDistances[edge[0]][edge[1]] = 1;
                minDistances[edge[1]][edge[0]] = 1;
            }
            int minTreeHeight = Integer.MAX_VALUE;
            List<Integer> minHeightTreeRoots = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                int maxHeight = 0;
                for (int j = 0; j < n; j++) {
                    if (j == i) {
                        continue;
                    }
                    int dist = getMinDistance(minDistances, i, j);
                    maxHeight = Math.max(maxHeight, dist);
                    if (maxHeight > minTreeHeight) {
                        maxHeight = Integer.MAX_VALUE;
                        break;
                    }
                }
                if (maxHeight <= minTreeHeight) {
                    if (maxHeight < minTreeHeight) {
                        minTreeHeight = maxHeight;
                        minHeightTreeRoots = new ArrayList<>();
                    }
                    minHeightTreeRoots.add(i);
                }
            }
            return minHeightTreeRoots;
        }
        public int getMinDistance(int[][] minDistances, int from, int to) {
            if (from == to) {
                return 0;
            }
            if (minDistances[from][to] > 0) {
                return minDistances[from][to];
            }
            if (minDistances[to][from] > 0) {
                return minDistances[to][from];
            }

            boolean[] fromVisited = new boolean[minDistances.length];
            fromVisited[from] = true;
            boolean[] toVisited = new boolean[minDistances.length];
            toVisited[to] = true;

            List<Integer> fromStack = new ArrayList<>();
            List<Integer> toStack = new ArrayList<>();
            fromStack.add(from);
            toStack.add(to);

            while (!fromStack.isEmpty() && !toStack.isEmpty()) {
                if (fromStack.size() > toStack.size()) {
                    List<Integer> tmpStack = fromStack;
                    fromStack = toStack;
                    toStack = tmpStack;

                    boolean[] tmpVisited = fromVisited;
                    fromVisited = toVisited;
                    toVisited = tmpVisited;

                    int tmp = from;
                    from = to;
                    to = tmp;
                }
                List<Integer> nextStack = new ArrayList<>();
                for (int i : fromStack) {
                    for (int j = 0; j < minDistances[i].length; j++) {
                        if (fromVisited[j] || minDistances[i][j] != 1) {
                            continue;
                        }
                        minDistances[from][j] = minDistances[from][i] + 1;
                        minDistances[j][from] = minDistances[from][j];
                        if (j == to) {
                            return minDistances[from][j];
                        }
                        nextStack.add(j);
                        fromVisited[j] = true;

                        // Check if two ends meet
                        if (toStack.contains(j)) {
                            minDistances[from][to] = minDistances[from][j] + minDistances[to][j];
                            minDistances[to][from] = minDistances[from][to];
                            return minDistances[from][to];
                        }
                    }
                }
                fromStack = nextStack;
            }
            minDistances[from][to] = Integer.MAX_VALUE;
            minDistances[to][from] = Integer.MAX_VALUE;
            return minDistances[from][to];
        }
    }

    /**
     * Solution 2: Two BFS
     * Explanation:
     *     (1) Pick a random node x from the graph, find the node y that has the longest distance from x with BFS
     *         Node y can be proved to be one of the endpoints of the longest path.
     *     (2) Start with y, use BFS again to retrieve the longest path.
     *     (3) The middle node(s) should then be the root(s) of the min height trees we are looking for.
     * Time Complexity: O(V)
     * Space Complexity: O(V + E)
     * Result: Accepted (23ms)
     */
    class Solution2 {
        public List<Integer> findMinHeightTrees(int n, int[][] edges) {
            if (n == 1) {
                return Arrays.asList(0);
            }
            Set<Integer>[] adjacentNodes = new Set[n];
            for (int[] edge : edges) {
                if (adjacentNodes[edge[0]] == null) {
                    adjacentNodes[edge[0]] = new HashSet<>();
                }
                adjacentNodes[edge[0]].add(edge[1]);

                if (adjacentNodes[edge[1]] == null) {
                    adjacentNodes[edge[1]] = new HashSet<>();
                }
                adjacentNodes[edge[1]].add(edge[0]);
            }
            // Store the prev node on the longest path
            int[] prevNode = new int[n];

            // Obtain the farthest nodes from node 0 with BFS
            List<Integer> farthestNodes = bfs(adjacentNodes, prevNode,0);

            // Do a second BFS from either of the farthest nodes obtained before to retrieve the longest path
            Arrays.fill(prevNode, -1);
            farthestNodes = bfs(adjacentNodes, prevNode, farthestNodes.get(0));

            // Rebuild the longest path
            List<Integer> longestPath = new ArrayList<>();
            int currIdx = farthestNodes.get(0);
            while (currIdx >= 0) {
                longestPath.add(currIdx);
                currIdx = prevNode[currIdx];
            }

            // The mid node(s) of the longest path should be the root(s) of min-height trees
            int treeHeight = longestPath.size();
            if ((treeHeight & 1) == 0) {
                return Arrays.asList(longestPath.get(treeHeight >> 1), longestPath.get((treeHeight >> 1) - 1));
            } else {
                return Arrays.asList(longestPath.get(treeHeight >> 1));
            }
        }
        private List<Integer> bfs(Set<Integer>[] adjacentNodes, int[] prevNode, int startIndex) {
            int n = adjacentNodes.length;
            List<Integer> stack = new ArrayList<>();
            boolean[] visited = new boolean[n];

            stack.add(startIndex);
            visited[startIndex] = true;

            while (!stack.isEmpty()) {
                List<Integer> nextStack = new ArrayList<>();
                for (int node : stack) {
                    for (int adj : adjacentNodes[node]) {
                        if (!visited[adj]) {
                            visited[adj] = true;
                            nextStack.add(adj);
                            prevNode[adj] = node;
                        }
                    }
                }
                if (nextStack.size() == 0) {
                    break;
                }
                stack = nextStack;
            }
            return stack;
        }
    }

    /**
     * Solution 3: Dynamic Programming (Tree DP)
     * Explanation: Pick a random node as our starting point, apply DFS to traverse down the tree (graph).
     *              When reaching node u, we can treat {u} as the root of the subtree rooted at {u}, as
     *              illustrated below.
     *
     *                                       o                 o
     *                                      /|\               /|\
     *                                     * u *             * u *
     *                                      /|\
     *                                     / | \
     *                                    v1 v2 *
     *                                   /| ...
     *                                  * * ...
     *
     *              With such recursion, we can get the max-height at {u} by taking the maximum of
     *              upper height {hU}, meaning the height of the max-path from o to u, and
     *              lower height {hL}, which is the max-height from u via v1/v2 to leaf nodes,
     *              i.e. minHeight = max{hU, hL}.
     *
     *              The value of {hL} can be derived with simple recursion. However, things can get a little
     *              more complicated when dealing with {hU}, as there are in fact two different scenarios.
     *
     *              Taking the {hU} of v1 as an example:
     *                  (1) The first route is straightforward, we simply take {o -> u -> v1} (R1) as {hU}
     *                  (2) The second route is more trivial, which goes {v* -> u -> v1} (R2)
     *              Hence, we have hU = max{R1, R2}
     *
     *              Does scenario (2) suggests that we need to obtain the heights of all branches of node u?
     *              Surely no. There are again only two scenarios for R2:
     *                  (1) If {v1 -> u -> v1} happens to be the longest branch of {u}, then R2 must be
     *                      the second-longest branch, if there exists one.
     *                  (2) If not, then R2 must be the longest branch.
     *
     *              Therefore, we only need to know the which branches have the top-two {hL} values at most.
     *              This saves a significant amount of runtime if the degree of node {u} is exceptionally large.
     *              In our code, we use two height arrays to store the largest and second-to-largest
     *              heights of all branches.
     *
     * Time Complexity: O(V)
     * Space Complexity: O(V + E)
     * Result: Accepted (19ms)
     */
    class Solution3 {
        int[] minHeights;
        int[] maxLowerHeights;
        int[] secondToMaxLowerHeights;

        public List<Integer> findMinHeightTrees(int n, int[][] edges) {
            if (n == 1) {
                return Arrays.asList(0);
            }
            Set<Integer>[] adjacentNodes = new Set[n];
            for (int[] edge : edges) {
                if (adjacentNodes[edge[0]] == null) {
                    adjacentNodes[edge[0]] = new HashSet<>();
                }
                adjacentNodes[edge[0]].add(edge[1]);

                if (adjacentNodes[edge[1]] == null) {
                    adjacentNodes[edge[1]] = new HashSet<>();
                }
                adjacentNodes[edge[1]].add(edge[0]);
            }

            minHeights = new int[n];
            maxLowerHeights = new int[n];
            secondToMaxLowerHeights = new int[n];

            getMaxLowerHeights(adjacentNodes, 0, -1);
            getMinHeights(adjacentNodes, 0, -1, 0);

            int minHeight = Integer.MAX_VALUE;
            List<Integer> minHeightTrees = new ArrayList<>();
            for (int i = 0; i < minHeights.length; i++) {
                if (minHeights[i] <= minHeight) {
                    if (minHeights[i] < minHeight) {
                        minHeightTrees = new ArrayList<>();
                        minHeight = minHeights[i];
                    }
                    minHeightTrees.add(i);
                }
            }
            return minHeightTrees;
        }
        private void getMaxLowerHeights(Set<Integer>[] adjacentNodes, int root, int parent) {
            maxLowerHeights[root] = secondToMaxLowerHeights[root] = Integer.MIN_VALUE;
            for (int child : adjacentNodes[root]) {
                if (child != parent) {
                    getMaxLowerHeights(adjacentNodes, child, root);

                    // Record the largest or second-to-largest lower heights
                    int maxBranchHeight = maxLowerHeights[child] + 1;
                    if (maxBranchHeight > maxLowerHeights[root]) {
                        secondToMaxLowerHeights[root] = maxLowerHeights[root];
                        maxLowerHeights[root] = maxBranchHeight;
                    } else if (maxBranchHeight > secondToMaxLowerHeights[root]) {
                        secondToMaxLowerHeights[root] = maxBranchHeight;
                    }
                }
            }
            // If root is a leaf node, reset max lower heights to be at least 0
            maxLowerHeights[root] = Math.max(0, maxLowerHeights[root]);
        }
        private void getMinHeights(Set<Integer>[] adjacentNodes, int root, int parent, int rootUpperHeight) {
            minHeights[root] = Math.max(rootUpperHeight, maxLowerHeights[root]);
            for (int child : adjacentNodes[root]) {
                if (child != parent) {
                    boolean isChildOnMaxBranch = (maxLowerHeights[child] + 1 == maxLowerHeights[root]);
                    int childUpperHeight = Math.max(rootUpperHeight,
                            isChildOnMaxBranch ? secondToMaxLowerHeights[root] : maxLowerHeights[root]) + 1;
                    getMinHeights(adjacentNodes, child, root, childUpperHeight);
                }
            }
        }
    }

    /**
     * Solution 4: Topological Sort
     * Explanation: To find the minimum height trees out of the graph, think of the graph as a couple
     *              concentric circles. We start from the out-most circle and peel it down layer by
     *              layer, until we find the centers, which should be the root we are looking for.
     *              Note that there could be up to two centers, as either node in a pair can be the
     *              root for the other one.
     * Time Complexity: O(V)
     * Space Complexity: O(V + E)
     * Result: Accepted (16ms)
     */
    class Solution4 {
        public List<Integer> findMinHeightTrees(int n, int[][] edges) {
            if (n == 1) {
                return Arrays.asList(0);
            }
            Set<Integer>[] adjacentNodes = new Set[n];
            for (int[] edge : edges) {
                if (adjacentNodes[edge[0]] == null) {
                    adjacentNodes[edge[0]] = new HashSet<>();
                }
                adjacentNodes[edge[0]].add(edge[1]);

                if (adjacentNodes[edge[1]] == null) {
                    adjacentNodes[edge[1]] = new HashSet<>();
                }
                adjacentNodes[edge[1]].add(edge[0]);
            }

            List<Integer> stack = new ArrayList<>();
            for (int i = 0; i < adjacentNodes.length; i++) {
                if (adjacentNodes[i].size() == 1) {
                    stack.add(i);
                }
            }
            int remainingNodes = n;
            while (remainingNodes > 2) {
                List<Integer> nextStack = new ArrayList<>();
                remainingNodes -= stack.size();
                for (int leaf : stack) {
                    for (int neighbor : adjacentNodes[leaf]) {
                        adjacentNodes[neighbor].remove(leaf);
                        if (adjacentNodes[neighbor].size() == 1) {
                            nextStack.add(neighbor);
                        }
                    }
                }
                stack = nextStack;
            }
            return stack;
        }
    }
    public static void main(String[] args) {
        Printer.printList(new MinimumHeightTrees().new Solution4().findMinHeightTrees(4,
                new int[][]{{1, 0}, {1, 2}, {1, 3}}));
        Printer.printList(new MinimumHeightTrees().new Solution4().findMinHeightTrees(6,
                new int[][]{{0, 3}, {1, 3}, {2, 3}, {4, 3}, {5, 4}}));
    }
}
