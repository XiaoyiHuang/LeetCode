package medium;

import helper.Printer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * [47] Permutations II
 *
 * Description:
 * Given a collection of numbers that might contain duplicates,
 * return all possible unique permutations.
 *
 * Example:
 * Input: [1,1,2]
 * Output:
 * [
 *   [1,1,2],
 *   [1,2,1],
 *   [2,1,1]
 * ]
 *
 * Result: Accepted (1ms)
 */
public class PermutationsII {
    List<List<Integer>> permutations = new ArrayList<>();
    public List<List<Integer>> permuteUnique(int[] nums) {
        Arrays.sort(nums);
        backtracking(nums, new ArrayList<>(), new boolean[nums.length]);
        return permutations;
    }
    private void backtracking(int[] nums, List<Integer> currComb, boolean[] visited) {
        if (currComb.size() == nums.length) {
            permutations.add(new ArrayList<>(currComb));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (!visited[i] && (i == 0 || visited[i - 1] || nums[i] != nums[i - 1])) {
                currComb.add(nums[i]);
                visited[i] = true;
                backtracking(nums, currComb, visited);
                visited[i] = false;
                currComb.remove(currComb.size() - 1);
            }
        }
    }
    public static void main(String[] args) {
        Printer.printList(new PermutationsII().permuteUnique(new int[]{1,1,2,2}));
        Printer.printList(new PermutationsII().permuteUnique(new int[]{2,2,3,3,3}));
        Printer.printList(new PermutationsII().permuteUnique(new int[]{-1,2,0,-1,1,0,1}));
    }
}
