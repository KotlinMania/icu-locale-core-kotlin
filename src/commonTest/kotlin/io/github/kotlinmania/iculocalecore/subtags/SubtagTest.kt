// port-lint: tests subtags/subtag.rs
package io.github.kotlinmania.iculocalecore.subtags

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SubtagTest {
    @Test
    fun parseValidSubtag() {
        val subtag = Subtag.parse("foo").getOrThrow()
        assertEquals("foo", subtag.asString())
        assertEquals(3, subtag.length())
    }

    @Test
    fun parseNormalizesCase() {
        val subtag = Subtag.parse("Foo").getOrThrow()
        assertEquals("foo", subtag.asString())
    }

    @Test
    fun parseTooShort() {
        assertTrue(Subtag.parse("f").isFailure)
    }

    @Test
    fun parseTooLong() {
        assertTrue(Subtag.parse("toolongggg").isFailure)
    }

    @Test
    fun parseAlphanumeric() {
        val subtag = Subtag.parse("foo12").getOrThrow()
        assertEquals("foo12", subtag.asString())
    }

    @Test
    fun normalizingEq() {
        val subtag = Subtag.parse("foo").getOrThrow()
        assertTrue(subtag.normalizingEq("FOO"))
        assertTrue(subtag.normalizingEq("foo"))
    }
}
