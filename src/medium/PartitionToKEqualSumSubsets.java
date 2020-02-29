package medium;

import helper.Printer;

import java.util.Arrays;

/**
 * [698] Partition to K Equal Sub Subsets
 *
 * Description:
 * Given an array of integers nums and a positive integer k,
 * find whether it's possible to divide this array into k non-empty subsets whose sums are all equal.
 *
 * Example 1:
 * Input: nums = [4, 3, 2, 3, 5, 2, 1], k = 4
 * Output: True
 * Explanation: It's possible to divide it into 4 subsets (5), (1, 4), (2,3), (2,3) with equal sums.

 * Note:
 *      1. 1 <= k <= len(nums) <= 16.
 *      2. 0 < nums[i] < 10000.
 */
public class PartitionToKEqualSumSubsets {
    /**
     * Solution 1: Backtracking
     * Time Complexity: O(k ^ N) (TODO)
     *            Note: If the search is done without the parameter 'startIndex', we can only eliminate one
     *                  element after each iteration, which means the time complexity for finding a valid
     *                  subset should be O(N!).
     *
     *                  However, with the leading 'startIndex', we can avoid some repetitive cases, such as
     *                  trying subsets with sequences '0-1-2' and '0-2-1'. In this case, all numbers are
     *                  either picked or not picked, i.e., it won't be repeatedly picked. Therefore, the
     *                  time complexity is cut down to O(2 ^ N). As we have k subsets in total,
     *                  the final time complexity should be somewhat close to
     *                  O(k * 2 ^ N).
     *
     *                  However, as the size of remaining numbers is changed every time a subset is confirmed,
     *                  and also that it's possible a valid subset is proved to be erroneous in later
     *                  recursions for other subsets, the above time complexity might not be accurate enough.
     *
     *                  Another theory seems more reasonable to me, which is based on the thought that
     *                  each number can be assigned to a subset at most once, and therefore has a time
     *                  complexity of O(k ^ N). This idea is proved in solution 2.
     * Space Complexity: O(N)
     * Result: Accepted (0ms)
     */
    class Solution1 {
        public boolean canPartitionKSubsets(int[] nums, int k) {
            if (k == 1) {
                return true;
            }
            int sum = 0;
            for (int num : nums) {
                sum += num;
            }
            if (sum % k != 0) {
                return false;
            }
            sum = sum / k;
            return search(nums, new boolean[nums.length], k, nums.length - 1, 0, sum);
        }
        public boolean search(int[] nums, boolean[] visited, int remainSet, int startIndex, int setSum, int target) {
            if (remainSet <= 0) {
                return true;
            }
            if (setSum > target) {
                return false;
            }
            if (setSum == target) {
                return search(nums, visited, remainSet - 1, nums.length - 1, 0, target);
            }
            for (int i = startIndex; i >= 0; i--) {
                if (visited[i]) {
                    continue;
                }
                visited[i] = true;
                boolean canPartition = search(nums, visited, remainSet, i - 1, setSum + nums[i], target);
                if (canPartition) {
                    return true;
                }
                visited[i] = false;
            }
            return false;
        }
    }

    /**
     * Solution 2: Backtracking with optimization
     * Explanation: We can take the problem from a different perspective. Instead of focusing on a subgroup
     *              and iterate over all numbers looking for appropriate ones to fit in (number-oriented),
     *              we can switch to a group-oriented approach by assigning a number to valid subsets (groups).
     *
     *              In each iteration, we could add the number we are currently focusing on (num) to either one
     *              of the k groups, as long as the group's sum won't exceed the target value (which is
     *              sum(nums) / k), if we managed to place all numbers, the search ends successfully.
     *
     *              One important tricks is that if we encounter an empty group while searching, which means that
     *              the current number cannot be placed into any groups under current scheme, we can break out of
     *              the iteration early. This helps avoiding many repeated work, for example, we only need to
     *              assign the number to ONE group in the first round, instead of all K groups.
     *
     *              The time complexity of this solution is optimized with this trick, down from O(k ^ N) to
     *              O(k ^ (N - k) * k!). Without the trick, we need to make k recursive calls each round in the
     *              worst case, can repeated for N recursions (k * k * ... * k). After optimization, the recursive
     *              calls goes to pattern (1 * 2 * 3 * ... * k * k * ... * k), which is O(k! * k ^ (N - k)).
     *
     * Time Complexity: O(k ^ (N - k) * k!) (See explanation above)
     * Space Complexity: O(N) (stack space for recursions)
     * Result: Accepted (2ms)
     */
    class Solution2 {
        public boolean canPartitionKSubsets(int[] nums, int k) {
            if (k == 1) {
                return true;
            }
            int sum = 0;
            for (int num : nums) {
                sum += num;
            }
            if (sum % k > 0) {
                return false;
            }
            int target = sum / k;

            Arrays.sort(nums);
            int numIndex = nums.length - 1;
            if (nums[numIndex] > target) {
                return false;
            }
            while (numIndex >= 0 && nums[numIndex] == target) {
                numIndex--;
                k--;
            }
            return search(new int[k], numIndex, nums, target);
        }
        public boolean search(int[] groups, int numIndex, int[] nums, int target) {
            if (numIndex < 0) return true;
            int v = nums[numIndex--];
            for (int i = 0; i < groups.length; i++) {
                if (groups[i] + v <= target) {
                    groups[i] += v;
                    if (search(groups, numIndex, nums, target)) {
                        return true;
                    }
                    groups[i] -= v;
                }
                if (groups[i] == 0) {
                    break;
                }
            }
            return false;
        }
    }

