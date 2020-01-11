package helper;

public class Printer {
    public static void printNum(int num) {
        System.out.println(num);
    }

    public static void printString(String str) {
        System.out.println(str);
    }

    public static void printListNode(ListNode head) {
        while (head.next != null) {
            System.out.print(head.val + " -> ");
            head = head.next;
        }

        System.out.print(head.val + "\n");
    }
}
