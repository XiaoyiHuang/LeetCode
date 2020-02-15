package medium;

import helper.Printer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * [216] Combination Sum III
 *
 * Description:
 * Find all possible combinations of k numbers that add up to a number n,
 * given that only numbers from 1 to 9 can be used and each combination
 * should be a unique set of numbers.
 *
 * Note:
 * All numbers will be positive integers.
 * The solution set must not contain duplicate combinations.
 *
 * Example 1:
 * Input: k = 3, n = 7
 * Output: [[1,2,4]]
 *
 * Example 2:
 * Input: k = 3, n = 9
 * Output: [[1,2,6], [1,3,5], [2,3,4]]
 *
 * Result: Accepted (0ms)
 */
public class CombinationSumIII {
    List<List<Integer>> combinations = new ArrayList<>();
    public List<List<Integer>> combinationSum3(int k, int n) {
        final int CANDIDATE_SUM = 45;
        if (n > CANDIDATE_SUM) {
            return new ArrayList<>();
        }
        int[] candidates = new int[]{1,2,3,4,5,6,7,8,9};
        for (int i = 0; i < candidates.length; i++) {
            if (candidates[i] > n) {
                break;
            }
            Integer[] comb = new Integer[k];
            comb[0] = candidates[i];
            backtracking(comb, candidates, 1, i + 1, n - candidates[i]);
        }
        return combinations;
    }
    private void backtracking(Integer[] comb, int[] candidates, int combIndex, int index, int target) {
        if (combIndex == comb.length) {
            if (target == 0) {
                combinations.add(Arrays.asList(Arrays.copyOf(comb, comb.length)));
            }
            return;
        }
        for (int i = index; i < candidates.length; i++) {
            if (candidates[i] > target) {
                return;
            }
            comb[combIndex] = candidates[i];
            backtracking(comb, candidates, combIndex + 1, i + 1, target - candidates[i]);
            comb[combIndex] = 0;
        }
    }
    public static void main(String[] args) {
        Printer.printList(new CombinationSumIII().combinationSum3(3, 7));
        Printer.printList(new CombinationSumIII().combinationSum3(3, 9));
    }
}
