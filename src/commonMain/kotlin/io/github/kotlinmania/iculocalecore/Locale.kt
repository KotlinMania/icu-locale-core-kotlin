// port-lint: source icu_locale_core/src/locale.rs
package io.github.kotlinmania.iculocalecore

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.extensions.Extensions
import io.github.kotlinmania.iculocalecore.parser.SubtagIterator
import io.github.kotlinmania.iculocalecore.parser.parseLocale
import io.github.kotlinmania.iculocalecore.subtags.Language
import io.github.kotlinmania.iculocalecore.subtags.Region
import io.github.kotlinmania.iculocalecore.subtags.Script

/**
 * A core struct representing a Unicode Locale Identifier.
 *
 * A locale is made of two parts:
 *  * Unicode Language Identifier
 *  * A set of Unicode Extensions
 *
 * [Locale] exposes all of the same fields and methods as [LanguageIdentifier], and
 * on top of that is able to parse, manipulate and serialize unicode extension fields.
 *
 * Parsing normalizes a well-formed locale identifier converting
 * `_` separators to `-` and adjusting casing to conform to the Unicode standard.
 *
 * Any syntactically invalid subtags will cause the parsing to fail with an error.
 *
 * This operation normalizes syntax to be well-formed. No legacy subtag
 * replacements is performed. For validation and canonicalization, see
 * `LocaleCanonicalizer`.
 *
 * Examples
 *
 * Simple example:
 * ```
 * val loc = Locale.parse("en-US-u-ca-buddhist").getOrThrow()
 *
 * assertEquals(loc.id.language, Language.parse("en").getOrThrow())
 * assertEquals(loc.id.script, null)
 * assertEquals(loc.id.region, Region.parse("US").getOrThrow())
 * assertEquals(loc.id.variants.size(), 0)
 * ```
 *
 * More complex example:
 * ```
 * val loc = Locale.parse("eN-latn-Us-Valencia-u-hC-H12").getOrThrow()
 *
 * assertEquals(loc.id.language, Language.parse("en").getOrThrow())
 * assertEquals(loc.id.script, Script.parse("Latn").getOrThrow())
 * assertEquals(loc.id.region, Region.parse("US").getOrThrow())
 * assertEquals(loc.id.variants.first(), io.github.kotlinmania.iculocalecore.subtags.Variant.parse("valencia").getOrThrow())
 * ```
 *
 * [Unicode Locale Identifier]: https://unicode.org/reports/tr35/tr35.html
 */
data class Locale(
    /** The basic language/script/region components along with any variants. */
    val id: LanguageIdentifier,
    /** Any extensions present in the locale identifier. */
    val extensions: Extensions,
) {
    companion object {
        /** The unknown locale "und". */
        val UNKNOWN: Locale = Locale(LanguageIdentifier.UNKNOWN, Extensions.empty())

        /** Parses a string into a well-formed [Locale]. */
        fun tryFromStr(s: String): Result<Locale> = tryFromUtf8(s.encodeToByteArray())

        /** Parses a UTF-8 byte array into a well-formed [Locale]. */
        fun tryFromUtf8(codeUnits: ByteArray): Result<Locale> = parseLocale(codeUnits)

        /** Parses a string into a well-formed [Locale]. */
        fun parse(s: String): Result<Locale> = tryFromStr(s)

        /** Normalizes a locale string. */
        fun normalize(input: String): Result<String> =
            tryFromStr(input).map { it.toString() }

        /** Normalizes a locale from UTF-8 bytes. */
        fun normalizeUtf8(input: ByteArray): Result<String> =
            tryFromUtf8(input).map { it.toString() }
    }

    /** Compare this [Locale] with BCP-47 bytes. */
    fun strictCmp(other: ByteArray): Int {
        val self = toString().encodeToByteArray()
        val lenCmp = self.size.compareTo(other.size)
        if (lenCmp != 0) return lenCmp
        for (i in self.indices) {
            val byteCmp = self[i].toInt().compareTo(other[i].toInt())
            if (byteCmp != 0) return byteCmp
        }
        return 0
    }

    /** Returns an ordering suitable for use in a sorted set. */
    fun totalCmp(other: Locale): Int {
        val idCmp = id.totalCmp(other.id)
        if (idCmp != 0) return idCmp
        return extensions.totalCmp(other.extensions)
    }

    /** Compare this [Locale] with a potentially unnormalized BCP-47 string. */
    fun normalizingEq(other: String): Boolean {
        val iter = SubtagIterator(other.encodeToByteArray())

        val langSubtag = iter.next() ?: return false
        val langResult = Language.tryFromUtf8(langSubtag)
        if (langResult.isFailure || langResult.getOrThrow() != id.language) return false

        if (id.script != null) {
            val scriptSubtag = iter.next() ?: return false
            val scriptResult = Script.tryFromUtf8(scriptSubtag)
            if (scriptResult.isFailure || scriptResult.getOrThrow() != id.script) return false
        }

        if (id.region != null) {
            val regionSubtag = iter.next() ?: return false
            val regionResult = Region.tryFromUtf8(regionSubtag)
            if (regionResult.isFailure || regionResult.getOrThrow() != id.region) return false
        }

        for (variant in id.variants) {
            val variantSubtag = iter.next() ?: return false
            val variantResult =
                io.github.kotlinmania.iculocalecore.subtags.Variant
                    .tryFromUtf8(variantSubtag)
            if (variantResult.isFailure || variantResult.getOrThrow() != variant) return false
        }

        if (!extensions.isEmpty()) {
            val extResult = Extensions.tryFromIter(iter)
            if (extResult.isFailure) return false
            if (extensions != extResult.getOrThrow()) return false
        }

        return iter.next() == null
    }

    /** Iterates subtag strings for serialization. */
    fun forEachSubtagStr(f: (String) -> Unit) {
        f(id.language.asString())
        id.script?.let { f(it.asString()) }
        id.region?.let { f(it.asString()) }
        for (variant in id.variants) {
            f(variant.asString())
        }
        extensions.forEachSubtagStr(f)
    }

    override fun toString(): String =
        buildString {
            append(id.toString())
            if (!extensions.isEmpty()) {
                val extStr = extensions.toString()
                if (extStr.isNotEmpty()) {
                    append("-").append(extStr)
                }
            }
        }
}

/** Convert from a [LanguageIdentifier] to a [Locale]. */
fun LanguageIdentifier.toLocale(): Locale = Locale(this, Extensions.empty())

/** Convert from a [Language] to a [Locale]. */
fun Language.toLocale(): Locale = Locale(toLanguageIdentifier(), Extensions.empty())

/** Convert from a [Script] to a [Locale]. */
fun Script.toLocale(): Locale =
    Locale(
        LanguageIdentifier(Language.UNKNOWN, this, null, io.github.kotlinmania.iculocalecore.subtags.Variants.EMPTY),
        Extensions.empty(),
    )

/** Convert from a [Region] to a [Locale]. */
fun Region.toLocale(): Locale =
    Locale(
        LanguageIdentifier(Language.UNKNOWN, null, this, io.github.kotlinmania.iculocalecore.subtags.Variants.EMPTY),
        Extensions.empty(),
    )
