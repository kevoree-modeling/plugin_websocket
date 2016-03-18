package org.kevoree.modeling.plugin.cache;

/**
 * Created by ludovicmouline on 11/02/16.
 */
public interface Cache {
    int DEFAULT_CAPACITY = 10000;

    String[] get(long[] keys);
    void put(long[] keys, String[] values);
}
