// port-lint: source preferences/extensions/unicode/keywords/first_day.rs
package io.github.kotlinmania.iculocalecore.preferences.extensions.unicode.keywords

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.extensions.unicode.Key
import io.github.kotlinmania.iculocalecore.extensions.unicode.Value
import io.github.kotlinmania.iculocalecore.preferences.PreferenceKey
import io.github.kotlinmania.iculocalecore.preferences.extensions.unicode.PreferencesParseError

/**
 * A Unicode First Day Identifier defines the preferred first day of the week for calendar display.
 *
 * The valid values are listed in LDML.
 */
enum class FirstDay(
    val subtag: String,
) : PreferenceKey {
    /** Sunday */
    Sun("sun"),

    /** Monday */
    Mon("mon"),

    /** Tuesday */
    Tue("tue"),

    /** Wednesday */
    Wed("wed"),

    /** Thursday */
    Thu("thu"),

    /** Friday */
    Fri("fri"),

    /** Saturday */
    Sat("sat"),
    ;

    fun asStr(): String = subtag

    override fun unicodeExtensionKey(): Key = KEY

    override fun unicodeExtensionValue(): Value = Value.tryFromStr(subtag).getOrThrow()

    companion object {
        val KEY: Key = Key("fw")

        fun tryFromValue(value: Value): Result<FirstDay> {
            val subtag =
                value.asSingleSubtag()?.asString()
                    ?: return Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))
            return when (subtag) {
                "sun" -> Result.success(Sun)
                "mon" -> Result.success(Mon)
                "tue" -> Result.success(Tue)
                "wed" -> Result.success(Wed)
                "thu" -> Result.success(Thu)
                "fri" -> Result.success(Fri)
                "sat" -> Result.success(Sat)
                else -> Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))
            }
        }

        fun tryFromKeyValue(key: Key, value: Value): Result<FirstDay?> {
            if (key != KEY) return Result.success(null)
            return tryFromValue(value)
        }

        fun tryFromStr(s: String): Result<FirstDay> =
            Value.tryFromStr(s).fold(
                onSuccess = { tryFromValue(it) },
                onFailure = { Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message)) },
            )

        fun parse(s: String): Result<FirstDay> = tryFromStr(s)
    }
}
