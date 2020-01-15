package medium;

import helper.Printer;

import java.util.ArrayList;
import java.util.List;

/**
 * [46] Permutations
 *
 * Description:
 * Given a collection of distinct integers, return all possible permutations.
 *
 * Example:
 * Input: [1,2,3]
 * Output:
 * [
 *   [1,2,3],
 *   [1,3,2],
 *   [2,1,3],
 *   [2,3,1],
 *   [3,1,2],
 *   [3,2,1]
 * ]
 *
 * Result:
 * Solution 1: Accepted (1ms)
 * Solution 2: Accepted (1ms)
 * Solution 3: Accepted (0ms)
 */
public class Permutations {
    /**
     * Solution 1
     * Time Complexity: O(N^2)
     * Space Complexity: O(N)
     */
    class Solution1 {
        public List<List<Integer>> permute(int[] nums) {
            List<List<Integer>> results = new ArrayList<>();
            backtracking(results, new ArrayList<>(), nums, new boolean[nums.length]);
            return results;
        }
        private void backtracking(List<List<Integer>> combs, List<Integer> currComb, int[] nums, boolean[] visited) {
            if (currComb.size() == nums.length) {
                combs.add(new ArrayList<>(currComb));
                return;
            }
            for (int i = 0; i < nums.length; i++) {
                if (!visited[i]) {
                    // Update state
                    currComb.add(nums[i]);
                    visited[i] = true;

                    // Recurse
                    backtracking(combs, currComb, nums, visited);

                    // Revert state
                    currComb.remove(currComb.size() - 1);
                    visited[i] = false;
                }
            }
        }
    }

    /**
     * Solution 2: Iterative Approach
     * Time Complexity: O(N^2)
     * Space Complexity: O(1)
     */
    class Solution2 {
        public List<List<Integer>> permute(int[] nums) {
            if (nums.length == 0) {
                return new ArrayList<>();
            }

            // Init by inserting the first number
            List<List<Integer>> combs = new ArrayList<>();
            combs.add(new ArrayList<>());
            combs.get(0).add(nums[0]);

            for (int i = 1; i < nums.length; i++) {
                List<List<Integer>> currCombs = new ArrayList<>();
                // Insert current number at all possible positions of each combinations
                for (int j = 0; j <= i; j++) {
                    for (List<Integer> comb : combs) {
                        List<Integer> currComb = new ArrayList<>(comb);
                        currComb.add(j, nums[i]);
                        currCombs.add(currComb);
                    }
                }
                combs = currCombs;
            }
            return combs;
        }
    }

    /**
     * Solution 3: Recursion with in-place swap
     * Time Complexity: O(N^2)
     * Space Complexity: O(1)
     */
    class Solution3 {
        List<List<Integer>> permutations = new ArrayList<>();

        public List<List<Integer>> permute(int[] nums) {
            permuteRange(nums, 0);
            return permutations;
        }
        private void permuteRange(int[] nums, int rangeFrom) {
            if (rangeFrom == nums.length) {
                List<Integer> permutation = new ArrayList<>();
                for (int num : nums) {
                    permutation.add(num);
                }
                permutations.add(permutation);
            }
            for (int i = rangeFrom; i < nums.length; i++) {
                // Pick nums[j] as the first element of range(fromIdx, len(nums) - 1))
                if (i != rangeFrom) {
                    swap(nums, i, rangeFrom);
                }
                // Recurse
                permuteRange(nums, rangeFrom + 1);
                // Revert changes
                if (i != rangeFrom) {
                    swap(nums, rangeFrom, i);
                }
            }
        }
        private void swap(int[] nums, int idx0, int idx1) {
            nums[idx0] ^= nums[idx1];
            nums[idx1] ^= nums[idx0];
            nums[idx0] ^= nums[idx1];
        }
    }
    public static void main(String[] args) {
        Printer.printList(new Permutations().new Solution1().permute(new int[]{1,2,3}));
        Printer.printList(new Permutations().new Solution2().permute(new int[]{1,2,3}));
        Printer.printList(new Permutations().new Solution3().permute(new int[]{1,2,3}));
    }
}
