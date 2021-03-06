package medium;

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
 * [127] Word Ladder
 *
 * Description:
 * Given two words (beginWord and endWord), and a dictionary's word list,
 * find the length of shortest transformation sequence from beginWord to
 * endWord, such that:
 * 1. Only one letter can be changed at a time.
 * 2. Each transformed word must exist in the word list.
 * Note that beginWord is not a transformed word.
 *
 * Note:
 * Return 0 if there is no such transformation sequence.
 * All words have the same length.
 * All words contain only lowercase alphabetic characters.
 * You may assume no duplicates in the word list.
 * You may assume beginWord and endWord are non-empty and are not the same.
 *
 * Example 1:
 * Input:
 * beginWord = "hit",
 * endWord = "cog",
 * wordList = ["hot","dot","dog","lot","log","cog"]
 * Output: 5
 * Explanation: As one shortest transformation is
 * "hit" -> "hot" -> "dot" -> "dog" -> "cog", return its length 5.
 *
 * Example 2:
 * Input:
 * beginWord = "hit"
 * endWord = "cog"
 * wordList = ["hot","dot","dog","lot","log"]
 * Output: 0
 * Explanation: The endWord "cog" is not in wordList,
 * therefore no possible transformation.
 *
 * Result:
 * Solution 1: Accepted (478ms)
 * Solution 2: Accepted (55ms)
 * Solution 3: Accepted (44ms)
 * Solution 4: Accepted (14ms)
 */
public class WordLadder {
    /**
     * Solution 1: Bottom-up BFS
     * Time Complexity: O(N^2) (N denotes len(wordList))
     * Space Complexity: O(N^2)
     */
    class Solution1 {
        public int ladderLength(String beginWord, String endWord, List<String> wordList) {
            boolean[][] wordNeighbors = new boolean[wordList.size()][wordList.size()];
            int endWordIndex = -1;
            for (int i = 0; i < wordList.size(); i++) {
                String baseWord = wordList.get(i);
                if (baseWord.equals(endWord)) {
                    endWordIndex = i;
                }
                for (int j = 0; j < wordList.size(); j++) {
                    if (i != j && getWordDistance(baseWord, wordList.get(j)) == 1) {
                        wordNeighbors[i][j] = true;
                    }
                }
            }
            if (endWordIndex == -1) {
                // If end word is not included in the dictionary
                return 0;
            }

            // Get neighbor words of the start word
            boolean[] beginWordNeighbors = new boolean[wordList.size()];
            boolean hasBeginWordNeighbors = false;
            for (int i = 0; i < wordList.size(); i++) {
                if (getWordDistance(wordList.get(i), beginWord) == 1) {
                    beginWordNeighbors[i] = true;
                    hasBeginWordNeighbors = true;
                }
            }
            // if start word is isolated
            if (!hasBeginWordNeighbors) {
                return 0;
            }

            int ladderLength = 1;
            boolean[] visited = new boolean[wordList.size()];
            LinkedList<Integer> stack = new LinkedList<>();
            stack.push(endWordIndex);
            visited[stack.get(0)] = true;

            // Classic Breadth-First-Search
            while (!stack.isEmpty()) {
                int stackSize = stack.size();
                for (int i = 0; i < stackSize; i++) {
                    int currWord = stack.poll();
                    if (beginWordNeighbors[currWord]) {
                        return ladderLength + 1;
                    }
                    for (int neighbor = 0; neighbor < wordNeighbors.length; neighbor++) {
                        if (wordNeighbors[currWord][neighbor] && !visited[neighbor]) {
                            stack.offer(neighbor);
                            visited[neighbor] = true;
                        }
                    }
                }
                ladderLength += 1;
            }
            return 0;
        }
        private int getWordDistance(String word0, String word1) {
            int distance = 0;
            for (int i = 0; i < word0.length(); i++) {
                if (word0.charAt(i) != word1.charAt(i)) {
                    distance += 1;
                }
            }
            return distance;
        }
    }

