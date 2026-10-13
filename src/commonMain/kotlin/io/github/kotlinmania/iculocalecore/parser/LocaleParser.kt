// port-lint: source parser/locale.rs
package io.github.kotlinmania.iculocalecore.parser

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.extensions.Extensions

/**
 * Parses a locale from a byte array.
 *
 * This parses a language identifier followed by any extensions.
 */
fun parseLocale(t: ByteArray): Result<io.github.kotlinmania.iculocalecore.Locale> {
    val iter = SubtagIterator(t)

    val idResult = parseLanguageIdentifierFromIter(iter, ParserMode.Locale)
    if (idResult.isFailure) return Result.failure(idResult.exceptionOrNull()!!)
    val id = idResult.getOrThrow()

    val extensions =
        if (iter.peek() != null) {
            val extResult = Extensions.tryFromIter(iter)
            if (extResult.isFailure) return Result.failure(extResult.exceptionOrNull()!!)
            extResult.getOrThrow()
        } else {
            Extensions.empty()
        }

    return Result.success(
        io.github.kotlinmania.iculocalecore
            .Locale(id, extensions),
    )
}

/**
 * Parses a locale with at most one variant and at most one unicode keyword
 * extension from a byte array.
 *
 * Kotlin equivalent of Rust's
 * `parse_locale_with_single_variant_single_keyword_unicode_keyword_extension`.
 * Delegates to [parseLocaleWithSingleVariantSingleKeywordUnicodeExtensionFromIter].
 */
fun parseLocaleWithSingleVariantSingleKeywordUnicodeKeywordExtension(
    t: ByteArray,
    mode: ParserMode,
): Result<SingleVariantKeywordParseResult> {
    val iter = SubtagIterator(t)
    return parseLocaleWithSingleVariantSingleKeywordUnicodeExtensionFromIter(iter, mode)
}
