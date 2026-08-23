// port-lint: source parser/langid.rs
package io.github.kotlinmania.iculocalecore.parser

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.LanguageIdentifier
import io.github.kotlinmania.iculocalecore.subtags.Language
import io.github.kotlinmania.iculocalecore.subtags.Region
import io.github.kotlinmania.iculocalecore.subtags.Script
import io.github.kotlinmania.iculocalecore.subtags.Variant
import io.github.kotlinmania.iculocalecore.subtags.Variants

enum class ParserMode {
    LanguageIdentifier,
    Locale,
    Partial,
}

private enum class ParserPosition {
    Script,
    Region,
    Variant,
}

/**
 * Parses a language identifier from a byte array.
 * In Locale mode, stops at single-character subtags (extension markers).
 */
fun parseLanguageIdentifier(
    bytes: ByteArray,
    isLocale: Boolean = false,
): Result<LanguageIdentifier> {
    val mode = if (isLocale) ParserMode.Locale else ParserMode.LanguageIdentifier
    val iter = SubtagIterator(bytes)
    return parseLanguageIdentifierFromIter(iter, mode)
}

fun parseLanguageIdentifierFromIter(
    iter: SubtagIterator,
    mode: ParserMode,
): Result<LanguageIdentifier> {
    var script: Script? = null
    var region: Region? = null
    var variants = mutableListOf<Variant>()

    val firstSubtag =
        iter.next()
            ?: return Result.failure(ParseException(ParseError.InvalidLanguage))
    val language =
        Language
            .tryFromUtf8(firstSubtag)
            .onFailure { return Result.failure(it) }
            .getOrThrow()

    var position = ParserPosition.Script

    while (true) {
        val subtag = iter.peek() ?: break

        if (mode != ParserMode.LanguageIdentifier && subtag.size == 1) {
            break
        }

        val subtagStr = subtag.decodeToString()

        if (position == ParserPosition.Script) {
            val scriptResult = Script.tryFromUtf8(subtag)
            if (scriptResult.isSuccess) {
                script = scriptResult.getOrThrow()
                position = ParserPosition.Region
            } else {
                val regionResult = Region.tryFromUtf8(subtag)
                if (regionResult.isSuccess) {
                    region = regionResult.getOrThrow()
                    position = ParserPosition.Variant
                } else {
                    val variantResult = Variant.tryFromUtf8(subtag)
                    if (variantResult.isSuccess) {
                        val v = variantResult.getOrThrow()
                        insertSortedDedup(variants, v)
                        position = ParserPosition.Variant
                    } else if (mode == ParserMode.Partial) {
                        break
                    } else {
                        return Result.failure(ParseException(ParseError.InvalidSubtag))
                    }
                }
            }
        } else if (position == ParserPosition.Region) {
            val regionResult = Region.tryFromUtf8(subtag)
            if (regionResult.isSuccess) {
                region = regionResult.getOrThrow()
                position = ParserPosition.Variant
            } else {
                val variantResult = Variant.tryFromUtf8(subtag)
                if (variantResult.isSuccess) {
                    val v = variantResult.getOrThrow()
                    insertSortedDedup(variants, v)
                    position = ParserPosition.Variant
                } else if (mode == ParserMode.Partial) {
                    break
                } else {
                    return Result.failure(ParseException(ParseError.InvalidSubtag))
                }
            }
        } else {
            val variantResult = Variant.tryFromUtf8(subtag)
            if (variantResult.isSuccess) {
                val v = variantResult.getOrThrow()
                val searchResult = variants.binarySearch(v)
                if (searchResult >= 0) {
                    return Result.failure(ParseException(ParseError.InvalidSubtag))
                }
                variants.add(-(searchResult + 1), v)
            } else if (mode == ParserMode.Partial) {
                break
            } else {
                return Result.failure(ParseException(ParseError.InvalidSubtag))
            }
        }

        iter.next()
    }

    return Result.success(
        LanguageIdentifier(
            language = language,
            script = script,
            region = region,
            variants = Variants(variants),
        ),
    )
}

private fun insertSortedDedup(list: MutableList<Variant>, v: Variant) {
    val searchResult = list.binarySearch(v)
    if (searchResult < 0) {
        list.add(-(searchResult + 1), v)
    }
}