    /**
     * Solution 2: Better BFS
     * Time Complexity: O(M*N) (M denotes len(word), N denotes len(wordList))
     * Space Complexity: O(M*N)
     */
    class Solution2 {
        public int ladderLength(String beginWord, String endWord, List<String> wordList) {
            int wordLen = beginWord.length();
            boolean hasEndWord = false;
            Map<String, List<String>> adjacentWordPatterns = new HashMap<>();
            for (String word : wordList) {
                if (word.equals(endWord)) {
                    hasEndWord = true;
                }
                for (int i = 0; i < wordLen; i++) {
                    String pattern = getAdjacentPattern(word, i);
                    adjacentWordPatterns.computeIfAbsent(pattern, v -> new ArrayList<>()).add(word);
                }
            }
            // If end word is not included in the dictionary
            if (!hasEndWord) {
                return 0;
            }

            // Compute adjacent patterns for begin word as well
            boolean isStartWordReachable = false;
            for (int i = 0; i < wordLen; i++) {
                List<String> patterns;
                if ((patterns = adjacentWordPatterns.get(getAdjacentPattern(beginWord, i))) != null) {
                    patterns.add(beginWord);
                    isStartWordReachable = true;
                }
            }
            // If start word is unreachable
            if (!isStartWordReachable) {
                return 0;
            }

            int ladderLength = 1;
            LinkedList<String> stack = new LinkedList<>();
            Set<String> visitedWords = new HashSet<>();
            stack.push(endWord);
            visitedWords.add(endWord);

            // Bottom-up BFS
            while (!stack.isEmpty()) {
                int stackSize = stack.size();
                for (int i = 0; i < stackSize; i++) {
                    String word = stack.poll();
                    if (word.equals(beginWord)) {
                        return ladderLength;
                    }
                    for (int j = 0; j < wordLen; j++) {
                        String pattern = getAdjacentPattern(word, j);
                        for (String adjacentWord : adjacentWordPatterns.get(pattern)) {
                            if (!visitedWords.contains(adjacentWord)) {
                                stack.offer(adjacentWord);
                                visitedWords.add(adjacentWord);
                            }
                        }
                    }
                }
                ladderLength += 1;
            }
            return 0;
        }
        private String getAdjacentPattern(String word, int wildCardIndex) {
            return word.substring(0, wildCardIndex) + "*" + word.substring(wildCardIndex + 1);
        }
    }

    /**
     * Solution 3: Bidirectional BFS
     * Time Complexity: O(M*N) (M denotes len(word), N denotes len(wordList))
     * Space Complexity: O(M*N)
     */
    class Solution3 {
        public int ladderLength(String beginWord, String endWord, List<String> wordList) {
            int wordLen = beginWord.length();
            boolean hasEndWord = false;
            Map<String, List<String>> adjacentWordPatterns = new HashMap<>();
            for (String word : wordList) {
                if (word.equals(endWord)) {
                    hasEndWord = true;
                }
                for (int i = 0; i < wordLen; i++) {
                    String pattern = getAdjacentPattern(word, i);
                    adjacentWordPatterns.computeIfAbsent(pattern, v -> new ArrayList<>()).add(word);
                }
            }
            // If end word is not included in the dictionary
            if (!hasEndWord) {
                return 0;
            }

            // Compute adjacent patterns for begin word as well
            boolean isStartWordReachable = false;
            for (int i = 0; i < wordLen; i++) {
                String pattern = getAdjacentPattern(beginWord, i);
                List<String> matchedWords;
                if ((matchedWords = adjacentWordPatterns.get(pattern)) != null) {
                    matchedWords.add(beginWord);
                    isStartWordReachable = true;
                } else {
                    matchedWords = new ArrayList<>();
                    matchedWords.add(beginWord);
                    adjacentWordPatterns.put(pattern, matchedWords);
                }
            }
            // If start word is unreachable
            if (!isStartWordReachable) {
                return 0;
            }

            LinkedList<String> topDownStack = new LinkedList<>();
            LinkedList<String> bottomUpStack = new LinkedList<>();
            topDownStack.push(beginWord);
            bottomUpStack.push(endWord);

            HashMap<String, Integer> topDownVisited = new HashMap<>();
            HashMap<String, Integer> bottomUpVisited = new HashMap<>();
            topDownVisited.put(beginWord, 1);
            bottomUpVisited.put(endWord, 1);

            int topDownLadderLength = 1;
            int bottomUpLadderLength = 1;

            while (!topDownStack.isEmpty() || !bottomUpStack.isEmpty()) {
                // Bottom-up BFS
                int bottomUpResult = proceedToNextLevel(adjacentWordPatterns, bottomUpStack,
                        bottomUpVisited, topDownVisited, bottomUpLadderLength++, wordLen);
                if (bottomUpResult != -1) {
                    return bottomUpResult;
                }
                // Top-down BFS
                int topDownResult = proceedToNextLevel(adjacentWordPatterns, topDownStack,
                        topDownVisited, bottomUpVisited, topDownLadderLength++, wordLen);
                if (topDownResult != -1) {
                    return topDownResult;
                }
            }
            return 0;
        }
        private int proceedToNextLevel(Map<String, List<String>> adjacentWordPatterns, LinkedList<String> currStack,
                                       Map<String, Integer> currVisited, Map<String, Integer> otherVisited,
                                       int currLadderLength, int wordLen) {
            int currStackSize = currStack.size();
            for (int i = 0; i < currStackSize; i++) {
                String word = currStack.poll();
                for (int j = 0; j < wordLen; j++) {
                    List<String> adjacentWords = adjacentWordPatterns.get(getAdjacentPattern(word, j));
                    for (String adjacentWord : adjacentWords) {
                        Integer otherLadderLength;
                        if ((otherLadderLength = otherVisited.get(adjacentWord)) != null) {
                              return otherLadderLength + currLadderLength;
                        }
                        if (!currVisited.containsKey(adjacentWord)) {
                            currVisited.put(adjacentWord, currLadderLength + 1);
                            currStack.offer(adjacentWord);
                        }
                    }
                }
            }
            return -1;
        }
        private String getAdjacentPattern(String word, int wildCardIndex) {
            return word.substring(0, wildCardIndex) + "*" + word.substring(wildCardIndex + 1);
        }
    }

