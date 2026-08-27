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
