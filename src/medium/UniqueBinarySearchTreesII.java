package medium;

import helper.Printer;
import helper.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * [95] Unique Binary Search Trees II
 *
 * Description:
 * Given an integer n, generate all structurally unique BST's (binary search trees)
 * that store values 1 ... n.
 *
 * Example:
 * Input: 3
 * Output:
 * [
 *   [1,null,3,2],
 *   [3,2,null,1],
 *   [3,1,null,null,2],
 *   [2,1,3],
 *   [1,null,2,null,3]
 * ]
 * Explanation:
 * The above output corresponds to the 5 unique BST's shown below:
 *
 *    1         3     3      2      1
 *     \       /     /      / \      \
 *      3     2     1      1   3      2
 *     /     /       \                 \
 *    2     1         2                 3
 */
public class UniqueBinarySearchTreesII {
    /**
     * Solution 1: Recursion
     * Time Complexity: O(N ^ 2)
     * Space Complexity: O(1)
     * Result: Accepted (1ms)
     */
    class Solution1 {
        public List<TreeNode> generateTrees(int n) {
            return generate(1, n);
        }
        public List<TreeNode> generate(int lo, int hi) {
            List<TreeNode> trees = new ArrayList<>();
            if (lo > hi) {
                return trees;
            }
            for (int i = lo; i <= hi; i++) {
                List<TreeNode> lSubs = generate(lo, i - 1);
                List<TreeNode> rSubs = generate(i + 1, hi);
                if (lSubs.size() == 0) {
                    lSubs.add(null);
                }
                if (rSubs.size() == 0) {
                    rSubs.add(null);
                }
                for (int l = 0; l < lSubs.size(); l++) {
                    for (int r = 0; r < rSubs.size(); r++) {
                        TreeNode root = new TreeNode(i);
                        root.left = lSubs.get(l);
                        root.right = rSubs.get(r);
                        trees.add(root);
                    }
                }
            }
            return trees;
        }
    }
    /**
     * Solution 2: Dynamic Programming
     * Time Complexity: O(N ^ 2)
     * Space Complexity: O(N)
     * Result: Accepted (1ms)
     */
    class Solution2 {
        public List<TreeNode> generateTrees(int n) {
            if (n <= 0) {
                return new ArrayList<>();
            }
            List<TreeNode>[] cache = new List[n + 1];
            cache[0] = new ArrayList<>();
            cache[0].add(null);

            for (int i = 1; i <= n; i++) {
                cache[i] = new ArrayList<>();
                for (int j = 0; j < i; j++) {
                    for (TreeNode lSub : cache[j]) {
                        for (TreeNode rSub : cache[i - j - 1]) {
                            TreeNode root = new TreeNode(j + 1);
                            root.left = lSub;   // lSub is exactly the Left sub-tree
                            root.right = alignTree(rSub, j + 1);
                                                // rSub is roughly correct, except that all
                                                // its numbers are offset by len(lSub) + 1
                            cache[i].add(root);
                        }
                    }
                }
            }
            return cache[n];
        }
        private TreeNode alignTree(TreeNode node, int offset) {
            if (node == null) {
                return node;
            }
            TreeNode alignedNode = new TreeNode(node.val + offset);
            alignedNode.left = alignTree(node.left, offset);
            alignedNode.right = alignTree(node.right, offset);
            return alignedNode;
        }
    }
    public static void main(String[] args) {
        Printer.printList(new UniqueBinarySearchTreesII().new Solution1().generateTrees(3));
        Printer.printList(new UniqueBinarySearchTreesII().new Solution2().generateTrees(3));
    }
}
