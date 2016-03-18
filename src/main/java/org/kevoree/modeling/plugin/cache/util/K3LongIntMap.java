package org.kevoree.modeling.plugin.cache.util;

/**
 * Created by ludovicmouline on 08/02/16.
 */
public interface K3LongIntMap {
    boolean contains(long universe, long time, long uuid);

    int get(long universe, long time, long uuid);

    void put(long universe, long time, long uuid, int value);

    void remove(long universe, long time, long uuid);


    //use for testing
    void each(K3LongMapCallBack callback);

    int size();

    void clear();


}
