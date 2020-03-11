package hard;

import helper.Printer;

import java.util.Arrays;

/**
 * [639] Decode Ways II
 *
 * Description:
 * A message containing letters from A-Z is being encoded to numbers using the following mapping way:
 *          'A' -> 1
 *          'B' -> 2
 *             ...
 *          'Z' -> 26
 * Beyond that, now the encoded string can also contain the character '*', which can be
 * treated as one of the numbers from 1 to 9.
 *
 * Given the encoded message containing digits and the character '*', return the total number of ways to decode it.
 *
 * Also, since the answer may be very large, you should return the output mod (10^9 + 7).
 *
 * Example 1:
 * Input: "*"
 * Output: 9
 * Explanation: The encoded message can be decoded to the string: "A", "B", "C", "D", "E", "F", "G", "H", "I".
 *
 * Example 2:
 * Input: "1*"
 * Output: 9 + 9 = 18
 *
 * Note:
 *      [1] The length of the input string will fit in range [1, 10^5].
 *      [2] The input string will only contain the character '*' and digits '0' - '9'.
 */
public class DecodeWaysII {
    /**
     * Solution 1: Dynamic Programming (Bottom-up)
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     * Result: Accepted (34ms)
     */
    class Solution1 {
        int MOD = 1000000007;
        public int numDecodings(String s) {
            int N = s.length();
            if (N == 0) {
                return 0;
            }
            char[] chars = s.toCharArray();
            long[] dp = new long[N + 1];
            dp[0] = 1;
            dp[1] = comb(chars, 0, 0);
            for (int i = 2; i <= N; i++) {
                dp[i] = mod(dp[i - 2] * comb(chars, i - 2, i - 1)) +
                        mod(dp[i - 1] * comb(chars, i - 1, i - 1));
            }
            return mod(dp[N]);
        }
        private int mod(long val) {
            return (int)(val % MOD);
        }
        private int comb(char[] chars, int from, int to) {
            if (from == to) {
                if (chars[from] == '*') {
                    return 9;
                }
                if (chars[from] == '0') {
                    return 0;
                }
                return 1;
            }

            int first = chars[from] - '0';
            int second = chars[to] - '0';
            if (chars[from] == '*' && chars[to] == '*') {
                return 15;                      // 10/11/.../26 combs
            }
            if (chars[from] == '*') {
                if (second > 6) {
                    return 1;                   // *7/*8/*9 combs
                } else {
                    return 2;                   // *0/*1/.../*6 combs
                }
            }
            if (chars[to] == '*') {
                if (first > 2 || chars[from] == '0') {
                    return 0;                   // 0*/3*/4*/.../9* combs
                }
                if (first == 2) {
                    return 6;                   // 2* combs
                }
                return 9;
            }
            if (chars[from] == '0' || first * 10 + second > 26) {
                return 0;
            }
            return 1;
        }
    }

    /**
     * Solution 2: Dynamic Programming (Top-down)
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     * Result: Accepted (53ms)
     */
    class Solution2 {
        int MOD = 1000000007;
        public int numDecodings(String s) {
            long[] memo = new long[s.length()];
            Arrays.fill(memo, -1);
            char[] chars = s.toCharArray();
            memo[0] = comb(chars, 0, 0);
            return mod(decode(chars, s.length() - 1, memo));
        }
        private long decode(char[] s, int index, long[] memo) {
            if (index < 0) {
                return 1;
            }
            if (memo[index] >= 0) {
                return memo[index];
            }
            memo[index] = mod(decode(s, index - 2, memo) * comb(s, index - 1, index)) +
                    mod(decode(s, index - 1, memo) * comb(s, index, index));
            return memo[index];
        }
        private int mod(long val) {
            return (int)(val % MOD);
        }
        private int comb(char[] chars, int from, int to) {
            if (from < 0 || to < from) {
                return 0;
            }
            if (from == to) {
                if (chars[from] == '*') {
                    return 9;
                }
                if (chars[from] == '0') {
                    return 0;
                }
                return 1;
            }

            int first = chars[from] - '0';
            int second = chars[to] - '0';
            if (chars[from] == '*' && chars[to] == '*') {
                return 15;                      // 10/11/.../26 combs
            }
            if (chars[from] == '*') {
                if (second > 6) {
                    return 1;                   // *7/*8/*9 combs
                } else {
                    return 2;                   // *0/*1/.../*6 combs
                }
            }
            if (chars[to] == '*') {
                if (first > 2 || chars[from] == '0') {
                    return 0;                   // 0*/3*/4*/.../9* combs
                }
                if (first == 2) {
                    return 6;                   // 2* combs
                }
                return 9;
            }
            if (chars[from] == '0' || first * 10 + second > 26) {
                return 0;
            }
            return 1;
        }
    }
    public static void main(String[] args) {
        Printer.printNum(new DecodeWaysII().new Solution1().numDecodings("*"));
        Printer.printNum(new DecodeWaysII().new Solution2().numDecodings("*"));
        Printer.printNum(new DecodeWaysII().new Solution1().numDecodings("1*"));
        Printer.printNum(new DecodeWaysII().new Solution2().numDecodings("1*"));
    }
}
