package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MapTest {
    private EgorMap<String, String> map;

    @BeforeEach
    public void setUp() {
        map = new EgorMap<>();
    }

    @Test
    public void testPutAndGet() {
        map.put("key1", "value1");
        assertEquals("value1", map.get("key1"));
    }

    @Test
    public void testRemove() {
        map.put("key1", "value1");
        assertEquals("value1", map.remove("key1"));
        assertNull(map.get("key1"));
    }

    @Test
    public void testSize() {
        map.put("key1", "value1");
        map.put("key2", "value2");
        assertEquals(2, map.size());
    }

    @Test
    public void testContainsKey() {
        map.put("key1", "value1");
        assertTrue(map.containsKey("key1"));
        assertFalse(map.containsKey("key2"));
    }

    @Test
    public void testContainsValue() {
        map.put("key1", "value1");
        assertTrue(map.containsValue("value1"));
        assertFalse(map.containsValue("value2"));
    }

    @Test
    public void testClear() {
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.clear();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    @Test
    public void testKeySet() {
        map.put("key1", "value1");
        map.put("key2", "value2");
        assertEquals(2, map.keySet().size());
        assertTrue(map.keySet().contains("key1"));
        assertTrue(map.keySet().contains("key2"));
    }

    @Test
    public void testValues() {
        map.put("key1", "value1");
        map.put("key2", "value2");
        assertEquals(2, map.values().size());
        assertTrue(map.values().contains("value1"));
        assertTrue(map.values().contains("value2"));
    }

    @Test
    public void testEntrySet() {
        map.put("key1", "value1");
        map.put("key2", "value2");
        assertEquals(2, map.entrySet().size());
        assertTrue(map.entrySet().stream().anyMatch(e -> e.getKey().equals("key1") && e.getValue().equals("value1")));
        assertTrue(map.entrySet().stream().anyMatch(e -> e.getKey().equals("key2") && e.getValue().equals("value2")));
    }
}
