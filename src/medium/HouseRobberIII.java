package medium;

import helper.Printer;
import helper.TreeNode;

/**
 * [337] House Robber III
 *
 * Description:
 * The thief has found himself a new place for his thievery again.
 * There is only one entrance to this area, called the "root."
 * Besides the root, each house has one and only one parent house.
 * After a tour, the smart thief realized that "all houses in this place forms a binary tree".
 * It will automatically contact the police if two directly-linked houses were broken into on the same night.
 *
 * Determine the maximum amount of money the thief can rob tonight without alerting the police.
 *
 * Example 1:
 * Input: [3,2,3,null,3,null,1]
 *
 *      3
 *     / \
 *    2   3
 *     \   \
 *      3   1
 *
 * Output: 7
 * Explanation: Maximum amount of money the thief can rob = 3 + 3 + 1 = 7.
 *
 * Example 2:
 * Input: [3,4,5,1,3,null,1]
 *
 *      3
 *     / \
 *    4   5
 *   / \   \
 *  1   3   1
 *
 * Output: 9
 * Explanation: Maximum amount of money the thief can rob = 4 + 5 = 9.
 */
public class HouseRobberIII {
    public int rob(TreeNode root) {
        int[] robRoot = robNode(root);
        return Math.max(robRoot[0], robRoot[1]);
    }
    public int[] robNode(TreeNode root) {
        if (root == null) {
            return new int[]{0, 0};
        }

        int[] robLeft = robNode(root.left);
        int[] robRight = robNode(root.right);

        int maxIfRobRoot = robLeft[1] + robRight[1] + root.val;
        int maxIfSkipRoot = Math.max(robLeft[0], robLeft[1]) + Math.max(robRight[0], robRight[1]);
        return new int[]{maxIfRobRoot, maxIfSkipRoot};
    }
    public static void main(String[] args) {
        Printer.printNum(new HouseRobberIII().rob(new TreeNode(3,2,3,null,3,null,1)));
        Printer.printNum(new HouseRobberIII().rob(new TreeNode(3,4,5,1,3,null,1)));
    }
}
