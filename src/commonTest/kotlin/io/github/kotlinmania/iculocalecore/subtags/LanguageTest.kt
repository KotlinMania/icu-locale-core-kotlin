package io.github.kotlinmania.iculocalecore.subtags

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LanguageTest {
    @Test
    fun parseValidLanguage() {
        val lang = Language.parse("en").getOrThrow()
        assertEquals("en", lang.asString())
    }

    @Test
    fun parseNormalizesCase() {
        val lang = Language.tryFromStr("EN").getOrThrow()
        assertEquals("en", lang.asString())
    }

    @Test
    fun parseInvalidLength() {
        assertTrue(Language.parse("419").isFailure)
        assertTrue(Language.parse("german").isFailure)
        assertTrue(Language.parse("en1").isFailure)
    }

    @Test
    fun unknownLanguage() {
        assertEquals("und", Language.UNKNOWN.asString())
        assertTrue(Language.UNKNOWN.isUnknown())
    }

    @Test
    fun normalizingEqCaseInsensitive() {
        val lang = Language.parse("en").getOrThrow()
        assertTrue(lang.normalizingEq("EN"))
        assertTrue(lang.normalizingEq("en"))
        assertFalse(lang.normalizingEq("fr"))
    }

    @Test
    fun toStringReturnsValue() {
        assertEquals("csb", Language("csb").toString())
    }
}
