// port-lint: tests parser/mod.rs
package io.github.kotlinmania.iculocalecore.parser

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SubtagIteratorTest {
    @Test
    fun emptyInput() {
        val iter = SubtagIterator("".encodeToByteArray())
        assertEquals("", iter.next()?.decodeToString())
        assertNull(iter.next())
    }

    @Test
    fun singleSubtag() {
        val iter = SubtagIterator("en".encodeToByteArray())
        assertEquals(listOf("en"), iter.toList())
    }

    @Test
    fun multipleSubtags() {
        val iter = SubtagIterator("de-at-u-ca-foobar".encodeToByteArray())
        assertEquals(listOf("de", "at", "u", "ca", "foobar"), iter.toList())
    }

    @Test
    fun trailingSeparator() {
        val iter = SubtagIterator("en-".encodeToByteArray())
        assertEquals(listOf("en", ""), iter.toList())
    }

    @Test
    fun peekDoesNotAdvance() {
        val iter = SubtagIterator("de-at".encodeToByteArray())
        assertEquals("de", iter.peek()?.decodeToString())
        assertEquals("de", iter.peek()?.decodeToString())
        assertEquals("de", iter.next()?.decodeToString())
        assertEquals("at", iter.peek()?.decodeToString())
    }
}
