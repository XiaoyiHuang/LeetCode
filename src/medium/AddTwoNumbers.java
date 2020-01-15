package medium;

import helper.ListNode;
import helper.Printer;

/**
 * [2] Add Two Numbers
 *
 * Description:
 * You are given two non-empty linked lists representing two non-negative integers. The digits are stored in reverse
 * order and each of their nodes contain a single digit. Add the two numbers and return it as a linked list.
 * You may assume the two numbers do not contain any leading zero, except the number 0 itself.
 *
 * Example:
 * Input: (2 -> 4 -> 3) + (5 -> 6 -> 4)
 * Output: 7 -> 0 -> 8
 * Explanation: 342 + 465 = 807.
 *
 * Result: Accepted (2ms)
 */
public class AddTwoNumbers {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode sumNode = new ListNode(-1);
        ListNode currSum = sumNode;
        int carry = 0;

        while (l1 != null || l2 != null) {
            int val1 = l1 == null ? 0 : l1.val;
            int val2 = l2 == null ? 0 : l2.val;
            int sum = val1 + val2 + carry;
            currSum.next = new ListNode(sum % 10);
            currSum = currSum.next;
            carry = sum / 10;

            if (l1 != null) {
                l1 = l1.next;
            }
            if (l2 != null) {
                l2 = l2.next;
            }
        }
        if (carry > 0) {
            currSum.next = new ListNode(carry);
        }

        return sumNode.next;
    }

    public static void main(String[] args) {
        AddTwoNumbers solution = new AddTwoNumbers();
        ListNode l1 = new ListNode(7, 4, 7);
        ListNode l2 = new ListNode(5, 6, 4);
        ListNode sum = solution.addTwoNumbers(l1, l2);
        Printer.printListNode(sum);
    }
}
