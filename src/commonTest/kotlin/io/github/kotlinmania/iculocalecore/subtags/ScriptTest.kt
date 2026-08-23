package io.github.kotlinmania.iculocalecore.subtags

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ScriptTest {
    @Test
    fun parseValidScript() {
        val script = Script.parse("Latn").getOrThrow()
        assertEquals("Latn", script.asString())
    }

    @Test
    fun parseNormalizesCase() {
        val script = Script.tryFromStr("latn").getOrThrow()
        assertEquals("Latn", script.asString())
    }

    @Test
    fun parseInvalid() {
        assertTrue(Script.parse("Latin").isFailure)
        assertTrue(Script.parse("La1").isFailure)
    }

    @Test
    fun normalizingEq() {
        val script = Script.parse("Latn").getOrThrow()
        assertTrue(script.normalizingEq("latn"))
        assertTrue(script.normalizingEq("LATN"))
        assertFalse(script.normalizingEq("Cyrl"))
    }
}
