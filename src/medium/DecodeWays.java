package medium;

import helper.Printer;

/**
 * [91] Decode Ways
 *
 * Description:
 * A message containing letters from A-Z is being encoded to numbers using the following mapping:
 *      'A' -> 1
 *      'B' -> 2
 *      ...
 *      'Z' -> 26
 * Given a non-empty string containing only digits, determine the total number of ways to decode it.
 *
 * Example 1:
 * Input: "12"
 * Output: 2
 * Explanation: It could be decoded as "AB" (1 2) or "L" (12).
 *
 * Example 2:
 * Input: "226"
 * Output: 3
 * Explanation: It could be decoded as "BZ" (2 26), "VF" (22 6), or "BBF" (2 2 6).
 *
 * Solution: Dynamic Programming
 * Result: Accepted (1ms)
 */
public class DecodeWays {
    public int numDecodings(String s) {
        int N = s.length();
        if (N == 0) {
            return 0;
        }
        int[] dp = new int[N + 1];
        char[] c = s.toCharArray();
        dp[0] = 1;
        for (int i = 1; i <= N; i++) {
            int currDigit, prevDigit;
            if ((currDigit = c[i - 1] - '0') > 0) {
                dp[i] = dp[i - 1];
            }
            if (i > 1 && (prevDigit = c[i - 2] - '0') > 0) {
                dp[i] += ((prevDigit * 10 + currDigit) <= 26) ? dp[i - 2] : 0;
            }
        }
        return dp[N];
    }
    public static void main(String[] args) {
        Printer.printNum(new DecodeWays().numDecodings("12"));
        Printer.printNum(new DecodeWays().numDecodings("226"));
    }
}
