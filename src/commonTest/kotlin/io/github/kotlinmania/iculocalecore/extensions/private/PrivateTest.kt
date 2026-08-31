// port-lint: tests icu_locale_core/src/extensions/private/mod.rs, extensions/other/mod.rs
package io.github.kotlinmania.iculocalecore.extensions.private

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PrivateSubtagTest {
    @Test
    fun parseValidSubtag() {
        assertEquals("foo", PrivateSubtag.parse("Foo").getOrThrow().asString())
        assertEquals("foo12", PrivateSubtag.parse("foo12").getOrThrow().asString())
        assertEquals("f", PrivateSubtag.parse("f").getOrThrow().asString())
    }

    @Test
    fun parseInvalidSubtag() {
        assertTrue(PrivateSubtag.parse("toolooong").isFailure)
        assertTrue(PrivateSubtag.parse("").isFailure)
        assertTrue(PrivateSubtag.parse("foo-bar").isFailure)
    }
}

class PrivateTest {
    @Test
    fun parsePrivateExtension() {
        val p = Private.parse("x-foo-bar-l-baz").getOrThrow()
        assertEquals("x-foo-bar-l-baz", p.toString())
        assertFalse(p.isEmpty())
    }

    @Test
    fun parseJustX() {
        assertTrue(Private.parse("x").isFailure)
    }

    @Test
    fun emptyPrivate() {
        val p = Private.empty()
        assertTrue(p.isEmpty())
        assertEquals("", p.toString())
    }

    @Test
    fun contains() {
        val p = Private.parse("x-foo-bar").getOrThrow()
        assertTrue(p.contains(PrivateSubtag.parse("foo").getOrThrow()))
        assertTrue(p.contains(PrivateSubtag.parse("bar").getOrThrow()))
        assertFalse(p.contains(PrivateSubtag.parse("baz").getOrThrow()))
    }
}
