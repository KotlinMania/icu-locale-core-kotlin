// port-lint: source preferences/extensions/unicode/keywords/calendar.rs
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
 * Hijri Calendar sub-type.
 *
 * The list is based on CLDR Calendars.
 */
enum class HijriCalendarAlgorithm(
    val subtag: String,
) {
    /** Hijri calendar, Umm al-Qura */
    Umalqura("umalqura"),

    /** Hijri calendar, tabular (intercalary years [2,5,7,10,13,16,18,21,24,26,29] - astronomical epoch) */
    Tbla("tbla"),

    /** Hijri calendar, tabular (intercalary years [2,5,7,10,13,16,18,21,24,26,29] - civil epoch) */
    Civil("civil"),

    /** Hijri calendar, Saudi Arabia sighting */
    Rgsa("rgsa"),
    ;

    fun asStr(): String = subtag

    companion object {
        fun fromSubtag(s: String): HijriCalendarAlgorithm? =
            when (s) {
                "umalqura" -> Umalqura
                "tbla" -> Tbla
                "civil" -> Civil
                "rgsa" -> Rgsa
                else -> null
            }
    }
}

/**
 * A Unicode Calendar Identifier defines a type of calendar.
 *
 * This selects calendar-specific data within a locale used for formatting and parsing,
 * such as date/time symbols and patterns; it also selects supplemental calendarData used
 * for calendrical calculations. The value can affect the computation of the first day of the week.
 *
 * The valid values are listed in LDML.
 */
sealed class CalendarAlgorithm : PreferenceKey {
    data object Buddhist : CalendarAlgorithm()

    data object Chinese : CalendarAlgorithm()

    data object Coptic : CalendarAlgorithm()

    data object Dangi : CalendarAlgorithm()

    data object Ethioaa : CalendarAlgorithm()

    data object Ethiopic : CalendarAlgorithm()

    data object Gregory : CalendarAlgorithm()

    data object Hebrew : CalendarAlgorithm()

    data object Indian : CalendarAlgorithm()

    data class Hijri(
        val algorithm: HijriCalendarAlgorithm? = null,
    ) : CalendarAlgorithm()

    data object Iso8601 : CalendarAlgorithm()

    data object Japanese : CalendarAlgorithm()

    data object Persian : CalendarAlgorithm()

    data object Roc : CalendarAlgorithm()

    override fun unicodeExtensionKey(): Key = KEY

    override fun unicodeExtensionValue(): Value =
        when (this) {
            is Buddhist -> Value.tryFromStr("buddhist").getOrThrow()
            is Chinese -> Value.tryFromStr("chinese").getOrThrow()
            is Coptic -> Value.tryFromStr("coptic").getOrThrow()
            is Dangi -> Value.tryFromStr("dangi").getOrThrow()
            is Ethioaa -> Value.tryFromStr("ethioaa").getOrThrow()
            is Ethiopic -> Value.tryFromStr("ethiopic").getOrThrow()
            is Gregory -> Value.tryFromStr("gregory").getOrThrow()
            is Hebrew -> Value.tryFromStr("hebrew").getOrThrow()
            is Indian -> Value.tryFromStr("indian").getOrThrow()
            is Hijri -> {
                if (algorithm != null) {
                    Value.fromTwoSubtags(
                        Subtag.parse("islamic").getOrThrow(),
                        Subtag.parse(algorithm.subtag).getOrThrow(),
                    )
                } else {
                    Value.fromSubtag(Subtag.parse("islamic").getOrThrow())
                }
            }
            is Iso8601 -> Value.tryFromStr("iso8601").getOrThrow()
            is Japanese -> Value.tryFromStr("japanese").getOrThrow()
            is Persian -> Value.tryFromStr("persian").getOrThrow()
            is Roc -> Value.tryFromStr("roc").getOrThrow()
        }

    companion object {
        val KEY: Key = Key("ca")

        fun tryFromValue(value: Value): Result<CalendarAlgorithm> {
            if (value.toString() == "islamicc") {
                return Result.success(Hijri(HijriCalendarAlgorithm.Civil))
            }
            val count = value.subtagCount()
            if (count == 0) {
                return Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))
            }
            val first =
                value.getSubtag(0)?.asString()
                    ?: return Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))

            if (first == "islamic") {
                return if (count == 1) {
                    Result.success(Hijri(null))
                } else if (count == 2) {
                    val second = value.getSubtag(1)!!.asString()
                    val alg =
                        HijriCalendarAlgorithm.fromSubtag(second)
                            ?: return Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))
                    Result.success(Hijri(alg))
                } else {
                    Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))
                }
            }

            if (count > 1) {
                return Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))
            }

            return when (first) {
                "buddhist" -> Result.success(Buddhist)
                "chinese" -> Result.success(Chinese)
                "coptic" -> Result.success(Coptic)
                "dangi" -> Result.success(Dangi)
                "ethioaa" -> Result.success(Ethioaa)
                "ethiopic" -> Result.success(Ethiopic)
                "gregory" -> Result.success(Gregory)
                "hebrew" -> Result.success(Hebrew)
                "indian" -> Result.success(Indian)
                "iso8601" -> Result.success(Iso8601)
                "japanese" -> Result.success(Japanese)
                "persian" -> Result.success(Persian)
                "roc" -> Result.success(Roc)
                else -> Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))
            }
        }

        fun tryFromKeyValue(key: Key, value: Value): Result<CalendarAlgorithm?> {
            if (key != KEY) return Result.success(null)
            return tryFromValue(value)
        }

        fun tryFromStr(s: String): Result<CalendarAlgorithm> =
            Value.tryFromStr(s).fold(
                onSuccess = { tryFromValue(it) },
                onFailure = { Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message)) },
            )

        fun parse(s: String): Result<CalendarAlgorithm> = tryFromStr(s)
    }
}
