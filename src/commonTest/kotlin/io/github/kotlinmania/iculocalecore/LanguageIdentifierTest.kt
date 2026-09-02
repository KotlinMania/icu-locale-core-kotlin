// port-lint: tests langid.rs
package io.github.kotlinmania.iculocalecore

import io.github.kotlinmania.iculocalecore.subtags.Language
import io.github.kotlinmania.iculocalecore.subtags.Region
import io.github.kotlinmania.iculocalecore.subtags.Script
import io.github.kotlinmania.iculocalecore.subtags.Variant
import io.github.kotlinmania.iculocalecore.subtags.Variants
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

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

    @Test
    fun testLangidSubtagLanguage() {
        var lang: Language = Language.parse("en").getOrThrow()
        assertEquals("en", lang.asString())

        lang = Language.UNKNOWN
        assertTrue(lang.isUnknown())
        assertEquals("und", lang.asString())
    }

    @Test
    fun testLangidSubtagRegion() {
        val region: Region = Region.parse("en").getOrThrow()
        assertEquals("EN", region.asString())
    }

    @Test
    fun testLangidSubtagScript() {
        val script: Script = Script.parse("Latn").getOrThrow()
        assertEquals("Latn", script.asString())
    }

    @Test
    fun testLangidSubtagVariant() {
        val variant: Variant = Variant.parse("macos").getOrThrow()
        assertEquals("macos", variant.asString())
    }

    @Test
    fun testLangidSubtagVariants() {
        val variant: Variant = Variant.parse("macos").getOrThrow()
        val variants = Variants.fromVecUnchecked(listOf(variant))
        assertEquals(variant, variants.first())
        assertEquals(1, variants.size())
    }

    @Test
    fun testLangidNormalizingEqStr() {
        val parsed = LanguageIdentifier.parse("eN-latn-Us-macos").getOrThrow()
        assertTrue(parsed.normalizingEq(parsed.toString()))
        assertTrue(parsed.normalizingEq("en-Latn-US-macos"))
        assertTrue(parsed.normalizingEq("EN-LATN-us-MACOS"))

        val lang = LanguageIdentifier.parse("en").getOrThrow()
        assertFalse(lang.normalizingEq("en-US"))
    }

    @Test
    fun testLangidStrictCmp() {
        val lang = LanguageIdentifier.parse("en-US").getOrThrow()
        assertEquals(0, lang.strictCmp("en-US".encodeToByteArray()))
        assertTrue(lang.strictCmp("en-GB".encodeToByteArray()) > 0)
        assertTrue(lang.strictCmp("en-US-macos".encodeToByteArray()) < 0)
    }

    @Test
    fun testWriteable() {
        assertEquals("und", LanguageIdentifier.UNKNOWN.toString())
        assertEquals("und-001", LanguageIdentifier.parse("und-001").getOrThrow().toString())
        assertEquals("und-Mymr", LanguageIdentifier.parse("und-Mymr").getOrThrow().toString())
        assertEquals("my-Mymr-MM", LanguageIdentifier.parse("my-Mymr-MM").getOrThrow().toString())
        assertEquals("my-Mymr-MM-posix", LanguageIdentifier.parse("my-Mymr-MM-posix").getOrThrow().toString())
        assertEquals("zh-macos-posix", LanguageIdentifier.parse("zh-macos-posix").getOrThrow().toString())
    }
}
