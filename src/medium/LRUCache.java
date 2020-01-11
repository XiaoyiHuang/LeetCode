package medium;

import helper.Printer;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * Design and implement a data structure for Least Recently Used (LRU) cache.
 * It should support the following operations: get and put.
 *  get(key) - Get the value (will always be positive) of the key if the key exists in the cache, otherwise return -1.
 *  put(key, value) - Set or insert the value if the key is not already present. When the cache reached its capacity,
 *      it should invalidate the least recently used item before inserting a new item.
 * The cache is initialized with a positive capacity.
 *
 * Follow up:
 * Could you do both operations in O(1) time complexity?
 *
 * Result: Accepted (14ms)
 */
public class LRUCache {

    // Inner LRU Node
    class LRUNode {
        int key;
        int value;
        LRUNode prev;
        LRUNode next;

        LRUNode(int key, int value) {
            this.key = key;
            this.value = value;
        }

        void remove() {
            prev.next = next;
            next.prev = prev;
        }

        void addToHead(LRUNode head) {
            head.next.prev = this;
            this.next = head.next;
            head.next = this;
            this.prev = head;
        }
    }

    private int capacity;
    private Map<Integer, LRUNode> cache;
    private LRUNode useHistoryHead;
    private LRUNode useHistoryTail;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>(capacity);
        this.useHistoryHead = new LRUNode(-1, -1);
        this.useHistoryTail = new LRUNode(-2, -2);
        this.useHistoryHead.next = this.useHistoryTail;
        this.useHistoryTail.prev = this.useHistoryHead;
    }

    public int get(int key) {
        LRUNode node;
        if ((node = cache.get(key)) != null) {
            node.remove();
            node.addToHead(this.useHistoryHead);
            return node.value;
        } else {
            return -1;
        }
    }

    public void put(int key, int value) {
        if (capacity <= 0) {
            return;
        }
        LRUNode node;
        if ((node = cache.get(key)) != null) {
            node.remove();
        }
        else if (cache.size() >= capacity) {
            LRUNode evictedNode = this.useHistoryTail.prev;
            int evictedKey = evictedNode.key;
            cache.remove(evictedKey);
            evictedNode.remove();
        }
        node = new LRUNode(key, value);
        node.addToHead(this.useHistoryHead);
        cache.put(key, node);
    }

    public static void main(String[] args) {
        LRUCache lruCache = new LRUCache(2);
        Printer.printNum(lruCache.get(2));
        lruCache.put(2, 6);
        Printer.printNum(lruCache.get(1));
        lruCache.put(1, 5);
        lruCache.put(1, 2);
        Printer.printNum(lruCache.get(1));
        Printer.printNum(lruCache.get(2));
    }
}
