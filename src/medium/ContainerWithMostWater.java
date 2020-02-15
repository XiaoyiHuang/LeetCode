package medium;

import helper.Printer;

/**
 * [11] Container With Most Water
 *
 * Description:
 * Given n non-negative integers a1, a2, ..., an , where each represents
 * a point at coordinate (i, ai). n vertical lines are drawn such that
 * the two endpoints of line i is at (i, ai) and (i, 0). Find two lines,
 * which together with x-axis forms a container, such that the container
 * contains the most water.
 *
 * Note: You may not slant the container and n is at least 2.
 *
 * Example:
 * Input: [1,8,6,2,5,4,8,3,7]
 * Output: 49
 *
 * Result: Accepted (2ms)
 */
public class ContainerWithMostWater {
    public int maxArea(int[] height) {
        int lPtr = 0, rPtr = height.length - 1;
        int maxSize = 0;

        while (lPtr < rPtr) {
            maxSize = Math.max(maxSize, Math.min(height[lPtr], height[rPtr]) * (rPtr - lPtr));
            if (height[lPtr] < height[rPtr]) {
                lPtr += 1;
            } else {
                rPtr -= 1;
            }
        }
        return maxSize;
    }
    public static void main(String[] args) {
        ContainerWithMostWater solution = new ContainerWithMostWater();
        int result = solution.maxArea(new int[]{1,8,6,2,5,4,8,3,7});
        Printer.printNum(result);
    }
}
