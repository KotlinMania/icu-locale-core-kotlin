// port-lint: tests icu_locale_core/src/shortvec/litemap.rs
package io.github.kotlinmania.iculocalecore.shortvec

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LiteMapTest {
    @Test
    fun emptyMap() {
        val map = LiteMap<String, Int>()
        assertTrue(map.isEmpty())
        assertEquals(0, map.size())
        assertNull(map.get("key"))
    }

    @Test
    fun insertAndGet() {
        val map = LiteMap<String, Int>()
        assertNull(map.insert("a", 1))
        assertEquals(1, map.size())
        assertEquals(1, map.get("a"))
    }

    @Test
    fun insertReplaces() {
        val map = LiteMap<String, Int>()
        map.insert("a", 1)
        assertEquals(1, map.insert("a", 2))
        assertEquals(2, map.get("a"))
        assertEquals(1, map.size())
    }

    @Test
    fun tryInsert() {
        val map = LiteMap<String, Int>()
        assertTrue(map.tryInsert("a", 1))
        assertFalse(map.tryInsert("a", 2))
        assertEquals(1, map.get("a"))
    }

    @Test
    fun maintainsSortedOrder() {
        val map = LiteMap<Int, String>()
        map.insert(3, "three")
        map.insert(1, "one")
        map.insert(2, "two")
        val keys = map.iter().map { it.first }
        assertEquals(listOf(1, 2, 3), keys)
    }

    @Test
    fun containsKey() {
        val map = LiteMap<String, Int>()
        map.insert("a", 1)
        assertTrue(map.containsKey("a"))
        assertFalse(map.containsKey("b"))
    }

    @Test
    fun remove() {
        val map = LiteMap<String, Int>()
        map.insert("a", 1)
        map.insert("b", 2)
        assertEquals(1, map.remove("a"))
        assertEquals(1, map.size())
        assertFalse(map.containsKey("a"))
    }

    @Test
    fun retain() {
        val map = LiteMap<Int, String>()
        map.insert(1, "one")
        map.insert(2, "two")
        map.insert(3, "three")
        map.retain { k, _ -> k >= 2 }
        assertEquals(2, map.size())
        assertFalse(map.containsKey(1))
        assertTrue(map.containsKey(2))
        assertTrue(map.containsKey(3))
    }

    @Test
    fun fromIterable() {
        val map = LiteMap.fromIterable(listOf(3 to "three", 1 to "one", 2 to "two"))
        assertEquals(listOf(1, 2, 3), map.iter().map { it.first })
    }
}
