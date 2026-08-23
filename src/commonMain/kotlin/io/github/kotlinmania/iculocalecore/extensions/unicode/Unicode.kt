// port-lint: source extensions/unicode/mod.rs
package io.github.kotlinmania.iculocalecore.extensions.unicode

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.parser.ParseError
import io.github.kotlinmania.iculocalecore.parser.ParseException
import io.github.kotlinmania.iculocalecore.parser.SubtagIterator

const val UNICODE_EXT_CHAR: Char = 'u'
const val UNICODE_EXT_STR: String = "u"

/**
 * Unicode Extensions provide information about user preferences in a given locale.
 *
 * The main struct is [Unicode] which contains [Keywords] and [Attributes].
 *
 * Examples
 * ```
 * val loc = Locale.parse("en-US-u-foobar-hc-h12").getOrThrow()
 * assertEquals(loc.extensions.unicode.keywords.get(Key.parse("hc").getOrThrow()), Value.parse("h12").getOrThrow())
 * assertTrue(loc.extensions.unicode.attributes.contains(Attribute.parse("foobar").getOrThrow()))
 * ```
 */
data class Unicode(
    /** The key-value pairs. */
    val keywords: Keywords,
    /** A canonically ordered sequence of single standalone subtags. */
    val attributes: Attributes,
) : Comparable<Unicode> {
    companion object {
        /** Returns a new empty map of Unicode extensions. */
        fun empty(): Unicode = Unicode(Keywords.empty(), Attributes.empty())

        /** Returns a [Unicode] containing exactly one keyword. */
        fun fromKeywords(keywords: Keywords): Unicode = Unicode(keywords, Attributes.empty())

        /** Parses a string into a well-formed [Unicode]. */
        fun tryFromStr(s: String): Result<Unicode> = tryFromUtf8(s.encodeToByteArray())

        /** Parses a UTF-8 byte array into a well-formed [Unicode]. */
        fun tryFromUtf8(codeUnits: ByteArray): Result<Unicode> {
            val iter = SubtagIterator(codeUnits)
            val ext = iter.next() ?: return Result.failure(ParseException(ParseError.InvalidExtension))
            if (ext.size != 1 || ext[0].toInt().toChar().lowercaseChar() != UNICODE_EXT_CHAR) {
                return Result.failure(ParseException(ParseError.InvalidExtension))
            }
            return tryFromIter(iter)
        }

        /** Parses a [Unicode] from a [SubtagIterator]. */
        fun tryFromIter(iter: SubtagIterator): Result<Unicode> {
            val attrsResult = Attributes.tryFromIter(iter)
            if (attrsResult.isFailure) return Result.failure(attrsResult.exceptionOrNull()!!)
            val attributes = attrsResult.getOrThrow()

            val kwResult = Keywords.tryFromIter(iter)
            if (kwResult.isFailure) return Result.failure(kwResult.exceptionOrNull()!!)
            val keywords = kwResult.getOrThrow()

            if (attributes.isEmpty() && keywords.isEmpty()) {
                return Result.failure(ParseException(ParseError.InvalidExtension))
            }

            return Result.success(Unicode(keywords, attributes))
        }

        /** Parses a string into a well-formed [Unicode]. */
        fun parse(s: String): Result<Unicode> = tryFromStr(s)
    }

    /** Returns whether there are no keywords and no attributes. */
    fun isEmpty(): Boolean = keywords.isEmpty() && attributes.isEmpty()

    /** Clears all Unicode extension keywords and attributes. */
    fun clear(): Unicode {
        val old = Unicode(keywords, attributes)
        return old
    }

    /** Returns an ordering suitable for use in a sorted set. */
    fun totalCmp(other: Unicode): Int {
        val attrCmp = attributes.compareTo(other.attributes)
        if (attrCmp != 0) return attrCmp
        return keywords.compareTo(other.keywords)
    }

    /** Iterates subtag strings for serialization, optionally including the extension marker. */
    fun forEachSubtagStr(f: (String) -> Unit, withExt: Boolean = false) {
        if (!isEmpty()) {
            if (withExt) f(UNICODE_EXT_STR)
            attributes.forEachSubtagStr(f)
            keywords.forEachSubtagStr(f)
        }
    }

    /** Extends this [Unicode] with values from another [Unicode]. */
    fun extend(other: Unicode): Unicode {
        val newKeywords = Keywords(keywords.inner)
        newKeywords.extendFromKeywords(other.keywords)
        val newAttributes = attributes.extendFromAttributes(other.attributes)
        return Unicode(newKeywords, newAttributes)
    }

    override fun compareTo(other: Unicode): Int = totalCmp(other)

    override fun toString(): String {
        if (isEmpty()) return ""
        return buildString {
            append(UNICODE_EXT_CHAR)
            if (!attributes.isEmpty()) {
                append("-")
                append(attributes.toString())
            }
            if (!keywords.isEmpty()) {
                append("-")
                append(keywords.toString())
            }
        }
    }
}
