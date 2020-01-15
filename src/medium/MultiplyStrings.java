package medium;

import helper.Printer;

/**
 * [43] Multiply Strings
 *
 * Description:
 * Given two non-negative integers num1 and num2 represented as strings, return
 * the product of num1 and num2, also represented as a string.
 *
 * Example 1:
 * Input: num1 = "2", num2 = "3"
 * Output: "6"
 *
 * Example 2:
 * Input: num1 = "123", num2 = "456"
 * Output: "56088"
 *
 * Note:
 * 1. The length of both num1 and num2 is < 110.
 * 2. Both num1 and num2 contain only digits 0-9.
 * 3. Both num1 and num2 do not contain any leading zero,
 *    except the number 0 itself.
 * 4. You must not use any built-in BigInteger library or
 *    convert the inputs to integer directly.
 *
 * Result: Accepted (2ms)
 */
public class MultiplyStrings {
    public String multiply(String num1, String num2) {
        if (num1.equals("0") || num2.equals("0")) {
            return "0";
        }
        char[] digits1 = num1.toCharArray();
        char[] digits2 = num2.toCharArray();
        int[] product = new int[digits1.length + digits2.length];

        for (int i = digits1.length - 1; i >= 0; i--) {
            for (int j = digits2.length - 1; j >= 0; j--) {
                product[i + j + 1] += (digits1[i] - '0') * (digits2[j] - '0');
            }
        }
        int carry = 0;
        for (int i = product.length - 1; i >= 0; i--) {
            int sum = product[i] + carry;
            product[i] = sum % 10;
            carry = sum / 10;
        }

        StringBuilder productStr = new StringBuilder();
        int productIdx = 0;
        while (product[productIdx] == 0) {
            productIdx++;
        }
        for (; productIdx < product.length; productIdx++) {
            productStr.append(product[productIdx]);
        }
        return productStr.toString();
    }
    public static void main(String[] args) {
        Printer.printString(new MultiplyStrings().multiply("123","456"));
        Printer.printString(new MultiplyStrings().multiply("2","3"));
    }
}
