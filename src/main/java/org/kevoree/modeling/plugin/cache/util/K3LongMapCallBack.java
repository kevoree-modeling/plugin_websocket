package org.kevoree.modeling.plugin.cache.util;

/**
 * Created by ludovicmouline on 09/02/16.
 */
public interface K3LongMapCallBack {
        void on(long universe, long time, long uuid, int value);
}
