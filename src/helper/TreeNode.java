package helper;

import java.util.LinkedList;

public class TreeNode {
    public int val;
    public TreeNode left = null;
    public TreeNode right = null;

    private TreeNode() {}

    public TreeNode(int val) {
        this.val = val;
    }

    public TreeNode(Integer... vals) {
        if (vals.length == 0 || vals[0] == null) {
            return;
        }

        // Setup root TreeNode
        int index = 0;
        this.val = vals[index];

        // Build the tree level-by-level
        LinkedList<TreeNode> level = new LinkedList<>();
        level.offer(this);
        while (!level.isEmpty()) {
            int levelSize = level.size();
            for (int i = 0; i < levelSize; i++) {
                TreeNode node;
                if ((node = level.poll()) == null) {
                    continue;
                }
                node.left = ((++index) >= vals.length || vals[index] == null) ? null : new TreeNode(vals[index]);
                node.right = ((++index) >= vals.length || vals[index] == null) ? null : new TreeNode(vals[index]);
                level.offer(node.left);
                level.offer(node.right);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder content = new StringBuilder().append("[");
        int endingNullCount = 0; // Mark if the output string ends with a null string
        final String nullString = "null";

        LinkedList<TreeNode> level = new LinkedList<>();
        level.offer(this);

        while (!level.isEmpty()) {
            int levelSize = level.size();
            for (int i = 0; i < levelSize; i++) {
                TreeNode node;
                if ((node = level.poll()) != null) {
                    content.append(node.val).append(",");
                    endingNullCount = 0;
                } else {
                    content.append(nullString).append(",");
                    endingNullCount += 1;
                    continue;
                }
                level.offer(node.left);
                level.offer(node.right);
            }
        }
        if (endingNullCount > 0) {
            // Remove the ending null string
            int deletionLength = (nullString.length()) * endingNullCount + (endingNullCount - 1);
            content.delete(content.length() - 1 - deletionLength, content.length());
        }
        content.setCharAt(content.length() - 1, ']');
        return content.toString();
    }
}
