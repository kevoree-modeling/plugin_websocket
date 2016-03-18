package org.kevoree.modeling.plugin.test;

/**
 * Created by ludovicmouline on 09/02/16.
 */
public class TestLRU {
    /*private class ToInsert {
        private long[] _keys;

        private String[] _value;

        ToInsert(int value) {
            _keys= new long[]{value,value,value};
            _value = new String[]{value + ""};
        }
    }

    @Test
    public void basicTestLRU() {
        final int CACHE_SIZE = 3;
        LRUKeys lru = new LRUKeys(CACHE_SIZE);


        ToInsert[] dataSet = {new ToInsert(0),new ToInsert(1),new ToInsert(2),new ToInsert(3), new ToInsert(4), new ToInsert(7)};
        int[] accessOrder = {5,0,1,2,0,3,0,4,2,3,0,3,2,1,2,0,1,5,0,1};

        for (int anAccessOrder : accessOrder) {
            lru.put(dataSet[anAccessOrder]._keys, dataSet[anAccessOrder]._value);
            System.out.println(lru);
        }

        String[] getResult;
       for(int i = 0; i<CACHE_SIZE; i++) {
           int index = accessOrder.length - 1 - i;
           System.out.println("Index=" + index);
           getResult = lru.get(dataSet[accessOrder[index]]._keys);
//           Assert.assertEquals(1,getResult.length);
           Assert.assertEquals(dataSet[accessOrder[index]]._value[0],getResult[0]);
       }


    }*/
}
