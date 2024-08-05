package org.example;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
public class EgorMap<K, V> implements Map<K, V>{
    private static final int DEFAULT_CAPACITY = 128;
    private static final int DEFAULT_LOAD_FACTOR = 0;
    // Переменные экземпляра для хранения емкости, коэффициента загрузки, размера и таблицы
    private int capacity;
    private float loadFactor = DEFAULT_LOAD_FACTOR;
    private int size;
    private List<Entry<K, V>>[] table;
    private final Lock lock = new ReentrantLock();

    public EgorMap() {
        this(DEFAULT_CAPACITY);
    }

    public EgorMap(int capacity) {
        this.capacity = capacity;
        // Инициализация таблицы с заданной емкостью
        this.table = new List[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new ArrayList<>();
        }
    }

    public EgorMap(int capacity, float loadFactor) {
        this.capacity = capacity;
        this.loadFactor = loadFactor;
        // Инициализация таблицы с заданной емкостью
        this.table = new List[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new ArrayList<>();
        }
    }

    @Override
    public int size() {
        return size;
    }

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
            if (size >= capacity * loadFactor) {
                resize(); // Если достигнут предел загрузки, увеличиваем размер таблицы
            }
            V oldValue = remove(key); // Удаляем существующую пару, если ключ уже есть
            int index = getHashCode(key);
            table[index].add(new Entry<>(key, value)); // Добавляем новую пару
            size++;
            return oldValue; // Возвращаем старое значение, если ключ уже был в мапе
        } finally {
            lock.unlock();
        }
    }

    // Увеличивает размер таблицы в два раза
    private void resize() {
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
    }

    @Override
    public V remove(Object key) {
        lock.lock();
        try {
            int index = getHashCode(key);
            for (Entry<K, V> entry : table[index]) {
                if (entry.key.equals(key)) {
                    table[index].remove(entry);
                    size--;
                    return entry.value;
                }
            }
            return null;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
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
