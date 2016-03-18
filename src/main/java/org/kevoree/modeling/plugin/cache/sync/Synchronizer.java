package org.kevoree.modeling.plugin.cache.sync;

/**
 * Created by ludovicmouline on 11/02/16.
 */
public interface Synchronizer {
    void updatePriority(long uuid);

    void forceSynchronize(long universe, long time, long uuid, String value);
}
