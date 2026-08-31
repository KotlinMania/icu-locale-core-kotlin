// port-lint: source icu_locale_core/src/preferences/extensions/unicode/keywords/line_break_word.rs
package io.github.kotlinmania.iculocalecore.preferences.extensions.unicode.keywords

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.extensions.unicode.Key
import io.github.kotlinmania.iculocalecore.extensions.unicode.Value
import io.github.kotlinmania.iculocalecore.preferences.PreferenceKey
import io.github.kotlinmania.iculocalecore.preferences.extensions.unicode.PreferencesParseError

/**
 * A Unicode Line Break Word Identifier defines preferred line break word handling behavior corresponding to the CSS level 3 word-break option.
 *
 * The valid values are listed in LDML.
 */
enum class LineBreakWordHandling(
    val subtag: String,
) : PreferenceKey {
    /** CSS lev 3 word-break=normal, normal script/language behavior for midword breaks */
    Normal("normal"),

    /** CSS lev 3 word-break=break-all, allow midword breaks unless forbidden by lb setting */
    BreakAll("breakall"),

    /** CSS lev 3 word-break=keep-all, prohibit midword breaks except for dictionary breaks */
    KeepAll("keepall"),

    /**
     * Prioritize keeping natural phrases (of multiple words) together when breaking,
     * used in short text like title and headline
     */
    Phrase("phrase"),
    ;

    fun asStr(): String = subtag

    override fun unicodeExtensionKey(): Key = KEY

    override fun unicodeExtensionValue(): Value = Value.tryFromStr(subtag).getOrThrow()

    companion object {
        val KEY: Key = Key("lw")

        fun tryFromValue(value: Value): Result<LineBreakWordHandling> {
            val subtag =
                value.asSingleSubtag()?.asString()
                    ?: return Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))
            return when (subtag) {
                "normal" -> Result.success(Normal)
                "breakall" -> Result.success(BreakAll)
                "keepall" -> Result.success(KeepAll)
                "phrase" -> Result.success(Phrase)
                else -> Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))
            }
        }

        fun tryFromKeyValue(key: Key, value: Value): Result<LineBreakWordHandling?> {
            if (key != KEY) return Result.success(null)
            return tryFromValue(value)
        }

        fun tryFromStr(s: String): Result<LineBreakWordHandling> =
            Value.tryFromStr(s).fold(
                onSuccess = { tryFromValue(it) },
                onFailure = { Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message)) },
            )

        fun parse(s: String): Result<LineBreakWordHandling> = tryFromStr(s)
    }
}
