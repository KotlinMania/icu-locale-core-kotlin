// port-lint: source preferences/extensions/unicode/keywords/measurement_system.rs
package io.github.kotlinmania.iculocalecore.preferences.extensions.unicode.keywords

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.extensions.unicode.Key
import io.github.kotlinmania.iculocalecore.extensions.unicode.Value
import io.github.kotlinmania.iculocalecore.preferences.PreferenceKey
import io.github.kotlinmania.iculocalecore.preferences.extensions.unicode.PreferencesParseError

/**
 * A Unicode Measurement System Identifier defines a preferred measurement system.
 *
 * Specifying "ms" in a locale identifier overrides the default value specified by supplemental measurement system data for the region.
 *
 * The valid values are listed in LDML.
 */
enum class MeasurementSystem(
    val subtag: String,
) : PreferenceKey {
    /** Metric System */
    Metric("metric"),

    /** US System of measurement: feet, pints, etc.; pints are 16oz */
    USSystem("ussystem"),

    /** UK System of measurement: feet, pints, etc.; pints are 20oz */
    UKSystem("uksystem"),
    ;

    fun asStr(): String = subtag

    override fun unicodeExtensionKey(): Key = KEY

    override fun unicodeExtensionValue(): Value = Value.tryFromStr(subtag).getOrThrow()

    companion object {
        val KEY: Key = Key("ms")

        fun tryFromValue(value: Value): Result<MeasurementSystem> {
            val subtag =
                value.asSingleSubtag()?.asString()
                    ?: return Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))
            return when (subtag) {
                "metric" -> Result.success(Metric)
                "ussystem" -> Result.success(USSystem)
                "uksystem" -> Result.success(UKSystem)
                else -> Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))
            }
        }

        fun tryFromKeyValue(key: Key, value: Value): Result<MeasurementSystem?> {
            if (key != KEY) return Result.success(null)
            return tryFromValue(value)
        }

        fun tryFromStr(s: String): Result<MeasurementSystem> =
            Value.tryFromStr(s).fold(
                onSuccess = { tryFromValue(it) },
                onFailure = { Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message)) },
            )

        fun parse(s: String): Result<MeasurementSystem> = tryFromStr(s)
    }
}
