package medium;

import helper.Printer;
import helper.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * [144] Binary Tree Preorder Traversal
 *
 * Description:
 * Given a binary tree, return the preorder traversal of its nodes' values.
 *
 * Example:
 * Input: [1,null,2,3]
 *    1
 *     \
 *      2
 *     /
 *    3
 * Output: [1,2,3]
 *
 * Follow up: Recursive solution is trivial, could you do it iteratively?
 */
public class BinaryTreePreorderTraversal {
    /**
     * Solution 1: Recursive
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     * Result: Accepted (0ms)
     */
    class Solution1 {
        List<Integer> ans = new ArrayList<>();
        public List<Integer> preorderTraversal(TreeNode root) {
            if (root == null) {
                return ans;
            }
            ans.add(root.val);
            preorderTraversal(root.left);
            preorderTraversal(root.right);
            return ans;
        }
    }

    /**
     * Solution 2: Iterative
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     * Result: Accepted (0ms)
     */
    class Solution2 {
        public List<Integer> preorderTraversal(TreeNode root) {
            if (root == null) {
                return new ArrayList<>();
            }
            List<Integer> ans = new ArrayList<>();
            LinkedList<TreeNode> stack = new LinkedList<>();
            stack.push(root);
            while (!stack.isEmpty()) {
                TreeNode node = stack.peek();
                while (node != null) {
                    ans.add(node.val);
                    if (node.left != null) {
                        stack.push(node.left);
                    }
                    node = node.left;
                }
                while (!stack.isEmpty()) {
                    node = stack.pop();
                    if (node.right != null) {
                        stack.push(node.right);
                        break;
                    }
                }
            }
            return ans;
        }
    }

    /**
     * Solution 3: Morris Traversal (without stack)
     * Time Complexity: O(N)
     * Space Complexity: O(1)
     * Result: Accepted (0ms)
     */
    class Solution3 {
        public List<Integer> preorderTraversal(TreeNode root) {
            List<Integer> ans = new ArrayList<>();
            TreeNode node = root;
            while (node != null) {
                if (node.left == null) {
                    ans.add(node.val);
                    node = node.right;
                } else {
                    TreeNode pre = node.left;
                    while (pre.right != null && pre.right != node) {
                        pre = pre.right;
                    }
                    if (pre.right == null) {
                        // Does NOT thread pre.right to its direct successor
                        pre.right = node;
                        ans.add(node.val);
                        node = node.left;
                    } else {
                        // Restore modified pointers
                        pre.right = null;
                        node = node.right;
                    }
                }
            }
            return ans;
        }

        // Another style of writing (will modify tree pointers)
        public List<Integer> morrisTraversalWithModifiedPointers(TreeNode root) {
            List<Integer> ans = new ArrayList<>();
            TreeNode node = root;
            while (node != null) {
                if (node.left == null) {
                    ans.add(node.val);
                    node = node.right;
                } else {
                    TreeNode pre = node.left;
                    while (pre.right != null && pre.right != node.right) {
                        // Thread pre.right to its successor
                        pre = pre.right;
                    }
                    if (pre.right == null) {
                        pre.right = node.right;
                        ans.add(node.val);
                        node = node.left;
                    }
                }
            }
            return ans;
        }
    }
    public static void main(String[] args) {
        TreeNode root = new TreeNode(1, null, 2, 3);
        Printer.printList(new BinaryTreePreorderTraversal().new Solution1().preorderTraversal(root));
        Printer.printList(new BinaryTreePreorderTraversal().new Solution2().preorderTraversal(root));
        Printer.printList(new BinaryTreePreorderTraversal().new Solution3().preorderTraversal(root));
    }
}
