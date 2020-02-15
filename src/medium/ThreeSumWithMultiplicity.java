package medium;

import helper.Printer;

import java.util.Arrays;

/**
 * [923] 3Sum With Multiplicity
 *
 * Description:
 * Given an integer array A, and an integer target, return the number of
 * tuples i, j, k such that i < j < k and A[i] + A[j] + A[k] == target.
 * As the answer can be very large, return it modulo 10^9 + 7.
 *
 * Example 1:
 * Input: A = [1,1,2,2,3,3,4,4,5,5], target = 8
 * Output: 20
 * Explanation:
 * Enumerating by the values (A[i], A[j], A[k]):
 * (1, 2, 5) occurs 8 times;
 * (1, 3, 4) occurs 8 times;
 * (2, 2, 4) occurs 2 times;
 * (2, 3, 3) occurs 2 times.
 *
 * Example 2:
 * Input: A = [1,1,2,2,2,2], target = 5
 * Output: 12
 * Explanation:
 * A[i] = 1, A[j] = A[k] = 2 occurs 12 times:
 * We choose one 1 from [1,1] in 2 ways,
 * and two 2s from [2,2,2,2] in 6 ways.
 */
public class ThreeSumWithMultiplicity {
    /**
     * Solution 1: Three Pointers
     * Time Complexity: O(N^2)
     * Space Complexity: O(1)
     * Result: Accepted (9ms)
     */
    class Solution1 {
        public int threeSumMulti(int[] A, int target) {
            Arrays.sort(A);
            long multiNum = 0;
            for (int i = 0; i < A.length; i++) {
                int baseFrom = i, baseTo = i;
                while (baseTo < A.length && A[baseTo] == A[baseFrom]) {
                    baseTo += 1;
                }
                multiNum += twoSumMulti(A, baseFrom, baseTo - 1, target - A[baseFrom]);
                i = baseTo - 1;
            }
            return (int)(multiNum % (long)(Math.pow(10, 9) + 7));
        }
        private long twoSumMulti(int[] A, int baseFrom, int baseTo, int target) {
            long multiNum = 0;
            int l = baseFrom + 1, r = A.length - 1;
            while (l < r) {
                int sum = A[l] + A[r];
                if (sum == target) {
                    // Scenario 1: FirstNum == SecondNum == ThirdNum
                    if (A[l] == A[r] && A[baseFrom] == A[l]) {
                        multiNum += getCombination(baseTo - baseFrom + 1, 3);
                        return multiNum;
                    }
                    // Scenario 2: FirstNum != SecondNum == ThirdNum
                    int baseNumCount = Math.min(baseTo, l - 1) - baseFrom + 1;
                    if (A[l] == A[r]) {
                        multiNum += baseNumCount * getCombination(r - l + 1, 2);
                        return multiNum;
                    }
                    int lMulti = 1, rMulti = 1;
                    if (A[baseFrom] == A[l]) {
                        // Scenario 3: FirstNum == SecondNum != ThirdNum
                        lMulti = (int)getCombination(baseTo - baseFrom + 1, 2);
                        l = baseTo;
                        while (r - 1 > l && A[r - 1] == A[r]) {
                            r -= 1;
                            rMulti += 1;
                        }
                        multiNum += lMulti * rMulti;
                    } else {
                        // Scenario 4: FirstNum != SecondNum != ThirdNum
                        while (l + 1 < r && A[l + 1] == A[l]) {
                            l += 1;
                            lMulti += 1;
                        }
                        while (r - 1 > l && A[r - 1] == A[r]) {
                            r -= 1;
                            rMulti += 1;
                        }
                        multiNum += baseNumCount * lMulti * rMulti;
                    }
                    l += 1;
                } else if (sum > target) {
                    r -= 1;
                } else {
                    l += 1;
                }
            }
            return multiNum;
        }
        private long getCombination(int range, int combSize) {
            long dividend = 1, divisor = 1;
            for (int i = 0; i < combSize; i++) {
                dividend *= (range - i);
            }
            for (int i = combSize; i > 0; i--) {
                divisor *= i;
            }
            return dividend / divisor;
        }
    }
    /**
     * Solution 2: Count Numbers
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     * Result: Accepted (8ms)
     */
    class Solution2 {
        public int threeSumMulti(int[] A, int target) {
            long[] count = new long[101];
            for (int a : A) {
                count[a] += 1;
            }
            long multiNum = 0;
            for (int i = 0; i < count.length; i++) {
                for (int j = i; j < count.length; j++) {
                    int k = target - i - j;
                    if (k < 0 || k > 100 || count[k] == 0) {
                        continue;
                    }
                    if (i == j && j == k) {
                        // Scenario 1: i == j == k
                        multiNum += (count[i] * (count[i] - 1) * (count[i] - 2)) / 6L;
                    } else if (i == j) {
                        // Scenario 2: i == j != k
                        // Note that we ignore the i != j == k scenario intentionally
                        // to avoid double-counting. For example, we only need to
                        // include either case (1,3,1) or case (1,1,3), rather than
                        // including both
                        multiNum += ((count[i] * (count[i] - 1)) / 2) * count[k];
                    } else if (j < k) {
                        // Scenario 3: i < j && j < k
                        multiNum += count[i] * count[j] * count[k];
                    }
                }
            }
            return (int)(multiNum % (1e9 + 7));
        }
    }
    public static void main(String[] args) {
        int[] A = new int[]{1,1,2,2,3,3,4,4,5,5};
        Printer.printNum(new ThreeSumWithMultiplicity().new Solution2().threeSumMulti(A, 8));
        A = new int[]{1,1,2,2,2,2};
        Printer.printNum(new ThreeSumWithMultiplicity().new Solution2().threeSumMulti(A, 5));
        A = new int[]{0,0,0,2};
        Printer.printNum(new ThreeSumWithMultiplicity().new Solution2().threeSumMulti(A, 2));
        A = new int[3000];
        Printer.printNum(new ThreeSumWithMultiplicity().new Solution2().threeSumMulti(A, 0));
    }
}
