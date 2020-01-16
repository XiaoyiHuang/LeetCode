package medium;

import helper.Printer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * [39] Combination Sum
 *
 * Description:
 * Given a set of candidate numbers (candidates) (without duplicates)
 * and a target number (target), find all unique combinations in
 * candidates where the candidate numbers sums to target.
 *
 * The same repeated number may be chosen from candidates unlimited
 * number of times.
 *
 * Note:
 * All numbers (including target) will be positive integers.
 * The solution set must not contain duplicate combinations.
 *
 * Example 1:
 * Input: candidates = [2,3,6,7], target = 7,
 * A solution set is:
 * [
 *   [7],
 *   [2,2,3]
 * ]
 *
 * Example 2:
 * Input: candidates = [2,3,5], target = 8,
 * A solution set is:
 * [
 *   [2,2,2,2],
 *   [2,3,3],
 *   [3,5]
 * ]
 *
 * Result: Accepted (2ms)
 */
public class CombinationSum {
    List<List<Integer>> combinations = new ArrayList<>();
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        Arrays.sort(candidates);
        backtracking(candidates, target, 0, new ArrayList<>());
        return combinations;
    }
    private void backtracking(int[] nums, int requiredSum, int startIdx, List<Integer> currComb) {
        if (requiredSum == 0) {
            combinations.add(new ArrayList<>(currComb));
            return;
        }
        for (int i = startIdx; i < nums.length; i++) {
            if (requiredSum < nums[i]) {
                // Break out of the loop if all numbers
                // afterwards cannot possibly sum up to target
                break;
            }
            currComb.add(nums[i]);
            backtracking(nums, requiredSum - nums[i], i, currComb);
            currComb.remove(currComb.size() - 1);
        }
    }
    public static void main(String[] args) {
        Printer.printList(new CombinationSum().combinationSum(new int[]{2,3,5}, 8));
    }
}
