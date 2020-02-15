package medium;

import helper.Printer;

/**
 * [5] Longest Palindrome Substring
 *
 * Description:
 * Given a string s, find the longest palindromic substring in s.
 * You may assume that the maximum length of s is 1000.
 *
 * Example 1:
 * Input: "babad"
 * Output: "bab"
 * Note: "aba" is also a valid answer.
 *
 * Example 2:
 * Input: "cbbd"
 * Output: "bb"
 *
 * Result:
 * Solution 1: Accepted (4ms)
 * Solution 2: Accepted (26ms)
 */
public class LongestPalindromeSubstring {
    /**
     * Solution 1: Expand Around Center
     * Time Complexity: O(N^2)
     * Space Complexity: O(1)
     */
    public class Solution1 {
        public String longestPalindrome(String s) {
            if (s.length() == 0) {
                return s;
            }

            char[] chars = s.toCharArray();
            int maxFrom = 0, maxLen = 1;

            for (int i = 0; i < chars.length - (maxLen >> 1); i++) {
                int[] singleCenter = extendPalindrome(chars, i, i);
                if (singleCenter[1] > maxLen) {
                    maxLen = singleCenter[1];
                    maxFrom = singleCenter[0];
                }

                if (i + 1 < chars.length && chars[i] == chars[i + 1]) {
                    int[] doublyCenter = extendPalindrome(chars, i, i + 1);
                    if (doublyCenter[1] > maxLen) {
                        maxLen = doublyCenter[1];
                        maxFrom = doublyCenter[0];
                    }
                }
            }

            return s.substring(maxFrom, maxFrom + maxLen);
        }

        private int[] extendPalindrome(char[] chars, int lCenter, int rCenter) {
            while (lCenter > 0 && rCenter < chars.length - 1) {
                if (chars[lCenter] == chars[rCenter]) {
                    lCenter -= 1;
                    rCenter += 1;
                } else {
                    break;
                }
            }
            if (chars[lCenter] != chars[rCenter]) {
                return new int[]{lCenter + 1, Math.max(0, rCenter - lCenter - 1)};
            } else {
                return new int[]{lCenter, rCenter - lCenter + 1};
            }
        }
    }

    /**
     * Solution 2: Dynamic Programming
     * Time Complexity: O(N^2)
     * Space Complexity: O(N^2)
     */
    public class Solution2 {
        public String longestPalindrome(String s) {
            if (s.length() == 0) {
                return s;
            }

            boolean[][] dp = new boolean[s.length()][s.length()];
            char[] chars = s.toCharArray();
            int maxFrom = 0, maxLen = 1;

            for (int i = 0; i < chars.length; i++) {
                dp[i][i] = true;
                for (int j = 0; j < i; j++) {
                    dp[i][j] = (chars[i] == chars[j]) && (j + 1 >= i - 1 || dp[i - 1][j + 1]);
                    if (dp[i][j] && i - j + 1 > maxLen) {
                        maxLen = i - j + 1;
                        maxFrom = j;
                    }
                }
            }
            return s.substring(maxFrom, maxFrom + maxLen);
        }
    }

    public static void main(String[] args) {
        LongestPalindromeSubstring solution = new LongestPalindromeSubstring();

        // Solution 1
        Solution1 solution1 = solution.new Solution1();
        String result1 = solution1.longestPalindrome("cbbd");
        Printer.printString(result1);

        // Solution 2
        Solution2 solution2 = solution.new Solution2();
        String result2 = solution2.longestPalindrome("cbbd");
        Printer.printString(result2);
    }
}
