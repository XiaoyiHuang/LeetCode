package helper;

import java.util.List;

public class Printer {
    // ======================== Numbers ======================== //
    public static void printNum(int num) {
        System.out.println(num);
    }

    // ======================== String ======================== //
    public static void printString(String str) {
        System.out.println(str);
    }

    // ======================== ListNode ======================== //
    public static void printListNode(ListNode head) {
        if (head == null) {
            System.out.println("[NULL HEAD]");
            return;
        }
        while (head.next != null) {
            System.out.print(head.val + " -> ");
            head = head.next;
        }

        System.out.print(head.val + "\n");
    }

    // ======================== Array ======================== //
    public static void printArray(int[] arr) {
        System.out.println(doPrintArray(arr));
    }

    private static String doPrintArray(int[] arr) {
        StringBuilder content = new StringBuilder().append('[');
        for (int num : arr) {
            content.append(num).append(",");
        }
        if (content.length() > 1) {
            content.delete(content.length() - 1, content.length());
        }
        content.append(']');
        return content.toString();
    }

    public static void printArray(int[][] arr) {
        StringBuilder content = new StringBuilder().append('[');
        for (int[] subArr : arr) {
            content.append("  ").append(doPrintArray(subArr)).append(",\n");
        }
        if (content.length() > 1) {
            content.delete(content.length() - 2, content.length());
        }
        content.append(']');
        System.out.println(content.toString());
    }

    // ======================== List ======================== //
    public static <T> void printList(List<T> list) {
        System.out.println(doPrintList(list));
    }

    private static <T> String doPrintList(List<T> list) {
        StringBuilder content = new StringBuilder().append('[');
        for (T member : list) {
            if (member instanceof List) {
                content.append(doPrintList((List<T>) member)).append(",\n");
            } else {
                content.append(String.valueOf(member)).append(", ");
            }
        }
        if (content.length() > 1) {
            content.delete(content.length() - 2, content.length());
        }
        content.append(']');
        return content.toString();
    }
}
