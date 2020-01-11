package medium;

import helper.Printer;

/**
 * Description:
 * The string "PAYPALISHIRING" is written in a zigzag pattern on a given number of rows like this:
 * (you may want to display this pattern in a fixed font for better legibility)
 *
 * P   A   H   N
 * A P L S I I G
 * Y   I   R
 * And then read line by line: "PAHNAPLSIIGYIR"
 * Write the code that will take a string and make this conversion given a number of rows:
 *      string convert(string s, int numRows);
 *
 * Example 1:
 * Input: s = "PAYPALISHIRING", numRows = 3
 * Output: "PAHNAPLSIIGYIR"
 *
 * Example 2:
 * Input: s = "PAYPALISHIRING", numRows = 4
 * Output: "PINALSIGYAHRPI"
 * Explanation:
 * P     I    N
 * A   L S  I G
 * Y A   H R
 * P     I
 *
 * Result: Accepted (3ms)
 */
public class ZigZagConversion {
    public String convert(String s, int numRows) {
        if (s.length() <= numRows || numRows == 1) {
            return s;
        }
        StringBuilder zigzag = new StringBuilder();
        char[] chars = s.toCharArray();

        for (int row = 0; row < numRows; row++) {
            int currIdx = row;
            zigzag.append(chars[currIdx]);

            boolean isParsingDownwards = true;
            while (true) {
                int rowOffset = isParsingDownwards ? numRows - row - 1 : row;
                if (rowOffset > 0) {
                    currIdx += (rowOffset << 1);
                    if (currIdx < s.length()) {
                        zigzag.append(chars[currIdx]);
                    } else {
                        break;
                    }
                }
                isParsingDownwards = !isParsingDownwards;
            }
        }
        return zigzag.toString();
    }
    public static void main(String[] args) {
        ZigZagConversion solution = new ZigZagConversion();
        String result = solution.convert("PAYPALISHIRING", 4);
        Printer.printString(result);
    }
}
