package medium;

import helper.Printer;

import java.util.HashSet;
import java.util.Set;

/**
 * [433] Minimum Genetic Mutation
 *
 * Description:
 * A gene string can be represented by an 8-character long string,
 * with choices from "A", "C", "G", "T".
 *
 * Suppose we need to investigate about a mutation (mutation from
 * "start" to "end"), where ONE mutation is defined as ONE single
 * character changed in the gene string.
 *
 * For example, "AACCGGTT" -> "AACCGGTA" is 1 mutation.
 *
 * Also, there is a given gene "bank", which records all the valid
 * gene mutations. A gene must be in the bank to make it a valid
 * gene string.
 *
 * Now, given 3 things - start, end, bank, your task is to determine
 * what is the minimum number of mutations needed to mutate from "
 * start" to "end". If there is no such a mutation, return -1.
 *
 * Note:
 * Starting point is assumed to be valid, so it might not be included
 * in the bank.
 * If multiple mutations are needed, all mutations during in the
 * sequence must be valid.
 * You may assume start and end string is not the same.
 *
 * Example 1:
 * start: "AACCGGTT"
 * end:   "AACCGGTA"
 * bank: ["AACCGGTA"]
 * return: 1
 *
 * Example 2:
 * start: "AACCGGTT"
 * end:   "AAACGGTA"
 * bank: ["AACCGGTA", "AACCGCTA", "AAACGGTA"]
 * return: 2
 *
 * Example 3:
 * start: "AAAAACCC"
 * end:   "AACCCCCC"
 * bank: ["AAAACCCC", "AAACCCCC", "AACCCCCC"]
 * return: 3
 *
 * Result: Accepted (1ms)
 */
public class MinimumGeneticMutation {
    public int minMutation(String start, String end, String[] bank) {
        char[] geneDict = new char[]{'A', 'C', 'G', 'T'};
        Set<String> bankSet = new HashSet<>();
        for (String bankWord : bank) {
            bankSet.add(bankWord);
        }
        if (bank.length == 0 || !bankSet.contains(end)) {
            return -1;
        }
        bankSet.remove(start);
        bankSet.remove(end);

        Set<String> startSet = new HashSet<>();
        Set<String> endSet = new HashSet<>();
        startSet.add(start);
        endSet.add(end);

        int mutateTimes = 0;

        while (!startSet.isEmpty() && !endSet.isEmpty()) {
            Set<String> nextStates = new HashSet<>();
            if (startSet.size() > endSet.size()) {
                Set<String> tmpSet = startSet;
                startSet = endSet;
                endSet = tmpSet;
            }
            for (String word : startSet) {
                char[] wordChars = word.toCharArray();
                for (int i = 0; i < wordChars.length; i++) {
                    char originalChar = wordChars[i];
                    for (int j = 0; j < geneDict.length; j++) {
                        wordChars[i] = geneDict[j];
                        String generatedWord = new String(wordChars);
                        if (endSet.contains(generatedWord)) {
                            return mutateTimes + 1;
                        }
                        if (bankSet.contains(generatedWord)) {
                            bankSet.remove(generatedWord);
                            nextStates.add(generatedWord);
                        }
                    }
                    wordChars[i] = originalChar;
                }
            }
            mutateTimes += 1;
            startSet = nextStates;
        }
        return -1;
    }
    public static void main(String[] args) {
        Printer.printNum(new MinimumGeneticMutation().minMutation("AACCGGTT", "AAACGGTA",
                new String[]{"AACCGGTA", "AACCGCTA", "AAACGGTA"}));
        Printer.printNum(new MinimumGeneticMutation().minMutation("AACCGGTT", "AACCGGTA",
                new String[]{"AACCGGTA"}));
        Printer.printNum(new MinimumGeneticMutation().minMutation("AAAACCCC", "CCCCCCCC",
                new String[]{"AAAACCCA","AAACCCCA","AACCCCCA","AACCCCCC","ACCCCCCC","CCCCCCCC",
                        "AAACCCCC","AACCCCCC"}));
    }
}
