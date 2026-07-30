// port-lint: source extensions/transform/mod.rs
package io.github.kotlinmania.iculocalecore.extensions.transform

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.LanguageIdentifier
import io.github.kotlinmania.iculocalecore.parser.ParseError
import io.github.kotlinmania.iculocalecore.parser.ParseException
import io.github.kotlinmania.iculocalecore.parser.ParserMode
import io.github.kotlinmania.iculocalecore.parser.SubtagIterator
import io.github.kotlinmania.iculocalecore.parser.parseLanguageIdentifierFromIter
import io.github.kotlinmania.iculocalecore.shortvec.LiteMap
import io.github.kotlinmania.iculocalecore.shortvec.ShortBoxSlice
import io.github.kotlinmania.iculocalecore.subtags.Language
import io.github.kotlinmania.iculocalecore.subtags.Subtag

const val TRANSFORM_EXT_CHAR: Char = 't'
const val TRANSFORM_EXT_STR: String = "t"

/**
 * Transform Extensions provide information on content transformations in a given locale.
 *
 * The main struct is [Transform] which contains [Fields] and an optional [LanguageIdentifier].
 *
 * Examples
 * ```
 * val loc = Locale.parse("en-US-t-es-ar-h0-hybrid").getOrThrow()
 * val lang = LanguageIdentifier.parse("es-AR").getOrThrow()
 * val key = Key.parse("h0").getOrThrow()
 * val value = Value.parse("hybrid").getOrThrow()
 * assertEquals(loc.extensions.transform.lang, lang)
 * assertEquals(loc.extensions.transform.fields.get(key), value)
 * ```
 */
data class Transform(
    /** The [LanguageIdentifier] specified with this locale extension, or null if not present. */
    val lang: LanguageIdentifier?,
    /** The key-value pairs. */
    val fields: Fields,
) : Comparable<Transform> {
    companion object {
        /** Returns a new empty map of Transform extensions. */
        fun empty(): Transform = Transform(null, Fields.empty())

        /** Parses a string into a well-formed [Transform]. */
        fun tryFromStr(s: String): Result<Transform> = tryFromUtf8(s.encodeToByteArray())

        /** Parses a UTF-8 byte array into a well-formed [Transform]. */
        fun tryFromUtf8(codeUnits: ByteArray): Result<Transform> {
            val iter = SubtagIterator(codeUnits)
            val ext = iter.next() ?: return Result.failure(ParseException(ParseError.InvalidExtension))
            if (ext.size != 1 || ext[0].toInt().toChar().lowercaseChar() != TRANSFORM_EXT_CHAR) {
                return Result.failure(ParseException(ParseError.InvalidExtension))
            }
            return tryFromIter(iter)
        }

        /** Parses a [Transform] from a [SubtagIterator]. */
        fun tryFromIter(iter: SubtagIterator): Result<Transform> {
            var tlang: LanguageIdentifier? = null
            val tfields = LiteMap<Key, Value>()

            val peek = iter.peek()
            if (peek != null && Language.tryFromUtf8(peek).isSuccess) {
                val langResult = parseLanguageIdentifierFromIter(iter, ParserMode.Partial)
                if (langResult.isFailure) return Result.failure(langResult.exceptionOrNull()!!)
                tlang = langResult.getOrThrow()
            }

            var currentTkey: Key? = null
            var currentTvalue: MutableList<Subtag> = mutableListOf()
            var hasCurrentTvalue = false

            while (true) {
                val subtag = iter.peek() ?: break
                val tkey = currentTkey
                if (tkey != null) {
                    val valResult = Value.parseSubtag(subtag)
                    if (valResult.isSuccess) {
                        hasCurrentTvalue = true
                        val v = valResult.getOrThrow()
                        if (v != null) currentTvalue.add(v)
                    } else {
                        if (!hasCurrentTvalue) {
                            return Result.failure(ParseException(ParseError.InvalidExtension))
                        }
                        tfields.insert(tkey, Value.fromShortSliceUnchecked(ShortBoxSlice.fromList(currentTvalue.toList())))
                        currentTkey = null
                        currentTvalue = mutableListOf()
                        hasCurrentTvalue = false
                        continue
                    }
                } else {
                    val keyResult = Key.tryFromUtf8(subtag)
                    if (keyResult.isSuccess) {
                        currentTkey = keyResult.getOrThrow()
                    } else {
                        break
                    }
                }
                iter.next()
            }

            val finalKey = currentTkey
            if (finalKey != null) {
                if (!hasCurrentTvalue) {
                    return Result.failure(ParseException(ParseError.InvalidExtension))
                }
                tfields.insert(finalKey, Value.fromShortSliceUnchecked(ShortBoxSlice.fromList(currentTvalue.toList())))
            }

            if (tlang == null && tfields.isEmpty()) {
                return Result.failure(ParseException(ParseError.InvalidExtension))
            }

            return Result.success(Transform(tlang, Fields(tfields)))
        }

        /** Parses a string into a well-formed [Transform]. */
        fun parse(s: String): Result<Transform> = tryFromStr(s)
    }

    /** Returns whether there are no fields and no lang. */
    fun isEmpty(): Boolean = lang == null && fields.isEmpty()

    /** Clears the transform extension. */
    fun clear(): Transform {
        val old = Transform(lang, fields)
        return old
    }

    /** Returns an ordering suitable for use in a sorted set. */
    fun totalCmp(other: Transform): Int {
        val langCmp = compareNullableLang(lang, other.lang)
        if (langCmp != 0) return langCmp
        return fields.compareTo(other.fields)
    }

    /** Iterates subtag strings for serialization, optionally including the extension marker. */
    fun forEachSubtagStr(f: (String) -> Unit, withExt: Boolean = false) {
        if (isEmpty()) return
        if (withExt) f(TRANSFORM_EXT_STR)
        lang?.let { l ->
            for (s in l.toString().lowercase().split("-")) {
                f(s)
            }
        }
        fields.forEachSubtagStr(f)
    }

    override fun compareTo(other: Transform): Int = totalCmp(other)

    override fun toString(): String {
        if (isEmpty()) return ""
        return buildString {
            append(TRANSFORM_EXT_CHAR)
            lang?.let { l ->
                append("-")
                append(l.toString().lowercase())
            }
            if (!fields.isEmpty()) {
                append("-")
                append(fields.toString())
            }
        }
    }
}

private fun compareNullableLang(a: LanguageIdentifier?, b: LanguageIdentifier?): Int =
    if (a == null && b == null) 0
    else if (a == null) -1
    else if (b == null) 1
    else a.totalCmp(b)