package hard;

import helper.Printer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * [691] Stickers To Spell Word
 *
 * Description:
 * We are given N different types of stickers. Each sticker has a lowercase English word on it.
 * You would like to spell out the given target string by cutting individual letters from your
 * collection of stickers and rearranging them.
 * You can use each sticker more than once if you want, and you have infinite quantities of each sticker.
 * What is the minimum number of stickers that you need to spell out the target? If the task is impossible,
 * return -1.
 *
 * Example 1:
 * Input: ["with", "example", "science"], "thehat"
 * Output: 3
 * Explanation:
 * We can use 2 "with" stickers, and 1 "example" sticker.
 * After cutting and rearrange the letters of those stickers, we can form the target "thehat".
 * Also, this is the minimum number of stickers necessary to form the target string.
 *
 * Example 2:
 * Input: ["notice", "possible"], "basicbasic"
 * Output: -1
 * Explanation:
 * We can't form the target "basicbasic" from cutting letters from the given stickers.
 *
 * Note:
 *      1. [stickers] has length in the range [1, 50].
 *      2. [stickers] consists of lowercase English words (without apostrophes).
 *      3. [target] has length in the range [1, 15], and consists of lowercase English letters.
 *      4. In all test cases, all words were chosen randomly from the 1000 most common US English
 *         words, and the target was chosen as a concatenation of two random words.
 *      5. The time limit may be more challenging than usual. It is expected that a 50 sticker test
 *         case can be solved within 35ms on average.
 */
