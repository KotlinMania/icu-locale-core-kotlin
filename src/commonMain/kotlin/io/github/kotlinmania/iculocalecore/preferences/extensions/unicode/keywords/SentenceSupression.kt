// port-lint: source preferences/extensions/unicode/keywords/sentence_supression.rs
package io.github.kotlinmania.iculocalecore.preferences.extensions.unicode.keywords

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.extensions.unicode.Key
import io.github.kotlinmania.iculocalecore.extensions.unicode.Value
import io.github.kotlinmania.iculocalecore.preferences.PreferenceKey
import io.github.kotlinmania.iculocalecore.preferences.extensions.unicode.PreferencesParseError

/**
 * A Unicode Sentence Break Suppressions Identifier defines a set of data to be used for suppressing certain
 * sentence breaks that would otherwise be found by UAX #14 rules.
 *
 * The valid values are listed in LDML.
 */
enum class SentenceBreakSupressions(
    val subtag: String,
) : PreferenceKey {
    /** Don’t use sentence break suppressions data (the default) */
    None("none"),

    /** Use sentence break suppressions data of type "standard" */
    Standard("standard"),
    ;

    fun asStr(): String = subtag

    override fun unicodeExtensionKey(): Key = KEY

    override fun unicodeExtensionValue(): Value = Value.tryFromStr(subtag).getOrThrow()

    companion object {
        val KEY: Key = Key("ss")
        val DEFAULT: SentenceBreakSupressions = None

        fun tryFromValue(value: Value): Result<SentenceBreakSupressions> {
            val subtag =
                value.asSingleSubtag()?.asString()
                    ?: return Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))
            return when (subtag) {
                "none" -> Result.success(None)
                "standard" -> Result.success(Standard)
                else -> Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))
            }
        }

        fun tryFromKeyValue(key: Key, value: Value): Result<SentenceBreakSupressions?> {
            if (key != KEY) return Result.success(null)
            return tryFromValue(value)
        }

        fun tryFromStr(s: String): Result<SentenceBreakSupressions> =
            Value.tryFromStr(s).fold(
                onSuccess = { tryFromValue(it) },
                onFailure = { Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message)) },
            )

        fun parse(s: String): Result<SentenceBreakSupressions> = tryFromStr(s)
    }
}
