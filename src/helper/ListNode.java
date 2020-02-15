package helper;

public class ListNode {
    public int val;
    public ListNode next;

    public ListNode(int x) {
        val = x;
    }

    public ListNode(int... vals) {
        if (vals == null || vals.length == 0) {
            return;
        }

        this.val = vals[0];
        ListNode curr = this;

        for (int i = 1; i < vals.length; i++) {
            curr.next = new ListNode(vals[i]);
            curr = curr.next;
        }
    }

    @Override
    public String toString() {
        StringBuilder content = new StringBuilder();
        ListNode curr = this;
        while (curr.next != null) {
            content.append(curr.val).append(" -> ");
            curr = curr.next;
        }
        content.append(curr.val).append("\n");
        return content.toString();
    }
}
