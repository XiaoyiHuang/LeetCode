package hard;

import helper.Printer;

import java.util.Arrays;

/**
 * [44] Wildcard Matching
 *
 * Description:
 * Given an input string (s) and a pattern (p), implement wildcard pattern matching with support for '?' and '*'.
 *      '?' Matches any single character.
 *      '*' Matches any sequence of characters (including the empty sequence).
 * The matching should cover the entire input string (not partial).
 *
 * Note:
 * s could be empty and contains only lowercase letters a-z.
 * p could be empty and contains only lowercase letters a-z, and characters like ? or *.
 *
 * Example 1:
 * Input:
 * s = "aa"
 * p = "a"
 * Output: false
 * Explanation: "a" does not match the entire string "aa".
 *
 * Example 2:
 * Input:
 * s = "aa"
 * p = "*"
 * Output: true
 * Explanation: '*' matches any sequence.
 *
 * Example 3:
 * Input:
 * s = "cb"
 * p = "?a"
 * Output: false
 * Explanation: '?' matches 'c', but the second letter is 'a', which does not match 'b'.
 *
 * Example 4:
 * Input:
 * s = "adceb"
 * p = "*a*b"
 * Output: true
 * Explanation: The first '*' matches the empty sequence, while the second '*' matches the substring "dce".
 *
 * Example 5:
 * Input:
 * s = "acdcb"
 * p = "a*c?b"
 * Output: false
 */
public class WildcardMatching {
    /**
     * Solution 1: Dynamic Programming (Bottom-up)
     * Time Complexity: O(sLen * pLen)
     * Space Complexity: O(sLen * pLen)
     * Result: Accepted (17ms)
     */
    class Solution1 {
        public boolean isMatch(String s, String p) {
            int sLen = s.length();
            int pLen = p.length();
            if (pLen == 0) {
                return sLen == 0;
            }
            char[] schar = s.toCharArray();
            char[] pchar = p.toCharArray();
            boolean[][] dp = new boolean[sLen + 2][pLen + 1];   // The last row is used when iterating over the matrix
                                                                // to store if any dp[k][j] is true (k = [0, i))
            dp[0][0] = true;
            dp[sLen + 1][0] = true;
            for (int j = 1; j <= pLen; j++) {
                if (pchar[j - 1] != '*') {
                    break;
                }
                dp[0][j] = true;
                dp[sLen + 1][j] = true;
            }
            for (int i = 1; i <= sLen; i++) {
                for (int j = 1; j <= pLen; j++) {
                    if (pchar[j - 1] == '*') {
                        dp[i][j] = dp[sLen + 1][j - 1];
                    } else {
                        dp[i][j] = dp[i - 1][j - 1] && (pchar[j - 1] == '?' || schar[i - 1] == pchar[j - 1]);
                    }
                    if (dp[i][j]) {
                        dp[sLen + 1][j] = true;                 // Update the last row if dp[i][j] is true
                    }
                }
            }
            return dp[sLen][pLen];
        }
    }

    /**
     * Solution 2: Dynamic Programming (Bottom-up)
     * Explanation: Turns out scanning backwards helps to eliminate the additional row in Solution 1
     * Time Complexity: O(sLen * pLen)
     * Space Complexity: O(sLen * pLen)
     * Result: Accepted (14ms)
     */
    class Solution2 {
        public boolean isMatch(String s, String p) {
            int sLen = s.length();
            int pLen = p.length();
            if (pLen == 0) {
                return sLen == 0;
            }
            char[] schar = s.toCharArray();
            char[] pchar = p.toCharArray();
            boolean[][] dp = new boolean[sLen + 1][pLen + 1];
            dp[sLen][pLen] = true;
            for (int j = pLen - 1; j >= 0; j--) {
                if (pchar[j] != '*') {
                    break;
                }
                dp[sLen][j] = true;
            }
            for (int i = sLen - 1; i >= 0; i--) {
                for (int j = pLen - 1; j >= 0; j--) {
                    if (pchar[j] == '*') {
                        dp[i][j] = dp[i + 1][j] || dp[i][j + 1];
                    } else {
                        dp[i][j] = dp[i + 1][j + 1] && (pchar[j] == '?' || schar[i] == pchar[j]);
                    }
                }
            }
            return dp[0][0];
        }
    }

