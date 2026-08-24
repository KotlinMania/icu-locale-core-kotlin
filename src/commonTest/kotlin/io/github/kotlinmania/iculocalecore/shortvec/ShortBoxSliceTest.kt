// port-lint: tests shortvec/mod.rs
package io.github.kotlinmania.iculocalecore.shortvec

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ShortBoxSliceTest {
    @Test
    fun emptySlice() {
        val slice = ShortBoxSlice.empty<Int>()
        assertEquals(0, slice.size())
        assertTrue(slice.isEmpty())
        assertNull(slice.single())
    }

    @Test
    fun singleElement() {
        val slice = ShortBoxSlice.of(42)
        assertEquals(1, slice.size())
        assertEquals(42, slice.single())
        assertFalse(slice.isEmpty())
    }

    @Test
    fun multipleElements() {
        val slice = ShortBoxSlice.fromList(listOf(1, 2, 3))
        assertEquals(3, slice.size())
        assertEquals(1, slice[0])
        assertEquals(2, slice[1])
        assertEquals(3, slice[2])
        assertNull(slice.single())
    }

    @Test
    fun push() {
        val slice = ShortBoxSlice.empty<Int>()
        val withOne = slice.push(100)
        assertEquals(1, withOne.size())
        val withTwo = withOne.push(200)
        assertEquals(2, withTwo.size())
        assertEquals(100, withTwo[0])
        assertEquals(200, withTwo[1])
    }

    @Test
    fun retain() {
        val slice = ShortBoxSlice.fromList(listOf(1, 2, 3, 4, 5))
        val evens = slice.retain { it % 2 == 0 }
        assertEquals(listOf(2, 4), evens.toList())
    }

    @Test
    fun clear() {
        val slice = ShortBoxSlice.fromList(listOf(1, 2, 3))
        val cleared = slice.clear()
        assertTrue(cleared.isEmpty())
    }

    @Test
    fun iterator() {
        val slice = ShortBoxSlice.fromList(listOf(10, 20, 30))
        val result = slice.toList()
        assertEquals(listOf(10, 20, 30), result)
    }
}
