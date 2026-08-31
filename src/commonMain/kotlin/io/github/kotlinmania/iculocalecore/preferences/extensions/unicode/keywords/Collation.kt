// port-lint: source icu_locale_core/src/preferences/extensions/unicode/keywords/collation.rs
package io.github.kotlinmania.iculocalecore.preferences.extensions.unicode.keywords

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.extensions.unicode.Key
import io.github.kotlinmania.iculocalecore.extensions.unicode.Value
import io.github.kotlinmania.iculocalecore.preferences.PreferenceKey
import io.github.kotlinmania.iculocalecore.preferences.extensions.unicode.PreferencesParseError

/**
 * A Unicode Collation Identifier defines a type of collation (sort order).
 *
 * The valid values are listed in LDML.
 */
enum class CollationType(
    val subtag: String,
) : PreferenceKey {
    /** A previous version of the ordering, for compatibility */
    Compat("compat"),

    /** Dictionary style ordering (such as in Sinhala) */
    Dict("dict"),

    /** The default Unicode collation element table order */
    Ducet("ducet"),

    /** Recommended ordering for emoji characters */
    Emoji("emoji"),

    /** European ordering rules */
    Eor("eor"),

    /** Phonebook style ordering (such as in German) */
    Phonebk("phonebk"),

    /** Phonetic ordering (sorting based on pronunciation) */
    Phonetic("phonetic"),

    /** Pinyin ordering for Latin and for CJK characters (used in Chinese) */
    Pinyin("pinyin"),

    /** Special collation type for string search */
    Search("search"),

    /** Special collation type for Korean initial consonant search */
    Searchjl("searchjl"),

    /** Default ordering for each language */
    Standard("standard"),

    /** Pinyin ordering for Latin, stroke order for CJK characters (used in Chinese) */
    Stroke("stroke"),

    /** Traditional style ordering (such as in Spanish) */
    Trad("trad"),

    /** Pinyin ordering for Latin, Unihan radical-stroke ordering for CJK characters (used in Chinese) */
    Unihan("unihan"),

    /** Pinyin ordering for Latin, zhuyin order for Bopomofo and CJK characters (used in Chinese) */
    Zhuyin("zhuyin"),
    ;

    fun asStr(): String = subtag

    override fun unicodeExtensionKey(): Key = KEY

    override fun unicodeExtensionValue(): Value = Value.tryFromStr(subtag).getOrThrow()

    companion object {
        val KEY: Key = Key("co")

        fun tryFromValue(value: Value): Result<CollationType> {
            if (value.subtagCount() > 1) {
                return Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))
            }
            val subtag = value.getSubtag(0)?.asString() ?: "true"
            return when (subtag) {
                "compat" -> Result.success(Compat)
                "dict" -> Result.success(Dict)
                "ducet" -> Result.success(Ducet)
                "emoji" -> Result.success(Emoji)
                "eor" -> Result.success(Eor)
                "phonebk" -> Result.success(Phonebk)
                "phonetic" -> Result.success(Phonetic)
                "pinyin" -> Result.success(Pinyin)
                "search" -> Result.success(Search)
                "searchjl" -> Result.success(Searchjl)
                "standard" -> Result.success(Standard)
                "stroke" -> Result.success(Stroke)
                "trad" -> Result.success(Trad)
                "unihan" -> Result.success(Unihan)
                "zhuyin" -> Result.success(Zhuyin)
                else -> Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))
            }
        }

        fun tryFromKeyValue(key: Key, value: Value): Result<CollationType?> {
            if (key != KEY) return Result.success(null)
            return tryFromValue(value)
        }

        fun tryFromStr(s: String): Result<CollationType> =
            Value.tryFromStr(s).fold(
                onSuccess = { tryFromValue(it) },
                onFailure = { Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message)) },
            )

        fun parse(s: String): Result<CollationType> = tryFromStr(s)
    }
}

/**
 * Collation parameter key for ordering by case.
 */
enum class CollationCaseFirst(
    val subtag: String,
) : PreferenceKey {
    /** Upper case to be sorted before lower case */
    Upper("upper"),

    /** Lower case to be sorted before upper case */
    Lower("lower"),

    /** No special case ordering */
    False("false"),
    ;

    fun asStr(): String = subtag

    override fun unicodeExtensionKey(): Key = KEY

    override fun unicodeExtensionValue(): Value = Value.tryFromStr(subtag).getOrThrow()

    companion object {
        val KEY: Key = Key("kf")
        val DEFAULT: CollationCaseFirst = False

        fun tryFromValue(value: Value): Result<CollationCaseFirst> {
            if (value.subtagCount() > 1) {
                return Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))
            }
            val subtag = value.getSubtag(0)?.asString() ?: "true"
            return when (subtag) {
                "upper" -> Result.success(Upper)
                "lower" -> Result.success(Lower)
                "false" -> Result.success(False)
                else -> Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))
            }
        }

        fun tryFromKeyValue(key: Key, value: Value): Result<CollationCaseFirst?> {
            if (key != KEY) return Result.success(null)
            return tryFromValue(value)
        }

        fun tryFromStr(s: String): Result<CollationCaseFirst> =
            Value.tryFromStr(s).fold(
                onSuccess = { tryFromValue(it) },
                onFailure = { Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message)) },
            )

        fun parse(s: String): Result<CollationCaseFirst> = tryFromStr(s)
    }
}

/**
 * Collation parameter key for numeric handling.
 */
enum class CollationNumericOrdering(
    val subtag: String,
) : PreferenceKey {
    /** A sequence of decimal digits is sorted at primary level with its numeric value */
    True("true"),

    /** No special handling for numeric ordering */
    False("false"),
    ;

    fun asStr(): String = subtag

    override fun unicodeExtensionKey(): Key = KEY

    override fun unicodeExtensionValue(): Value = Value.tryFromStr(subtag).getOrThrow()

    companion object {
        val KEY: Key = Key("kn")
        val DEFAULT: CollationNumericOrdering = False

        fun tryFromValue(value: Value): Result<CollationNumericOrdering> {
            if (value.subtagCount() > 1) {
                return Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))
            }
            val subtag = value.getSubtag(0)?.asString() ?: "true"
            return when (subtag) {
                "true" -> Result.success(True)
                "false" -> Result.success(False)
                else -> Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message))
            }
        }

        fun tryFromKeyValue(key: Key, value: Value): Result<CollationNumericOrdering?> {
            if (key != KEY) return Result.success(null)
            return tryFromValue(value)
        }

        fun tryFromStr(s: String): Result<CollationNumericOrdering> =
            Value.tryFromStr(s).fold(
                onSuccess = { tryFromValue(it) },
                onFailure = { Result.failure(IllegalArgumentException(PreferencesParseError.InvalidKeywordValue.message)) },
            )

        fun parse(s: String): Result<CollationNumericOrdering> = tryFromStr(s)
    }
}
