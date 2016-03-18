package org.kevoree.modeling.plugin.old.lru.impl;

import org.kevoree.modeling.KConfig;
import org.kevoree.modeling.plugin.old.lru.LRUCache;

/**
 * Created by ludovicmouline on 10/02/16.
 */
public class LRUCache_OneMap implements LRUCache{
    //Mapstructure
    private int _nbElement;
    private int _threshold;
    private final int _initialCapacity;
    private final float _loadFactor;
    private Entry[] _data;

    //PageReplacement structure
    private Entry _head; //the least recently used



    static final class Entry {
        //Map Structure
        Entry _next;

        //key
        long _universe;
        long _time;
        long _uuid;

        //value
        String _value;

        //Double linked list for PageReplacement
        Entry _lruPrev;//younger value
        Entry _lruNext;//older value

        public Entry(long universe, long time, long uuid, String value) {
            _universe = universe;
            _time = time;
            _uuid = uuid;
            _value = value;
        }

        boolean asSameKey(long universe, long time, long uuid){
            return _universe == universe && _time == time && _uuid == uuid;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == null || !(obj instanceof Entry)) {
                return false;
            }
            Entry e = (Entry) obj;
            return asSameKey(e._universe,e._time,e._uuid);
        }
    }

    public LRUCache_OneMap(int initialCapacity) {
        _initialCapacity = initialCapacity;
        _loadFactor = KConfig.CACHE_LOAD_FACTOR;

        _data = new Entry[_initialCapacity];
        updateThreshold();
    }

    @Override
    public void put(long[] keys, String[] values) {
        int nbChunk = keys.length / 3;
        long universe, time, uuid;
        for(int i=0;i<nbChunk;i++){
            universe = keys[i];
            time = keys[i + 1];
            uuid = keys[i + 2];
            if(!contains(universe,time,uuid)) {
                Entry lruNext = _head._lruNext;
//                removeValue(_head);
                addValue(universe,time,uuid,values[i]);
                _head = lruNext;
            } else if(_head.asSameKey(universe,time,uuid)) {
                _head = _head._lruNext;
            } else {

            }
        }

    }

    private void removeValue(Entry toRemove) {
        if(_nbElement == 0 || toRemove == null){
            return;
        }
        int index = index(toRemove._universe,toRemove._time,toRemove._uuid);
        Entry entry = _data[index];
        Entry last = null;
        while(entry != null && !entry.equals(toRemove)) {
            last = entry;
            entry = entry._next;
        }

        if(last == null){
            _data[index] = toRemove._next;
        } else {
            last._next = toRemove._next;
        }
        _nbElement--;
    }

    @Override
    public String[] get(long[] keys) {
        return new String[0];
    }

    private void updateThreshold() {
        _threshold = (int) (_data.length * _loadFactor);
    }

  private boolean contains(long universe, long time, long uuid) {
//      int index = index(universe, time, uuid);
      Entry search = findEntryAt(universe,time,uuid/*,index*/);
      return search != null;
  }

    private int index(long universe, long time, long uuid) {
        return hash(universe,time,uuid) % _data.length;
    }

    private Entry findEntryAt(long universe, long time, long uuid/*, int index*/) {
        int index = index(universe,time,uuid);
        Entry current = _data[index];
        while(current != null) {
            if(current._universe == universe && current._time == time && current._uuid == uuid) {
                return current;
            }
            current = current._next;
        }
        return null;
    }

    private void addValue(long universe, long time, long uuid, String value){
        Entry toAdd = null;
        if(_nbElement != 0) {
           toAdd = findEntryAt(universe,time,uuid);
        }
        if(toAdd == null){
            if(++_nbElement > _threshold) {
                rehash();
            }
            toAdd = new Entry(universe,time,uuid,value);
            int index = index(universe,time,uuid);
            toAdd._next = _data[index];
            _data[index] = toAdd;
        } else {
            toAdd._value = value;
            if(_head.equals(toAdd)) {

            } else {

            }
        }
    }

    private void rehash() {
        int length = (_data.length == 0 ? 1 : _data.length * 2);
        Entry[] newData = new Entry[length];
        for (Entry elmtData : _data) {
            Entry entry = elmtData;
            while (entry != null) {
                int index =index(entry._universe,entry._time,entry._uuid);
                Entry next = entry._next;
                entry._next = newData[index];
                newData[index] = entry;
                entry = next;
            }
        }
        _data = newData;
        updateThreshold();
    }


    private int hash(long universe, long time, long uuid) {
        return (int)(universe ^ time ^ uuid) & 0x7fffffff;
    }
}
