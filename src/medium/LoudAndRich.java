package medium;

import helper.Printer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * [851] Loud and Rich
 *
 * Description:
 * In a group of N people (labelled 0, 1, 2, ..., N-1), each person has
 * different amounts of money, and different levels of quietness.
 *
 * For convenience, we'll call the person with label x, simply "person x".
 * We'll say that richer[i] = [x, y] if person x definitely has more money
 * than person y.  Note that richer may only be a subset of valid observations.
 *
 * Also, we'll say quiet[x] = q if person x has quietness q.
 * Now, return answer, where answer[x] = y if y is the least quiet person
 * (that is, the person y with the smallest value of quiet[y]), among all people
 * who definitely have equal to or more money than person x.

 * Example 1:
 * Input: richer = [[1,0],[2,1],[3,1],[3,7],[4,3],[5,3],[6,3]], quiet = [3,2,5,4,6,1,7,0]
 * Output: [5,5,2,5,4,5,6,7]
 * Explanation:
 * 1. answer[0] = 5.
 * Person 5 has more money than 3, which has more money than 1, which has more money than 0.
 * The only person who is quieter (has lower quiet[x]) is person 7, but
 * it isn't clear if they have more money than person 0.
 *
 * 2. answer[7] = 7.
 * Among all people that definitely have equal to or more money than person 7
 * (which could be persons 3, 4, 5, 6, or 7), the person who is the quietest (has lower quiet[x])
 * is person 7.
 *
 * The other answers can be filled out with similar reasoning.
 *
 * Note:
 *      1. 1 <= quiet.length = N <= 500
 *      2. 0 <= quiet[i] < N, all quiet[i] are different.
 *      3. 0 <= richer.length <= N * (N-1) / 2
 *      4. 0 <= richer[i][j] < N
 *      5. richer[i][0] != richer[i][1]
 *      6. richer[i]'s are all different.
 *      7. The observations in richer are all logically consistent.
 */
public class LoudAndRich {
    /**
     * Solution 1: DFS
     * Time Complexity: O(M) (M denotes the number of edges)
     * Space Complexity: O(M + N) (N denotes the number of people)
     * Result: Accepted (7ms)
     */
    public int[] loudAndRich(int[][] richer, int[] quiet) {
        List<Integer>[] richerPeople = new List[quiet.length];
        for (int[] r : richer) {
            if (richerPeople[r[1]] == null) {
                richerPeople[r[1]] = new ArrayList<>();
            }
            richerPeople[r[1]].add(r[0]);
        }
        int[] loudRich = new int[quiet.length];
        Arrays.fill(loudRich, -1);
        for (int i = 0; i < loudRich.length; i++) {
            find(richerPeople, quiet, i, loudRich);
        }
        return loudRich;
    }
    public int find(List<Integer>[] richerPeople, int[] quiet, int people, int[] loudRich) {
        if (loudRich[people] > 0) {
            return loudRich[people];
        }
        if (richerPeople[people] == null) {
            loudRich[people] = people;
            return loudRich[people];
        }
        int loudestParent = people;
        for (int parent : richerPeople[people]) {
            int currLoudestParent = find(richerPeople, quiet, parent, loudRich);
            if (quiet[loudestParent] > quiet[currLoudestParent]) {
                loudestParent = currLoudestParent;
            }
        }
        loudRich[people] = loudestParent;
        return loudestParent;
    }

    public static void main(String[] args) {
        int[][] richer = new int[][]{{1,0},{2,1},{3,1},{3,7},{4,3},{5,3},{6,3}};
        int[] quiet = new int[]{3,2,5,4,6,1,7,0};
        Printer.printArray(new LoudAndRich().loudAndRich(richer, quiet));
//        int[][] richer = new int[][]{{0,6},{0,7},{1,12},{2,14},{2,16},{3,14},{3,17},{5,14},{6,14},{7,14},{7,15},{11,15},{13,18},{14,19},{16,17}};
//        int[] quiet = new int[]{6,15,17,12,0,11,19,16,13,2,8,18,10,5,4,7,14,3,1,9};
//        Printer.printArray(new LoudAndRich().loudAndRich(richer, quiet));
    }
}
