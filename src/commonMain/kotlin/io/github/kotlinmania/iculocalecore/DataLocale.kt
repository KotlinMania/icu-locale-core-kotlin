// port-lint: source data.rs
package io.github.kotlinmania.iculocalecore

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.parser.ParseError
import io.github.kotlinmania.iculocalecore.subtags.Language
import io.github.kotlinmania.iculocalecore.subtags.Region
import io.github.kotlinmania.iculocalecore.subtags.Script
import io.github.kotlinmania.iculocalecore.subtags.Subtag
import io.github.kotlinmania.iculocalecore.subtags.Variant
import io.github.kotlinmania.iculocalecore.subtags.Variants

/**
 * A locale type optimized for use in fallbacking and the ICU4X data pipeline.
 *
 * [DataLocale] contains less functionality than [Locale] but more than
 * [LanguageIdentifier] for better size and performance while still meeting
 * the needs of the ICU4X data pipeline.
 *
 * [DataLocale] only supports `-u-sd` keywords, to reflect the current state of CLDR data
 * lookup and fallback. This may change in the future.
 */
data class DataLocale(
    /** Language subtag */
    val language: Language,
    /** Script subtag */
    val script: Script?,
    /** Region subtag */
    val region: Region?,
    /** Variant subtag */
    val variant: Variant?,
    /** Subdivision (-u-sd-) subtag */
    val subdivision: Subtag?,
) {
    companion object {
        val DEFAULT: DataLocale =
            DataLocale(
                Language.UNKNOWN,
                null,
                null,
                null,
                null,
            )

        /**
         * Parses a [DataLocale] from a string.
         * Returns [ParseError.InvalidExtension] if the locale contains unsupported extensions.
         */
        fun parse(s: String): Result<DataLocale> = tryFromUtf8(s.encodeToByteArray())

        /**
         * Parses a [DataLocale] from a UTF-8 byte array.
         * Returns [ParseError.InvalidExtension] if the locale contains unsupported extensions.
         */
        fun tryFromUtf8(codeUnits: ByteArray): Result<DataLocale> {
            // For now, we only support the core language identifier portion.
            // Full Locale parsing with extension support will be added when
            // the Locale type is fully ported.
            val langIdResult = LanguageIdentifier.tryFromUtf8(codeUnits)
            if (langIdResult.isFailure) return Result.failure(langIdResult.exceptionOrNull()!!)

            val langId = langIdResult.getOrThrow()
            return Result.success(
                DataLocale(
                    language = langId.language,
                    script = langId.script,
                    region = langId.region,
                    variant = langId.variants.first(),
                    subdivision = null,
                ),
            )
        }
    }

    /** Returns whether this [DataLocale] is `und` in the locale and extensions portion. */
    fun isUnknown(): Boolean =
        language.isUnknown() && script == null && region == null && variant == null && subdivision == null

    /** Converts this [DataLocale] into a [LanguageIdentifier]. */
    fun toLanguageIdentifier(): LanguageIdentifier =
        LanguageIdentifier(
            language = language,
            script = script,
            region = region,
            variants = if (variant != null) Variants(listOf(variant)) else Variants.EMPTY,
        )

    /** Compare this [DataLocale] with BCP-47 bytes. */
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

    /** Compare this [DataLocale] with another field-by-field. */
    fun totalCmp(other: DataLocale): Int {
        val langCmp = language.compareTo(other.language)
        if (langCmp != 0) return langCmp
        val scriptCmp = compareNullable(script, other.script)
        if (scriptCmp != 0) return scriptCmp
        val regionCmp = compareNullable(region, other.region)
        if (regionCmp != 0) return regionCmp
        val variantCmp = compareNullable(variant, other.variant)
        if (variantCmp != 0) return variantCmp
        return compareNullable(subdivision, other.subdivision)
    }

    override fun toString(): String =
        buildString {
            append(language.asString())
            script?.let { append("-").append(it.asString()) }
            region?.let { append("-").append(it.asString()) }
            variant?.let { append("-").append(it.asString()) }
            subdivision?.let {
                append("-u-sd-").append(it.asString())
            }
        }
}

/** Convert from a [LanguageIdentifier] to a [DataLocale]. */
fun LanguageIdentifier.toDataLocale(): DataLocale =
    DataLocale(
        language = language,
        script = script,
        region = region,
        variant = variants.first(),
        subdivision = null,
    )

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
