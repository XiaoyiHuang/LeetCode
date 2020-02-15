package hard;

import helper.Printer;

/**
 * [135] Candy
 *
 * Description:
 * There are N children standing in a line. Each child is assigned a rating value.
 * You are giving candies to these children subjected to the following requirements:
 *      1. Each child must have at least one candy.
 *      2. Children with a higher rating get more candies than their neighbors.
 * What is the minimum candies you must give?
 *
 * Example 1:
 * Input: [1,0,2]
 * Output: 5
 * Explanation: You can allocate to the first, second and third child
 *              with 2, 1, 2 candies respectively.
 *
 * Example 2:
 * Input: [1,2,2]
 * Output: 4
 * Explanation: You can allocate to the first, second and third child
 *              with 1, 2, 1 candies respectively. The third child
 *              gets 1 candy because it satisfies the above two conditions.
 */
public class Candy {
    /**
     * Solution 1: One-pass
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     * Result: Accepted (2ms)
     */
    class Solution1 {
        public int candy(int[] ratings) {
            if (ratings.length == 0) {
                return 0;
            }
            int[] candies = new int[ratings.length];
            candies[0] = 1;
            int currIndex = 1;
            int totalCandies = 1;

            while (currIndex < ratings.length) {
                if (ratings[currIndex] < ratings[currIndex - 1]) {
                    int revOrderRangeFrom = currIndex - 1, revOrderRangeTo;
                    while (currIndex < ratings.length && ratings[currIndex] < ratings[currIndex - 1]) {
                        currIndex += 1;
                    }
                    revOrderRangeTo = (--currIndex);
                    candies[currIndex--] = 1;
                    totalCandies += 1;
                    while (currIndex >= revOrderRangeFrom) {
                        int newCandyNum = candies[currIndex + 1] + 1;
                        if (newCandyNum > candies[currIndex]) {
                            totalCandies += (newCandyNum - candies[currIndex]);
                            candies[currIndex] = newCandyNum;
                        }
                        currIndex -= 1;
                    }
                    currIndex = revOrderRangeTo + 1;
                }
                if (currIndex < ratings.length && ratings[currIndex] > ratings[currIndex - 1]) {
                    while (currIndex < ratings.length && ratings[currIndex] > ratings[currIndex - 1]) {
                        candies[currIndex] = candies[currIndex - 1] + 1;
                        totalCandies += candies[currIndex++];
                    }
                }
                if (currIndex < ratings.length && ratings[currIndex] == ratings[currIndex - 1]) {
                    while (currIndex < ratings.length && ratings[currIndex] == ratings[currIndex - 1]) {
                        candies[currIndex++] = 1;
                        totalCandies += 1;
                    }
                }
            }
            return totalCandies;
        }
    }

    /**
     * Solution 2: Two-pass
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     * Result: Accepted (2ms)
     */
    class Solution2 {
        public int candy(int[] ratings) {
            int[] candies = new int[ratings.length];
            candies[0] = 1;
            for (int i = 1; i < ratings.length; i++) {
                if (ratings[i] > ratings[i - 1]) {
                    candies[i] = candies[i - 1] + 1;
                } else {
                    candies[i] = 1;
                }
            }
            int totalCandies = candies[ratings.length - 1];
            for (int i = ratings.length - 2; i >= 0; i--) {
                if (ratings[i] > ratings[i + 1]) {
                    candies[i] = Math.max(candies[i], candies[i + 1] + 1);
                }
                totalCandies += candies[i];
            }
            return totalCandies;
        }
    }
    public static void main(String[] args) {
        Printer.printNum(new Candy().new Solution1().candy(new int[]{1,0,2}));
        Printer.printNum(new Candy().new Solution2().candy(new int[]{1,0,2}));
        Printer.printNum(new Candy().new Solution1().candy(new int[]{1,2,2}));
        Printer.printNum(new Candy().new Solution2().candy(new int[]{1,2,2}));
        Printer.printNum(new Candy().new Solution1().candy(new int[]{1,2,2,3,2,1,3,4}));
        Printer.printNum(new Candy().new Solution2().candy(new int[]{1,2,2,3,2,1,3,4}));
    }
}
