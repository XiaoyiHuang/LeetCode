package medium;

import helper.Printer;

import java.util.Arrays;

/**
 * [416] Partition Equal Subset Sum
 *
 * Description:
 * Given a non-empty array containing only positive integers, find if the array can be
 * partitioned into two subsets such that the sum of elements in both subsets is equal.
 *
 * Note:
 * Each of the array element will not exceed 100.
 * The array size will not exceed 200.
 *
 *
 * Example 1:
 * Input: [1, 5, 11, 5]
 * Output: true
 * Explanation: The array can be partitioned as [1, 5, 5] and [11].
 *
 *
 * Example 2:
 * Input: [1, 2, 3, 5]
 * Output: false
 * Explanation: The array cannot be partitioned into equal sum subsets.
 */
public class PartitionEqualSubsetSum {
    /**
     * Solution 1: Dynamic Programming
     * Time Complexity: O(N * s) (s is the sum of all elements in nums)
     * Space Complexity: O(N * s)
     * Result: Accepted (31ms)
     */
    class Solution1 {
        public boolean canPartition(int[] nums) {
            int sum = 0;
            for (int num : nums) {
                sum += num;
            }
            if ((sum & 1) == 1) {
                return false;
            } else {
                sum >>= 1;
            }
            boolean[][] dp = new boolean[nums.length + 1][sum + 1];
            dp[0][0] = true;
            for (int i = 1; i <= nums.length; i++) {
                for (int j = 0; j <= sum; j++) {
                    int tmp;
                    if ((tmp = j + nums[i - 1]) <= sum) {
                        dp[i][tmp] = dp[i - 1][j] || dp[i - 1][tmp];
                    }
                    dp[i][j] = dp[i][j] || dp[i - 1][j];
                }
                if (dp[i][sum]) {
                    return true;
                }
            }
            return dp[nums.length][sum];
        }
    }

    /**
     * Solution 2: Dynamic Programming (Space Optimization)
     * Time Complexity: O(N * s) (s is the sum of all elements in nums)
     * Space Complexity: O(s)
     * Result: Accepted (9ms)
     */
    class Solution2 {
        public boolean canPartition(int[] nums) {
            int sum = 0;
            for (int num : nums) {
                sum += num;
            }
            if ((sum & 1) == 1) {
                return false;
            } else {
                sum >>= 1;
            }
            boolean[] dp = new boolean[sum + 1];
            dp[0] = true;
            for (int i = 1; i <= nums.length; i++) {
                for (int j = sum; j >= nums[i - 1]; j--) {
                    dp[j] = dp[j] || dp[j - nums[i - 1]];
                }
                if (dp[sum]) {
                    return true;
                }
            }
            return dp[sum];
        }
    }

    /**
     * Solution 3: DFS + Memoization
     * Time Complexity: O(N * k) (s is the sum of all elements in nums)
     * Space Complexity: O(N * k)
     * Result: Accepted (2ms)
     */
    class Solution3 {
        public boolean canPartition(int[] nums) {
            int sum = 0;
            for (int num : nums) {
                sum += num;
            }
            if ((sum & 1) == 1) {
                return false;
            } else {
                sum >>= 1;
            }

            // states[i][j] would be true if numbers 0...i CANNOT sum up to j
            boolean[][] states = new boolean[nums.length][sum + 1];

            Arrays.sort(nums);
            return dfs(nums, nums.length - 1, sum, states);
        }
        private boolean dfs(int[] nums, int index, int target, boolean[][] states) {
            if (target == 0) {
                return true;
            }
            if (index < 0 || states[index][target]) {
                return false;
            }
            for (int i = index; i >= 0; i--) {
                if (nums[i] > target) {
                    continue;
                }
                if (i + 1 < nums.length && i != index && nums[i] == nums[i + 1]) {
                    // For such scenario, we know that: (1) pick the prior number, ignore the latter one, equals
                    // (2) ignore the prior one, and pick the latter number, therefore we can cut out either
                    // situation to reduce some runtime (here we leave out (1))
                    continue;
                }
                boolean res = dfs(nums, i - 1, target - nums[i], states);
                if (res) {
                    return true;
                }
            }
            states[index][target] = true;
            return false;
        }
    }
    public static void main(String[] args) {
        Printer.printBool(new PartitionEqualSubsetSum().new Solution1().canPartition(new int[]{1,5,11,5}));
        Printer.printBool(new PartitionEqualSubsetSum().new Solution2().canPartition(new int[]{1,5,11,5}));
        Printer.printBool(new PartitionEqualSubsetSum().new Solution3().canPartition(new int[]{1,5,11,5}));
        Printer.printBool(new PartitionEqualSubsetSum().new Solution1().canPartition(new int[]{1,2,3,5}));
        Printer.printBool(new PartitionEqualSubsetSum().new Solution2().canPartition(new int[]{1,2,3,5}));
        Printer.printBool(new PartitionEqualSubsetSum().new Solution3().canPartition(new int[]{1,2,3,5}));
    }
}
