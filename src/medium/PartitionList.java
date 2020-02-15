package medium;

import helper.ListNode;
import helper.Printer;

/**
 * [86] Partition List
 *
 * Description:
 * Given a linked list and a value x, partition it such that all nodes less
 * than x come before nodes greater than or equal to x.
 *
 * You should preserve the original relative order of the nodes in each of the two partitions.
 *
 * Example:
 * Input: head = 1->4->3->2->5->2, x = 3
 * Output: 1->2->2->4->3->5
 *
 * Result: Accepted (0ms)
 */
public class PartitionList {
    public ListNode partition(ListNode head, int x) {
        ListNode pseudoHead = new ListNode(-1);
        pseudoHead.next = head;
        ListNode lNode = head, lPrev = pseudoHead;
        while (lNode != null && lNode.val < x) {
            lNode = lNode.next;
            lPrev = lPrev.next;
        }
        if (lNode == null) {
            return pseudoHead.next;
        }
        ListNode rNode = lNode, rPrev = lPrev;
        while (rNode != null) {
            if (rNode.val < x) {
                lPrev.next = rNode;
                rPrev.next = rNode.next;
                rNode.next = lNode;

                lPrev = rNode;
                rNode = rPrev.next;
            } else {
                rNode = rNode.next;
                rPrev = rPrev.next;
            }
        }
        return pseudoHead.next;
    }
    public static void main(String[] args) {
        Printer.printListNode(new PartitionList().partition(new ListNode(1,4,3,2,5,2), 3));
        Printer.printListNode(new PartitionList().partition(new ListNode(2,1), 2));
    }
}
