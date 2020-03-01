package hard;

import helper.Printer;

import java.util.Arrays;

/**
 * [10] Regular Expression Matching
 *
 * Description:
 * Given an input string (s) and a pattern (p), implement regular expression matching with support for '.' and '*'.
 *      '.' Matches any single character.
 *      '*' Matches zero or more of the preceding element.
 * The matching should cover the entire input string (not partial).
 *
 * Note:
 *      s could be empty and contains only lowercase letters a-z.
 *      p could be empty and contains only lowercase letters a-z, and characters like . or *.
 *
 * Example 1:
 * Input:
 *      s = "aa"
 *      p = "a"
 * Output: false
 * Explanation: "a" does not match the entire string "aa".
 *
 * Example 2:
 * Input:
 *      s = "aa"
 *      p = "a*"
 * Output: true
 * Explanation: '*' means zero or more of the preceding element, 'a'.
 *              Therefore, by repeating 'a' once, it becomes "aa".
 *
 * Example 3:
 * Input:
 *      s = "ab"
 *      p = ".*"
 * Output: true
 * Explanation: ".*" means "zero or more (*) of any character (.)".
 *
 * Example 4:
 * Input:
 *      s = "aab"
 *      p = "c*a*b"
 * Output: true
 * Explanation: c can be repeated 0 times, a can be repeated 1 time.
 *              Therefore, it matches "aab".
 *
 * Example 5:
 * Input:
 *      s = "mississippi"
 *      p = "mis*is*p*."
 * Output: false
 */
public class RegularExpressionMatching {
    /**
     * Solution 1: Dynamic Programming (Bottom-up)
     * Time Complexity: O(sLen * pLen)
     * Space Complexity: O(sLen * pLen)
     * Result: Accepted (1ms)
     */
    class Solution1 {
        public boolean isMatch(String s, String p) {
            char[] schar = s.toCharArray();
            char[] pchar = p.toCharArray();
            int sLen = schar.length;
            int pLen = pchar.length;

            boolean[][] match = new boolean[sLen + 1][pLen + 1];
            match[0][0] = true;

            // Deal with leading wildcards
            int curr = 1;
            while (curr < pLen) {
                if (pchar[curr] == '*') {
                    match[0][curr] = true;
                    match[0][curr + 1] = true;
                    curr += 2;
                } else {
                    break;
                }
            }

            for (int i = 1; i <= sLen; i++) {
                for (int j = 1; j <= pLen; j++) {
                    if (pchar[j - 1] == '.') {
                        match[i][j] = match[i - 1][j - 1];
                    } else if (pchar[j - 1] == '*') {
                        if (j < 2) {
                            return false;
                        }
                        boolean matchedChar = (pchar[j - 2] == '.' || schar[i - 1] == pchar[j - 2]);
                        match[i][j] = match[i][j - 2] || (matchedChar && match[i - 1][j]);
                    } else {
                        match[i][j] = match[i - 1][j - 1] && schar[i - 1] == pchar[j - 1];
                    }
                }
            }
            return match[sLen][pLen];
        }
    }

    /**
     * Solution 2: Dynamic Programming (Top-down)
     * Time Complexity: O(sLen * pLen)
     * Space Complexity: O(sLen * pLen)
     * Result: Accepted (1ms)
     */
    class Solution2 {
        public boolean isMatch(String s, String p) {
            int[][] memo = new int[s.length() + 1][p.length() + 1];
            return match(s.toCharArray(), p.toCharArray(), s.length() - 1, p.length() - 1, memo);
        }
        public boolean match(char[] s, char[] p, int sIndex, int pIndex, int[][] memo) {
            if (sIndex < 0 && pIndex < 0) {
                return true;
            }
            if (pIndex < 0) {
                return false;
            }
            if (memo[sIndex + 1][pIndex + 1] != 0) {
                // Memoization at here
                return memo[sIndex + 1][pIndex + 1] == 1;
            }
            boolean result = false;
            if (p[pIndex] == '.') {
                if (sIndex >= 0) {
                    result = match(s, p, sIndex - 1, pIndex - 1, memo);
                }
            } else if (p[pIndex] == '*') {
                char preChar = p[pIndex - 1];
                if (sIndex >= 0 && (preChar == '.' || preChar == s[sIndex])) {
                    result = match(s, p, sIndex - 1, pIndex, memo);
                }
                result = result || match(s, p, sIndex, pIndex - 2, memo);
            } else {
                if (sIndex >= 0 && p[pIndex] == s[sIndex]) {
                    result = match(s, p, sIndex - 1, pIndex - 1, memo);
                }
            }
            memo[sIndex + 1][pIndex + 1] = (result ? 1 : -1);
            return result;
        }
    }
    public static void main(String[] args) {
        Printer.printBool(new RegularExpressionMatching().new Solution1().isMatch("aa", "a"));
        Printer.printBool(new RegularExpressionMatching().new Solution1().isMatch("aa", "a*"));
        Printer.printBool(new RegularExpressionMatching().new Solution1().isMatch("ab", ".*"));
        Printer.printBool(new RegularExpressionMatching().new Solution1().isMatch("aab", "c*a*b"));
        Printer.printBool(new RegularExpressionMatching().new Solution1().isMatch("mississippi", "mis*is*p*."));
        Printer.printBool(new RegularExpressionMatching().new Solution1().isMatch("a", ".*..a*"));
    }
}
