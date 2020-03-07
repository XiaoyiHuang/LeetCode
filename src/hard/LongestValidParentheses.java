package hard;

import helper.Printer;

import java.util.LinkedList;

/**
 * [32] Longest Valid Parentheses
 *
 * Description:
 * Given a string containing just the characters '(' and ')', find the length
 * of the longest valid (well-formed) parentheses substring.
 *
 * Example 1:
 * Input: "(()"
 * Output: 2
 * Explanation: The longest valid parentheses substring is "()"
 *
 * Example 2:
 * Input: ")()())"
 * Output: 4
 * Explanation: The longest valid parentheses substring is "()()"
 */
public class LongestValidParentheses {
    /**
     * Solution 1: Dynamic Programming
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     * Result: Accepted (0ms)
     */
    class Solution1 {
        public int longestValidParentheses(String s) {
            int N = s.length();
            char[] chars = s.toCharArray();
            int[] dp = new int[N + 1];
            int layers = 0;
            int maxLen = 0;
            for (int i = 1; i <= N; i++) {
                if (chars[i - 1] == '(') {
                    layers += 1;
                }
                if (chars[i - 1] == ')') {
                    if (layers > 0) {
                        if (chars[i - 2] == ')') {
                            dp[i] = dp[i - 1] + 2;              // If previous character is ')', the '()' pair that ends
                                                                // with the previous character is wrapped by current pair
                            dp[i] += dp[i - dp[i]];             // It is important to add up the sequence prior to the
                                                                // matching '(', if exists any
                        } else {
                            if (i > 2 && chars[i - 3] == ')') {
                                dp[i] = dp[i - 2] + 2;          // If previous character is '(', and the one before the
                                                                // previous character is ')', then current '()' is
                                                                // concatenated with the previous '()' pair
                            } else {
                                dp[i] = 2;                      // Otherwise, current '()' is the first valid pair
                            }
                        }
                        maxLen = Math.max(maxLen, dp[i]);
                    }
                    layers = Math.max(0, layers - 1);
                }
            }
            return maxLen;
        }
    }

    /**
     * Solution 2: Stack
     * Explanation: Use stack to record the last index of 'abnormality' in the string,
     *              i.e. the index position where the wrong parenthesis is placed.
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     * Result: Accepted (2ms)
     */
    class Solution2 {
        public int longestValidParentheses(String s) {
            int N = s.length();
            char[] chars = s.toCharArray();
            int maxLen = 0;
            LinkedList<Integer> stack = new LinkedList<>();
            stack.push(-1);                         // Used as a pseudo head, in case any ')' matches with it,
                                                    // it means the ')' does not have a matching ')' in the string
            for (int i = 0; i < N; i++) {
                if (chars[i] == '(') {
                    stack.push(i);
                } else {
                    stack.pop();
                    if (stack.isEmpty()) {          // Current ')' does not have a matching ')'
                        stack.push(i);              // Current ')' act as the 'pseudo head' for next valid pairs
                    } else {
                        maxLen = Math.max(maxLen, i - stack.peek());
                    }
                }
            }
            return maxLen;
        }
    }

    /**
     * Solution 3: Two-pass iteration
     * Explanation: A left-to-right scan can find almost all valid sequences by comparing the
     *              number of '(' and ')', except for sequences in the form of '(()', i.e.,
     *              the number of '(' is over ')'. Regarding this, a right-to-left scan can deal
     *              with this and include all missing valid sequences from the first pass.
     * Time Complexity: O(N)
     * Space Complexity: O(1)
     * Result: Accepted (1ms)
     */
    class Solution3 {
        public int longestValidParentheses(String s) {
            int N = s.length();
            char[] chars = s.toCharArray();
            int layers = 0;
            int lNum = 0, rNum = 0, maxLen = 0;

            // Left-to-right scan (first pass)
            for (int i = 0; i < N; i++) {
                if (chars[i] == '(') {
                    lNum += 1;
                } else {
                    rNum += 1;
                }
                if (lNum == rNum) {
                    maxLen = Math.max(maxLen, lNum + rNum);
                } else if (lNum < rNum) {
                    // Reset when the wrong parenthesis is placed
                    lNum = 0;
                    rNum = 0;
                }
            }
            // Reset states
            if (maxLen == N) {
                return N;
            } else {
                lNum = 0;
                rNum = 0;
            }
            // Right-to-left scan (second pass)
            for (int i = N - 1; i >= 0; i--) {
                if (chars[i] == ')') {
                    rNum += 1;
                } else {
                    lNum += 1;
                }
                if (lNum == rNum) {
                    maxLen = Math.max(maxLen, lNum + rNum);
                } else if (rNum < lNum) {
                    // Reset when the wrong parenthesis is placed
                    lNum = 0;
                    rNum = 0;
                }
            }
            return maxLen;
        }
    }
    public static void main(String[] args) {
        Printer.printNum(new LongestValidParentheses().new Solution3().longestValidParentheses("(()"));
        Printer.printNum(new LongestValidParentheses().new Solution3().longestValidParentheses(")()())"));
        Printer.printNum(new LongestValidParentheses().new Solution3().longestValidParentheses("))()()(("));
        Printer.printNum(new LongestValidParentheses().new Solution3().longestValidParentheses("())"));
        Printer.printNum(new LongestValidParentheses().new Solution3().longestValidParentheses("()()()"));
        Printer.printNum(new LongestValidParentheses().new Solution3().longestValidParentheses("()(()))()"));
        Printer.printNum(new LongestValidParentheses().new Solution3().longestValidParentheses("()(()"));
        Printer.printNum(new LongestValidParentheses().new Solution3().longestValidParentheses("(()()"));
    }
}
