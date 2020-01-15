package medium;

import helper.Printer;

import java.util.ArrayList;
import java.util.List;

/**
 * [22] Generate Parentheses
 *
 * Description:
 * Given n pairs of parentheses, write a function to generate all combinations of
 * well-formed parentheses.
 *
 * For example, given n = 3, a solution set is:
 * [
 *   "((()))",
 *   "(()())",
 *   "(())()",
 *   "()(())",
 *   "()()()"
 * ]
 *
 * Result: Accepted (1ms)
 */
public class GenerateParentheses {
    public List<String> generateParenthesis(int n) {
        List<String> combinations = new ArrayList<>();
        backtracking(combinations, new StringBuilder(), 0, 0, n);
        return combinations;
    }

    private void backtracking(List<String> combinations, StringBuilder currStr,
                              int lParenNum, int rParenNum, int n) {
        if (lParenNum == n && rParenNum == lParenNum) {
            combinations.add(currStr.toString());
            return;
        }
        if (lParenNum < n && lParenNum >= rParenNum) {
            currStr.append('(');
            backtracking(combinations, currStr, lParenNum + 1, rParenNum, n);
            currStr.deleteCharAt(currStr.length() - 1);
        }
        if (lParenNum > 0 && rParenNum < lParenNum) {
            currStr.append(')');
            backtracking(combinations, currStr, lParenNum, rParenNum + 1, n);
            currStr.deleteCharAt(currStr.length() - 1);
        }
    }

    public static void main(String[] args) {
        Printer.printList(new GenerateParentheses().generateParenthesis(3));
    }
}
