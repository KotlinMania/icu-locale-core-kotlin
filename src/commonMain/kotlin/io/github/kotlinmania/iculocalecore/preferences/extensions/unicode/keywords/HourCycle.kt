// port-lint: source preferences/extensions/unicode/keywords/hour_cycle.rs
package io.github.kotlinmania.iculocalecore.preferences.extensions.unicode.keywords

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.extensions.unicode.Key
import io.github.kotlinmania.iculocalecore.extensions.unicode.Value
import io.github.kotlinmania.iculocalecore.preferences.PreferenceKey
import io.github.kotlinmania.iculocalecore.preferences.extensions.unicode.PreferencesParseError

/**
 * A Unicode Hour Cycle Identifier defines the preferred time cycle. Specifying "hc" in a locale identifier overrides the default value specified by supplemental time data for the region.
 *
 * The valid values are listed in [LDML](https://unicode.org/reports/tr35/#UnicodeHourCycleIdentifier).
 */
enum class HourCycle(
    val subtag: String,
) : PreferenceKey {
    /** The typical 12-hour clock. Hours are numbered 1–12. Corresponds to "h" in patterns. */
    H12("h12"),

    /** The 24-hour clock. Hour are numbered 0–23. Corresponds to "H" in patterns. */
    H23("h23"),

    /** Variant of the 12-hour clock, sometimes used in Japan. Hours are numbered 0–11. Corresponds to "K" in patterns. */
    H11("h11"),
    ;

    fun asStr(): String = subtag

    override fun unicodeExtensionKey(): Key = KEY

    override fun unicodeExtensionValue(): Value = Value.tryFromStr(subtag).getOrThrow()

    companion object {
        val KEY: Key = Key("hc")

        fun tryFromValue(value: Value): Result<HourCycle> {
            val subtag =
                value.asSingleSubtag()?.asString()
                    ?: return Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))
            return when (subtag) {
                "h12" -> Result.success(H12)
                "h23" -> Result.success(H23)
                "h11" -> Result.success(H11)
                else -> Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))
            }
        }

        fun tryFromKeyValue(key: Key, value: Value): Result<HourCycle?> {
            if (key != KEY) return Result.success(null)
            return tryFromValue(value)
        }

        fun tryFromStr(s: String): Result<HourCycle> =
            Value.tryFromStr(s).fold(
                onSuccess = { tryFromValue(it) },
                onFailure = { Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message)) },
            )

        fun parse(s: String): Result<HourCycle> = tryFromStr(s)
    }
}
