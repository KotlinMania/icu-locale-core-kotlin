// port-lint: tests locale.rs, icu_locale_core/src/extensions/mod.rs, icu_locale_core/src/parser/locale.rs
package io.github.kotlinmania.iculocalecore

import io.github.kotlinmania.iculocalecore.extensions.unicode.Key
import io.github.kotlinmania.iculocalecore.extensions.unicode.Value
import io.github.kotlinmania.iculocalecore.subtags.Language
import io.github.kotlinmania.iculocalecore.subtags.Region
import io.github.kotlinmania.iculocalecore.subtags.Script
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LocaleTest {
    @Test
    fun parseSimpleLocale() {
        val loc = Locale.parse("en-US").getOrThrow()
        assertEquals(Language.parse("en").getOrThrow(), loc.id.language)
        assertEquals(null, loc.id.script)
        assertEquals(Region.parse("US").getOrThrow(), loc.id.region)
        assertTrue(loc.extensions.isEmpty())
    }

    @Test
    fun parseLocaleWithUnicodeExtension() {
        val loc = Locale.parse("en-US-u-ca-buddhist").getOrThrow()
        assertEquals("en-US-u-ca-buddhist", loc.toString())
        val key = Key.parse("ca").getOrThrow()
        assertEquals(
            Value.parse("buddhist").getOrThrow(),
            loc.extensions.unicode.keywords
                .get(key),
        )
    }

    @Test
    fun parseLocaleWithExtensions() {
        val loc = Locale.parse("en-US-u-ca-buddhist-t-en-us-h0-hybrid-x-foo").getOrThrow()
        assertEquals("en-US-t-en-us-h0-hybrid-u-ca-buddhist-x-foo", loc.toString())
        assertFalse(loc.extensions.isEmpty())
    }

    @Test
    fun parseUndWithExtensions() {
        val loc = Locale.parse("und-a-foo-t-foo-u-foo-w-foo-z-foo-x-foo").getOrThrow()
        assertEquals("und-a-foo-t-foo-u-foo-w-foo-z-foo-x-foo", loc.toString())
    }

    @Test
    fun unknownLocale() {
        assertEquals("und", Locale.UNKNOWN.toString())
        assertTrue(Locale.UNKNOWN.extensions.isEmpty())
    }

    @Test
    fun parseNormalize() {
        val result = Locale.normalize("pL-latn-pl-U-HC-H12")
        assertEquals("pl-Latn-PL-u-hc-h12", result.getOrThrow())
    }

    @Test
    fun normalizingEq() {
        val loc = Locale.parse("en-US-u-ca-buddhist").getOrThrow()
        assertTrue(loc.normalizingEq("en-US-u-ca-buddhist"))
        assertTrue(loc.normalizingEq("EN-us-U-ca-buddhist"))
        assertFalse(loc.normalizingEq("en-US"))
    }

    @Test
    fun normalizingEqTrailingChars() {
        val loc = Locale.parse("en").getOrThrow()
        assertFalse(loc.normalizingEq("en-US"))
    }

    @Test
    fun conversions() {
        val locale = Locale.UNKNOWN
        val langid = locale.id
        val locale2 = langid.toLocale()
        assertEquals(locale, locale2)
    }

    @Test
    fun parseComplexLocale() {
        val loc = Locale.parse("eN-latn-Us-Valencia-u-hC-H12").getOrThrow()
        assertEquals(Language.parse("en").getOrThrow(), loc.id.language)
        assertEquals(Script.parse("Latn").getOrThrow(), loc.id.script)
        assertEquals(Region.parse("US").getOrThrow(), loc.id.region)
    }

    @Test
    fun duplicatedExtensionFails() {
        assertTrue(Locale.parse("und-u-hc-h12-u-ca-calendar").isFailure)
    }

    @Test
    fun fromLanguage() {
        val lang = Language.parse("en").getOrThrow()
        val loc = lang.toLocale()
        assertEquals("en", loc.toString())
    }

    @Test
    fun fromScript() {
        val script = Script.parse("Latn").getOrThrow()
        val loc = script.toLocale()
        assertEquals("und-Latn", loc.toString())
    }

    @Test
    fun fromRegion() {
        val region = Region.parse("US").getOrThrow()
        val loc = region.toLocale()
        assertEquals("und-US", loc.toString())
    }

    @Test
    fun strictCmp() {
        val loc = Locale.parse("en-US").getOrThrow()
        assertEquals(0, loc.strictCmp("en-US".encodeToByteArray()))
        assertTrue(loc.strictCmp("en-GB".encodeToByteArray()) > 0)
    }

    @Test
    fun totalCmp() {
        val a = Locale.parse("en-US-u-ca-buddhist").getOrThrow()
        val b = Locale.parse("en-US-u-ca-hebrew").getOrThrow()
        assertTrue(a.totalCmp(b) < 0)
        assertTrue(b.totalCmp(a) > 0)
        assertEquals(0, a.totalCmp(a))
    }

    @Test
    fun forEachSubtagStr() {
        val loc = Locale.parse("en-Latn-US-valencia-u-ca-buddhist").getOrThrow()
        val subtags = mutableListOf<String>()
        loc.forEachSubtagStr { subtags.add(it) }
        assertEquals(listOf("en", "Latn", "US", "valencia", "u", "ca", "buddhist"), subtags)
    }

    @Test
    fun normalizeUtf8() {
        val result = Locale.normalizeUtf8("pL-latn-pl-U-HC-H12".encodeToByteArray())
        assertEquals("pl-Latn-PL-u-hc-h12", result.getOrThrow())
    }
}
