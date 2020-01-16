package medium;

import helper.Printer;

/**
 * [516] Longest Palindromic Subsequence
 *
 * Description:
 * Given a string s, find the longest palindromic subsequence's length in s.
 * You may assume that the maximum length of s is 1000.
 *
 * Example 1:
 * Input:
 * "bbbab"
 * Output:
 * 4
 * One possible longest palindromic subsequence is "bbbb".
 *
 * Example 2:
 * Input:
 * "cbbd"
 * Output:
 * 2
 * One possible longest palindromic subsequence is "bb".
 *
 * Result: Accepted (23ms)
 */
public class LongestPalindromicSubsequence {
    public int longestPalindromeSubseq(String s) {
        int[][] dp = new int[s.length()][s.length()];
        char[] chars = s.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            dp[i][i] = 1;
            for (int j = i - 1; j >= 0; j--) {
                if (chars[i] == chars[j]) {
                    dp[i][j] = i > j + 1 ? dp[i - 1][j + 1] + 2 : 2;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j + 1]);
                }
            }
        }
        return dp[s.length() - 1][0];
    }
    public static void main(String[] args) {
        Printer.printNum(new LongestPalindromicSubsequence().longestPalindromeSubseq("aadcfckdccaza"));
    }
}
