package org.kevoree.modeling.plugin.cache.impl;

import org.kevoree.modeling.KConfig;
import org.kevoree.modeling.plugin.cache.Cache;
import org.kevoree.modeling.plugin.cache.lru.PageReplacement;
import org.kevoree.modeling.plugin.cache.sync.Synchronizer;
import org.kevoree.modeling.plugin.cache.util.Array3LongIntMap;
import org.kevoree.modeling.plugin.cache.util.K3LongIntMap;

/**
 * Created by ludovicmouline on 11/02/16.
 */
public class CacheImpl implements Cache {
    private PageReplacement _pageReplacement;
    private Synchronizer _synchronizer;

    private String[] _data;

    /**
     * _data[i] : a value
     * _dataKeys[3i] : universe of _data[i]
     * _dataKeys[3i + 1] : time of _data[i]
     * _dataKeys[3i + 2] : uuid of _data[i]
     */
    private long[] _dataKeys;

    K3LongIntMap _indexes;


    public CacheImpl(PageReplacement pageReplacement, Synchronizer synchroniser, int capacity) {
        _pageReplacement = pageReplacement;
        _synchronizer = synchroniser;
        _pageReplacement.init(capacity);

        _data = new String[capacity];
        _dataKeys = new long[3 * capacity];
        _indexes =new Array3LongIntMap(capacity, KConfig.CACHE_LOAD_FACTOR);
    }


    /**
     *
     * @param keys
     *      obligation : keys.lenght % 3 == 0
     *          unless, thes last (one or two) values will be ignored
     *      keys[3i] : universe
     *      keys[3i + 1] : time
     *      keys[3i + 2] : uuid
     * @return
     *   for each triplet, the value of the KChunk, or null if does not exist
     */
    @Override
    public String[] get(long[] keys) {
        int nbKChunks = keys.length / 3;
        String[] result = new String[nbKChunks];
        int indexValue;
        for(int i=0;i<nbKChunks;i++) {
            indexValue = _indexes.get(keys[i],keys[i+1],keys[i+2]);
            if(indexValue == -1) {
                result[i]= null;
            } else {
                result[i] = _data[indexValue];
                _pageReplacement.valueHasBeenAccessed(indexValue);
                _synchronizer.updatePriority(keys[i + 2]);
            }
        }
        return result;
    }

    @Override
    public void put(long[] keys, String[] values) {
        int nbKChunks = keys.length / 3;
        int indexValue;
        for(int i=0;i<nbKChunks;i++) {
            indexValue = _indexes.get(keys[i],keys[i+1],keys[i+2]);
            if(indexValue == -1) { //a new value is inserted
                //ask the page replacement where the new value should be added
                indexValue = _pageReplacement.getAndUpdateHead();

                if(_data[indexValue] != null) {
                    _indexes.remove(indexValue * 3,indexValue * 3 + 1,indexValue * 3 + 2);
                    _synchronizer.forceSynchronize(_dataKeys[i],_dataKeys[i + 1],_dataKeys[i + 2], _data[indexValue]);
                }
                _indexes.put(keys[i],keys[i+1],keys[i+2],indexValue);
                _synchronizer.updatePriority(keys[i+2]);

            } else {//an existing value is modified
                _pageReplacement.valueHasBeenAccessed(indexValue);
                _synchronizer.updatePriority(keys[i+2]);
            }

            //change the value
            _data[indexValue] = values[i];
            _dataKeys[indexValue * 3] = keys[i];
            _dataKeys[indexValue * 3 + 1] = keys[i + 1];
            _dataKeys[indexValue * 3 + 2] = keys[i + 2];
        }
    }
}
