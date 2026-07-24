package io.github.kotlinmania.iculocalecore

import io.github.kotlinmania.iculocalecore.subtags.Language
import io.github.kotlinmania.iculocalecore.subtags.Region
import io.github.kotlinmania.iculocalecore.subtags.Script
import io.github.kotlinmania.iculocalecore.subtags.Variant
import io.github.kotlinmania.iculocalecore.subtags.Variants
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlin.test.assertNull

class LanguageIdentifierTest {
    @Test
    fun parseSimple() {
        val li = LanguageIdentifier.parse("en").getOrThrow()
        assertEquals("en", li.language.asString())
        assertNull(li.script)
        assertNull(li.region)
        assertTrue(li.variants.isEmpty())
    }

    @Test
    fun parseWithRegion() {
        val li = LanguageIdentifier.parse("en-US").getOrThrow()
        assertEquals("en", li.language.asString())
        assertEquals("US", li.region?.asString())
    }

    @Test
    fun parseWithScript() {
        val li = LanguageIdentifier.parse("en-Latn").getOrThrow()
        assertEquals("en", li.language.asString())
        assertEquals("Latn", li.script?.asString())
    }

    @Test
    fun parseFull() {
        val li = LanguageIdentifier.parse("en-Latn-US-macos").getOrThrow()
        assertEquals("en", li.language.asString())
        assertEquals("Latn", li.script?.asString())
        assertEquals("US", li.region?.asString())
        assertEquals("macos", li.variants.first()?.asString())
    }

    @Test
    fun parseNormalizesCase() {
        val li = LanguageIdentifier.parse("eN-latn-Us").getOrThrow()
        assertEquals("en", li.language.asString())
        assertEquals("Latn", li.script?.asString())
        assertEquals("US", li.region?.asString())
    }

    @Test
    fun parseUnknown() {
        val li = LanguageIdentifier.parse("und").getOrThrow()
        assertTrue(li.isUnknown())
        assertEquals("und", li.toString())
    }

    @Test
    fun parseUnknownWithIdentifier() {
        assertEquals(LanguageIdentifier.UNKNOWN, LanguageIdentifier.parse("und").getOrThrow())
    }

    @Test
    fun parseInvalidLanguage() {
        assertTrue(LanguageIdentifier.parse("419").isFailure)
    }

    @Test
    fun parseInvalidSubtag() {
        assertTrue(LanguageIdentifier.parse("en-#@2X").isFailure)
    }

    @Test
    fun parseWithVariants() {
        val li = LanguageIdentifier.parse("en-US-macos-posix").getOrThrow()
        assertEquals(2, li.variants.size())
        assertEquals("macos", li.variants.first()?.asString())
        assertEquals("macos-posix", li.variants.toString())
    }

    @Test
    fun parseDuplicateVariantFails() {
        assertTrue(LanguageIdentifier.parse("en-macos-macos").isFailure)
    }

    @Test
    fun toStringRoundTrip() {
        val inputs = listOf("en", "en-US", "en-Latn", "en-Latn-US", "und", "und-Mymr", "my-Mymr-MM")
        for (input in inputs) {
            val li = LanguageIdentifier.parse(input).getOrThrow()
            assertEquals(input, li.toString())
        }
    }

    @Test
    fun toStringWithVariants() {
        val li = LanguageIdentifier.parse("zh-macos-posix").getOrThrow()
        assertEquals("zh-macos-posix", li.toString())
    }

    @Test
    fun tryFromLocaleBytes() {
        val li = LanguageIdentifier.tryFromLocaleBytes("en-US".encodeToByteArray()).getOrThrow()
        assertEquals("en", li.language.asString())
        assertEquals("US", li.region?.asString())
    }

    @Test
    fun fromLanguage() {
        val lang = Language.parse("en").getOrThrow()
        val li = lang.toLanguageIdentifier()
        assertEquals("en", li.language.asString())
        assertTrue(li.variants.isEmpty())
    }

    @Test
    fun languageIdentifierOf() {
        val lang = Language.parse("en").getOrThrow()
        val script = Script.parse("Latn").getOrThrow()
        val region = Region.parse("US").getOrThrow()
        val li = languageIdentifierOf(lang, script, region)
        assertEquals("en-Latn-US", li.toString())
    }

    @Test
    fun totalCmp() {
        val a = LanguageIdentifier.parse("en-US").getOrThrow()
        val b = LanguageIdentifier.parse("en-GB").getOrThrow()
        assertTrue(a.totalCmp(b) > 0)
        assertTrue(b.totalCmp(a) < 0)
        assertEquals(0, a.totalCmp(a))
    }

    @Test
    fun strictCmp() {
        val li = LanguageIdentifier.parse("en-US").getOrThrow()
        assertEquals(0, li.strictCmp("en-US".encodeToByteArray()))
        assertTrue(li.strictCmp("en-GB".encodeToByteArray()) > 0)
    }

    @Test
    fun isUnknown() {
        assertTrue(LanguageIdentifier.UNKNOWN.isUnknown())
        assertFalse(LanguageIdentifier.parse("en").getOrThrow().isUnknown())
    }
}