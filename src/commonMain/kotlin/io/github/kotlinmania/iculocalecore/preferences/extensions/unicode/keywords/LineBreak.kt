// port-lint: source preferences/extensions/unicode/keywords/line_break.rs
package io.github.kotlinmania.iculocalecore.preferences.extensions.unicode.keywords

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.extensions.unicode.Key
import io.github.kotlinmania.iculocalecore.extensions.unicode.Value
import io.github.kotlinmania.iculocalecore.preferences.PreferenceKey
import io.github.kotlinmania.iculocalecore.preferences.extensions.unicode.PreferencesParseError

/**
 * A Unicode Line Break Style Identifier defines a preferred line break style corresponding to the CSS level 3 line-break option.
 *
 * The valid values are listed in LDML.
 */
enum class LineBreakStyle(
    val subtag: String,
) : PreferenceKey {
    /** CSS level 3 line-break=strict, e.g. treat CJ as NS */
    Strict("strict"),

    /** CSS level 3 line-break=normal, e.g. treat CJ as ID, break before hyphens for ja,zh */
    Normal("normal"),

    /** CSS lev 3 line-break=loose */
    Loose("loose"),
    ;

    fun asStr(): String = subtag

    override fun unicodeExtensionKey(): Key = KEY

    override fun unicodeExtensionValue(): Value = Value.tryFromStr(subtag).getOrThrow()

    companion object {
        val KEY: Key = Key("lb")

        fun tryFromValue(value: Value): Result<LineBreakStyle> {
            val subtag =
                value.asSingleSubtag()?.asString()
                    ?: return Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))
            return when (subtag) {
                "strict" -> Result.success(Strict)
                "normal" -> Result.success(Normal)
                "loose" -> Result.success(Loose)
                else -> Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))
            }
        }

        fun tryFromKeyValue(key: Key, value: Value): Result<LineBreakStyle?> {
            if (key != KEY) return Result.success(null)
            return tryFromValue(value)
        }

        fun tryFromStr(s: String): Result<LineBreakStyle> =
            Value.tryFromStr(s).fold(
                onSuccess = { tryFromValue(it) },
                onFailure = { Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message)) },
            )

        fun parse(s: String): Result<LineBreakStyle> = tryFromStr(s)
    }
}
