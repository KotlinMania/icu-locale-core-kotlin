// port-lint: source icu_locale_core/src/subtags/script.rs
package io.github.kotlinmania.iculocalecore.subtags

import io.github.kotlinmania.iculocalecore.parser.ParseError
import io.github.kotlinmania.iculocalecore.parser.ParseException

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

/**
 * A script subtag (examples: `"Latn"`, `"Arab"`, etc.)
 *
 * [Script] represents a Unicode base language code conformant to the
 * `unicode_script_id` field of the Language and Locale Identifier.
 *
 * Examples
 * ```
 * val script: Script = Script.parse("Latn").getOrThrow()
 * ```
 *
 * [unicode_script_id]: https://unicode.org/reports/tr35/#unicode_script_id
 */
data class Script(
    val value: String,
) : Comparable<Script> {
    init {
        require(value.length == 4 && value.all { it.isLetter() }) {
            "Invalid script: $value"
        }
        require(value[0].isUpperCase() && value.substring(1).all { it.isLowerCase() }) {
            "Script must be titlecased: $value"
        }
    }

    companion object {
        /**
         * Parses a string into a well-formed [Script], normalizing to titlecase.
         * Returns [ParseError.InvalidSubtag] if the string is not a valid script subtag.
         */
        fun tryFromStr(s: String): Result<Script> {
            if (s.length != 4) return Result.failure(ParseException(ParseError.InvalidSubtag))
            if (!s.all { it.isLetter() }) return Result.failure(ParseException(ParseError.InvalidSubtag))
            return Result.success(Script(s.lowercase().replaceFirstChar { it.uppercase() }))
        }

        /**
         * Parses a string into a well-formed [Script], normalizing to titlecase.
         * Returns [ParseError.InvalidSubtag] if the string is not a valid script subtag.
         */
        fun parse(s: String): Result<Script> = tryFromStr(s)

        /**
         * Parses a UTF-8 byte array into a well-formed [Script], normalizing to titlecase.
         * Returns [ParseError.InvalidSubtag] if the byte array is not a valid script subtag.
         */
        fun tryFromUtf8(codeUnits: ByteArray): Result<Script> {
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

    override fun compareTo(other: Script): Int = value.compareTo(other.value)

    override fun toString(): String = value
}
