// port-lint: tests extensions/transform/mod.rs, extensions/transform/key.rs, extensions/transform/value.rs, extensions/transform/fields.rs
package io.github.kotlinmania.iculocalecore.extensions.transform

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TransformKeyTest {
    @Test
    fun parseValidKey() {
        assertEquals("k0", Key.parse("k0").getOrThrow().asString())
        assertEquals("h0", Key.parse("H0").getOrThrow().asString())
    }

    @Test
    fun parseInvalidKey() {
        assertTrue(Key.parse("").isFailure)
        assertTrue(Key.parse("k").isFailure)
        assertTrue(Key.parse("0k").isFailure)
        assertTrue(Key.parse("k12").isFailure)
    }
}

class TransformValueTest {
    @Test
    fun parseValidValue() {
        assertEquals("hybrid", Value.parse("hybrid").getOrThrow().toString())
        assertEquals("hybrid-foobar", Value.parse("hybrid-foobar").getOrThrow().toString())
    }

    @Test
    fun parseInvalidValue() {
        assertTrue(Value.parse("no").isFailure)
        assertTrue(Value.parse("").isFailure)
    }

    @Test
    fun defaultValueIsTrue() {
        assertEquals("true", Value.fromShortSliceUnchecked(io.github.kotlinmania.iculocalecore.shortvec.ShortBoxSlice.empty()).toString())
    }
}

class FieldsTest {
    @Test
    fun createAndAccess() {
        val value = Value.parse("hybrid").getOrThrow()
        val key = Key.parse("h0").getOrThrow()
        val fields = Fields.fromPairs(listOf(key to value))
        assertEquals("h0-hybrid", fields.toString())
        assertEquals(value, fields.get(key))
        assertTrue(fields.containsKey(key))
    }

    @Test
    fun emptyFields() {
        val fields = Fields.empty()
        assertTrue(fields.isEmpty())
    }
}

class TransformTest {
    @Test
    fun parseTransformExtension() {
        val t = Transform.parse("t-en-us-h0-hybrid").getOrThrow()
        assertEquals("t-en-us-h0-hybrid", t.toString())
        assertFalse(t.isEmpty())
    }

    @Test
    fun parseJustT() {
        assertTrue(Transform.parse("t").isFailure)
    }

    @Test
    fun emptyTransform() {
        val t = Transform.empty()
        assertTrue(t.isEmpty())
        assertEquals("", t.toString())
    }

    @Test
    fun parseTransformWithLangOnly() {
        val t = Transform.parse("t-en-us").getOrThrow()
        assertEquals("t-en-us", t.toString())
        assertFalse(t.isEmpty())
    }
}