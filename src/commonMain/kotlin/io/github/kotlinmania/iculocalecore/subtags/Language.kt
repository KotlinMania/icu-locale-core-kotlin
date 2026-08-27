// port-lint: source subtags/language.rs
package io.github.kotlinmania.iculocalecore.subtags

import io.github.kotlinmania.iculocalecore.parser.ParseError
import io.github.kotlinmania.iculocalecore.parser.ParseException

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

/**
 * A language subtag (examples: `"en"`, `"csb"`, `"zh"`, `"und"`, etc.)
 *
 * [Language] represents a Unicode base language code conformant to the
 * `unicode_language_id` field of the Language and Locale Identifier.
 *
 * Examples
 * ```
 * val language: Language = Language.parse("en").getOrThrow()
 * ```
 *
 * If the [Language] has no value assigned, it serializes to a string `"und"`, which
 * can be then parsed back to an empty [Language] field.
 *
 * Examples
 * ```
 * assertEquals(Language.UNKNOWN.asString(), "und")
 * ```
 *
 * Notice: ICU4X uses a narrow form of language subtag of 2-3 characters.
 * The specification allows language subtag to optionally also be 5-8 characters
 * but that form has not been used and ICU4X does not support it right now.
 *
 * [unicode_language_id]: https://unicode.org/reports/tr35/#unicode_language_id
 */
data class Language(
    val value: String,
) : Comparable<Language> {
    init {
        require(value.length in 2..3 && value.all { it.isLowerCase() && it.isLetter() }) {
            "Invalid language: $value"
        }
    }

    /** The unknown language "und". */
    companion object {
        val UNKNOWN: Language = Language("und")

        /**
         * Parses a string into a well-formed [Language].
         * Returns [ParseError.InvalidLanguage] if the string is not a valid language subtag.
         */
        fun parse(s: String): Result<Language> {
            if (s.length !in 2..3) return Result.failure(ParseException(ParseError.InvalidLanguage))
            if (!s.all { it.isLetter() && it.isLowerCase() }) return Result.failure(ParseException(ParseError.InvalidLanguage))
            return Result.success(Language(s.lowercase()))
        }

        /**
         * Parses a string into a well-formed [Language], normalizing case.
         * Returns [ParseError.InvalidLanguage] if the string is not a valid language subtag.
         */
        fun tryFromStr(s: String): Result<Language> {
            if (s.length !in 2..3) return Result.failure(ParseException(ParseError.InvalidLanguage))
            if (!s.all { it.isLetter() }) return Result.failure(ParseException(ParseError.InvalidLanguage))
            return Result.success(Language(s.lowercase()))
        }

        /**
         * Parses a UTF-8 byte array into a well-formed [Language], normalizing case.
         * Returns [ParseError.InvalidLanguage] if the byte array is not a valid language subtag.
         */
        fun tryFromUtf8(codeUnits: ByteArray): Result<Language> {
            val s = codeUnits.decodeToString()
            return tryFromStr(s)
        }
    }

    /** A helper function for displaying as a string. */
    fun asString(): String = value

    /** Whether this [Language] equals [Language.UNKNOWN]. */
    fun isUnknown(): Boolean = this == UNKNOWN

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

    override fun compareTo(other: Language): Int = value.compareTo(other.value)

    override fun toString(): String = value
}
