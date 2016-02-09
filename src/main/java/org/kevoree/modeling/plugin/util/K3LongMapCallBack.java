package org.kevoree.modeling.plugin.util;

/**
 * Created by ludovicmouline on 09/02/16.
 */
public interface K3LongMapCallBack {
        void on(long universe, long time, long uuid, int value);
}
