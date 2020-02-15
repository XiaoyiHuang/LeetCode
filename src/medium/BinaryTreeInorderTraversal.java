package medium;

import helper.Printer;
import helper.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * [94] Binary Tree Inorder Traversal
 *
 * Description:
 * Given a binary tree, return the inorder traversal of its nodes' values.
 *
 * Example:
 * Input: [1,null,2,3]
 *    1
 *     \
 *      2
 *     /
 *    3
 * Output: [1,3,2]
 * Follow up: Recursive solution is trivial, could you do it iteratively?
 */
public class BinaryTreeInorderTraversal {
    /**
     * Solution 1: Recursive
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     * Result: Accepted (0ms)
     */
    class Solution1 {
        List<Integer> ans = new ArrayList<>();
        public List<Integer> inorderTraversal(TreeNode root) {
            if (root == null) {
                return ans;
            }
            inorderTraversal(root.left);
            ans.add(root.val);
            inorderTraversal(root.right);
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
        public List<Integer> inorderTraversal(TreeNode root) {
            if (root == null) {
                return new ArrayList<>();
            }
            List<Integer> ans = new ArrayList<>();
            LinkedList<TreeNode> stack = new LinkedList<>();
            stack.push(root);
            while (!stack.isEmpty()) {
                TreeNode node = stack.peek();
                while (node.left != null) {
                    stack.push(node.left);
                    node = node.left;
                }
                while (!stack.isEmpty()) {
                    node = stack.pop();
                    ans.add(node.val);
                    if (node.right != null) {
                        // Only breaks out (and traverse along left child) if
                        // we push a right-child into the stack
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
        public List<Integer> inorderTraversal(TreeNode root) {
            List<Integer> ans = new ArrayList<>();
            TreeNode node = root;
            while (node != null) {
                if (node.left == null) {
                    ans.add(node.val);
                    node = node.right;
                } else {
                    TreeNode pre = node.left;
                    while (pre.right != null && pre.right != node) {
                        // Find the predecessor (pp) of pre (p)
                        pre = pre.right;
                    }
                    if (pre.right == null) {
                        // Setup node as the successor of pp (thread the tree)
                        pre.right = node;
                        node = node.left;
                    } else {
                        // Remove the thread
                        pre.right = null;
                        ans.add(node.val);
                        node = node.right;
                    }
                }
            }
            return ans;
        }
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1, null, 2, 3);
        Printer.printList(new BinaryTreeInorderTraversal().new Solution1().inorderTraversal(root));
        Printer.printList(new BinaryTreeInorderTraversal().new Solution2().inorderTraversal(root));
        Printer.printList(new BinaryTreeInorderTraversal().new Solution3().inorderTraversal(root));
    }
}
