package org.kevoree.modeling.plugin.cache.lru.impl;

import org.kevoree.modeling.plugin.cache.lru.PageReplacement;

/**
 * Created by ludovicmouline on 11/02/16.
 */
public class LRUImpl implements PageReplacement {
    private int[] _next;
    private int[] _prev;
    private int _head;


    public void init(int capacity) {
        _next = new int[capacity];
        _prev = new int[capacity];

        for(int i = 0; i< capacity; i++) {
            _next[i] = (i + 1) % capacity;
            _prev[i] = ((i - 1) % capacity + capacity) % capacity;
        }

    }

    @Override
    public void valueHasBeenAccessed(int indexValue) {
        if(indexValue == _head) {
           _head = _next[_head]; //just continue "the process"
        } else {
             //move the access value just behind the head, i.e _prev[head]
             //thus now it's the "first recently used"
            //The head does not change because it still the "last recently used"
            _next[_prev[indexValue]] = _next[indexValue];
            _prev[_next[indexValue]] = _prev[indexValue];
            _next[indexValue] = _head;
            _prev[indexValue] = _prev[_head];
            _next[_prev[_head]] = indexValue;
            _prev[_head] = indexValue;
        }
    }

    @Override
    public int getAndUpdateHead() {
        int oldHead = _head;
        _head = _next[_head];
        return oldHead;
    }


}
