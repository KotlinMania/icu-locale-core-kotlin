// port-lint: source extensions/transform/key.rs
package io.github.kotlinmania.iculocalecore.extensions.transform

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.parser.ParseError
import io.github.kotlinmania.iculocalecore.parser.ParseException

/**
 * A key used in a list of [Fields].
 *
 * The key has to be two ASCII characters long, with the first
 * character being alphabetic, and the second being a number.
 *
 * Examples
 * ```
 * val key1 = Key.parse("k0").getOrThrow()
 * assertEquals(key1.asString(), "k0")
 * ```
 */
data class Key(val value: String) : Comparable<Key> {
    init {
        require(value.length == 2) { "Key must be exactly 2 characters" }
        require(value[0].isLetter()) { "First character must be alphabetic" }
        require(value[1].isDigit()) { "Second character must be a digit" }
        require(!value[0].isUpperCase()) { "Key must be lowercase" }
    }

    companion object {
        /** Parses a string into a well-formed [Key], normalizing to lowercase. */
        fun tryFromStr(s: String): Result<Key> {
            if (s.length != 2) return Result.failure(ParseException(ParseError.InvalidExtension))
            if (!s[0].isLetter()) return Result.failure(ParseException(ParseError.InvalidExtension))
            if (!s[1].isDigit()) return Result.failure(ParseException(ParseError.InvalidExtension))
            return Result.success(Key(s.lowercase()))
        }

        /** Parses a UTF-8 byte array into a well-formed [Key], normalizing to lowercase. */
        fun tryFromUtf8(codeUnits: ByteArray): Result<Key> = tryFromStr(codeUnits.decodeToString())

        /** Parses a string into a well-formed [Key]. */
        fun parse(s: String): Result<Key> = tryFromStr(s)
    }

    /** Returns the key as a string. */
    fun asString(): String = value

    override fun compareTo(other: Key): Int = value.compareTo(other.value)

    override fun toString(): String = value
}