public class StickersToSpellWord {
    /**
     * Solution 1: BFS
     * Time Complexity: O(2 ^ N * M * 26) (N is the length of the target, M is the number of stickers)
     * Space Complexity: O(M * 26)
     * Result: Accepted (20ms)
     */
    class Solution1 {
        public int minStickers(String[] stickers, String target) {
            // Summarize target word letter frequency
            int[] targetFreq = new int[26];
            for (char c : target.toCharArray()) {
                targetFreq[c - 'a'] += 1;
            }

            // Summarize sticker letter frequency
            int[][] stickerLetters = new int[stickers.length][26];
            for (int i = 0; i < stickers.length; i++) {
                for (int c : stickers[i].toCharArray()) {
                    stickerLetters[i][c - 'a'] += 1;
                }
            }

            // Use BFS to spell the target with minimum sticker usage
            int usedStickers = 0;
            Set<String> visitedStates = new HashSet<>();
            LinkedList<int[]> candidates = new LinkedList<>();
            candidates.offer(targetFreq);

            while (!candidates.isEmpty()) {
                int candidateNum = candidates.size();
                for (int i = 0; i < candidateNum; i++) {
                    int[] currState = candidates.poll();
                    String currKey = toKey(currState);
                    if (currKey.length() == 0) {
                        return usedStickers;
                    }
                    if (visitedStates.add(currKey)) {
                        for (int[] letters : stickerLetters) {
                            if (letters[currKey.charAt(0) - 'a'] == 0) {
                                // Only focus on the first missing letter for now
                                continue;
                            }
                            int[] nextState = currState.clone();
                            for (int j = 0; j < letters.length; j++) {
                                nextState[j] -= letters[j];
                                nextState[j] = Math.max(0, nextState[j]);
                            }
                            candidates.offer(nextState);
                        }
                    }
                }
                usedStickers += 1;
            }
            return -1;
        }
        private String toKey(int[] state) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < state.length; i++) {
                for (int j = 0; j < state[i]; j++) {
                    sb.append((char)('a' + i));
                }
            }
            return sb.toString();
        }
    }

    /**
     * Solution 2: Memoization
     * Time Complexity: O(2 ^ N * M * 26) (N is the length of the target, M is the number of stickers)
     * Space Complexity: O(2 ^ N)
     * Result: Accepted (14ms)
     */
    class Solution2 {
        public int minStickers(String[] stickers, String target) {
            int[][] stickerLetters = new int[stickers.length][26];
            for (int i = 0; i < stickers.length; i++) {
                for (char c : stickers[i].toCharArray()) {
                    stickerLetters[i][c - 'a'] += 1;
                }
            }
            Map<String, Integer> dp = new HashMap<>();
            dp.put("", 0);
            return applyStickers(stickerLetters, target, dp);
        }
        private int applyStickers(int[][] stickerLetters, String target, Map<String, Integer> dp) {
            Integer stickerNum;
            if ((stickerNum = dp.get(target)) != null) {
                return stickerNum;
            } else {
                stickerNum = Integer.MAX_VALUE;
            }

            int[] targetLetters = new int[26];
            for (char c : target.toCharArray()) {
                targetLetters[c - 'a'] += 1;
            }
            // Try with all the stickers
            for (int i = 0; i < stickerLetters.length; i++) {
                if (stickerLetters[i][target.charAt(0) - 'a'] == 0) {
                    continue;
                }
                StringBuilder nextTarget = new StringBuilder();
                for (int j = 0; j < targetLetters.length; j++) {
                    int letterNumAfterUsing = Math.max(0, targetLetters[j] - stickerLetters[i][j]);
                    for (int k = 0; k < letterNumAfterUsing; k++) {
                        nextTarget.append((char)('a' + j));
                    }
                }
                int result = applyStickers(stickerLetters, nextTarget.toString(), dp);
                if (result != -1) {
                    stickerNum = Math.min(stickerNum, result + 1);
                }
            }
            stickerNum = (stickerNum == Integer.MAX_VALUE ? -1 : stickerNum);
            dp.put(target, stickerNum);
            return stickerNum;
        }
    }

    /**
     * Solution 3: Dynamic Programming (with bit-ops)
     * Time Complexity: O(2 ^ T * S * T)
     * (T denotes the number of letters in target, S denotes the total number of stickers in all letters)
     * Space Complexity: O(2 ^ T)
     * Result: Accepted (216ms)
     */
    class Solution3 {
        public int minStickers(String[] stickers, String target) {
            int N = target.length();
            int[] dp = new int[1 << N];
            Arrays.fill(dp, 1, dp.length, Integer.MAX_VALUE);

            for (int state = 0; state < (1 << N); state++) {
                if (dp[state] == Integer.MAX_VALUE) {
                    continue;
                }
                // Try with all stickers
                for (String sticker : stickers) {
                    int nextState = state;
                    for (char letter : sticker.toCharArray()) {
                        // Check against all sticker letters
                        for (int i = 0; i < N; i++) {
                            // Find and set the first unset bit of current state
                            if (((nextState >> i) & 1) == 1) {
                                continue;
                            }
                            if (target.charAt(i) == letter) {
                                nextState |= (1 << i);
                                break;
                            }
                        }
                    }
                    dp[nextState] = Math.min(dp[state] + 1, dp[nextState]);
                }
            }
            return dp[dp.length - 1] == Integer.MAX_VALUE ? -1 : dp[dp.length - 1];
        }
    }
    public static void main(String[] args) {
        Printer.printNum(new StickersToSpellWord()
                .new Solution3().minStickers(new String[]{"notice", "possible"}, "basicbasic"));
        Printer.printNum(new StickersToSpellWord()
                .new Solution3().minStickers(new String[]{"old","center","shape","fig","skin","come"}, "togethernear"));
        Printer.printNum(new StickersToSpellWord()
                .new Solution3().minStickers(new String[]{"search","win","never","air","field","corner","if"}, "ranwind"));
        Printer.printNum(new StickersToSpellWord().new Solution3().minStickers(
                new String[]{"write","their","read","quiet","against","down","process","check"}, "togetherhand"));
        Printer.printNum(new StickersToSpellWord().new Solution3().minStickers(
                new String[]{"hour","supply","plain","fruit","pretty","touch","property"}, "sharpcenter"));
    }
}
