// port-lint: tests icu_locale_core/src/subtags/region.rs
package io.github.kotlinmania.iculocalecore.subtags

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RegionTest {
    @Test
    fun parseTwoLetterRegion() {
        val region = Region.parse("DE").getOrThrow()
        assertEquals("DE", region.asString())
        assertTrue(region.isAlphabetic())
    }

    @Test
    fun parseThreeDigitRegion() {
        val region = Region.parse("123").getOrThrow()
        assertEquals("123", region.asString())
        assertFalse(region.isAlphabetic())
    }

    @Test
    fun parseNormalizesCase() {
        val region = Region.tryFromStr("us").getOrThrow()
        assertEquals("US", region.asString())
    }

    @Test
    fun parseInvalid() {
        assertTrue(Region.parse("12").isFailure)
        assertTrue(Region.parse("FRA").isFailure)
        assertTrue(Region.parse("b2").isFailure)
    }

    @Test
    fun normalizingEq() {
        val region = Region.parse("US").getOrThrow()
        assertTrue(region.normalizingEq("us"))
        assertTrue(region.normalizingEq("US"))
        assertFalse(region.normalizingEq("GB"))
    }
}
