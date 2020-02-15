package medium;

import helper.Printer;
import helper.TreeNode;

import java.util.LinkedList;

/**
 * [98] Validate Binary Search Tree
 *
 * Description:
 * Given a binary tree, determine if it is a valid binary search tree (BST).
 *
 * Assume a BST is defined as follows:
 * The left subtree of a node contains only nodes with keys less than the node's key.
 * The right subtree of a node contains only nodes with keys greater than the node's key.
 * Both the left and right subtrees must also be binary search trees.
 *
 * Example 1:
 *
 *     2
 *    / \
 *   1   3
 *
 * Input: [2,1,3]
 * Output: true
 *
 * Example 2:
 *
 *     5
 *    / \
 *   1   4
 *      / \
 *     3   6
 *
 * Input: [5,1,4,null,null,3,6]
 * Output: false
 * Explanation: The root node's value is 5 but its right child's value is 4.
 */
public class ValidateBinarySearchTree {
    /**
     * Solution 1: Recursion
     * Time Complexity: O(N)
     * Space Complexity: O(1)
     * Result: Accepted (0ms)
     */
    class Solution1 {
        public boolean isValidBST(TreeNode root) {
            return validate(root, null, null);
        }
        private boolean validate(TreeNode root, Integer min, Integer max) {
            if (root == null) {
                return true;
            }
            if ((min != null && root.val <= min) || (max != null && root.val >= max)) {
                return false;
            }
            return validate(root.left, min, root.val) && validate(root.right, root.val, max);
        }
    }

    /**
     * Solution 2: Iteration (with stack)
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     * Result: Accepted (2ms)
     */
    class Solution2 {
        public boolean isValidBST(TreeNode root) {
            if (root == null) {
                return true;
            }
            LinkedList<TreeNode> stack = new LinkedList<>();
            LinkedList<Integer> upperLimits = new LinkedList<>();
            LinkedList<Integer> lowerLimits = new LinkedList<>();
            stack.push(root);
            upperLimits.push(null);
            lowerLimits.push(null);
            while (!stack.isEmpty()) {
                TreeNode node = stack.pop();
                Integer upperLimit = upperLimits.pop();
                Integer lowerLimit = lowerLimits.pop();
                if ((upperLimit != null && node.val >= upperLimit) ||
                        (lowerLimit != null && node.val <= lowerLimit)) {
                    return false;
                }
                if (node.right != null) {
                    stack.push(node.right);
                    upperLimits.push(upperLimit);
                    lowerLimits.push(lowerLimit != null ? Math.max(node.val, lowerLimit) : node.val);
                }
                if (node.left != null) {
                    stack.push(node.left);
                    upperLimits.push(upperLimit != null ? Math.min(node.val, upperLimit) : node.val);
                    lowerLimits.push(lowerLimit);
                }
            }
            return true;
        }
    }

    /**
     * Solution 3: In-order Traversal
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     * Result: Accepted (1ms)
     */
    class Solution3 {
        public boolean isValidBST(TreeNode root) {
            if (root == null) {
                return true;
            }
            LinkedList<TreeNode> stack = new LinkedList<>();
            TreeNode curr = root;
            Integer prevNum = null;
            while (curr != null || !stack.isEmpty()) {
                while (curr != null) {
                    stack.push(curr);
                    curr = curr.left;
                }
                curr = stack.pop();
                // Visit the node
                if (prevNum != null && curr.val <= prevNum) {
                    return false;
                } else {
                    prevNum = curr.val;
                }
                curr = curr.right;
            }
            return true;
        }
    }
    public static void main(String[] args) {
        Printer.printBool(new ValidateBinarySearchTree().new Solution2()
                .isValidBST(new TreeNode(2,1,3)));
        Printer.printBool(new ValidateBinarySearchTree().new Solution2()
                .isValidBST(new TreeNode(5,1,6,null,null,3,7)));
        Printer.printBool(new ValidateBinarySearchTree().new Solution2()
                .isValidBST(new TreeNode(3,1,5,0,2,4,6)));
    }
}
