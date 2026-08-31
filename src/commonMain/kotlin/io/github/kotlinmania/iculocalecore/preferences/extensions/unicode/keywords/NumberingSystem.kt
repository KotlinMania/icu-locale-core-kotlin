// port-lint: source icu_locale_core/src/preferences/extensions/unicode/keywords/numbering_system.rs
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
 * A Unicode Number System Identifier defines a type of number system.
 *
 * The valid values are listed in LDML.
 */
data class NumberingSystem(
    val subtag: Subtag,
) : PreferenceKey {
    override fun unicodeExtensionKey(): Key = KEY

    override fun unicodeExtensionValue(): Value = Value.fromSubtag(subtag)

    companion object {
        val KEY: Key = Key("nu")

        fun tryFromValue(value: Value): Result<NumberingSystem> {
            val subtag =
                value.intoSingleSubtag()
                    ?: return Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))
            return Result.success(NumberingSystem(subtag))
        }

        fun tryFromKeyValue(key: Key, value: Value): Result<NumberingSystem?> {
            if (key != KEY) return Result.success(null)
            return tryFromValue(value)
        }

        fun tryFromStr(s: String): Result<NumberingSystem> =
            Value.tryFromStr(s).fold(
                onSuccess = { tryFromValue(it) },
                onFailure = { Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message)) },
            )

        fun parse(s: String): Result<NumberingSystem> = tryFromStr(s)
    }
}