    /**
     * Solution 3: Dynamic Programming (Top-down)
     * Time Complexity: O(sLen * pLen)
     * Space Complexity: O(sLen * pLen)
     * Result: Accepted (14ms)
     */
    class Solution3 {
        public boolean isMatch(String s, String p) {
            int[][] state = new int[s.length() + 1][p.length() + 1];
            return match(s.toCharArray(), p.toCharArray(), s.length() - 1, p.length() - 1, state);
        }
        public boolean match(char[] s, char[] p, int sIdx, int pIdx, int[][] state) {
            if (pIdx < 0) {
                return sIdx < 0;
            }
            if (state[sIdx + 1][pIdx + 1] != 0) {
                return state[sIdx + 1][pIdx + 1] == 1;
            }
            if (sIdx < 0) {
                return p[pIdx] == '*' && match(s, p, sIdx, pIdx - 1, state);
            }
            boolean ans = false;
            if (p[pIdx] == '*') {
                ans = match(s, p, sIdx - 1, pIdx, state) || match(s, p, sIdx, pIdx - 1, state);
            } else if (p[pIdx] == s[sIdx] || p[pIdx] == '?') {
                ans = match(s, p, sIdx - 1, pIdx - 1, state);
            }
            state[sIdx + 1][pIdx + 1] = ans ? 1 : -1;
            return ans;
        }
    }

    /**
     * Solution 4: Finite State Machine
     * Time Complexity: O(sLen * pLen)
     * Space Complexity: O(pLen * 28) (28 meaning 26 characters + '*' + '?')
     * Result: Accepted (12ms)
     */
    class Solution4 {
        public boolean isMatch(String s, String p) {
            int sLen = s.length();
            int pLen = p.length();
            if (pLen == 0) {
                return sLen == 0;
            }
            char[] schar = s.toCharArray();
            char[] pchar = p.toCharArray();
            int[][] transfer = new int[pLen][28];

            // Build FSM
            for (int i = 0; i < pLen; i++) {
                Arrays.fill(transfer[i], -1);
            }
            int state = 0, endState;
            for (int i = 0; i < pLen; i++) {
                state = build(transfer, state, pchar[i]);
            }
            endState = state;

            // Validate with FSM
            boolean[] states = new boolean[endState + 1];
            states[0] = true;
            for (int c : schar) {
                int[] tokens = new int[]{c - 'a', 26, 27};
                boolean[] nextStates = new boolean[endState + 1];
                for (int i = 0; i < states.length; i++) {
                    if (!states[i]) {
                        continue;
                    }
                    for (int token : tokens) {
                        if (i < transfer.length && transfer[i][token] >= 0) {
                            nextStates[transfer[i][token]] = true;
                        }
                    }
                }
                states = nextStates;
            }
            return states[endState];
        }
        private int build(int[][] transfer, int state, char c) {
            int nextState;
            if (c == '*') {
                transfer[state][27] = state;
                nextState = state;
            } else if (c == '?') {
                transfer[state][26] = state + 1;
                nextState = state + 1;
            } else {
                transfer[state][c - 'a'] = state + 1;
                nextState = state + 1;
            }
            return nextState;
        }
    }
    public static void main(String[] args) {
        Printer.printBool(new WildcardMatching().new Solution1().isMatch("aa", "*"));
        Printer.printBool(new WildcardMatching().new Solution1().isMatch("cb", "?a"));
        Printer.printBool(new WildcardMatching().new Solution1().isMatch("adceb", "*a*b"));
        Printer.printBool(new WildcardMatching().new Solution1().isMatch("acdcb", "a*c?b"));
        Printer.printBool(new WildcardMatching().new Solution1().isMatch("mississippi", "m??*ss*?i*pi"));
    }
}