    /**
     * Solution 4: Bidirectional BFS with better adjacency-lookup strategy
     * Time Complexity: O(M*N) (M denotes len(word), N denotes len(wordList))
     * Space Complexity: O(N)
     */
    class Solution4 {
        public int ladderLength(String beginWord, String endWord, List<String> wordList) {
            if (wordList.size() == 0 || beginWord.equals(endWord)) {
                return 0;
            }
            Set<String> wordSet = new HashSet<>(wordList);
            if (!wordSet.contains(endWord)) {
                // If end word is not included in the dictionary
                return 0;
            }
            Set<String> beginSet = new HashSet<>(wordList.size()), endSet = new HashSet<>(wordList.size());
            beginSet.add(beginWord);
            endSet.add(endWord);
            wordSet.remove(beginWord);
            wordSet.remove(endWord);

            int ladderLength = 2;
            while (!beginSet.isEmpty() && !endSet.isEmpty()) {
                Set<String> nextLevelSet = new HashSet<>();
                if (beginSet.size() > endSet.size()) {
                    // In each iteration, we only proceed on either one of
                    // the two sided movements (top-down or bottom-up),
                    // and we always choose to proceed on the end with less
                    // remaining options
                    Set<String> tmpSet = endSet;
                    endSet = beginSet;
                    beginSet = tmpSet;
                }
                for (String word : beginSet) {
                    char[] chars = word.toCharArray();
                    for (int i = 0; i < chars.length; i++) {
                        char originalChar = chars[i];
                        for (char replaceChar = 'a'; replaceChar < 'z'; replaceChar++) {
                            chars[i] = replaceChar;
                            String generatedStr = new String(chars);
                            if (endSet.contains(generatedStr)) {
                                return ladderLength;
                            }
                            if (wordSet.contains(generatedStr)) {
                                nextLevelSet.add(generatedStr);
                                // Avoid re-visit
                                wordSet.remove(generatedStr);
                            }
                            chars[i] = originalChar;
                        }
                    }
                }
                ladderLength += 1;
                beginSet = nextLevelSet;
            }
            return 0;
        }
    }
    public static void main(String[] args) {
        List<String> wordList = Arrays.asList("hot","dot","dog","lot","log","cog");
        Printer.printNum(new WordLadder().new Solution1().ladderLength("hit", "cog", wordList));
        Printer.printNum(new WordLadder().new Solution2().ladderLength("hit", "cog", wordList));
        Printer.printNum(new WordLadder().new Solution3().ladderLength("hit", "cog", wordList));
        Printer.printNum(new WordLadder().new Solution4().ladderLength("hit", "cog", wordList));

        wordList = Arrays.asList("hot","dot","dog","lot","log");
        Printer.printNum(new WordLadder().new Solution1().ladderLength("hit", "cog", wordList));
        Printer.printNum(new WordLadder().new Solution2().ladderLength("hit", "cog", wordList));
        Printer.printNum(new WordLadder().new Solution3().ladderLength("hit", "cog", wordList));
    }
}
