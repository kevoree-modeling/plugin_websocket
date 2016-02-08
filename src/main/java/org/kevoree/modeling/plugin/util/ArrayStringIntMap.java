package org.kevoree.modeling.plugin.util;

/* From an original idea https://code.google.com/p/jdbm2/
 *
 * A very specific HashMap to store positive int
 * So, ArrayStringIntMap.get(<key>) == -1 means "no value for key <key>"
 */

import org.kevoree.modeling.memory.chunk.KStringMapCallBack;
import org.kevoree.modeling.util.PrimitiveHelper;

/**
 * @native ts
 * constructor(initalCapacity: number, loadFactor : number) { }
 * public clear():void { for(var p in this){ if(this.hasOwnProperty(p)){ delete this[p];} } }
 * public get(key:string):V { return this[key]; }
 * public put(key:string, pval : V):V { var previousVal = this[key];this[key] = pval;return previousVal;}
 * public contains(key:string):boolean { return this.hasOwnProperty(key);}
 * public remove(key:string):V { var tmp = this[key]; delete this[key]; return tmp; }
 * public size():number { return Object.keys(this).length; }
 * public each(callback: (p : string, p1 : V) => void): void { for(var p in this){ if(this.hasOwnProperty(p)){ callback(<string>p,this[p]); } } }
 */
public class ArrayStringIntMap implements KStringIntMap {

    protected int elementCount;

    protected Entry[] elementData;

    private int elementDataSize;

    protected int threshold;

    private final int initalCapacity;

    private final float loadFactor;

    /**
     * @ignore ts
     */
    static final class Entry {
        Entry next;
        String key;
        int value;

        Entry(String theKey, int theValue) {
            this.key = theKey;
            this.value = theValue;
        }
    }

    @SuppressWarnings("unchecked")
    Entry[] newElementArray(int s) {
        return new Entry[s];
    }

    public ArrayStringIntMap(int p_initalCapacity, float p_loadFactor) {
        this.initalCapacity = p_initalCapacity;
        this.loadFactor = p_loadFactor;
        elementCount = 0;
        elementData = newElementArray(initalCapacity);
        elementDataSize = initalCapacity;
        computeMaxSize();
    }

    public void clear() {
        if (elementCount > 0) {
            elementCount = 0;
            this.elementData = newElementArray(initalCapacity);
            this.elementDataSize = initalCapacity;
        }
    }

    private void computeMaxSize() {
        threshold = (int) (elementDataSize * loadFactor);
    }

    @Override
    public boolean contains(String key) {
        if (elementDataSize == 0 || key == null) {
            return false;
        }
        int hash = key.hashCode();
        int index = (hash & 0x7FFFFFFF) % elementDataSize;
        Entry m = findNonNullKeyEntry(key, index);
        return m != null;
    }

    @Override
    public int get(String key) {
        if (key == null || elementDataSize == 0) {
            return -1;
        }
        Entry m;
        int hash = key.hashCode();
        int index = (hash & 0x7FFFFFFF) % elementDataSize;
        m = findNonNullKeyEntry(key, index);
        if (m != null) {
            return m.value;
        }
        return -1;
    }

    final Entry findNonNullKeyEntry(String key, int index) {
        Entry m = elementData[index];
        while (m != null) {
            if (PrimitiveHelper.equals(key, m.key)) {
                return m;
            }
            m = m.next;
        }
        return null;
    }

    @Override
    public void each(KStringMapCallBack callback) {
        for (int i = 0; i < elementDataSize; i++) {
            if (elementData[i] != null) {
                Entry current = elementData[i];
                callback.on(elementData[i].key, elementData[i].value);
                while (current.next != null) {
                    current = current.next;
                    callback.on(current.key, current.value);
                }
            }
        }
    }

    @Override
    public void put(String key, int value) {
        if (key == null) {
            return;
        }
        Entry entry = null;
        int index = -1;
        int hash = key.hashCode();
        if (elementDataSize != 0) {
            index = (hash & 0x7FFFFFFF) % elementDataSize;
            entry = findNonNullKeyEntry(key, index);
        }
        if (entry == null) {
            if (++elementCount > threshold) {
                rehash();
                index = (hash & 0x7FFFFFFF) % elementDataSize;
            }
            entry = createHashedEntry(key, index);
        }
        entry.value = value;
    }

    Entry createHashedEntry(String key, int index) {
        Entry entry = new Entry(key, -1);
        entry.next = elementData[index];
        elementData[index] = entry;
        return entry;
    }

    void rehashCapacity(int capacity) {
        int length = (capacity == 0 ? 1 : capacity << 1);
        Entry[] newData = newElementArray(length);
        for (int i = 0; i < elementDataSize; i++) {
            Entry entry = elementData[i];
            while (entry != null) {
                int hash = entry.key.hashCode();
                int index = (hash & 0x7FFFFFFF) % length;
                Entry next = entry.next;
                entry.next = newData[index];
                newData[index] = entry;
                entry = next;
            }
        }
        elementData = newData;
        elementDataSize = length;
        computeMaxSize();
    }

    void rehash() {
        rehashCapacity(elementDataSize);
    }

    @Override
    public void remove(String key) {
        if (elementDataSize == 0) {
            return;
        }
        Entry entry;
        Entry last = null;
        int hash = key.hashCode();
        int index = (hash & 0x7FFFFFFF) % elementDataSize;
        entry = elementData[index];
        while (entry != null && !(PrimitiveHelper.equals(key, entry.key))) {
            last = entry;
            entry = entry.next;
        }
        if (entry == null) {
            return;
        }
        if (last == null) {
            elementData[index] = entry.next;
        } else {
            last.next = entry.next;
        }
        elementCount--;
    }

    public int size() {
        return elementCount;
    }

}



