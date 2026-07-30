// port-lint: source extensions/private/other.rs
package io.github.kotlinmania.iculocalecore.extensions.private

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.parser.ParseError
import io.github.kotlinmania.iculocalecore.parser.ParseException

/**
 * A single item used in a list of [Private] extensions.
 *
 * The subtag has to be an ASCII alphanumeric string no shorter than
 * one character and no longer than eight.
 *
 * This is different from the generic [io.github.kotlinmania.iculocalecore.subtags.Subtag]
 * which is between two and eight characters.
 *
 * Examples
 * ```
 * val subtag1 = PrivateSubtag.parse("Foo").getOrThrow()
 * assertEquals(subtag1.asString(), "foo")
 * ```
 */
data class PrivateSubtag(val value: String) : Comparable<PrivateSubtag> {
    init {
        require(value.length in 1..8) { "PrivateSubtag must be 1-8 characters" }
        require(value.all { it.isLetterOrDigit() }) { "PrivateSubtag must be alphanumeric" }
        require(value.none { it.isUpperCase() }) { "PrivateSubtag must be lowercase" }
    }

    companion object {
        /** Parses a string into a well-formed [PrivateSubtag], normalizing to lowercase. */
        fun tryFromStr(s: String): Result<PrivateSubtag> {
            if (s.length !in 1..8) return Result.failure(ParseException(ParseError.InvalidExtension))
            if (!s.all { it.isLetterOrDigit() }) return Result.failure(ParseException(ParseError.InvalidExtension))
            return Result.success(PrivateSubtag(s.lowercase()))
        }

        /** Parses a UTF-8 byte array into a well-formed [PrivateSubtag], normalizing to lowercase. */
        fun tryFromUtf8(codeUnits: ByteArray): Result<PrivateSubtag> = tryFromStr(codeUnits.decodeToString())

        /** Parses a string into a well-formed [PrivateSubtag]. */
        fun parse(s: String): Result<PrivateSubtag> = tryFromStr(s)
    }

    /** Returns the subtag as a string. */
    fun asString(): String = value

    override fun compareTo(other: PrivateSubtag): Int = value.compareTo(other.value)

    override fun toString(): String = value
}