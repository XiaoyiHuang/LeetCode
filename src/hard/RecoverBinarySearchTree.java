package hard;

import helper.Printer;
import helper.TreeNode;

import java.util.LinkedList;

/**
 * [99] Recover Binary Search Tree
 *
 * Description:
 * Two elements of a binary search tree (BST) are swapped by mistake.
 * Recover the tree without changing its structure.
 *
 * Example 1:
 * Input: [1,3,null,null,2]
 *         1
 *        /
 *       3
 *        \
 *         2
 * Output: [3,1,null,null,2]
 *         3
 *        /
 *       1
 *        \
 *         2
 *
 * Example 2:
 * Input: [3,1,4,null,null,2]
 *        3
 *       / \
 *      1   4
 *         /
 *        2
 * Output: [2,1,4,null,null,3]
 *        2
 *       / \
 *      1   4
 *         /
 *        3
 *
 * Follow up:
 * A solution using O(n) space is pretty straight forward.
 * Could you devise a constant space solution?
 */
public class RecoverBinarySearchTree {
    /**
     * Solution 1: Iterative in-order traversal
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     * Result: Accepted (2ms)
     */
    class Solution1 {
        public void recoverTree(TreeNode root) {
            TreeNode[] swappedNodes = new TreeNode[2];
            LinkedList<TreeNode> stack = new LinkedList<>();
            TreeNode curr = root;
            TreeNode prev = null;
            while (!stack.isEmpty() || curr != null) {
                while (curr != null) {
                    stack.push(curr);
                    curr = curr.left;
                }
                curr = stack.pop();
                if (prev != null && curr.val < prev.val) {
                    if (swappedNodes[0] == null) {
                        swappedNodes[0] = prev;
                    }
                    swappedNodes[1] = curr;
                }
                prev = curr;
                curr = curr.right;
            }
            // Recover from the erroneous swap
            if (swappedNodes[0] != null && swappedNodes[1] != null) {
                int tmpVal = swappedNodes[0].val;
                swappedNodes[0].val = swappedNodes[1].val;
                swappedNodes[1].val = tmpVal;
            }
        }
    }

    /**
     * Solution 2: Morris Traversal
     * Time Complexity: O(N)
     * Space Complexity: O(1)
     * Result: Accepted (2ms)
     */
    class Solution2 {
        public void recoverTree(TreeNode root) {
            TreeNode[] swappedNodes = new TreeNode[2];
            TreeNode node = root;
            TreeNode prev = null;
            while (node != null) {
                if (node.left == null) {
                    // Visit node
                    visitNode(swappedNodes, node, prev);
                    prev = node;
                    node = node.right;
                } else {
                    TreeNode pre = node.left;
                    while (pre.right != null && pre.right != node) {
                        pre = pre.right;
                    }
                    if (pre.right == null) {
                        pre.right = node;
                        node = node.left;
                    } else {
                        pre.right = null;
                        // Visit node
                        visitNode(swappedNodes, node, prev);
                        prev = node;
                        node = node.right;
                    }
                }
            }
            // Recover from the erroneous swap
            if (swappedNodes[0] != null && swappedNodes[1] != null) {
                int tmpVal = swappedNodes[0].val;
                swappedNodes[0].val = swappedNodes[1].val;
                swappedNodes[1].val = tmpVal;
            }
        }
        private void visitNode(TreeNode[] swappedNodes, TreeNode curr, TreeNode prev) {
            if (prev != null && curr.val < prev.val) {
                if (swappedNodes[0] == null) {
                    swappedNodes[0] = prev;
                }
                swappedNodes[1] = curr;
            }
        }
    }
    public static void main(String[] args) {
        TreeNode root = new TreeNode(3,1,4,null,null,2);
        new RecoverBinarySearchTree().new Solution1().recoverTree(root);
        Printer.printTree(root);
        root = new TreeNode(3,1,4,null,null,2);
        new RecoverBinarySearchTree().new Solution2().recoverTree(root);
        Printer.printTree(root);
        root = new TreeNode(1,3,null,null,2);
        new RecoverBinarySearchTree().new Solution1().recoverTree(root);
        Printer.printTree(root);
        root = new TreeNode(1,3,null,null,2);
        new RecoverBinarySearchTree().new Solution2().recoverTree(root);
        Printer.printTree(root);
    }
}
