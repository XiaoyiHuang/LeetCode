package helper;

import java.util.List;

public class Printer {
    // ======================== Numbers ======================== //
    public static void printNum(int num) {
        System.out.println(num);
    }

    public static void printNum(long num) {
        System.out.println(num);
    }

    // ======================== String ======================== //
    public static void printString(String str) {
        System.out.println(str);
    }

    // ======================== Boolean ======================== //
    public static void printBool(boolean bool) {
        System.out.println(bool);
    }

    // ======================== ListNode ======================== //
    public static String getListNodeString(ListNode head) {
        if (head == null) {
            return "[NULL HEAD]";
        } else {
            return head.toString();
        }
    }

    public static void printListNode(ListNode head) {
        System.out.println(getListNodeString(head));
    }

    // ======================== TreeNode ======================== //
    public static String getTreeString(TreeNode root) {
        if (root == null) {
            return "[NULL ROOT]";
        } else {
            return root.toString();
        }
    }

    public static void printTree(TreeNode root) {
        System.out.println(getTreeString(root));
    }

    // ======================== Array ======================== //
    public static void printArray(int[] arr) {
        System.out.println(getArrayString(arr));
    }

    public static String getArrayString(int[] arr) {
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
            content.append(getArrayString(subArr)).append(",\n");
        }
        if (content.length() > 1) {
            content.delete(content.length() - 2, content.length());
        }
        content.append(']');
        System.out.println(content.toString());
    }

    // ======================== List ======================== //
    public static <T> void printList(List<T> list) {
        System.out.println(getListString(list));
    }

    private static <T> String getListString(List<T> list) {
        StringBuilder content = new StringBuilder().append('[');
        for (T member : list) {
            if (member instanceof List) {
                content.append(getListString((List<T>) member)).append(",\n");
            } else if (member instanceof int[]) {
                content.append(getArrayString((int[])member)).append(",\n");
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
