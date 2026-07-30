// port-lint: source extensions/unicode/attribute.rs
package io.github.kotlinmania.iculocalecore.extensions.unicode

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.parser.ParseError
import io.github.kotlinmania.iculocalecore.parser.ParseException

/**
 * An attribute used in a set of [Attributes].
 *
 * An attribute has to be a sequence of alphanumeric characters no
 * shorter than three and no longer than eight characters.
 *
 * Examples
 * ```
 * val attr: Attribute = Attribute.parse("buddhist").getOrThrow()
 * assertEquals(attr, Attribute.parse("buddhist").getOrThrow())
 * ```
 */
data class Attribute(val value: String) : Comparable<Attribute> {
    init {
        require(value.length in 3..8) { "Attribute must be 3-8 characters" }
        require(value.all { it.isLetterOrDigit() }) { "Attribute must be alphanumeric" }
        require(value.none { it.isUpperCase() }) { "Attribute must be lowercase" }
    }

    companion object {
        /** Parses a string into a well-formed [Attribute], normalizing to lowercase. */
        fun tryFromStr(s: String): Result<Attribute> {
            if (s.length !in 3..8) return Result.failure(ParseException(ParseError.InvalidExtension))
            if (!s.all { it.isLetterOrDigit() }) return Result.failure(ParseException(ParseError.InvalidExtension))
            return Result.success(Attribute(s.lowercase()))
        }

        /** Parses a UTF-8 byte array into a well-formed [Attribute], normalizing to lowercase. */
        fun tryFromUtf8(codeUnits: ByteArray): Result<Attribute> = tryFromStr(codeUnits.decodeToString())

        /** Parses a string into a well-formed [Attribute]. */
        fun parse(s: String): Result<Attribute> = tryFromStr(s)
    }

    /** Returns the attribute as a string. */
    fun asString(): String = value

    override fun compareTo(other: Attribute): Int = value.compareTo(other.value)

    override fun toString(): String = value
}