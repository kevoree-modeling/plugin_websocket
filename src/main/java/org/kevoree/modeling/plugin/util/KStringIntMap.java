package org.kevoree.modeling.plugin.util;

import org.kevoree.modeling.memory.chunk.KStringMapCallBack;

/**
 * Created by ludovicmouline on 08/02/16.
 */
public interface KStringIntMap {
    boolean contains(String key);

    int get(String key);

    void put(String key, int value);

    void each(KStringMapCallBack<Integer> callback);

    int size();

    void clear();

    void remove(String key);
}
