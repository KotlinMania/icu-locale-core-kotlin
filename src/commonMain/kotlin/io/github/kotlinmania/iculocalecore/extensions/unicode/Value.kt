// port-lint: source extensions/unicode/value.rs
package io.github.kotlinmania.iculocalecore.extensions.unicode

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.parser.ParseError
import io.github.kotlinmania.iculocalecore.parser.ParseException
import io.github.kotlinmania.iculocalecore.parser.SubtagIterator
import io.github.kotlinmania.iculocalecore.shortvec.ShortBoxSlice
import io.github.kotlinmania.iculocalecore.subtags.Subtag

/**
 * A value used in a list of [Keywords].
 *
 * The value has to be a sequence of one or more alphanumeric strings
 * separated by `-`. Each part has to be no shorter than three characters
 * and no longer than eight.
 *
 * The value "true" has the special empty-string representation.
 *
 * Examples
 * ```
 * assertEquals(Value.parse("gregory").getOrThrow().toString(), "gregory")
 * assertEquals(Value.parse("islamic-civil").getOrThrow().toString(), "islamic-civil")
 * assertEquals(Value.parse("true").getOrThrow().toString(), "")
 * ```
 */
class Value internal constructor(
    internal val inner: ShortBoxSlice<Subtag>,
) : Comparable<Value> {
    companion object {
        private val TRUE_VALUE: Subtag = Subtag.parse("true").getOrThrow()

        /** Creates an empty [Value], which corresponds to a "true" value. */
        fun newEmpty(): Value = Value(ShortBoxSlice.empty())

        /** Creates a [Value] from a single optional subtag. None or "true" produces empty. */
        fun fromSubtag(subtag: Subtag?): Value {
            if (subtag == null || subtag == TRUE_VALUE) return Value(ShortBoxSlice.empty())
            return Value(ShortBoxSlice.of(subtag))
        }

        /** Creates a [Value] from two subtags. */
        fun fromTwoSubtags(first: Subtag, second: Subtag): Value =
            Value(ShortBoxSlice.fromList(listOf(first, second)))

        /** Creates a [Value] from a pre-sorted list. */
        fun fromVecUnchecked(input: List<Subtag>): Value =
            Value(ShortBoxSlice.fromList(input))

        /** Parses a string into a well-formed [Value]. */
        fun tryFromStr(s: String): Result<Value> = tryFromUtf8(s.encodeToByteArray())

        /** Parses a UTF-8 byte array into a well-formed [Value]. */
        fun tryFromUtf8(codeUnits: ByteArray): Result<Value> {
            val v = mutableListOf<Subtag>()
            if (codeUnits.isNotEmpty()) {
                val iter = SubtagIterator(codeUnits)
                while (true) {
                    val chunk = iter.next() ?: break
                    val subtagResult = Subtag.tryFromUtf8(chunk)
                    if (subtagResult.isFailure) return Result.failure(subtagResult.exceptionOrNull()!!)
                    val subtag = subtagResult.getOrThrow()
                    if (subtag != TRUE_VALUE) {
                        v.add(subtag)
                    }
                }
            }
            return Result.success(Value(ShortBoxSlice.fromList(v)))
        }

        /** Parses a string into a well-formed [Value]. */
        fun parse(s: String): Result<Value> = tryFromStr(s)

        /** Creates a [Value] from a [ShortBoxSlice] of subtags. */
        internal fun fromShortSliceUnchecked(input: ShortBoxSlice<Subtag>): Value = Value(input)

        /** Parses a single subtag from UTF-8 bytes, returning None for "true". */
        fun parseSubtagFromUtf8(t: ByteArray): Result<Subtag?> {
            val subtagResult = Subtag.tryFromUtf8(t)
            if (subtagResult.isFailure) return Result.failure(ParseException(ParseError.InvalidSubtag))
            val subtag = subtagResult.getOrThrow()
            return Result.success(if (subtag == TRUE_VALUE) null else subtag)
        }
    }

    /** Returns a reference to a single [Subtag] if the [Value] contains exactly one subtag. */
    fun asSingleSubtag(): Subtag? = inner.single()

    /** Destructs into a single [Subtag] if the [Value] contains exactly one subtag. */
    fun intoSingleSubtag(): Subtag? = inner.intoSingle()

    /** Returns the subtags as a list. */
    fun asSubtagsSlice(): List<Subtag> = inner.toList()

    /** Appends a subtag to the back of a [Value], returning a new instance. */
    fun pushSubtag(subtag: Subtag): Value = Value(inner.push(subtag))

    /** Returns the number of subtags. */
    fun subtagCount(): Int = inner.size()

    /** Returns whether the [Value] has no subtags. */
    fun isEmpty(): Boolean = inner.isEmpty()

    /** Removes and returns the subtag at the given position. */
    fun removeSubtag(idx: Int): Subtag? {
        if (inner.size() <= idx) return null
        val (newSlice, item) = inner.removeAt(idx)
        return item
    }

    /** Returns a reference to the subtag at the given index. */
    fun getSubtag(idx: Int): Subtag? = if (idx < inner.size()) inner[idx] else null

    /** Creates a [Value] from a [ShortBoxSlice] of subtags (companion factory). */
    fun toValue(): Value = Value(inner)

    /** Iterates subtag strings for serialization. */
    fun forEachSubtagStr(f: (String) -> Unit) {
        for (s in inner) {
            f(s.asString())
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

    /** Compares this [Value] with a string. */
    fun eq(other: String): Boolean = toString() == other

    override fun equals(other: Any?): Boolean =
        other is Value && inner == other.inner

    override fun hashCode(): Int = inner.hashCode()

    override fun toString(): String {
        if (inner.isEmpty()) return ""
        return inner.toList().joinToString("-") { it.asString() }
    }
}
