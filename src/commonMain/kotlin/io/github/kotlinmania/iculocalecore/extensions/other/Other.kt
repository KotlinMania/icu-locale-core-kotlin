// port-lint: source extensions/other/mod.rs
package io.github.kotlinmania.iculocalecore.extensions.other

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.parser.ParseError
import io.github.kotlinmania.iculocalecore.parser.ParseException
import io.github.kotlinmania.iculocalecore.parser.SubtagIterator
import io.github.kotlinmania.iculocalecore.shortvec.ShortBoxSlice
import io.github.kotlinmania.iculocalecore.subtags.Subtag

/**
 * A list of Other Use Extensions as defined in the Unicode Locale
 * Identifier specification.
 *
 * Those extensions are treated as a pass-through, and no Unicode related
 * behavior depends on them.
 *
 * Examples
 * ```
 * val subtag1 = Subtag.parse("foo").getOrThrow()
 * val subtag2 = Subtag.parse("bar").getOrThrow()
 * val other = Other.fromVecUnchecked('a'.code.toByte(), listOf(subtag1, subtag2))
 * assertEquals(other.toString(), "a-foo-bar")
 * ```
 */
class Other internal constructor(
    val ext: Byte,
    internal val keys: ShortBoxSlice<Subtag>,
) : Comparable<Other> {
    init {
        require(ext.toInt().toChar().isLetter()) { "Extension byte must be ASCII alphabetic" }
    }

    companion object {
        /** Creates an [Other] from a pre-sorted list of subtags. */
        internal fun fromVecUnchecked(ext: Byte, keys: List<Subtag>): Other =
            Other(ext, ShortBoxSlice.fromList(keys))

        /** Creates an [Other] from a [ShortBoxSlice] of subtags. */
        internal fun fromShortSliceUnchecked(ext: Byte, keys: ShortBoxSlice<Subtag>): Other {
            require(ext.toInt().toChar().isLetter()) { "Extension byte must be ASCII alphabetic" }
            return Other(ext, keys)
        }

        /** Parses a string into a well-formed [Other]. */
        fun tryFromStr(s: String): Result<Other> = tryFromUtf8(s.encodeToByteArray())

        /** Parses a UTF-8 byte array into a well-formed [Other]. */
        fun tryFromUtf8(codeUnits: ByteArray): Result<Other> {
            val iter = SubtagIterator(codeUnits)
            val ext = iter.next() ?: return Result.failure(ParseException(ParseError.InvalidExtension))
            if (ext.size != 1) return Result.failure(ParseException(ParseError.InvalidExtension))
            val extByte =
                ext[0]
                    .toInt()
                    .toChar()
                    .lowercaseChar()
                    .code
                    .toByte()
            if (!extByte.toInt().toChar().isLetter()) return Result.failure(ParseException(ParseError.InvalidExtension))
            return tryFromIter(extByte, iter)
        }

        /** Parses an [Other] from a [SubtagIterator]. */
        fun tryFromIter(ext: Byte, iter: SubtagIterator): Result<Other> {
            val keys = mutableListOf<Subtag>()
            while (true) {
                val subtag = iter.peek() ?: break
                if (!Subtag.validKey(subtag)) break
                val keyResult = Subtag.tryFromUtf8(subtag)
                if (keyResult.isSuccess) {
                    keys.add(keyResult.getOrThrow())
                }
                iter.next()
            }
            if (keys.isEmpty()) return Result.failure(ParseException(ParseError.InvalidExtension))
            return Result.success(Other(ext, ShortBoxSlice.fromList(keys)))
        }

        /** Parses a string into a well-formed [Other]. */
        fun parse(s: String): Result<Other> = tryFromStr(s)
    }

    /** Gets the tag character for this extension as a string. */
    fun getExtStr(): String = ext.toInt().toChar().toString()

    /** Gets the tag character for this extension as a char. */
    fun getExt(): Char = ext.toInt().toChar()

    /** Gets the tag character for this extension as a byte. */
    fun getExtByte(): Byte = ext

    /** Returns whether there are no keys. */
    fun isEmpty(): Boolean = keys.isEmpty()

    /** Iterates subtag strings for serialization, optionally including the extension marker. */
    fun forEachSubtagStr(f: (String) -> Unit, withExt: Boolean = false) {
        if (keys.isEmpty()) return
        if (withExt) f(getExtStr())
        for (s in keys) {
            f(s.asString())
        }
    }

    override fun compareTo(other: Other): Int {
        val extCmp = ext.compareTo(other.ext)
        if (extCmp != 0) return extCmp
        val a = keys.toList()
        val b = other.keys.toList()
        val sizeCmp = a.size.compareTo(b.size)
        if (sizeCmp != 0) return sizeCmp
        for (i in a.indices) {
            val cmp = a[i].compareTo(b[i])
            if (cmp != 0) return cmp
        }
        return 0
    }

    override fun equals(other: Any?): Boolean =
        other is Other && ext == other.ext && keys == other.keys

    override fun hashCode(): Int = 31 * ext.hashCode() + keys.hashCode()

    override fun toString(): String {
        if (keys.isEmpty()) return ""
        return buildString {
            append(getExtStr())
            for (s in keys) {
                append("-")
                append(s.asString())
            }
        }
    }
}
