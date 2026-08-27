// port-lint: source icu_locale_core/src/extensions/transform/value.rs
package io.github.kotlinmania.iculocalecore.extensions.transform

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.parser.ParseError
import io.github.kotlinmania.iculocalecore.parser.ParseException
import io.github.kotlinmania.iculocalecore.parser.SubtagIterator
import io.github.kotlinmania.iculocalecore.shortvec.ShortBoxSlice
import io.github.kotlinmania.iculocalecore.subtags.Subtag

private val TYPE_LENGTH: IntRange = 3..8
private val TRUE_TVALUE: Subtag = Subtag.parse("true").getOrThrow()

/**
 * A value used in a list of [Fields].
 *
 * The value has to be a sequence of one or more alphanumeric strings
 * separated by `-`. Each part has to be no shorter than three characters
 * and no longer than eight.
 *
 * Examples
 * ```
 * assertTrue(Value.parse("hybrid").isSuccess)
 * assertTrue(Value.parse("hybrid-foobar").isSuccess)
 * assertTrue(Value.parse("no").isFailure)
 * ```
 */
class Value internal constructor(
    internal val inner: ShortBoxSlice<Subtag>,
) : Comparable<Value> {
    companion object {
        /** Parses a string into a well-formed [Value]. */
        fun tryFromStr(s: String): Result<Value> = tryFromUtf8(s.encodeToByteArray())

        /** Parses a UTF-8 byte array into a well-formed [Value]. */
        fun tryFromUtf8(codeUnits: ByteArray): Result<Value> {
            val v = mutableListOf<Subtag>()
            var hasValue = false

            val iter = SubtagIterator(codeUnits)
            while (true) {
                val subtag = iter.next() ?: break
                if (!isTypeSubtag(subtag)) {
                    return Result.failure(ParseException(ParseError.InvalidExtension))
                }
                hasValue = true
                val subtagResult = Subtag.tryFromUtf8(subtag)
                if (subtagResult.isFailure) return Result.failure(ParseException(ParseError.InvalidExtension))
                val val_ = subtagResult.getOrThrow()
                if (val_ != TRUE_TVALUE) {
                    v.add(val_)
                }
            }

            if (!hasValue) return Result.failure(ParseException(ParseError.InvalidExtension))
            return Result.success(Value(ShortBoxSlice.fromList(v)))
        }

        /** Parses a string into a well-formed [Value]. */
        fun parse(s: String): Result<Value> = tryFromStr(s)

        /** Creates a [Value] from a [ShortBoxSlice] of subtags. */
        internal fun fromShortSliceUnchecked(input: ShortBoxSlice<Subtag>): Value = Value(input)

        /** Returns whether the byte array is a valid type subtag (3-8 alphanumeric chars). */
        fun isTypeSubtag(t: ByteArray): Boolean =
            t.size in TYPE_LENGTH && t.all { it.toInt().toChar().isLetterOrDigit() }

        /** Parses a single subtag, returning None for "true". */
        fun parseSubtag(t: ByteArray): Result<Subtag?> {
            if (t.size !in TYPE_LENGTH) return Result.failure(ParseException(ParseError.InvalidExtension))
            val sResult = Subtag.tryFromUtf8(t)
            if (sResult.isFailure) return Result.failure(ParseException(ParseError.InvalidSubtag))
            val s = sResult.getOrThrow()
            return Result.success(if (s == TRUE_TVALUE) null else s)
        }
    }

    /** Iterates subtag strings for serialization. */
    fun forEachSubtagStr(f: (String) -> Unit) {
        if (inner.isEmpty()) {
            f(TRUE_TVALUE.asString())
        } else {
            for (s in inner) {
                f(s.asString())
            }
        }
    }

    override fun compareTo(other: Value): Int {
        val a = inner.toList()
        val b = other.inner.toList()
        val sizeCmp = a.size.compareTo(b.size)
        if (sizeCmp != 0) return sizeCmp
        for (i in a.indices) {
            val cmp = a[i].compareTo(b[i])
            if (cmp != 0) return cmp
        }
        return 0
    }

    override fun equals(other: Any?): Boolean =
        other is Value && inner == other.inner

    override fun hashCode(): Int = inner.hashCode()

    override fun toString(): String {
        if (inner.isEmpty()) return "true"
        return inner.toList().joinToString("-") { it.asString() }
    }
}
