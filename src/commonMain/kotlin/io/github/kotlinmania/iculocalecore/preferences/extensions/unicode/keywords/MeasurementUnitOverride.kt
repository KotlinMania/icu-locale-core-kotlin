// port-lint: source icu_locale_core/src/preferences/extensions/unicode/keywords/measurement_unit_override.rs
package io.github.kotlinmania.iculocalecore.preferences.extensions.unicode.keywords

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.extensions.unicode.Key
import io.github.kotlinmania.iculocalecore.extensions.unicode.Value
import io.github.kotlinmania.iculocalecore.preferences.PreferenceKey
import io.github.kotlinmania.iculocalecore.preferences.extensions.unicode.PreferencesParseError

/**
 * A Measurement Unit Preference Override defines an override for measurement unit preference.
 *
 * The valid values are listed in LDML.
 */
enum class MeasurementUnitOverride(
    val subtag: String,
) : PreferenceKey {
    /** Celsius as temperature unit */
    Celsius("celsius"),

    /** Kelvin as temperature unit */
    Kelvin("kelvin"),

    /** Fahrenheit as temperature unit */
    Fahrenheit("fahrenhe"),
    ;

    fun asStr(): String = subtag

    override fun unicodeExtensionKey(): Key = KEY

    override fun unicodeExtensionValue(): Value = Value.tryFromStr(subtag).getOrThrow()

    companion object {
        val KEY: Key = Key("mu")

        fun tryFromValue(value: Value): Result<MeasurementUnitOverride> {
            val subtag =
                value.asSingleSubtag()?.asString()
                    ?: return Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))
            return when (subtag) {
                "celsius" -> Result.success(Celsius)
                "kelvin" -> Result.success(Kelvin)
                "fahrenhe" -> Result.success(Fahrenheit)
                else -> Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))
            }
        }

        fun tryFromKeyValue(key: Key, value: Value): Result<MeasurementUnitOverride?> {
            if (key != KEY) return Result.success(null)
            return tryFromValue(value)
        }

        fun tryFromStr(s: String): Result<MeasurementUnitOverride> =
            Value.tryFromStr(s).fold(
                onSuccess = { tryFromValue(it) },
                onFailure = { Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message)) },
            )

        fun parse(s: String): Result<MeasurementUnitOverride> = tryFromStr(s)
    }
}
