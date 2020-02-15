package medium;

import helper.Printer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * [1253] Reconstruct A 2-Row Binary Matrix
 *
 * Description:
 * Given the following details of a matrix with [n] columns and [2] rows:
 *      1. The matrix is a binary matrix, which means each element in the
 *         matrix can be 0 or 1.
 *      2. The sum of elements of the 0-th(upper) row is given as [upper].
 *      3. The sum of elements of the 1-st(lower) row is given as [lower].
 *      4. The sum of elements in the i-th column(0-indexed) is colsum[i],
 *         where [colsum] is given as an integer array with length n.
 * Your task is to reconstruct the matrix with [upper], [lower] and [colsum].
 * Return it as a 2-D integer array.
 *
 * If there are more than one valid solution, any of them will be accepted.
 * If no valid solution exists, return an empty 2-D array.
 *
 * Example 1:
 * Input: upper = 2, lower = 1, colsum = [1,1,1]
 * Output: [[1,1,0],[0,0,1]]
 * Explanation: [[1,0,1],[0,1,0]], and [[0,1,1],[1,0,0]] are also correct answers.
 *
 * Example 2:
 * Input: upper = 2, lower = 3, colsum = [2,2,1,1]
 * Output: []
 *
 * Example 3:
 * Input: upper = 5, lower = 5, colsum = [2,1,2,0,1,0,1,2,0,1]
 * Output: [[1,1,1,0,1,0,0,1,0,0],[1,0,1,0,0,0,1,1,0,1]]
 */
public class ReconstructATwoRowBinaryMatrix {
    /**
     * Solution 1: Backtracking (DFS)
     * Time Complexity: O(2^N)
     * Space Complexity: O(1)
     * Result: TLE
     */
    class Solution1 {
        List<Integer> upperRow = new ArrayList<>();
        List<Integer> lowerRow = new ArrayList<>();
        public List<List<Integer>> reconstructMatrix(int upper, int lower, int[] colsum) {
            List<List<Integer>> matrix = new ArrayList<>();
            boolean result = dfs(0, 0, 0, upper, lower, colsum);
            if (result) {
                matrix.add(upperRow);
                matrix.add(lowerRow);
            }
            return matrix;
        }
        private boolean dfs(int col, int currUpper, int currLower,
                            int expectedUpper, int expectedLower, int[] colsum) {
            if (col == colsum.length) {
                return currUpper == expectedUpper && currLower == expectedLower;
            }
            List<List<Integer>> colCandidates = new ArrayList<>();
            switch (colsum[col]) {
                case 0:
                    colCandidates.add(Arrays.asList(0, 0));
                    break;
                case 1:
                    if (currUpper == expectedUpper && currLower == expectedLower) {
                        return false;
                    }
                    if (currUpper < expectedUpper) {
                        colCandidates.add(Arrays.asList(1, 0));
                    }
                    if (currLower < expectedLower) {
                        colCandidates.add(Arrays.asList(0, 1));
                    }
                    break;
                case 2:
                    if (currUpper == expectedUpper || currLower == expectedLower) {
                        return false;
                    }
                    colCandidates.add(Arrays.asList(1, 1));
                    break;
            }
            for (List<Integer> candidate : colCandidates) {
                int upperNum = candidate.get(0);
                int lowerNum = candidate.get(1);
                upperRow.add(upperNum);
                lowerRow.add(lowerNum);
                boolean result = dfs(col + 1, currUpper + upperNum, currLower + lowerNum,
                        expectedUpper, expectedLower, colsum);
                if (result) {
                    return true;
                } else {
                    upperRow.remove(upperRow.size() - 1);
                    lowerRow.remove(lowerRow.size() - 1);
                }
            }
            return false;
        }
    }

    /**
     * Solution 2: Linear Scan (Two-pass)
     * Time Complexity: O(N)
     * Space Complexity: O(1)
     * Result: Accepted (9ms)
     */
    class Solution2 {
        public List<List<Integer>> reconstructMatrix(int upper, int lower, int[] colsum) {
            int[] upperRow = new int[colsum.length];
            int[] lowerRow = new int[colsum.length];
            for (int i = 0; i < colsum.length; i++) {
                if (colsum[i] == 2) {
                    if (upper == 0|| lower == 0) {
                        return new ArrayList<>();
                    }
                    upperRow[i] = 1;
                    lowerRow[i] = 1;
                    upper -= 1;
                    lower -= 1;
                }
            }
            for (int i = 0; i < colsum.length; i++) {
                if (colsum[i] == 0 || colsum[i] == 2) {
                    continue;
                }
                if (upper > 0) {
                    upperRow[i] = 1;
                    upper -= 1;
                } else if (lower > 0) {
                    lowerRow[i] = 1;
                    lower -= 1;
                } else {
                    return new ArrayList<>();
                }
            }
            return (upper > 0 || lower > 0) ? new ArrayList<>() :
                    new ArrayList(Arrays.asList(upperRow, lowerRow));
        }
    }

    /**
     * Solution 3: Linear Scan (One-pass)
     * Time Complexity: O(N)
     * Space Complexity: O(1)
     * Result: Accepted (7ms)
     */
    class Solution3 {
        public List<List<Integer>> reconstructMatrix(int upper, int lower, int[] colsum) {
            int[][] answer = new int[2][colsum.length];
            for (int i = 0; i < colsum.length; i++) {
                answer[0][i] = (colsum[i] == 2 || (colsum[i] == 1 && upper > lower)) ? 1 : 0;
                answer[1][i] = (colsum[i] == 2 || (colsum[i] == 1 && answer[0][i] == 0)) ? 1 : 0;
                upper -= answer[0][i];
                lower -= answer[1][i];
            }
            return (upper == 0 && lower == 0) ? new ArrayList(Arrays.asList(answer[0], answer[1]))
                    : new ArrayList<>();
        }
    }
    public static void main(String[] args) {
        Printer.printList(new ReconstructATwoRowBinaryMatrix()
                .new Solution3().reconstructMatrix(2, 1, new int[]{1,1,1}));
        Printer.printList(new ReconstructATwoRowBinaryMatrix()
                .new Solution3().reconstructMatrix(2, 3, new int[]{2,2,1,1}));
        Printer.printList(new ReconstructATwoRowBinaryMatrix()
                .new Solution3().reconstructMatrix(5, 5, new int[]{2,1,2,0,1,0,1,2,0,1}));
    }
}
