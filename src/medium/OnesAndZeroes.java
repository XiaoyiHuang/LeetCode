package medium;

import helper.Printer;

/**
 * [474] Ones And Zeroes
 *
 * Description:
 * In the computer world, use restricted resource you have to generate
 * maximum benefit is what we always want to pursue.
 *
 * For now, suppose you are a dominator of m 0s and n 1s respectively.
 * On the other hand, there is an array with strings consisting of only 0s and 1s.
 *
 * Now your task is to find the maximum number of strings that you
 * can form with given m 0s and n 1s. Each 0 and 1 can be used at most once.
 *
 * Note:
 * The given numbers of 0s and 1s will both not exceed 100
 * The size of given string array won't exceed 600.
 *
 * Example 1:
 * Input: Array = {"10", "0001", "111001", "1", "0"}, m = 5, n = 3
 * Output: 4
 * Explanation: This are totally 4 strings can be formed by the using of 5 0s and 3 1s,
 * which are “10,”0001”,”1”,”0”
 *
 * Example 2:
 * Input: Array = {"10", "0", "1"}, m = 1, n = 1
 * Output: 2
 * Explanation: You could form "10", but then you'd have nothing left. Better form "0" and "1".
 *
 * Result: Accepted (28ms)
 */
public class OnesAndZeroes {
    public int findMaxForm(String[] strs, int m, int n) {
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 0; i < strs.length; i++) {
            int mNum = 0, nNum = 0;
            for (char c : strs[i].toCharArray()) {
                if (c == '0') {
                    mNum += 1;
                } else {
                    nNum += 1;
                }
            }
            for (int j = m; j >= mNum; j--) {
                for (int k = n; k >= nNum; k--) {
                    dp[j][k] = Math.max(dp[j - mNum][k - nNum] + 1, dp[j][k]);
                }
            }
        }
        return dp[m][n];
    }
    public static void main(String[] args) {
        Printer.printNum(new OnesAndZeroes().findMaxForm(new String[]{"10","0001","111001","1","0"}, 5, 3));
        Printer.printNum(new OnesAndZeroes().findMaxForm(new String[]{"10","0","1"}, 1, 1));
    }
}
