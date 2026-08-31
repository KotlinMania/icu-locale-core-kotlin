// port-lint: tests icu_locale_core/src/subtags/variants.rs
package io.github.kotlinmania.iculocalecore.subtags

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class VariantsTest {
    @Test
    fun emptyVariants() {
        val variants = Variants.EMPTY
        assertTrue(variants.isEmpty())
        assertEquals(0, variants.size())
        assertEquals("", variants.toString())
    }

    @Test
    fun fromVariant() {
        val variant = Variant.parse("posix").getOrThrow()
        val variants = Variants.fromVariant(variant)
        assertEquals(1, variants.size())
        assertEquals("posix", variants.first()?.asString())
    }

    @Test
    fun fromVecUnchecked() {
        val macos = Variant.parse("macos").getOrThrow()
        val posix = Variant.parse("posix").getOrThrow()
        val variants = Variants.fromSortedDeduped(listOf(macos, posix))
        assertEquals(2, variants.size())
        assertEquals("macos-posix", variants.toString())
    }

    @Test
    fun clear() {
        val posix = Variant.parse("posix").getOrThrow()
        val variants = Variants.fromVariant(posix)
        val cleared = variants.clear()
        assertTrue(cleared.isEmpty())
    }

    @Test
    fun sortedDeduplication() {
        val macos = Variant.parse("macos").getOrThrow()
        val posix = Variant.parse("posix").getOrThrow()
        // Pass in unsorted — fromSortedDeduped sorts
        val variants = Variants.fromSortedDeduped(listOf(posix, macos))
        assertEquals("macos", variants.first()?.asString())
    }
}
