// port-lint: source parser/langid.rs
package io.github.kotlinmania.iculocalecore.parser

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.LanguageIdentifier
import io.github.kotlinmania.iculocalecore.extensions.ExtensionType
import io.github.kotlinmania.iculocalecore.extensions.unicode.Attribute
import io.github.kotlinmania.iculocalecore.extensions.unicode.Key
import io.github.kotlinmania.iculocalecore.extensions.unicode.Value
import io.github.kotlinmania.iculocalecore.subtags.Language
import io.github.kotlinmania.iculocalecore.subtags.Region
import io.github.kotlinmania.iculocalecore.subtags.Script
import io.github.kotlinmania.iculocalecore.subtags.Subtag
import io.github.kotlinmania.iculocalecore.subtags.Variant
import io.github.kotlinmania.iculocalecore.subtags.Variants

/**
 * Result of parsing a language identifier with at most one variant
 * (const-context equivalent in Rust, used by the langid! macro).
 */
data class SingleVariantParseResult(
    val language: Language,
    val script: Script?,
    val region: Region?,
    val variant: Variant?,
)

/**
 * A key-value pair for a single unicode keyword extension in const-context parsing.
 */
data class SingleKeyword(
    val key: Key,
    val value: Subtag?,
)

/**
 * Result of parsing a locale with at most one variant and one unicode
 * keyword extension (const-context equivalent in Rust, used by the locale! macro).
 */
data class SingleVariantKeywordParseResult(
    val language: Language,
    val script: Script?,
    val region: Region?,
    val variant: Variant?,
    val keyword: SingleKeyword?,
)

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
    val variants = mutableListOf<Variant>()

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

/**
 * Parses a locale with at most one variant and at most one unicode keyword
 * extension from a [SubtagIterator].
 *
 * This is the Kotlin equivalent of the Rust const fn
 * `parse_locale_with_single_variant_single_keyword_unicode_extension_from_iter`.
 * In Rust this is used in const context by the `locale!` macro; in Kotlin
 * it serves as the underlying parser for [parseLanguageIdentifierWithSingleVariant]
 * and [parseLocaleWithSingleVariantSingleKeywordUnicodeKeywordExtension].
 *
 * Returns [ParseError.InvalidSubtag] if the input contains more than one
 * variant, attributes, more than one keyword, or non-unicode extensions.
 */
fun parseLocaleWithSingleVariantSingleKeywordUnicodeExtensionFromIter(
    iter: SubtagIterator,
    mode: ParserMode,
): Result<SingleVariantKeywordParseResult> {
    val firstSubtag =
        iter.next()
            ?: return Result.failure(ParseException(ParseError.InvalidLanguage))
    val language =
        Language
            .tryFromUtf8(firstSubtag)
            .onFailure { return Result.failure(it) }
            .getOrThrow()

    var script: Script? = null
    var region: Region? = null
    var variant: Variant? = null
    var keyword: SingleKeyword? = null

    var position = ParserPosition.Script

    while (true) {
        val subtag = iter.peek() ?: break

        if (mode != ParserMode.LanguageIdentifier && subtag.size == 1) {
            break
        }

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
                        if (variant != null) {
                            return Result.failure(ParseException(ParseError.InvalidSubtag))
                        }
                        variant = variantResult.getOrThrow()
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
                    if (variant != null) {
                        return Result.failure(ParseException(ParseError.InvalidSubtag))
                    }
                    variant = variantResult.getOrThrow()
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
                if (variant != null) {
                    return Result.failure(ParseException(ParseError.InvalidSubtag))
                }
                variant = variantResult.getOrThrow()
            } else if (mode == ParserMode.Partial) {
                break
            } else {
                return Result.failure(ParseException(ParseError.InvalidSubtag))
            }
        }

        iter.next()
    }

    if (mode == ParserMode.Locale) {
        val extSubtag = iter.peek()
        if (extSubtag != null) {
            val extTypeResult = ExtensionType.tryFromUtf8(extSubtag)
            if (extTypeResult.isFailure) {
                return Result.failure(extTypeResult.exceptionOrNull()!!)
            }
            val extType = extTypeResult.getOrThrow()
            if (extType is ExtensionType.Unicode) {
                iter.next()

                val peek = iter.peek()
                if (peek != null && Attribute.tryFromUtf8(peek).isSuccess) {
                    return Result.failure(ParseException(ParseError.InvalidSubtag))
                }

                var key: Key? = null
                var currentType: Subtag? = null

                while (true) {
                    val kwPeek = iter.peek() ?: break
                    if (kwPeek.size == 2) {
                        if (key != null) {
                            return Result.failure(ParseException(ParseError.InvalidSubtag))
                        }
                        val keyResult = Key.tryFromUtf8(kwPeek)
                        if (keyResult.isFailure) {
                            return Result.failure(keyResult.exceptionOrNull()!!)
                        }
                        key = keyResult.getOrThrow()
                    } else if (key != null) {
                        val typeResult = Value.parseSubtagFromUtf8(kwPeek)
                        if (typeResult.isFailure) {
                            return Result.failure(typeResult.exceptionOrNull()!!)
                        }
                        val t = typeResult.getOrThrow()
                        if (t != null) {
                            if (currentType != null) {
                                return Result.failure(ParseException(ParseError.InvalidSubtag))
                            }
                            currentType = t
                        }
                    } else {
                        break
                    }
                    iter.next()
                }

                key?.let { keyword = SingleKeyword(it, currentType) }
            } else {
                return Result.failure(ParseException(ParseError.InvalidSubtag))
            }
        }
    }

    return Result.success(
        SingleVariantKeywordParseResult(language, script, region, variant, keyword),
    )
}

/**
 * Parses a language identifier with at most one variant from a byte array.
 *
 * Kotlin equivalent of Rust's `parse_language_identifier_with_single_variant`.
 * Delegates to [parseLocaleWithSingleVariantSingleKeywordUnicodeExtensionFromIter]
 * and discards the keyword field.
 */
fun parseLanguageIdentifierWithSingleVariant(
    t: ByteArray,
    mode: ParserMode,
): Result<SingleVariantParseResult> {
    val iter = SubtagIterator(t)
    val result = parseLocaleWithSingleVariantSingleKeywordUnicodeExtensionFromIter(iter, mode)
    if (result.isFailure) return Result.failure(result.exceptionOrNull()!!)
    val r = result.getOrThrow()
    return Result.success(
        SingleVariantParseResult(r.language, r.script, r.region, r.variant),
    )
}
