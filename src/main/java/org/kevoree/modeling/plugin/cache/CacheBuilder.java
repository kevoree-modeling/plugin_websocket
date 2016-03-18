package org.kevoree.modeling.plugin.cache;

import org.kevoree.modeling.plugin.cache.impl.CacheImpl;
import org.kevoree.modeling.plugin.cache.lru.PageReplacement;
import org.kevoree.modeling.plugin.cache.lru.impl.LRUImpl;
import org.kevoree.modeling.plugin.cache.sync.Synchronizer;
import org.kevoree.modeling.plugin.cache.sync.impl.SynchronizerImpl;

/**
 * Created by ludovicmouline on 11/02/16.
 */
public class CacheBuilder {
    private PageReplacement _pageReplacement;
    private Synchronizer _synchroniser;
    private int _capacity;


    private CacheBuilder() {
        _capacity = -1;
    }

    public static CacheBuilder create() {
        return new CacheBuilder();
    }

    public PageReplacement pageReplacementAlgo() {
        if(_pageReplacement == null) {
            _pageReplacement = new LRUImpl();
        }
        return _pageReplacement;
    }

    public Synchronizer synchroniser() {
        if(_synchroniser == null) {
            _synchroniser = new SynchronizerImpl();
        }
        return _synchroniser;
    }

    public int capacity(){
        if(_capacity == -1) {
            _capacity = Cache.DEFAULT_CAPACITY;
        }
        return _capacity;
    }

    public CacheBuilder withPageReplacement(PageReplacement pageReplacement) {
        _pageReplacement = pageReplacement;
        return this;
    }

    public CacheBuilder withSynchronizer(Synchronizer synchronizer){
        _synchroniser = synchronizer;
        return this;
    }

    public CacheBuilder withCapacity(int capacity) {
        _capacity = capacity;
        return this;
    }

    public Cache build() {
        return new CacheImpl(pageReplacementAlgo(),synchroniser(),capacity());
    }
}
