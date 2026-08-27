// port-lint: source icu_locale_core/src/preferences/extensions/unicode/keywords/timezone.rs
package io.github.kotlinmania.iculocalecore.preferences.extensions.unicode.keywords

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.extensions.unicode.Key
import io.github.kotlinmania.iculocalecore.extensions.unicode.Value
import io.github.kotlinmania.iculocalecore.preferences.PreferenceKey
import io.github.kotlinmania.iculocalecore.preferences.extensions.unicode.PreferencesParseError
import io.github.kotlinmania.iculocalecore.subtags.Subtag

/**
 * A Unicode Timezone Identifier defines a timezone.
 *
 * The valid values are listed in LDML.
 */
data class TimeZoneShortId(
    val subtag: Subtag,
) : PreferenceKey {
    override fun unicodeExtensionKey(): Key = KEY

    override fun unicodeExtensionValue(): Value = Value.fromSubtag(subtag)

    companion object {
        val KEY: Key = Key("tz")

        fun tryFromValue(value: Value): Result<TimeZoneShortId> {
            val subtag =
                value.intoSingleSubtag()
                    ?: return Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))
            return Result.success(TimeZoneShortId(subtag))
        }

        fun tryFromKeyValue(key: Key, value: Value): Result<TimeZoneShortId?> {
            if (key != KEY) return Result.success(null)
            return tryFromValue(value)
        }

        fun tryFromStr(s: String): Result<TimeZoneShortId> =
            Value.tryFromStr(s).fold(
                onSuccess = { tryFromValue(it) },
                onFailure = { Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message)) },
            )

        fun parse(s: String): Result<TimeZoneShortId> = tryFromStr(s)
    }
}
