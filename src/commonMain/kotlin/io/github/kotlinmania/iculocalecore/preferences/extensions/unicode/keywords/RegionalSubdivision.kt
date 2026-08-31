// port-lint: source icu_locale_core/src/preferences/extensions/unicode/keywords/regional_subdivision.rs
package io.github.kotlinmania.iculocalecore.preferences.extensions.unicode.keywords

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.extensions.unicode.Key
import io.github.kotlinmania.iculocalecore.extensions.unicode.SubdivisionId
import io.github.kotlinmania.iculocalecore.extensions.unicode.Value
import io.github.kotlinmania.iculocalecore.preferences.PreferenceKey
import io.github.kotlinmania.iculocalecore.preferences.extensions.unicode.PreferencesParseError
import io.github.kotlinmania.iculocalecore.subtags.Region

/**
 * A Unicode Subdivision Identifier defines a regional subdivision used for locales.
 *
 * The valid values are listed in LDML.
 */
data class RegionalSubdivision(
    val subdivisionId: SubdivisionId,
) : PreferenceKey {
    val region: Region get() = subdivisionId.region
    val suffix get() = subdivisionId.suffix

    override fun unicodeExtensionKey(): Key = KEY

    override fun unicodeExtensionValue(): Value = Value.fromSubtag(subdivisionId.intoSubtag())

    companion object {
        val KEY: Key = Key("sd")

        fun tryFromValue(value: Value): Result<RegionalSubdivision> {
            val subtag =
                value.asSingleSubtag()
                    ?: return Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))
            val subdivisionIdResult = SubdivisionId.tryFromStr(subtag.asString())
            if (subdivisionIdResult.isFailure) {
                return Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))
            }
            return Result.success(RegionalSubdivision(subdivisionIdResult.getOrThrow()))
        }

        fun tryFromKeyValue(key: Key, value: Value): Result<RegionalSubdivision?> {
            if (key != KEY) return Result.success(null)
            return tryFromValue(value)
        }

        fun tryFromStr(s: String): Result<RegionalSubdivision> =
            Value.tryFromStr(s).fold(
                onSuccess = { tryFromValue(it) },
                onFailure = { Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message)) },
            )

        fun parse(s: String): Result<RegionalSubdivision> = tryFromStr(s)
    }
}
