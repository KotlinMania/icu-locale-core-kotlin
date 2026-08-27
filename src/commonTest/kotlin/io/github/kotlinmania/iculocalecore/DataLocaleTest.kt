// port-lint: tests icu_locale_core/src/data.rs
package io.github.kotlinmania.iculocalecore

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DataLocaleTest {
    @Test
    fun defaultIsUnknown() {
        assertTrue(DataLocale.DEFAULT.isUnknown())
        assertEquals("und", DataLocale.DEFAULT.toString())
    }

    @Test
    fun parseSimple() {
        val dl = DataLocale.parse("und").getOrThrow()
        assertTrue(dl.isUnknown())
    }

    @Test
    fun parseWithLanguage() {
        val dl = DataLocale.parse("en").getOrThrow()
        assertEquals("en", dl.language.asString())
        assertFalse(dl.isUnknown())
    }

    @Test
    fun parseWithRegion() {
        val dl = DataLocale.parse("en-US").getOrThrow()
        assertEquals("en", dl.language.asString())
        assertEquals("US", dl.region?.asString())
    }

    @Test
    fun fromLanguageIdentifier() {
        val li = LanguageIdentifier.parse("en-Latn-US").getOrThrow()
        val dl = li.toDataLocale()
        assertEquals("en", dl.language.asString())
        assertEquals("Latn", dl.script?.asString())
        assertEquals("US", dl.region?.asString())
    }

    @Test
    fun toLanguageIdentifier() {
        val dl = DataLocale.parse("en-US").getOrThrow()
        val li = dl.toLanguageIdentifier()
        assertEquals("en-US", li.toString())
    }

    @Test
    fun totalCmp() {
        val a = DataLocale.parse("en-US").getOrThrow()
        val b = DataLocale.parse("en-GB").getOrThrow()
        assertTrue(a.totalCmp(b) > 0)
        assertTrue(b.totalCmp(a) < 0)
        assertEquals(0, a.totalCmp(a))
    }

    @Test
    fun strictCmp() {
        val dl = DataLocale.parse("en-US").getOrThrow()
        assertEquals(0, dl.strictCmp("en-US".encodeToByteArray()))
    }
}
