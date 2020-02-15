package medium;

import helper.Printer;

import java.util.LinkedList;

/**
 * [1111] Maximum Nesting Depth of Two Valid Parentheses Strings
 *
 * Description:
 * A string is a valid parentheses string (denoted VPS) if and only if it
 * consists of "(" and ")" characters only, and:
 *      1. It is the empty string, or
 *      2. It can be written as AB (A concatenated with B), where A and B are VPS's, or
 *      3. It can be written as (A), where A is a VPS.
 *
 * We can similarly define the nesting depth depth(S) of any VPS [S] as follows:
 *      1. depth("") = 0
 *      2. depth(A + B) = max(depth(A), depth(B)), where A and B are VPS's
 *      3. depth("(" + A + ")") = 1 + depth(A), where A is a VPS.
 *
 * For example,  "", "()()", and "()(()())" are VPS's (with nesting depths 0, 1, and 2),
 * and ")(" and "(()" are not VPS's.
 *
 * Given a VPS seq, split it into two disjoint subsequences A and B, such that A and B
 * are VPS's (and A.length + B.length = seq.length).
 *
 * Now choose any such A and B such that max(depth(A), depth(B)) is the minimum possible value.
 *
 * Return an [answer] array (of length seq.length) that encodes such a choice of A and B:
 * answer[i] = 0 if seq[i] is part of A, else answer[i] = 1.
 * Note that even though multiple answers may exist, you may return any of them.
 *
 * Example 1:
 * Input: seq = "(()())"
 * Output: [0,1,1,1,1,0]
 *
 * Example 2:
 * Input: seq = "()(())()"
 * Output: [0,0,0,1,1,0,1,1]
 *
 * Result: Accepted (1ms)
 */
public class MaximumNestingDepthOfTwoValidParenthesesStrings {
    /**
     * Solution 1: Stack
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     */
    class Solution1 {
        public int[] maxDepthAfterSplit(String seq) {
            char[] chars = seq.toCharArray();
            int[] answer = new int[chars.length];
            int[] stack = new int[chars.length];
            int currNum = 0, stackTop = -1;
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == '(') {
                    answer[i] = stackTop < 0 ? currNum : (stack[stackTop] ^ 1);
                    stack[++stackTop] = answer[i];
                    currNum ^= 1;
                } else {
                    answer[i] = stack[stackTop--];
                }
            }
            return answer;
        }
    }

    /**
     * Solution 2: Separate Even & Odd Indices
     * Analysis: Left brackets with the same depth must resides in odd indices
     * (Suppose that the first left bracket starts at index 0). With this,
     * we can separate brackets at even & odd indices to two groups with
     * different depth.
     * Time Complexity: O(N)
     * Space Complexity: O(1)
     */
    class Solution2 {
        public int[] maxDepthAfterSplit(String seq) {
            int n = seq.length(), res[] = new int[n];
            for (int i = 0; i < n; ++i)
                res[i] = seq.charAt(i) == '(' ? i & 1 : (1 - i & 1);
            return res;
        }
    }
    public static void main(String[] args) {
        Printer.printArray(new MaximumNestingDepthOfTwoValidParenthesesStrings()
                .new Solution2().maxDepthAfterSplit("(()())"));
        Printer.printArray(new MaximumNestingDepthOfTwoValidParenthesesStrings()
                .new Solution2().maxDepthAfterSplit("()(())()"));
        Printer.printArray(new MaximumNestingDepthOfTwoValidParenthesesStrings()
                .new Solution2().maxDepthAfterSplit("((()))"));
    }
}
