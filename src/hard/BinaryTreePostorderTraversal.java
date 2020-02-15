package hard;

import helper.Printer;
import helper.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * [145] Binary Tree Postorder Traversal
 *
 * Description:
 * Given a binary tree, return the postorder traversal of its nodes' values.
 *
 * Example:
 * Input: [1,null,2,3]
 *    1
 *     \
 *      2
 *     /
 *    3
 * Output: [3,2,1]
 *
 * Follow up: Recursive solution is trivial, could you do it iteratively?
 */
public class BinaryTreePostorderTraversal {
    /**
     * Solution 1: Recursive
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     * Result: Accepted (0ms)
     */
    class Solution1 {
        List<Integer> ans = new ArrayList<>();
        public List<Integer> postorderTraversal(TreeNode root) {
            if (root == null) {
                return ans;
            }
            postorderTraversal(root.left);
            postorderTraversal(root.right);
            ans.add(root.val);
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
        public List<Integer> postorderTraversal(TreeNode root) {
            if (root == null) {
                return new ArrayList<>();
            }
            LinkedList<TreeNode> stack = new LinkedList<>();
            LinkedList<Integer> ans = new LinkedList<>();
            stack.push(root);
            while (!stack.isEmpty()) {
                TreeNode node = stack.pop();
                ans.addFirst(node.val);
                if (node.left != null) {
                    stack.push(node.left);
                }
                if (node.right != null) {
                    stack.push(node.right);
                }
            }
            return ans;
        }
    }

    /**
     * Solution 3: Iterative (preserve node relationship)
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     * Result: Accepted (0ms)
     */
    class Solution3 {
        public List<Integer> postorderTraversal(TreeNode root) {
            List<Integer> ans = new ArrayList<>();
            LinkedList<TreeNode> stack = new LinkedList<>();
            TreeNode lastVisitedNode = null;
            TreeNode currNode = root;
            while (!stack.isEmpty() || currNode != null) {
                if (currNode != null) {
                    stack.push(currNode);
                    currNode = currNode.left;
                } else {
                    // Stack must not be empty in this path
                    TreeNode node = stack.peek();
                    if (node.right != null && node.right != lastVisitedNode) {
                        currNode = node.right;
                    } else {
                        ans.add(node.val);
                        lastVisitedNode = stack.pop();
                    }
                }
            }
            return ans;
        }
    }

    /**
     * Solution 4: Morris Traversal (without stack)
     * Explanation: For the following tree structure: [I,E,H,A,D,null,G,null,null,B,C,F]
     *            I
     *          /  \
     *        E     H
     *       / \     \
     *      A   D     G
     *         / \   /
     *        B   C F
     *  Similar to morris traversal for pre- and in- order, morris traversal for post-order
     *  also thread a node to its in-order successor, i.e., A --> E, B --> D, C --> I and
     *  F --> G in this example. Using the added threads, we can retrieve the elements
     *  post-order-ly. For example, output E -> D -> C with the help of the C --> I thread.
     *
     *  Its worth mentioning that we need to manually reverse the traverse sequence, i.e.,
     *  from E -> D -> C to C -> D -> E, to derive the correct output.
     *
     *  Another thing that makes post-order with morris traversal more tricky is the
     *  I -> H -> G sequence. As G is in fact the last element in in-order traverse, we need
     *  to provide an additional dummy node as the root of the tree. In this case, we can
     *  get the final thread of the tree, G --> DUMMY.
     *
     * Time Complexity: O(N)
     * Space Complexity: O(1)
     * Result: Accepted (0ms)
     */
    class Solution4 {
        public List<Integer> postorderTraversal(TreeNode root) {
            LinkedList<Integer> ans = new LinkedList<>();
            TreeNode dummyHead = new TreeNode(-1);
            dummyHead.left = root;
            TreeNode node = dummyHead;
            while (node != null) {
                if (node.left == null) {
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
                        pre = node.left;
                        // Reversely visit the nodes from pre to the rightmost child
                        reverseVisit(ans, pre);
                        node = node.right;
                    }
                }
            }
            return ans;
        }
        private void reverseVisit(List<Integer> nums, TreeNode node) {
            int lPtr = nums.size();
            do {
                nums.add(node.val);
                node = node.right;
            } while (node != null);
            int rPtr = nums.size() - 1;
            while (lPtr < rPtr) {
                int tmp = nums.get(lPtr);
                nums.set(lPtr, nums.get(rPtr));
                nums.set(rPtr, tmp);
                lPtr += 1;
                rPtr -= 1;
            }
        }
    }
    public static void main(String[] args) {
        TreeNode root = new TreeNode(1, null, 2, 3);
        Printer.printList(new BinaryTreePostorderTraversal().new Solution3().postorderTraversal(root));
        Printer.printList(new BinaryTreePostorderTraversal().new Solution4().postorderTraversal(root));
    }
}