    /**
     * Solution 3: Dynamic Programming (Top-down)
     * Time Complexity: O(N * 2 ^ N) (N + N + ... + N, repeated for 2 ^ N times in the worst case)
     * Space Complexity: O(2 ^ N)
     * Result: Accepted (4ms)
     */
    class Solution3 {
        public boolean canPartitionKSubsets(int[] nums, int k) {
            if (k == 1) {
                return true;
            }
            int sum = 0;
            for (int num : nums) {
                sum += num;
            }
            if (sum % k != 0) {
                return false;
            }
            int target = sum / k;
            int[] memo = new int[1 << nums.length]; // Each num can be either used or not, hence
                                                    // there are (2 ^ N) possibilities in all
            memo[memo.length - 1] = 1;              // The last memo is when all numbers are used,
                                                    // in this case the result should be true
            return search(nums, 0, memo, target, sum);
        }
        private boolean search(int[] nums, int visited, int[] memo, int target, int unusedSum) {
            if (memo[visited] != 0) {
                return memo[visited] == 1;
            }
            int maxNextNum = (unusedSum - 1) % target + 1;
            for (int i = 0; i < nums.length; i++) {
                if (((visited >> i) & 1) == 0 && nums[i] <= maxNextNum) {
                    boolean result = search(nums, (visited | (1 << i)), memo, target, unusedSum - nums[i]);
                    if (result) {
                        memo[visited] = 1;
                        return true;
                    }
                }
            }
            memo[visited] = -1;
            return false;
        }
    }

    /**
     * Solution 4: Dynamic Programming (Bottom-up)
     * Time Complexity: O(N * 2 ^ N)
     * Space Complexity: O(2 ^ N)
     * Result: Accepted (20ms)
     */
    class Solution4 {
        public boolean canPartitionKSubsets(int[] nums, int k) {
            if (k == 1) {
                return true;
            }
            int sum = 0;
            for (int num : nums) {
                sum += num;
            }
            if (sum % k != 0) {
                return false;
            }
            int target = sum / k;

            Arrays.sort(nums);
            boolean[] dp = new boolean[1 << nums.length];   // If current bit states can be reached
            int[] sums = new int[1 << nums.length];         // The sum of set numbers under the corresponding bit states
            dp[0] = true;

            for (int i = 0; i < dp.length; i++) {
                if (!dp[i]) {
                    continue;
                }
                int maxNextNum = target - sums[i] % target;
                for (int j = 0; j < nums.length; j++) {
                    int nextState = i | (1 << j);
                    if (nextState != i && !dp[nextState]) {
                        if (nums[j] <= maxNextNum) {
                            dp[nextState] = true;
                            sums[nextState] = sums[i] + nums[j];
                        } else {
                            // Since nums is sorted, no need to check others
                            break;
                        }
                    }
                }
            }
            return dp[dp.length - 1];
        }
    }
    public static void main(String[] args) {
        Printer.printBool(new PartitionToKEqualSumSubsets().new Solution1()
                .canPartitionKSubsets(new int[]{4,3,2,3,5,2,1}, 4));
        Printer.printBool(new PartitionToKEqualSumSubsets().new Solution1()
                .canPartitionKSubsets(new int[]{5,1,1,1}, 2));
        Printer.printBool(new PartitionToKEqualSumSubsets().new Solution1()
                .canPartitionKSubsets(new int[]{10,10,10,7,7,7,7,7,7,6,6,6}, 3));
        Printer.printBool(new PartitionToKEqualSumSubsets().new Solution1()
                .canPartitionKSubsets(new int[]{2,11,1,10,4,10,1,4,2}, 3));
    }
}
