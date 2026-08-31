// port-lint: source icu_locale_core/src/extensions/unicode/subdivision.rs
package io.github.kotlinmania.iculocalecore.extensions.unicode

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.parser.ParseError
import io.github.kotlinmania.iculocalecore.parser.ParseException
import io.github.kotlinmania.iculocalecore.subtags.Region
import io.github.kotlinmania.iculocalecore.subtags.Subtag

/**
 * A subdivision suffix used in [SubdivisionId].
 *
 * A subdivision suffix has to be a sequence of alphanumeric characters no
 * shorter than one and no longer than four characters.
 *
 * Examples
 * ```
 * val ss = SubdivisionSuffix.parse("sct").getOrThrow()
 * ```
 */
data class SubdivisionSuffix(
    val value: String,
) : Comparable<SubdivisionSuffix> {
    init {
        require(value.length in 1..4) { "SubdivisionSuffix must be 1-4 characters" }
        require(value.all { it.isLetterOrDigit() }) { "SubdivisionSuffix must be alphanumeric" }
        require(value.none { it.isUpperCase() }) { "SubdivisionSuffix must be lowercase" }
    }

    companion object {
        /** Parses a string into a well-formed [SubdivisionSuffix], normalizing to lowercase. */
        fun tryFromStr(s: String): Result<SubdivisionSuffix> {
            if (s.length !in 1..4) return Result.failure(ParseException(ParseError.InvalidExtension))
            if (!s.all { it.isLetterOrDigit() }) return Result.failure(ParseException(ParseError.InvalidExtension))
            return Result.success(SubdivisionSuffix(s.lowercase()))
        }

        /** Parses a UTF-8 byte array into a well-formed [SubdivisionSuffix], normalizing to lowercase. */
        fun tryFromUtf8(codeUnits: ByteArray): Result<SubdivisionSuffix> = tryFromStr(codeUnits.decodeToString())

        /** Parses a string into a well-formed [SubdivisionSuffix]. */
        fun parse(s: String): Result<SubdivisionSuffix> = tryFromStr(s)
    }

    /** Returns the suffix as a string. */
    fun asString(): String = value

    override fun compareTo(other: SubdivisionSuffix): Int = value.compareTo(other.value)

    override fun toString(): String = value
}

/**
 * A Subdivision Id as defined in the Unicode Locale Identifier specification.
 *
 * The subdivision is composed of a [Region] and a [SubdivisionSuffix].
 *
 * Examples
 * ```
 * val ss = SubdivisionSuffix.parse("zzzz").getOrThrow()
 * val region = Region.parse("gb").getOrThrow()
 * val si = SubdivisionId(region, ss)
 * assertEquals(si.toString(), "gbzzzz")
 * ```
 */
data class SubdivisionId(
    /** A region field of a Subdivision Id. */
    val region: Region,
    /** A subdivision suffix field of a Subdivision Id. */
    val suffix: SubdivisionSuffix,
) : Comparable<SubdivisionId> {
    companion object {
        /** Creates a new [SubdivisionId]. */
        fun new(region: Region, suffix: SubdivisionSuffix): SubdivisionId = SubdivisionId(region, suffix)

        /** Parses a string into a well-formed [SubdivisionId]. */
        fun tryFromStr(s: String): Result<SubdivisionId> = tryFromUtf8(s.encodeToByteArray())

        /** Parses a UTF-8 byte array into a well-formed [SubdivisionId]. */
        fun tryFromUtf8(codeUnits: ByteArray): Result<SubdivisionId> {
            if (codeUnits.isEmpty()) return Result.failure(ParseException(ParseError.InvalidExtension))
            val first = codeUnits[0].toInt().toChar()
            val isAlpha = first.isLetter()
            if (!isAlpha && !first.isDigit()) return Result.failure(ParseException(ParseError.InvalidExtension))
            val regionLen = if (isAlpha) 2 else 3
            if (codeUnits.size < regionLen) return Result.failure(ParseException(ParseError.InvalidExtension))
            val regionCodeUnits = codeUnits.copyOfRange(0, regionLen)
            val suffixCodeUnits = codeUnits.copyOfRange(regionLen, codeUnits.size)
            val regionResult = Region.tryFromUtf8(regionCodeUnits)
            if (regionResult.isFailure) return Result.failure(ParseException(ParseError.InvalidExtension))
            val suffixResult = SubdivisionSuffix.tryFromUtf8(suffixCodeUnits)
            if (suffixResult.isFailure) return Result.failure(suffixResult.exceptionOrNull()!!)
            return Result.success(SubdivisionId(regionResult.getOrThrow(), suffixResult.getOrThrow()))
        }

        /** Parses a string into a well-formed [SubdivisionId]. */
        fun parse(s: String): Result<SubdivisionId> = tryFromStr(s)
    }

    /** Convert to [Subtag]. */
    fun intoSubtag(): Subtag {
        val combined = region.asString().lowercase() + suffix.asString()
        return Subtag.parse(combined).getOrThrow()
    }

    override fun compareTo(other: SubdivisionId): Int {
        val regionCmp = region.compareTo(other.region)
        if (regionCmp != 0) return regionCmp
        return suffix.compareTo(other.suffix)
    }

    override fun toString(): String = region.asString().lowercase() + suffix.asString()
}
