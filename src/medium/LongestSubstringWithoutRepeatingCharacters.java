package medium;

import helper.Printer;

import java.util.Arrays;

/**
 * [3] Longest Substring Without Repeating Characters
 *
 * Description:
 * Given a string, find the length of the longest substring without repeating characters.
 *
 * Example 1:
 * Input: "abcabcbb"
 * Output: 3
 * Explanation: The answer is "abc", with the length of 3.
 *
 * Example 2:
 * Input: "bbbbb"
 * Output: 1
 * Explanation: The answer is "b", with the length of 1.
 *
 * Example 3:
 * Input: "pwwkew"
 * Output: 3
 * Explanation: The answer is "wke", with the length of 3.
 *              Note that the answer must be a substring, "pwke" is a subsequence and not a substring.
 *
 * Result: Accepted (2ms)
 */
public class LongestSubstringWithoutRepeatingCharacters {
    public int lengthOfLongestSubstring(String s) {
        int[] windows = new int[128];
        char[] chars = s.toCharArray();
        int maxLen = 0;
        int windowFrom = 0;

        Arrays.fill(windows, -1);

        for (int i = 0; i < chars.length; i++) {
            int prevPos;
            if ((prevPos = windows[chars[i]]) >= 0 && prevPos >= windowFrom) {
                maxLen = Math.max(maxLen, i - windowFrom);
                windowFrom = prevPos + 1;
            }
            windows[chars[i]] = i;
        }
        maxLen = Math.max(maxLen, chars.length - windowFrom);
        return maxLen;
    }

    public static void main(String[] args) {
        LongestSubstringWithoutRepeatingCharacters solution = new LongestSubstringWithoutRepeatingCharacters();
        int result = solution.lengthOfLongestSubstring("bacabcbb");
        Printer.printNum(result);
    }
}
