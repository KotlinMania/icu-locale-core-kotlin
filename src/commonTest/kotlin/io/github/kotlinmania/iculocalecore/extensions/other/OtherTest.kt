// port-lint: tests extensions/other/mod.rs
package io.github.kotlinmania.iculocalecore.extensions.other

import io.github.kotlinmania.iculocalecore.subtags.Subtag
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OtherTest {
    @Test
    fun parseOtherExtension() {
        val o = Other.parse("o-foo-bar").getOrThrow()
        assertEquals("o-foo-bar", o.toString())
        assertEquals('o', o.getExt())
    }

    @Test
    fun parseJustO() {
        assertTrue(Other.parse("o").isFailure)
    }

    @Test
    fun fromVecUnchecked() {
        val subtag1 = Subtag.parse("foo").getOrThrow()
        val subtag2 = Subtag.parse("bar").getOrThrow()
        val other = Other.fromVecUnchecked('a'.code.toByte(), listOf(subtag1, subtag2))
        assertEquals("a-foo-bar", other.toString())
    }

    @Test
    fun getExtStr() {
        val other = Other.fromVecUnchecked('z'.code.toByte(), listOf(Subtag.parse("foo").getOrThrow()))
        assertEquals("z", other.getExtStr())
    }

    @Test
    fun emptyOther() {
        val other = Other.fromVecUnchecked('a'.code.toByte(), emptyList())
        assertTrue(other.isEmpty())
    }
}
