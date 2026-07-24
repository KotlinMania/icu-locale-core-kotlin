// port-lint: source subtags/mod.rs
package io.github.kotlinmania.iculocalecore.subtags

import io.github.kotlinmania.iculocalecore.parser.ParseError
import io.github.kotlinmania.iculocalecore.parser.ParseException

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

/**
 * A generic subtag.
 *
 * The subtag has to be an ASCII alphanumerical string no shorter than
 * two characters and no longer than eight.
 *
 * Examples
 * ```
 * val subtag1: Subtag = Subtag.parse("Foo").getOrThrow()
 * assertEquals(subtag1.asString(), "foo")
 * ```
 */
data class Subtag(val value: String) : Comparable<Subtag> {

    init {
        require(value.length in 2..8 && value.all { it.isLetterOrDigit() && !it.isUpperCase() }) {
            "Invalid subtag: $value"
        }
    }

    companion object {
        /**
         * Parses a string into a well-formed [Subtag], normalizing to lowercase.
         * Returns [ParseError.InvalidSubtag] if the string is not a valid subtag.
         */
        fun tryFromStr(s: String): Result<Subtag> {
            if (s.length !in 2..8) return Result.failure(ParseException(ParseError.InvalidSubtag))
            if (!s.all { it.isLetterOrDigit() }) return Result.failure(ParseException(ParseError.InvalidSubtag))
            return Result.success(Subtag(s.lowercase()))
        }

        /**
         * Parses a string into a well-formed [Subtag], normalizing to lowercase.
         * Returns [ParseError.InvalidSubtag] if the string is not a valid subtag.
         */
        fun parse(s: String): Result<Subtag> = tryFromStr(s)

        /**
         * Parses a UTF-8 byte array into a well-formed [Subtag], normalizing to lowercase.
         * Returns [ParseError.InvalidSubtag] if the byte array is not a valid subtag.
         */
        fun tryFromUtf8(codeUnits: ByteArray): Result<Subtag> {
            val s = codeUnits.decodeToString()
            return tryFromStr(s)
        }
    }

    /** Returns the length of this subtag. */
    fun length(): Int = value.length

    /** A helper function for displaying as a string. */
    fun asString(): String = value

    /** Compare with BCP-47 bytes. The result is a total order suitable for binary search. */
    fun strictCmp(other: ByteArray): Int {
        val self = value.encodeToByteArray()
        val lenCmp = self.size.compareTo(other.size)
        if (lenCmp != 0) return lenCmp
        for (i in self.indices) {
            val byteCmp = self[i].toInt().compareTo(other[i].toInt())
            if (byteCmp != 0) return byteCmp
        }
        return 0
    }

    /** Compare with a potentially unnormalized BCP-47 string. */
    fun normalizingEq(other: String): Boolean = value.equals(other, ignoreCase = true)

    override fun compareTo(other: Subtag): Int = value.compareTo(other.value)

    override fun toString(): String = value
}