import java.util.Iterator;
import java.util.LinkedList;

public class HashMap<K, V> implements Map61BL<K, V> {

    /* TODO: Instance variables here */
    private LinkedList<Entry<K, V>>[] buckets;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private double maxLoadFactor;



    /* TODO: Constructors here */

    public HashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public HashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public HashMap(int initialCapacity, double loadFactor) {
        buckets = (LinkedList<Entry<K, V>>[]) new LinkedList[initialCapacity];
        for (int i = 0; i < initialCapacity; i++) {
            buckets[i] = new LinkedList<>();
        }
        size = 0;
        maxLoadFactor = loadFactor;
    }


    /* TODO: Interface methods here */


    @Override
    public void clear() {
        for (int i = 0; i < buckets.length; i++) {
            buckets[i].clear();
        }
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public V get(K key) {
        int index = Math.floorMod(key.hashCode(), buckets.length);//Map to the bucket
        LinkedList<Entry<K, V>> bucket = buckets[index];//get the corresponding buckets of the keys

        for (Entry<K, V> entry : bucket) {//Return the value
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }


    @Override
    public void put(K key, V value) {
        int index = Math.floorMod(key.hashCode(), buckets.length);//Map to the bucket
        LinkedList<Entry<K, V>> bucket = buckets[index];

        for (Entry<K, V> entry : bucket) {//check the bucket
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }

        bucket.add(new Entry<>(key, value));//update the bucket
        size++;

        if ((double) size / buckets.length > maxLoadFactor) {
            resize(buckets.length * 2);
        }
    }


    @Override
    public V remove(K key) {
        int index = Math.floorMod(key.hashCode(), buckets.length);
        LinkedList<Entry<K, V>> bucket = buckets[index];
        Iterator<Entry<K, V>> iter = bucket.iterator();

        while (iter.hasNext()) {
            Entry<K, V> entry = iter.next();
            if (entry.key.equals(key)) {
                V value = entry.value;
                iter.remove();
                size--;
                return value;
            }
        }
        return null;
    }

    @Override
    public boolean remove(K key, V value) {
        int index = Math.floorMod(key.hashCode(), buckets.length);
        LinkedList<Entry<K, V>> bucket = buckets[index];
        Iterator<Entry<K, V>> iter = bucket.iterator();

        while (iter.hasNext()) {
            Entry<K, V> entry = iter.next();
            if (entry.key.equals(key) && entry.value.equals(value)) {
                iter.remove();
                size--;
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }


    private static class Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public boolean keyEquals(Entry<K, V> other) {
            return key.equals(other.key);
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof Entry) {
                Entry<?, ?> o = (Entry<?, ?>) other;
                return key.equals(o.key) && value.equals(o.value);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return key.hashCode(); // optional: key.hashCode() ^ value.hashCode()
        }
    }

    private void resize(int newCapacity) {
        LinkedList<Entry<K, V>>[] newBuckets = (LinkedList<Entry<K, V>>[]) new LinkedList[newCapacity];
        for (int i = 0; i < newCapacity; i++) {
            newBuckets[i] = new LinkedList<>();
        }

        for (LinkedList<Entry<K, V>> bucket : buckets) {
            for (Entry<K, V> entry : bucket) {
                int index = Math.floorMod(entry.key.hashCode(), newCapacity);
                newBuckets[index].add(entry);
            }
        }

        buckets = newBuckets;
    }

    public int capacity() {
        return buckets.length;
    }


}
