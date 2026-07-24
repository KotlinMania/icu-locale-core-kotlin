// port-lint: source subtags/region.rs
package io.github.kotlinmania.iculocalecore.subtags

import io.github.kotlinmania.iculocalecore.parser.ParseError
import io.github.kotlinmania.iculocalecore.parser.ParseException

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

/**
 * A region subtag (examples: `"US"`, `"CN"`, `"AR"` etc.)
 *
 * [Region] represents a Unicode base language code conformant to the
 * `unicode_region_id` field of the Language and Locale Identifier.
 *
 * Examples
 * ```
 * val region: Region = Region.parse("DE").getOrThrow()
 * ```
 *
 * [unicode_region_id]: https://unicode.org/reports/tr35/#unicode_region_id
 */
data class Region(val value: String) : Comparable<Region> {

    init {
        require(value.length == 2 || value.length == 3) {
            "Invalid region length: $value"
        }
        if (value.length == 2) {
            require(value.all { it.isUpperCase() && it.isLetter() }) {
                "2-letter region must be uppercase alphabetic: $value"
            }
        } else {
            require(value.all { it.isDigit() }) {
                "3-letter region must be numeric: $value"
            }
        }
    }

    companion object {
        /**
         * Parses a string into a well-formed [Region], normalizing case.
         * 2-letter regions are uppercased; 3-letter regions stay numeric.
         * Returns [ParseError.InvalidSubtag] if the string is not a valid region subtag.
         */
        fun tryFromStr(s: String): Result<Region> {
            if (s.length !in 2..3) return Result.failure(ParseException(ParseError.InvalidSubtag))
            if (s.length == 2) {
                if (!s.all { it.isLetter() }) return Result.failure(ParseException(ParseError.InvalidSubtag))
                return Result.success(Region(s.uppercase()))
            } else {
                if (!s.all { it.isDigit() }) return Result.failure(ParseException(ParseError.InvalidSubtag))
                return Result.success(Region(s))
            }
        }

        /**
         * Parses a string into a well-formed [Region], normalizing case.
         * Returns [ParseError.InvalidSubtag] if the string is not a valid region subtag.
         */
        fun parse(s: String): Result<Region> = tryFromStr(s)

        /**
         * Parses a UTF-8 byte array into a well-formed [Region], normalizing case.
         * Returns [ParseError.InvalidSubtag] if the byte array is not a valid region subtag.
         */
        fun tryFromUtf8(codeUnits: ByteArray): Result<Region> {
            val s = codeUnits.decodeToString()
            return tryFromStr(s)
        }
    }

    /** Returns true if the Region has an alphabetic code. */
    fun isAlphabetic(): Boolean = value.length == 2

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

    override fun compareTo(other: Region): Int = value.compareTo(other.value)

    override fun toString(): String = value
}