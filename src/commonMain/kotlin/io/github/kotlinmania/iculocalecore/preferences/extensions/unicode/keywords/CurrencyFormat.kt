// port-lint: source icu_locale_core/src/preferences/extensions/unicode/keywords/currency_format.rs
package io.github.kotlinmania.iculocalecore.preferences.extensions.unicode.keywords

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.extensions.unicode.Key
import io.github.kotlinmania.iculocalecore.extensions.unicode.Value
import io.github.kotlinmania.iculocalecore.preferences.PreferenceKey
import io.github.kotlinmania.iculocalecore.preferences.extensions.unicode.PreferencesParseError

/**
 * A Unicode Currency Format Identifier defines a style for currency formatting.
 *
 * The valid values are listed in LDML.
 */
enum class CurrencyFormatStyle(
    val subtag: String,
) : PreferenceKey {
    /** Negative numbers use the minusSign symbol (the default) */
    Standard("standard"),

    /** Negative numbers use parentheses or equivalent */
    Account("account"),
    ;

    fun asStr(): String = subtag

    override fun unicodeExtensionKey(): Key = KEY

    override fun unicodeExtensionValue(): Value = Value.tryFromStr(subtag).getOrThrow()

    companion object {
        val KEY: Key = Key("cf")
        val DEFAULT: CurrencyFormatStyle = Standard

        fun tryFromValue(value: Value): Result<CurrencyFormatStyle> {
            val subtag =
                value.asSingleSubtag()?.asString()
                    ?: return Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))
            return when (subtag) {
                "standard" -> Result.success(Standard)
                "account" -> Result.success(Account)
                else -> Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))
            }
        }

        fun tryFromKeyValue(key: Key, value: Value): Result<CurrencyFormatStyle?> {
            if (key != KEY) return Result.success(null)
            return tryFromValue(value)
        }

        fun tryFromStr(s: String): Result<CurrencyFormatStyle> =
            Value.tryFromStr(s).fold(
                onSuccess = { tryFromValue(it) },
                onFailure = { Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message)) },
            )

        fun parse(s: String): Result<CurrencyFormatStyle> = tryFromStr(s)
    }
}
