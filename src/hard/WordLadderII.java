package hard;

import helper.Printer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * [126] Word Ladder II
 *
 * Description:
 * Given two words (beginWord and endWord), and a dictionary's word list,
 * find all shortest transformation sequence(s) from beginWord to endWord,
 * such that:
 *      1. Only one letter can be changed at a time
 *      2. Each transformed word must exist in the word list.
 *         Note that beginWord is not a transformed word.
 * Note:
 *      Return an empty list if there is no such transformation sequence.
 *      All words have the same length.
 *      All words contain only lowercase alphabetic characters.
 *      You may assume no duplicates in the word list.
 *      You may assume beginWord and endWord are non-empty and are not the same.
 *
 * Example 1:
 * Input:
 * beginWord = "hit",
 * endWord = "cog",
 * wordList = ["hot","dot","dog","lot","log","cog"]
 * Output:
 * [
 *   ["hit","hot","dot","dog","cog"],
 *   ["hit","hot","lot","log","cog"]
 * ]
 *
 * Example 2:
 * Input:
 * beginWord = "hit"
 * endWord = "cog"
 * wordList = ["hot","dot","dog","lot","log"]
 * Output: []
 * Explanation: The endWord "cog" is not in wordList, therefore
 * no possible transformation.
 */
public class WordLadderII {
    /**
     * Solution 1: BFS + DFS
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     * Result: Accepted (264ms)
     */
    class Solution1 {
        private List<List<String>> ladders = new ArrayList<>();
        private Map<String, List<String>> wordParents = new HashMap<>();

        public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
            Set<String> wordSet = new HashSet<>(wordList);
            if (!wordSet.contains(endWord)) {
                return new ArrayList<>();
            }
            bfs(beginWord, endWord, wordSet);
            LinkedList<String> initialLadder = new LinkedList<>();
            initialLadder.add(endWord);
            dfs(endWord, initialLadder, beginWord);
            return ladders;
        }
        private void bfs(String beginWord, String endWord, Set<String> wordSet) {
            Set<String> currLevel = new HashSet<>();
            currLevel.add(beginWord);
            wordSet.remove(beginWord);

            while (!currLevel.isEmpty()) {
                Set<String> nextLevel = new HashSet<>();
                Set<String> availableWords = new HashSet<>(wordSet);
                boolean hasReachedEnd = false;

                for (String currWord : currLevel) {
                    char[] currChars = currWord.toCharArray();
                    for (int j = 0; j < currChars.length; j++) {
                        char originalChar = currChars[j];
                        for (char c = 'a'; c <= 'z'; c++) {
                            if (originalChar == c) {
                                continue;
                            }
                            currChars[j] = c;
                            String nextWord = new String(currChars);
                            if (wordSet.contains(nextWord)) {
                                nextLevel.add(nextWord);
                                availableWords.remove(nextWord);

                                // Record currWord as the parent node of nextWord (on the route to endWord)
                                wordParents.computeIfAbsent(nextWord, r -> new ArrayList<>()).add(currWord);
                            }
                            if (nextWord.equals(endWord)) {
                                hasReachedEnd = true;
                            }
                        }
                        currChars[j] = originalChar;
                    }
                }
                if (hasReachedEnd) {
                    return;
                }
                wordSet = availableWords;
                currLevel = nextLevel;
            }
        }
        public void dfs(String currWord, LinkedList<String> currLadder, String beginWord) {
            if (currWord.equals(beginWord)) {
                ladders.add(new ArrayList<>(currLadder));
                return;
            }
            List<String> parents = wordParents.get(currWord);
            if (parents == null) {
                return;
            }
            for (String parent : parents) {
                currLadder.addFirst(parent);
                dfs(parent, currLadder, beginWord);
                currLadder.poll();
            }
        }
    }

    /**
     * Solution 2: Bidirectional BFS + DFS
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     * Result: Accepted (35ms)
     */
    class Solution2 {
        private List<List<String>> ladders = new ArrayList<>();
        private Map<String, List<String>> wordParents = new HashMap<>();

        public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
            Set<String> wordSet = new HashSet<>(wordList);
            if (!wordSet.contains(endWord)) {
                return new ArrayList<>();
            }
            bfs(beginWord, endWord, wordSet);
            LinkedList<String> initialLadder = new LinkedList<>();
            initialLadder.add(endWord);
            dfs(endWord, initialLadder, beginWord);
            return ladders;
        }
        private void bfs(String beginWord, String endWord, Set<String> wordSet) {
            Set<String> beginSet = new HashSet<>();
            Set<String> endSet = new HashSet<>();

            beginSet.add(beginWord);
            endSet.add(endWord);
            wordSet.remove(endWord);
            wordSet.remove(beginWord);

            boolean isFlipped = false;
            while (!beginSet.isEmpty() && !endSet.isEmpty()) {
                Set<String> nextLevelSet = new HashSet<>();
                if (beginSet.size() > endSet.size()) {
                    Set<String> tmpSet = beginSet;
                    beginSet = endSet;
                    endSet = tmpSet;
                    isFlipped = !isFlipped;
                }
                boolean hasTwoEndsMet = false;
                Set<String> availableWords = new HashSet<>(wordSet);

                for (String currWord : beginSet) {
                    char[] currChars = currWord.toCharArray();
                    for (int i = 0; i < currChars.length; i++) {
                        char originalChar = currChars[i];
                        for (char c = 'a'; c <= 'z'; c++) {
                            if (originalChar == c) {
                                continue;
                            }
                            currChars[i] = c;
                            String nextWord = new String(currChars);
                            if (wordSet.contains(nextWord)) {
                                nextLevelSet.add(nextWord);
                                availableWords.remove(nextWord);
                                if (!isFlipped) {
                                    wordParents.computeIfAbsent(nextWord, r -> new ArrayList<>())
                                            .add(currWord);
                                } else {
                                    wordParents.computeIfAbsent(currWord, r -> new ArrayList<>())
                                            .add(nextWord);
                                }
                            }
                            if (endSet.contains(nextWord)) {
                                hasTwoEndsMet = true;
                                if (!isFlipped) {
                                    wordParents.computeIfAbsent(nextWord, r -> new ArrayList<>())
                                            .add(currWord);
                                } else {
                                    wordParents.computeIfAbsent(currWord, r -> new ArrayList<>())
                                            .add(nextWord);
                                }
                            }
                        }
                        currChars[i] = originalChar;
                    }
                }
                beginSet = nextLevelSet;
                wordSet = availableWords;
                if (hasTwoEndsMet) {
                    return;
                }
            }
        }
        public void dfs(String currWord, LinkedList<String> currLadder, String beginWord) {
            if (currWord.equals(beginWord)) {
                ladders.add(new ArrayList<>(currLadder));
                return;
            }
            List<String> parents = wordParents.get(currWord);
            if (parents == null) {
                return;
            }
            for (String parent : parents) {
                currLadder.addFirst(parent);
                dfs(parent, currLadder, beginWord);
                currLadder.poll();
            }
        }
    }
    public static void main(String[] args) {
        List<String> wordList = Arrays.asList("hot","dot","dog","lot","log","cog");
        Printer.printList(new WordLadderII().new Solution2().
                findLadders("hit", "cog", wordList));
        wordList = Arrays.asList("hot","dot","dog","lot","log");
        Printer.printList(new WordLadderII().new Solution2()
                .findLadders("hit", "cog", wordList));
    }
}
