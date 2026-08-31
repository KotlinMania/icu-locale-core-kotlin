// port-lint: source parser/errors.rs
package io.github.kotlinmania.iculocalecore.parser

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

/**
 * List of parser errors that can be generated
 * while parsing [LanguageIdentifier], [Locale],
 * `subtags` or `extensions`.
 *
 * This enum is non-exhaustive: new variants may be added in future releases.
 */
enum class ParseError(
    val message: String,
) {
    /**
     * Invalid language subtag.
     *
     * # Examples
     *
     * ```
     * import io.github.kotlinmania.iculocalecore.subtags.Language
     * import io.github.kotlinmania.iculocalecore.parser.ParseError
     *
     * assertEquals(Result.failure(ParseError.InvalidLanguage), Language.parse("x2"))
     * ```
     */
    InvalidLanguage("The given language subtag is invalid"),

    /**
     * Invalid script, region or variant subtag.
     *
     * # Examples
     *
     * ```
     * import io.github.kotlinmania.iculocalecore.subtags.Region
     * import io.github.kotlinmania.iculocalecore.parser.ParseError
     *
     * assertEquals(Result.failure(ParseError.InvalidSubtag), Region.parse("#@2X"))
     * ```
     */
    InvalidSubtag("Invalid subtag"),

    /**
     * Invalid extension subtag.
     *
     * # Examples
     *
     * ```
     * import io.github.kotlinmania.iculocalecore.extensions.unicode.Key
     * import io.github.kotlinmania.iculocalecore.parser.ParseError
     *
     * assertEquals(Result.failure(ParseError.InvalidExtension), Key.parse("#@2X"))
     * ```
     */
    InvalidExtension("Invalid extension"),

    /**
     * Duplicated extension.
     *
     * # Examples
     *
     * ```
     * import io.github.kotlinmania.iculocalecore.Locale
     * import io.github.kotlinmania.iculocalecore.parser.ParseError
     *
     * assertEquals(
     *     Result.failure(ParseError.DuplicatedExtension),
     *     Locale.parse("und-u-hc-h12-u-ca-calendar"),
     * )
     * ```
     */
    DuplicatedExtension("Duplicated extension"),
    ;

    override fun toString(): String = message
}

/** Wraps a [ParseError] so it can be used with Kotlin's [Result] type. */
class ParseException(
    val error: ParseError,
) : Exception(error.message)
