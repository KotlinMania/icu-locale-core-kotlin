// port-lint: source preferences/extensions/unicode/keywords/currency.rs
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
 * A Unicode Currency Identifier defines a type of currency.
 *
 * The valid values are listed in LDML.
 */
data class CurrencyType(
    val code: String,
) : PreferenceKey {
    init {
        require(code.length == 3 && code.all { it.isLetter() && it.isLowerCase() }) {
            "Currency code must be 3 lowercase ascii letters"
        }
    }

    override fun unicodeExtensionKey(): Key = KEY

    override fun unicodeExtensionValue(): Value = Value.fromSubtag(Subtag.parse(code).getOrNull())

    companion object {
        val KEY: Key = Key("cu")

        fun tryFromValue(value: Value): Result<CurrencyType> {
            val subtag =
                value.asSingleSubtag()?.asString()
                    ?: return Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))
            if (subtag.length == 3 && subtag.all { it.isLetter() }) {
                return Result.success(CurrencyType(subtag.lowercase()))
            }
            return Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))
        }

        fun tryFromKeyValue(key: Key, value: Value): Result<CurrencyType?> {
            if (key != KEY) return Result.success(null)
            return tryFromValue(value)
        }

        fun tryFromStr(s: String): Result<CurrencyType> =
            Value.tryFromStr(s).fold(
                onSuccess = { tryFromValue(it) },
                onFailure = { Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message)) },
            )

        fun parse(s: String): Result<CurrencyType> = tryFromStr(s)
    }
}
