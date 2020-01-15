package medium;

import helper.ListNode;
import helper.Printer;

/**
 * [24] Swap Nodes in Pairs
 *
 * Description:
 * Given a linked list, swap every two adjacent nodes and return its head.
 *
 * You may not modify the values in the list's nodes, only nodes itself may
 * be changed.
 *
 * Example:
 * Given 1->2->3->4, you should return the list as 2->1->4->3.
 *
 * Result: Accepted (0ms)
 *
 */
public class SwapNodesInPairs {
    public ListNode swapPairs(ListNode head) {
        ListNode pseudoHead = new ListNode(-1);
        ListNode currNode = pseudoHead;
        pseudoHead.next = head;

        while (currNode.next != null) {
            ListNode pairTail;
            if ((pairTail = currNode.next.next) == null) {
                break;
            } else {
                currNode.next.next = pairTail.next;
                pairTail.next = currNode.next;
                currNode.next = pairTail;
            }
            currNode = pairTail.next;
        }
        return pseudoHead.next;
    }

    public static void main(String[] args) {
        Printer.printListNode(new SwapNodesInPairs().swapPairs(new ListNode(1,2,3,4)));
        Printer.printListNode(new SwapNodesInPairs().swapPairs(new ListNode(1,2,3)));
        Printer.printListNode(new SwapNodesInPairs().swapPairs(new ListNode(1)));
    }
}
