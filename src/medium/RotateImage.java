package medium;

import helper.Printer;

/**
 * [48] Rotate Image
 *
 * Description:
 * You are given an n x n 2D matrix representing an image.
 * Rotate the image by 90 degrees (clockwise).
 *
 * Note:
 * You have to rotate the image in-place, which means you have to
 * modify the input 2D matrix directly. DO NOT allocate another 2D
 * matrix and do the rotation.
 *
 * Example 1:
 * Given input matrix =
 * [
 *   [1,2,3],
 *   [4,5,6],
 *   [7,8,9]
 * ],
 * rotate the input matrix in-place such that it becomes:
 * [
 *   [7,4,1],
 *   [8,5,2],
 *   [9,6,3]
 * ]
 *
 * Example 2:
 * Given input matrix =
 * [
 *   [ 5, 1, 9,11],
 *   [ 2, 4, 8,10],
 *   [13, 3, 6, 7],
 *   [15,14,12,16]
 * ],
 * rotate the input matrix in-place such that it becomes:
 * [
 *   [15,13, 2, 5],
 *   [14, 3, 4, 1],
 *   [12, 6, 8, 9],
 *   [16, 7,10,11]
 * ]
 *
 * Result:
 * Solution 1: Accepted (0ms)
 * Solution 2: Accepted (0ms)
 */
public class RotateImage {
    /**
     * Solution 1: Simulate Rotation in reverse order
     * Time Complexity: O(N^2)
     * Space Complexity: O(1)
     */
    public class Solution1 {
        public void rotate(int[][] matrix) {
            if (matrix.length == 0) {
                return;
            }
            int dimen = matrix.length;
            for (int row = 0; row < (dimen >> 1); row++) {
                for (int col = row; col < dimen - row - 1; col++) {
                    swap(matrix, row, col, dimen - col - 1, row);
                    swap(matrix, dimen - col - 1, row, dimen - row - 1, dimen - col - 1);
                    swap(matrix, dimen - row - 1, dimen - col - 1, col, dimen - row - 1);
                }
            }
        }
    }

    /**
     * Solution 1: Divide rotation into two steps:
     *       (1) First flip diagonally
     *       (2) Then flip from left to right
     * Time Complexity: O(N^2)
     * Space Complexity: O(1)
     */
    public class Solution2 {
        public void rotate(int[][] matrix) {
            if (matrix.length == 0) {
                return;
            }
            int dimen = matrix.length;
            // First flip the matrix across diagonal
            for (int row = 1; row < dimen; row++) {
                for (int col = 0; col < row; col++) {
                    swap(matrix, row, col, col, row);
                }
            }
            // Then flip the matrix in 'left-right' direction
            for (int row = 0; row < dimen; row++) {
                for (int col = 0; col < (dimen >> 1); col++) {
                    swap(matrix, row, col, row, dimen - col - 1);
                }
            }
        }
    }
    private void swap(int[][] matrix, int x0, int y0, int x1, int y1) {
        matrix[x0][y0] ^= matrix[x1][y1];
        matrix[x1][y1] ^= matrix[x0][y0];
        matrix[x0][y0] ^= matrix[x1][y1];
    }
    
    public static void main(String[] args) {
        int[][] matrix = new int[][] {
                {5,1,9,11},
                {2,4,8,10},
                {13,3,6,7},
                {15,14,12,16}
        };
        new RotateImage().new Solution2().rotate(matrix);
        Printer.printArray(matrix);

        matrix = new int[][] {
                {1,2,3},
                {4,5,6},
                {7,8,9},
        };
        new RotateImage().new Solution2().rotate(matrix);
        Printer.printArray(matrix);

        matrix = new int[][] {
                {1}
        };
        new RotateImage().new Solution2().rotate(matrix);
        Printer.printArray(matrix);
    }
}
