package org.kevoree.modeling.plugin.cache.lru;

/**
 * Created by ludovicmouline on 11/02/16.
 */
public interface PageReplacement {
  /*  void put(long universe, long time, long uuid, String value);
    String get(long universe, long time, long uuid);*/

    void init(int capacity);

    void valueHasBeenAccessed(int indexValue);

    int getAndUpdateHead();
}
