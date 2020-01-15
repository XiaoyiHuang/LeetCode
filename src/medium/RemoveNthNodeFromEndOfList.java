package medium;

import helper.ListNode;
import helper.Printer;

/**
 * [19] Remove Nth Node From End of List
 *
 * Description:
 * Given a linked list, remove the n-th node from the end of list and
 * return its head.
 *
 * Example:
 * Given linked list: 1->2->3->4->5, and n = 2.
 * After removing the second node from the end, the linked list becomes
 * 1->2->3->5.
 *
 * Note:
 * Given n will always be valid.
 *
 * Follow up:
 * Could you do this in one pass?
 *
 * Result: Accepted (0ms)
 */
public class RemoveNthNodeFromEndOfList {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode pseudoHead = new ListNode(-1), slow = head,
                fast = head, prev = pseudoHead;
        pseudoHead.next = head;
        int dist = 0;

        while (dist < n - 1) {
            fast = fast.next;
            dist += 1;
        }
        while (fast.next != null) {
            fast = fast.next;
            slow = slow.next;
            prev = prev.next;
        }
        prev.next = slow.next;
        return pseudoHead.next;
    }

    public static void main(String[] args) {
        Printer.printListNode(
                new RemoveNthNodeFromEndOfList().removeNthFromEnd(new ListNode(1,2,3,4,5), 2));
        Printer.printListNode(
                new RemoveNthNodeFromEndOfList().removeNthFromEnd(new ListNode(1), 1));
        Printer.printListNode(
                new RemoveNthNodeFromEndOfList().removeNthFromEnd(new ListNode(1,2), 2));
    }
}
