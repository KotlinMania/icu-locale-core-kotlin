// port-lint: tests icu_locale_core/src/icu_locale_core/src/extensions/unicode/mod.rs, icu_locale_core/src/extensions/unicode/key.rs, icu_locale_core/src/extensions/unicode/attribute.rs, icu_locale_core/src/extensions/unicode/keywords.rs, icu_locale_core/src/extensions/unicode/value.rs
package io.github.kotlinmania.iculocalecore.extensions.unicode

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class KeyTest {
    @Test
    fun parseValidKeys() {
        assertEquals("ca", Key.parse("ca").getOrThrow().asString())
        assertEquals("8a", Key.parse("8a").getOrThrow().asString())
        assertEquals("hc", Key.parse("HC").getOrThrow().asString())
    }

    @Test
    fun parseInvalidKeys() {
        assertTrue(Key.parse("a").isFailure)
        assertTrue(Key.parse("a8").isFailure)
        assertTrue(Key.parse("abc").isFailure)
        assertTrue(Key.parse("#@").isFailure)
    }
}

class AttributeTest {
    @Test
    fun parseValidAttributes() {
        assertEquals("foo12", Attribute.parse("foo12").getOrThrow().asString())
        assertEquals("buddhist", Attribute.parse("buddhist").getOrThrow().asString())
        assertEquals("foobar", Attribute.parse("FooBar").getOrThrow().asString())
    }

    @Test
    fun parseInvalidAttributes() {
        assertTrue(Attribute.parse("no").isFailure)
        assertTrue(Attribute.parse("toolooong").isFailure)
        assertTrue(Attribute.parse("foo-bar").isFailure)
    }
}

class ValueTest {
    @Test
    fun parseSingleSubtag() {
        val v = Value.parse("gregory").getOrThrow()
        assertEquals("gregory", v.toString())
        assertEquals(1, v.subtagCount())
        assertFalse(v.isEmpty())
    }

    @Test
    fun parseMultiSubtag() {
        val v = Value.parse("islamic-civil").getOrThrow()
        assertEquals("islamic-civil", v.toString())
        assertEquals(2, v.subtagCount())
    }

    @Test
    fun trueValueIsEmptyString() {
        val v = Value.parse("true").getOrThrow()
        assertEquals("", v.toString())
        assertTrue(v.isEmpty())
    }

    @Test
    fun defaultValueIsEmpty() {
        val v = Value.newEmpty()
        assertEquals("", v.toString())
        assertTrue(v.isEmpty())
    }
}

class KeywordsTest {
    @Test
    fun parseSimpleKeywords() {
        val kw = Keywords.parse("hc-h12").getOrThrow()
        assertEquals("hc-h12", kw.toString())
        assertEquals(Value.parse("h12").getOrThrow(), kw.get(Key.parse("hc").getOrThrow()))
    }

    @Test
    fun parseMultipleKeywords() {
        val kw = Keywords.parse("hc-h23-kc").getOrThrow()
        assertEquals("hc-h23-kc", kw.toString())
        assertTrue(kw.containsKey(Key.parse("hc").getOrThrow()))
        assertFalse(kw.containsKey(Key.parse("ca").getOrThrow()))
    }

    @Test
    fun emptyKeywords() {
        val kw = Keywords.empty()
        assertTrue(kw.isEmpty())
        assertEquals("", kw.toString())
    }

    @Test
    fun setAndGet() {
        val kw = Keywords.empty()
        val key = Key.parse("ca").getOrThrow()
        val val1 = Value.parse("buddhist").getOrThrow()
        assertNull(kw.set(key, val1))
        assertEquals(val1, kw.get(key))
        val val2 = Value.parse("gregory").getOrThrow()
        assertEquals(val1, kw.set(key, val2))
        assertEquals(val2, kw.get(key))
    }

    @Test
    fun remove() {
        val kw = Keywords.parse("hc-h12").getOrThrow()
        val key = Key.parse("hc").getOrThrow()
        assertEquals(Value.parse("h12").getOrThrow(), kw.remove(key))
        assertTrue(kw.isEmpty())
    }
}

class AttributesTest {
    @Test
    fun parseAttributes() {
        val attrs = Attributes.parse("foo-bar").getOrThrow()
        assertEquals("bar-foo", attrs.toString())
    }

    @Test
    fun emptyAttributes() {
        val attrs = Attributes.empty()
        assertTrue(attrs.isEmpty())
        assertEquals("", attrs.toString())
    }

    @Test
    fun contains() {
        val attrs = Attributes.parse("foobar-testing").getOrThrow()
        assertTrue(attrs.contains(Attribute.parse("foobar").getOrThrow()))
        assertTrue(attrs.contains(Attribute.parse("testing").getOrThrow()))
        assertFalse(attrs.contains(Attribute.parse("missing").getOrThrow()))
    }
}

class UnicodeTest {
    @Test
    fun parseUnicodeExtension() {
        val u = Unicode.parse("u-foo-hc-h12").getOrThrow()
        assertEquals("u-foo-hc-h12", u.toString())
        assertFalse(u.isEmpty())
    }

    @Test
    fun parseUnicodeWithKeywordsOnly() {
        val u = Unicode.parse("u-ca-buddhist-hc-h12").getOrThrow()
        assertEquals("u-ca-buddhist-hc-h12", u.toString())
        assertFalse(u.isEmpty())
    }

    @Test
    fun emptyUnicode() {
        val u = Unicode.empty()
        assertTrue(u.isEmpty())
        assertEquals("", u.toString())
    }

    @Test
    fun parseJustU() {
        assertTrue(Unicode.parse("u").isFailure)
    }
}

class SubdivisionIdTest {
    @Test
    fun parseAlphaRegion() {
        val si = SubdivisionId.tryFromStr("gbzzzz").getOrThrow()
        assertEquals("gbzzzz", si.toString())
        assertEquals("GB", si.region.asString())
        assertEquals("zzzz", si.suffix.asString())
    }

    @Test
    fun parseInvalid() {
        assertTrue(SubdivisionId.tryFromStr("").isFailure)
        assertTrue(SubdivisionId.tryFromStr("gb").isFailure)
        assertTrue(SubdivisionId.tryFromStr("o").isFailure)
    }
}
