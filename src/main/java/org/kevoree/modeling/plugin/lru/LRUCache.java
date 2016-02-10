package org.kevoree.modeling.plugin.lru;

/**
 * Created by ludovicmouline on 09/02/16.
 */
public interface LRUCache {
    void put(long[] keys, String[] values);

    String[] get(long[] keys);
}
