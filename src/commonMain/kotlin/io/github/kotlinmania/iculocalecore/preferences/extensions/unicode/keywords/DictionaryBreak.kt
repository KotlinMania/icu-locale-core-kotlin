// port-lint: source icu_locale_core/src/preferences/extensions/unicode/keywords/dictionary_break.rs
package io.github.kotlinmania.iculocalecore.preferences.extensions.unicode.keywords

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.extensions.unicode.Key
import io.github.kotlinmania.iculocalecore.extensions.unicode.Value
import io.github.kotlinmania.iculocalecore.preferences.PreferenceKey
import io.github.kotlinmania.iculocalecore.preferences.extensions.unicode.PreferencesParseError
import io.github.kotlinmania.iculocalecore.subtags.Script
import io.github.kotlinmania.iculocalecore.subtags.Subtag

/**
 * A Unicode Dictionary Break Exclusion Identifier specifies
 * scripts to be excluded from dictionary-based text break (for words and lines).
 *
 * The valid values are of one or more items of type [Script].
 */
data class DictionaryBreakScriptExclusions(
    val scripts: List<Script>,
) : PreferenceKey {
    init {
        require(scripts.isNotEmpty()) { "Script exclusions list cannot be empty" }
    }

    override fun unicodeExtensionKey(): Key = KEY

    override fun unicodeExtensionValue(): Value {
        val subtags = scripts.map { Subtag.parse(it.asString()).getOrThrow() }
        return Value.fromVecUnchecked(subtags)
    }

    companion object {
        val KEY: Key = Key("dx")

        fun tryFromValue(value: Value): Result<DictionaryBreakScriptExclusions> {
            val subtags = value.asSubtagsSlice()
            if (subtags.isEmpty()) {
                return Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))
            }
            val scripts = mutableListOf<Script>()
            for (subtag in subtags) {
                val scriptResult = Script.tryFromStr(subtag.asString())
                if (scriptResult.isFailure) {
                    return Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))
                }
                scripts.add(scriptResult.getOrThrow())
            }
            return Result.success(DictionaryBreakScriptExclusions(scripts))
        }

        fun tryFromKeyValue(key: Key, value: Value): Result<DictionaryBreakScriptExclusions?> {
            if (key != KEY) return Result.success(null)
            return tryFromValue(value)
        }

        fun tryFromStr(s: String): Result<DictionaryBreakScriptExclusions> =
            Value.tryFromStr(s).fold(
                onSuccess = { tryFromValue(it) },
                onFailure = { Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message)) },
            )

        fun parse(s: String): Result<DictionaryBreakScriptExclusions> = tryFromStr(s)
    }
}
