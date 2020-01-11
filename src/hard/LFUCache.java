package hard;

import helper.Printer;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Description:
 * Design and implement a data structure for Least Frequently Used (LFU) cache. It should support the following
 * operations: get and put.
 *  get(key) - Get the value (will always be positive) of the key if the key exists in the cache, otherwise return -1.
 *  put(key, value) - Set or insert the value if the key is not already present. When the cache reaches its capacity,
 *      it should invalidate the least frequently used item before inserting a new item. For the purpose of this problem,
 *      when there is a tie (i.e., two or more keys that have the same frequency), the least recently used key would be
 *      evicted.
 * Note that the number of times an item is used is the number of calls to the get and put functions for that item since
 * it was inserted. This number is set to zero when the item is removed.
 *
 * Follow up:
 * Could you do both operations in O(1) time complexity?
 *
 * Result: Accepted (19ms)
 */
public class LFUCache {
    class LFUNode {
        int key;
        int value;
        int freq;

        LFUNode prev;
        LFUNode next;

        LFUNode(int key, int value) {
            this.key = key;
            this.value = value;
            this.freq = 1;
        }

        void addToHead(LFUNode head) {
            head.next.prev = this;
            this.next = head.next;
            head.next = this;
            this.prev = head;
        }

        void incrFreq() {
            this.freq += 1;
        }

        void setValue(int value) {
            this.value = value;
        }
    }

    /** Maximum capacity */
    int capacity;

    /** K: Usage frequency | V: Head & Tail of LRU list (Namely a doubly linked-list) */
    TreeMap<Integer, LFUNode[]> freq;

    /** K: Cache key | V: Corresponding LFUNode */
    Map<Integer, LFUNode> cache;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.freq = new TreeMap<>();
        this.cache = new HashMap<>();
    }

    public int get(int key) {
        LFUNode node;
        if ((node = cache.get(key)) != null) {
            removeNode(node);
            node.incrFreq();
            node.addToHead(getOrInitFreqList(node.freq)[0]);
            return node.value;
        } else {
            return -1;
        }
    }

    public void put(int key, int value) {
        if (capacity <= 0) {
            return;
        }
        LFUNode node;
        if ((node = cache.get(key)) != null) {
            removeNode(node);
            node.incrFreq();
            node.addToHead(getOrInitFreqList(node.freq)[0]);
            node.setValue(value);
        } else {
            if (cache.size() >= capacity) {
                LFUNode evictedNode = freq.firstEntry().getValue()[1].prev;
                int evictedKey = evictedNode.key;
                removeNode(evictedNode);
                cache.remove(evictedKey);
            }
            node = new LFUNode(key, value);
            node.addToHead(getOrInitFreqList(node.freq)[0]);
            cache.put(key, node);
        }
    }

    private LFUNode[] getOrInitFreqList(int currFreq) {
        LFUNode[] lfuNodes;
        if ((lfuNodes = freq.get(currFreq)) == null) {
            LFUNode head = new LFUNode(-1, -1);
            LFUNode tail = new LFUNode(-2, -2);
            head.next = tail;
            tail.prev = head;
            lfuNodes = new LFUNode[]{head, tail};
            freq.put(currFreq, lfuNodes);
        }
        return lfuNodes;
    }

    private void removeNode(LFUNode node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;

        LFUNode headOfFreq;
        if ((headOfFreq = freq.get(node.freq)[0]) == null || headOfFreq.next == null || headOfFreq.next.value < 0) {
            freq.remove(node.freq);
        }
    }

    public static void main(String[] args) {
        LFUCache cache = new LFUCache(2);
        cache.put(1, 1);
        cache.put(2, 2);
        Printer.printNum(cache.get(1));
        cache.put(2, 3);
        cache.put(3, 3);
        Printer.printNum(cache.get(1));
    }
}
