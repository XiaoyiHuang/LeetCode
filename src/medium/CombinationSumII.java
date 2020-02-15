package medium;

import helper.Printer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * [40] Combination Sum II
 *
 * Description:
 * Given a collection of candidate numbers (candidates) and a target number (target),
 * find all unique combinations in candidates where the candidate numbers sums to target.
 *
 * Each number in candidates may only be used once in the combination.
 *
 * Note:
 * All numbers (including target) will be positive integers.
 * The solution set must not contain duplicate combinations.
 *
 * Example 1:
 * Input: candidates = [10,1,2,7,6,1,5], target = 8,
 * A solution set is:
 * [
 *   [1, 7],
 *   [1, 2, 5],
 *   [2, 6],
 *   [1, 1, 6]
 * ]
 *
 * Example 2:
 * Input: candidates = [2,5,2,1,2], target = 5,
 * A solution set is:
 * [
 *   [1,2,2],
 *   [5]
 * ]
 *
 * Result: Accepted (3ms)
 */
public class CombinationSumII {
    List<List<Integer>> combinations = new ArrayList<>();
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        Arrays.sort(candidates);
        for (int i = 0; i < candidates.length; i++) {
            if ((i > 0 && candidates[i] == candidates[i - 1]) || candidates[i] > target) {
                continue;
            }
            List<Integer> comb = new ArrayList<>();
            comb.add(candidates[i]);
            backtracking(comb, candidates, i + 1, target - candidates[i]);
        }
        return combinations;
    }
    private void backtracking(List<Integer> comb, int[] candidates, int index, int target) {
        if (target == 0) {
            combinations.add(new ArrayList<>(comb));
            return;
        }
        for (int i = index; i < candidates.length; i++) {
            if ((i > index && candidates[i] == candidates[i - 1]) || candidates[i] > target) {
                continue;
            }
            comb.add(candidates[i]);
            backtracking(comb, candidates, i + 1, target - candidates[i]);
            comb.remove(comb.size() - 1);
        }
    }
    public static void main(String[] args) {
        Printer.printList(new CombinationSumII().combinationSum2(new int[]{10,1,2,7,6,1,5}, 8));
        Printer.printList(new CombinationSumII().combinationSum2(new int[]{2,5,2,1,2}, 5));
    }
}
