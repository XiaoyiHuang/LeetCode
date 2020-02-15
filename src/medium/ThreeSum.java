package medium;

import helper.Printer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * [15] 3Sum
 *
 * Description:
 * Given an array nums of n integers, are there elements a, b, c in nums
 * such that a + b + c = 0? Find all unique triplets in the array
 * which gives the sum of zero.
 *
 * Note:
 * The solution set must not contain duplicate triplets.
 *
 * Example:
 * Given array nums = [-1, 0, 1, 2, -1, -4], a solution set is:
 * [
 *   [-1, 0, 1],
 *   [-1, -1, 2]
 * ]
 *
 * Result:
 * Solution 1: Accepted (32ms)
 * Solution 2: Accepted (11ms)
 */
public class ThreeSum {
    /**
     * Solution 1: Two pointers
     * Time Complexity: O(N^2)
     * Space Complexity: O(1)
     */
    public class Solution1 {
        public List<List<Integer>> threeSum(int[] nums) {
            List<List<Integer>> triplets = new ArrayList<>();
            Arrays.sort(nums);
            for (int i = 0; i < nums.length; i++) {
                triplets.addAll(twoSum(nums, i));
                while (i < nums.length - 1 && nums[i] == nums[i + 1]) {
                    i++;
                }
            }
            return triplets;
        }
        private List<List<Integer>> twoSum(int[] nums, int thirdNumIdx) {
            List<List<Integer>> pairs = new ArrayList<>();
            int targetSum = -nums[thirdNumIdx];
            int lPtr = thirdNumIdx + 1, rPtr = nums.length - 1;

            while (lPtr < rPtr) {
                int sum = nums[lPtr] + nums[rPtr];
                if (sum == targetSum) {
                    pairs.add(Arrays.asList(nums[thirdNumIdx], nums[lPtr++], nums[rPtr--]));
                    while (lPtr < rPtr && nums[lPtr - 1] == nums[lPtr]) {
                        lPtr += 1;
                    }
                    while (rPtr > lPtr && nums[rPtr + 1] == nums[rPtr]) {
                        rPtr -= 1;
                    }
                } else if (sum > targetSum) {
                    rPtr -= 1;
                } else {
                    lPtr += 1;
                }
            }
            return pairs;
        }
    }

    /**
     * Solution 2: Trimming + Map
     * Time Complexity: O(N^N)
     * Space Complexity: O(N)
     */
    public class Solution2 {
        public List<List<Integer>> threeSum(int[] nums) {
            List<List<Integer>> triplets = new ArrayList<>();
            int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE, negCount = 0, posCount = 0, zeroCount = 0;

            for (int num : nums) {
                if (num < min) {
                    min = num;
                }
                if (num > max) {
                    max = num;
                }
                if (num < 0) {
                    negCount += 1;
                }
                if (num > 0) {
                    posCount += 1;
                }
                if (num == 0) {
                    zeroCount += 1;
                }
            }
            if (zeroCount >= 3) {
                triplets.add(Arrays.asList(0, 0, 0));
            }
            if (negCount == 0 || posCount == 0) {
                return triplets;
            }
            if ((min << 1) + max > 0) {
                // Calculate the max possibly-used number
                max = -(min << 1);
            }
            if (min + (max << 1) < 0) {
                // Calculate the min possibly-used number
                min = -(max << 1);
            }

            // Format a size-optimized bucket
            int[] map = new int[max - min + 1];
            int[] negativeNums = new int[negCount];
            int[] positiveNums = new int[posCount];
            negCount = 0;
            posCount = 0;

            for (int num : nums) {
                if (num >= min && num <= max) {
                    // Re-map numbers within range to the bucket
                    if (map[num - min]++ == 0) {
                        // Only record numbers and count frequency once
                        if (num < 0) {
                            negativeNums[negCount++] = num;
                        }
                        if (num > 0) {
                            positiveNums[posCount++] = num;
                        }
                    }
                }
            }

            Arrays.sort(positiveNums, 0, posCount);
            Arrays.sort(negativeNums, 0, negCount);

            int basePosIdx = 0;
            for (int negIdx = negCount - 1; negIdx >= 0; negIdx--) {
                // Search negative numbers in reverse order
                int negNum = negativeNums[negIdx];
                // Skip positive numbers that satisfy: 2 * pos + neg < 0
                while (positiveNums[basePosIdx] < ((-negNum) >>> 1)) {
                    basePosIdx += 1;
                }
                for (int posIdx = basePosIdx; posIdx < posCount; posIdx++) {
                    int posNum = positiveNums[posIdx];
                    int thirdNum = -(negNum + posNum);

                    // To avoid deriving the duplicate triplets, the third number is
                    // limited to be between negNum and posNum, that is, if the third
                    // number is negative, it should have a smaller absolute value
                    // than {negNum}, otherwise, it should be smaller than {posNum}.
                    // Therefore, the third number can only be one of the numbers we
                    // have already visited
                    if (thirdNum >= negNum && thirdNum <= posNum) {
                        if (thirdNum == negNum) {
                            if (map[thirdNum - min] > 1) {
                                triplets.add(Arrays.asList(thirdNum, thirdNum, posNum));
                            }
                        } else if (thirdNum == posNum) {
                            if (map[thirdNum - min] > 1) {
                                triplets.add(Arrays.asList(negNum, thirdNum, thirdNum));
                            }
                        } else {
                            if (map[thirdNum - min] > 0) {
                                if (-negNum > posNum) {
                                    triplets.add(Arrays.asList(negNum, Math.min(thirdNum, posNum),
                                            Math.max(thirdNum, posNum)));
                                } else if (-negNum < posNum) {
                                    triplets.add(Arrays.asList(Math.min(negNum, thirdNum),
                                            Math.max(negNum, thirdNum), posNum));
                                } else {
                                    triplets.add(Arrays.asList(negNum, 0, posNum));
                                }
                            }
                        }
                    } else if (thirdNum < negNum) {
                        // In case the positive number has a larger absolute value
                        // than that of the negative number, we break out of the
                        // search to prevent generating duplicate triplets
                        break;
                    }
                }
            }
            return triplets;
        }
    }

    public static void main(String[] args) {
        int[] nums = new int[]{-1, 0, 1, 2, -1, -4};
        List<List<Integer>> results = new ThreeSum().new Solution1().threeSum(nums);
        Printer.printList(results);
        results = new ThreeSum().new Solution2().threeSum(nums);
        Printer.printList(results);
    }
}
