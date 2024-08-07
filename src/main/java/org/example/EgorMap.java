package org.example;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class EgorMap<K, V> implements Map<K, V> {
    private static final int CAPACITY = 128;
    private static final float LOAD_FACTOR = 0.75F;
    private final Lock lock = new ReentrantLock();
    // Переменные экземпляра для хранения емкости, коэффициента загрузки, размера и таблицы
    private int capacity;
    private float loadFactor = LOAD_FACTOR;
    private int size = 0;
    private List<Entry<K, V>>[] table;

    public EgorMap() {
        this(CAPACITY);
    }

    public EgorMap(int capacity) {
        this.capacity = capacity;
        // Инициализация таблицы с заданной емкостью
        this.table = new List[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new ArrayList<>();
        }
    }

    public  EgorMap(int capacity, float loadFactor){
        this(capacity);
        this.loadFactor = loadFactor;
    }

    @Override
    public int size() {
        return size;
    }
    public int getCapacity() { return capacity; }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        for (List<Entry<K, V>> bucket : table) {
            for (Entry<K, V> entry : bucket) {
                if (entry.value.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        int index = getHashCode(key);
        for (Entry<K, V> entry : table[index]) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        lock.lock();
        try {
            if (size > capacity * loadFactor) {
                resize();
            }
            V oldValue = remove(key);
            int index = getHashCode(key);
            table[index].add(new Entry<>(key, value));
            size++;
            return oldValue;
        } finally {
            lock.unlock();
        }
    }

    // Увеличивает размер таблицы в два раза
    private void resize() {
        lock.lock();
        try {
            capacity *= 2;
            List<Entry<K, V>>[] newTable = new List[capacity];
            for (int i = 0; i < capacity; i++) {
                newTable[i] = new ArrayList<>();
            }
            for (List<Entry<K, V>> bucket : table) {
                for (Entry<K, V> entry : bucket) {
                    int index = getHashCode(entry.key);
                    newTable[index].add(entry);
                }
            }
            table = newTable;
        } finally {
            lock.unlock();
        }
    }


    @Override
    public V remove(Object key) {
        lock.lock();
        try {
            int index = getHashCode(key);
            Iterator<Entry<K, V>> iterator = table[index].iterator();
            while (iterator.hasNext()) {
                Entry<K, V> entry = iterator.next();
                if (entry.key.equals(key)) {
                    iterator.remove();
                    size--;
                    return entry.value;
                }
            }
            return null;
        } finally {
            lock.unlock();
        }
    }

    // Копирует все пары ключ-значение из заданной мапы в текущую мапу
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    // Очищает мапу
    @Override
    public void clear() {
        lock.lock();
        try {
            for (int i = 0; i < capacity; i++) {
                table[i].clear();
            }
            size = 0;
        } finally {
            lock.unlock();
        }
    }

    // Возвращает множество всех ключей в мапе
    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        for (List<Entry<K, V>> bucket : table) {
            for (Entry<K, V> entry : bucket) {
                keys.add(entry.key);
            }
        }
        return keys;
    }

    // Возвращает коллекцию всех значений в мапе
    @Override
    public Collection<V> values() {
        List<V> values = new ArrayList<>();
        for (List<Entry<K, V>> bucket : table) {
            for (Entry<K, V> entry : bucket) {
                values.add(entry.value);
            }
        }
        return values;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> entries = new HashSet<>();
        for (List<Entry<K, V>> bucket : table) {
            entries.addAll(bucket);
        }
        return entries;
    }

    private int getHashCode(Object key) {
        if (key == null) {
            return 0;
        }
        return (key.hashCode() & 0x7FFFFFFF) % capacity;
    }

    // Внутренний класс для представления пары ключ-значение
    private static class Entry<K, V> implements Map.Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Entry<?, ?> entry = (Entry<?, ?>) o;
            return Objects.equals(key, entry.key) && Objects.equals(value, entry.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
    }
}
