package io.github.kotlinmania.iculocalecore.subtags

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class VariantTest {
    @Test
    fun parseValidVariant() {
        val variant = Variant.parse("macos").getOrThrow()
        assertEquals("macos", variant.asString())
    }

    @Test
    fun parseNumericVariant() {
        val variant = Variant.parse("1996").getOrThrow()
        assertEquals("1996", variant.asString())
    }

    @Test
    fun parseNormalizesCase() {
        val variant = Variant.tryFromStr("MacOS").getOrThrow()
        assertEquals("macos", variant.asString())
    }

    @Test
    fun parseInvalid() {
        assertTrue(Variant.parse("yes").isFailure)
        assertTrue(Variant.parse("toolonggg").isFailure)
    }

    @Test
    fun fourCharMustStartWithDigit() {
        assertTrue(Variant.parse("abcd").isFailure)
    }

    @Test
    fun normalizingEq() {
        val variant = Variant.parse("macos").getOrThrow()
        assertTrue(variant.normalizingEq("MacOS"))
        assertTrue(variant.normalizingEq("macos"))
    }
}
