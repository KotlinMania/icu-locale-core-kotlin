// port-lint: source preferences/extensions/unicode/keywords/emoji.rs
package io.github.kotlinmania.iculocalecore.preferences.extensions.unicode.keywords

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.extensions.unicode.Key
import io.github.kotlinmania.iculocalecore.extensions.unicode.Value
import io.github.kotlinmania.iculocalecore.preferences.PreferenceKey
import io.github.kotlinmania.iculocalecore.preferences.extensions.unicode.PreferencesParseError

/**
 * A Unicode Emoji Presentation Style Identifier.
 *
 * It specifies a request for the preferred emoji presentation style.
 * The valid values are listed in LDML.
 */
enum class EmojiPresentationStyle(
    val subtag: String,
) : PreferenceKey {
    /** Use an emoji presentation for emoji characters if possible */
    Emoji("emoji"),

    /** Use a text presentation for emoji characters if possible */
    Text("text"),

    /** Use the default presentation for emoji characters as specified in UTR #51 Presentation Style */
    Default("default"),
    ;

    fun asStr(): String = subtag

    override fun unicodeExtensionKey(): Key = KEY

    override fun unicodeExtensionValue(): Value = Value.tryFromStr(subtag).getOrThrow()

    companion object {
        val KEY: Key = Key("em")
        val DEFAULT: EmojiPresentationStyle = Default

        fun tryFromValue(value: Value): Result<EmojiPresentationStyle> {
            val subtag =
                value.asSingleSubtag()?.asString()
                    ?: return Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))
            return when (subtag) {
                "emoji" -> Result.success(Emoji)
                "text" -> Result.success(Text)
                "default" -> Result.success(Default)
                else -> Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))
            }
        }

        fun tryFromKeyValue(key: Key, value: Value): Result<EmojiPresentationStyle?> {
            if (key != KEY) return Result.success(null)
            return tryFromValue(value)
        }

        fun tryFromStr(s: String): Result<EmojiPresentationStyle> =
            Value.tryFromStr(s).fold(
                onSuccess = { tryFromValue(it) },
                onFailure = { Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message)) },
            )

        fun parse(s: String): Result<EmojiPresentationStyle> = tryFromStr(s)
    }
}
