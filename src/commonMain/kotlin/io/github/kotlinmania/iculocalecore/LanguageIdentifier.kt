// port-lint: source icu_locale_core/src/langid.rs
package io.github.kotlinmania.iculocalecore

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.parser.parseLanguageIdentifier
import io.github.kotlinmania.iculocalecore.subtags.Language
import io.github.kotlinmania.iculocalecore.subtags.Region
import io.github.kotlinmania.iculocalecore.subtags.Script
import io.github.kotlinmania.iculocalecore.subtags.Variants

/**
 * A core struct representing a Unicode BCP47 Language Identifier.
 *
 * Parsing normalizes a well-formed language identifier converting
 * `_` separators to `-` and adjusting casing to conform to the Unicode standard.
 *
 * Any syntactically invalid subtags will cause the parsing to fail with an error.
 *
 * This operation normalizes syntax to be well-formed. No legacy subtag
 * replacements are performed. For validation and canonicalization, see
 * `LocaleCanonicalizer`.
 *
 * Examples
 *
 * Simple example:
 * ```
 * val li = LanguageIdentifier.parse("en-US").getOrThrow()
 *
 * assertEquals(li.language, Language.parse("en").getOrThrow())
 * assertEquals(li.script, null)
 * assertEquals(li.region, Region.parse("US").getOrThrow())
 * assertEquals(li.variants.size(), 0)
 * ```
 *
 * More complex example:
 * ```
 * val li = LanguageIdentifier.parse("eN-latn-Us-Valencia").getOrThrow()
 *
 * assertEquals(li.language, Language.parse("en").getOrThrow())
 * assertEquals(li.script, Script.parse("Latn").getOrThrow())
 * assertEquals(li.region, Region.parse("US").getOrThrow())
 * assertEquals(li.variants.first(), Variant.parse("valencia").getOrThrow())
 * ```
 *
 * [Unicode BCP47 Language Identifier]: https://unicode.org/reports/tr35/tr35.html
 */
data class LanguageIdentifier(
    /** Language subtag of the language identifier. */
    val language: Language,
    /** Script subtag of the language identifier. */
    val script: Script?,
    /** Region subtag of the language identifier. */
    val region: Region?,
    /** Variant subtags of the language identifier. */
    val variants: Variants,
) {
    /** The unknown language identifier "und". */
    companion object {
        val UNKNOWN: LanguageIdentifier =
            LanguageIdentifier(
                Language.UNKNOWN,
                null,
                null,
                Variants.EMPTY,
            )

        /**
         * A constructor which takes a utf8 slice, parses it and
         * produces a well-formed [LanguageIdentifier].
         */
        fun tryFromUtf8(codeUnits: ByteArray): Result<LanguageIdentifier> =
            parseLanguageIdentifier(codeUnits)

        /**
         * A constructor which takes a string, parses it and
         * produces a well-formed [LanguageIdentifier].
         */
        fun tryFromStr(s: String): Result<LanguageIdentifier> =
            tryFromUtf8(s.encodeToByteArray())

        /**
         * Parses a string into a well-formed [LanguageIdentifier].
         */
        fun parse(s: String): Result<LanguageIdentifier> = tryFromStr(s)

        /**
         * A constructor which takes a utf8 slice which may contain extension keys,
         * parses it and produces a well-formed [LanguageIdentifier].
         * All extensions will be lost.
         */
        fun tryFromLocaleBytes(v: ByteArray): Result<LanguageIdentifier> =
            parseLanguageIdentifier(v, isLocale = true)
    }

    /** Whether this [LanguageIdentifier] equals [LanguageIdentifier.UNKNOWN]. */
    fun isUnknown(): Boolean =
        language.isUnknown() && script == null && region == null && variants.isEmpty()

    /**
     * Compare this [LanguageIdentifier] with BCP-47 bytes.
     *
     * The return value is equivalent to what would happen if you first converted this
     * [LanguageIdentifier] to a BCP-47 string and then performed a byte comparison.
     *
     * This function is case-sensitive and results in a total order, so it is appropriate
     * for binary search. The only argument producing [Ordering.Equal] is `self.toString()`.
     */
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

    /** Compare this [LanguageIdentifier] with another field-by-field. */
    fun totalCmp(other: LanguageIdentifier): Int {
        val langCmp = language.compareTo(other.language)
        if (langCmp != 0) return langCmp
        val scriptCmp = compareNullable(script, other.script)
        if (scriptCmp != 0) return scriptCmp
        val regionCmp = compareNullable(region, other.region)
        if (regionCmp != 0) return regionCmp
        return variants.inner.compareTo(other.variants.inner)
    }

    /** Compare with a potentially unnormalized BCP-47 string. */
    fun normalizingEq(other: String): Boolean {
        val parsed = LanguageIdentifier.tryFromStr(other)
        return parsed.getOrNull() == this
    }

    /**
     * Normalize the language identifier.
     * This operation will normalize casing and the separator.
     */
    fun normalize(input: String): Result<String> =
        LanguageIdentifier.tryFromStr(input).map { it.toString() }

    override fun toString(): String =
        buildString {
            append(language.asString())
            script?.let { append("-").append(it.asString()) }
            region?.let { append("-").append(it.asString()) }
            for (variant in variants) {
                append("-").append(variant.asString())
            }
        }
}

/** Convert from a [Language] to a [LanguageIdentifier]. */
fun Language.toLanguageIdentifier(): LanguageIdentifier =
    LanguageIdentifier(this, null, null, Variants.EMPTY)

/** Convert from an LSR tuple to a [LanguageIdentifier]. */
fun languageIdentifierOf(
    language: Language,
    script: Script? = null,
    region: Region? = null,
): LanguageIdentifier = LanguageIdentifier(language, script, region, Variants.EMPTY)

private fun <T : Comparable<T>> compareNullable(a: T?, b: T?): Int =
    if (a == null && b == null) {
        0
    } else if (a == null) {
        -1
    } else if (b == null) {
        1
    } else {
        a.compareTo(b)
    }

private fun <T : Comparable<T>> List<T>.compareTo(other: List<T>): Int {
    val sizeCmp = size.compareTo(other.size)
    if (sizeCmp != 0) return sizeCmp
    for (i in indices) {
        val cmp = this[i].compareTo(other[i])
        if (cmp != 0) return cmp
    }
    return 0
}
