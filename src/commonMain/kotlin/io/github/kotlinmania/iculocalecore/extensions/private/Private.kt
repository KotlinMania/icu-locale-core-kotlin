// port-lint: source icu_locale_core/src/extensions/private/mod.rs
package io.github.kotlinmania.iculocalecore.extensions.private

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.parser.ParseError
import io.github.kotlinmania.iculocalecore.parser.ParseException
import io.github.kotlinmania.iculocalecore.parser.SubtagIterator
import io.github.kotlinmania.iculocalecore.shortvec.ShortBoxSlice

const val PRIVATE_EXT_CHAR: Char = 'x'
const val PRIVATE_EXT_STR: String = "x"

/**
 * A list of Private Use Extensions as defined in the Unicode Locale
 * Identifier specification.
 *
 * Those extensions are treated as a pass-through, and no Unicode related
 * behavior depends on them.
 *
 * Examples
 * ```
 * val subtag1 = PrivateSubtag.parse("foo").getOrThrow()
 * val subtag2 = PrivateSubtag.parse("bar").getOrThrow()
 * val private_ = Private.fromVecUnchecked(listOf(subtag1, subtag2))
 * assertEquals(private_.toString(), "x-foo-bar")
 * ```
 */
class Private internal constructor(
    internal val inner: ShortBoxSlice<PrivateSubtag>,
) : Comparable<Private> {
    companion object {
        /** Returns a new empty list of private-use extensions. */
        fun empty(): Private = Private(ShortBoxSlice.empty())

        /** Creates a [Private] from a pre-sorted list. */
        internal fun fromVecUnchecked(input: List<PrivateSubtag>): Private =
            Private(ShortBoxSlice.fromList(input))

        /** Creates a [Private] containing a single subtag. */
        fun newSingle(input: PrivateSubtag): Private = Private(ShortBoxSlice.of(input))

        /** Parses a string into a well-formed [Private]. */
        fun tryFromStr(s: String): Result<Private> = tryFromUtf8(s.encodeToByteArray())

        /** Parses a UTF-8 byte array into a well-formed [Private]. */
        fun tryFromUtf8(codeUnits: ByteArray): Result<Private> {
            val iter = SubtagIterator(codeUnits)
            val ext = iter.next() ?: return Result.failure(ParseException(ParseError.InvalidExtension))
            if (ext.size != 1 || ext[0].toInt().toChar().lowercaseChar() != PRIVATE_EXT_CHAR) {
                return Result.failure(ParseException(ParseError.InvalidExtension))
            }
            return tryFromIter(iter)
        }

        /** Parses a [Private] from a [SubtagIterator]. */
        fun tryFromIter(iter: SubtagIterator): Result<Private> {
            val keys = mutableListOf<PrivateSubtag>()
            while (true) {
                val subtag = iter.next() ?: break
                val result = PrivateSubtag.tryFromUtf8(subtag)
                if (result.isFailure) return Result.failure(result.exceptionOrNull()!!)
                keys.add(result.getOrThrow())
            }
            if (keys.isEmpty()) return Result.failure(ParseException(ParseError.InvalidExtension))
            return Result.success(Private(ShortBoxSlice.fromList(keys)))
        }

        /** Parses a string into a well-formed [Private]. */
        fun parse(s: String): Result<Private> = tryFromStr(s)
    }

    /** Returns whether there are no subtags. */
    fun isEmpty(): Boolean = inner.isEmpty()

    /** Returns the number of subtags. */
    fun size(): Int = inner.size()

    /** Returns an iterator over the subtags. */
    fun iter(): Iterator<PrivateSubtag> = inner.iterator()

    /** Returns whether the list contains the given subtag. */
    fun contains(subtag: PrivateSubtag): Boolean {
        for (s in inner) {
            if (s == subtag) return true
        }
        return false
    }

    /** Clears the [Private] list. */
    fun clear(): Private = Private(ShortBoxSlice.empty())

    /** Iterates subtag strings for serialization, optionally including the extension marker. */
    fun forEachSubtagStr(f: (String) -> Unit, withExt: Boolean = false) {
        if (isEmpty()) return
        if (withExt) f(PRIVATE_EXT_STR)
        for (s in inner) {
            f(s.asString())
        }
    }

    override fun compareTo(other: Private): Int {
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
        other is Private && inner == other.inner

    override fun hashCode(): Int = inner.hashCode()

    override fun toString(): String {
        if (isEmpty()) return ""
        return buildString {
            append(PRIVATE_EXT_CHAR)
            for (s in inner) {
                append("-")
                append(s.asString())
            }
        }
    }
}
