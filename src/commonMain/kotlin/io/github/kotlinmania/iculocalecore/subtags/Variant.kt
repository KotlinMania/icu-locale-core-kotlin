// port-lint: source icu_locale_core/src/subtags/variant.rs
package io.github.kotlinmania.iculocalecore.subtags

import io.github.kotlinmania.iculocalecore.parser.ParseError
import io.github.kotlinmania.iculocalecore.parser.ParseException

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

/**
 * A variant subtag (examples: `"macos"`, `"posix"`, `"1996"` etc.)
 *
 * [Variant] represents a Unicode base language code conformant to the
 * Unicode variant ID field of the Language and Locale Identifier.
 *
 * Examples
 * ```
 * val variant: Variant = Variant.parse("macos").getOrThrow()
 * ```
 */
data class Variant(
    val value: String,
) : Comparable<Variant> {
    init {
        require(value.length in 4..8 && value.all { it.isLetterOrDigit() && !it.isUpperCase() }) {
            "Invalid variant: $value"
        }
        if (value.length == 4) {
            require(value[0].isDigit()) {
                "4-char variant must start with a digit: $value"
            }
        }
    }

    companion object {
        /**
         * Parses a string into a well-formed [Variant], normalizing to lowercase.
         * Returns [ParseError.InvalidSubtag] if the string is not a valid variant subtag.
         */
        fun tryFromStr(s: String): Result<Variant> {
            if (s.length !in 4..8) return Result.failure(ParseException(ParseError.InvalidSubtag))
            if (!s.all { it.isLetterOrDigit() }) return Result.failure(ParseException(ParseError.InvalidSubtag))
            if (s.length == 4 && !s[0].isDigit()) return Result.failure(ParseException(ParseError.InvalidSubtag))
            return Result.success(Variant(s.lowercase()))
        }

        /**
         * Parses a string into a well-formed [Variant], normalizing to lowercase.
         * Returns [ParseError.InvalidSubtag] if the string is not a valid variant subtag.
         */
        fun parse(s: String): Result<Variant> = tryFromStr(s)

        /**
         * Parses a UTF-8 byte array into a well-formed [Variant], normalizing to lowercase.
         * Returns [ParseError.InvalidSubtag] if the byte array is not a valid variant subtag.
         */
        fun tryFromUtf8(codeUnits: ByteArray): Result<Variant> {
            val s = codeUnits.decodeToString()
            return tryFromStr(s)
        }
    }

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

    override fun compareTo(other: Variant): Int = value.compareTo(other.value)

    override fun toString(): String = value
}
