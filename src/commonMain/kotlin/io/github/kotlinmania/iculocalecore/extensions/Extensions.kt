// port-lint: source extensions/mod.rs
package io.github.kotlinmania.iculocalecore.extensions

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.extensions.other.Other
import io.github.kotlinmania.iculocalecore.extensions.private.Private
import io.github.kotlinmania.iculocalecore.extensions.private.PRIVATE_EXT_CHAR
import io.github.kotlinmania.iculocalecore.extensions.transform.Transform
import io.github.kotlinmania.iculocalecore.extensions.transform.TRANSFORM_EXT_CHAR
import io.github.kotlinmania.iculocalecore.extensions.unicode.Unicode
import io.github.kotlinmania.iculocalecore.extensions.unicode.UNICODE_EXT_CHAR
import io.github.kotlinmania.iculocalecore.parser.ParseError
import io.github.kotlinmania.iculocalecore.parser.ParseException
import io.github.kotlinmania.iculocalecore.parser.SubtagIterator

/**
 * Defines the type of extension.
 */
sealed class ExtensionType {
    /** Transform Extension Type marked as `t`. */
    object Transform : ExtensionType()

    /** Unicode Extension Type marked as `u`. */
    object Unicode : ExtensionType()

    /** Private Extension Type marked as `x`. */
    object Private : ExtensionType()

    /** All other extension types. */
    data class Other(val byte: Byte) : ExtensionType()

    companion object {
        /** Tries to create an [ExtensionType] from a byte slice. */
        fun tryFromByteSlice(key: ByteArray): Result<ExtensionType> {
            if (key.size != 1) return Result.failure(ParseException(ParseError.InvalidExtension))
            return tryFromByte(key[0])
        }

        /** Tries to create an [ExtensionType] from a single byte. */
        fun tryFromByte(key: Byte): Result<ExtensionType> {
            val lower = key.toInt().toChar().lowercaseChar().code.toByte()
            return when (lower.toInt().toChar()) {
                UNICODE_EXT_CHAR -> Result.success(ExtensionType.Unicode)
                TRANSFORM_EXT_CHAR -> Result.success(ExtensionType.Transform)
                PRIVATE_EXT_CHAR -> Result.success(ExtensionType.Private)
                in 'a'..'z' -> Result.success(ExtensionType.Other(lower))
                else -> Result.failure(ParseException(ParseError.InvalidExtension))
            }
        }

        /** Tries to create an [ExtensionType] from UTF-8 code units. */
        fun tryFromUtf8(codeUnits: ByteArray): Result<ExtensionType> {
            if (codeUnits.size != 1) return Result.failure(ParseException(ParseError.InvalidExtension))
            return tryFromByte(codeUnits[0])
        }
    }
}

/**
 * A map of extensions associated with a given [Locale].
 *
 * There are four types of extensions:
 *  - [Unicode] Extensions - marked as `u`
 *  - [Transform] Extensions - marked as `t`
 *  - [Private] Use Extensions - marked as `x`
 *  - [Other] Extensions - marked as any `a-z` except `u`, `t`, and `x`
 */
data class Extensions(
    /** Unicode extension data. */
    val unicode: Unicode,
    /** Transform extension data. */
    val transform: Transform,
    /** Private-use extension data. */
    val private: Private,
    /** Other extension data, sorted alphabetically by extension key. */
    val other: List<Other>,
) {
    companion object {
        /** Returns a new empty map of extensions. */
        fun empty(): Extensions = Extensions(
            Unicode.empty(),
            Transform.empty(),
            Private.empty(),
            emptyList(),
        )

        /** Creates an [Extensions] containing exactly one unicode extension. */
        fun fromUnicode(unicode: Unicode): Extensions = Extensions(
            unicode,
            Transform.empty(),
            Private.empty(),
            emptyList(),
        )

        /** Parses an [Extensions] from a [SubtagIterator]. */
        fun tryFromIter(iter: SubtagIterator): Result<Extensions> {
            var unicode: Unicode? = null
            var transform: Transform? = null
            var private: Private? = null
            val other = mutableListOf<Other>()

            while (true) {
                val subtag = iter.next() ?: break
                if (subtag.isEmpty()) return Result.failure(ParseException(ParseError.InvalidExtension))
                if (subtag.size != 1) return Result.failure(ParseException(ParseError.InvalidExtension))

                val extTypeResult = ExtensionType.tryFromByte(subtag[0])
                if (extTypeResult.isFailure) return Result.failure(extTypeResult.exceptionOrNull()!!)
                val extType = extTypeResult.getOrThrow()

                when (extType) {
                    is ExtensionType.Unicode -> {
                        if (unicode != null) return Result.failure(ParseException(ParseError.DuplicatedExtension))
                        val result = Unicode.tryFromIter(iter)
                        if (result.isFailure) return Result.failure(result.exceptionOrNull()!!)
                        unicode = result.getOrThrow()
                    }
                    is ExtensionType.Transform -> {
                        if (transform != null) return Result.failure(ParseException(ParseError.DuplicatedExtension))
                        val result = Transform.tryFromIter(iter)
                        if (result.isFailure) return Result.failure(result.exceptionOrNull()!!)
                        transform = result.getOrThrow()
                    }
                    is ExtensionType.Private -> {
                        if (private != null) return Result.failure(ParseException(ParseError.DuplicatedExtension))
                        val result = Private.tryFromIter(iter)
                        if (result.isFailure) return Result.failure(result.exceptionOrNull()!!)
                        private = result.getOrThrow()
                    }
                    is ExtensionType.Other -> {
                        if (other.any { it.getExtByte() == extType.byte }) {
                            return Result.failure(ParseException(ParseError.DuplicatedExtension))
                        }
                        val result = Other.tryFromIter(extType.byte, iter)
                        if (result.isFailure) return Result.failure(result.exceptionOrNull()!!)
                        val parsed = result.getOrThrow()
                        val insertIdx = other.binarySearch(parsed)
                        if (insertIdx < 0) {
                            other.add(-(insertIdx + 1), parsed)
                        } else {
                            return Result.failure(ParseException(ParseError.InvalidExtension))
                        }
                    }
                }
            }

            return Result.success(Extensions(
                unicode ?: Unicode.empty(),
                transform ?: Transform.empty(),
                private ?: Private.empty(),
                other,
            ))
        }
    }

    /** Returns whether there are no extensions present. */
    fun isEmpty(): Boolean =
        unicode.isEmpty() && transform.isEmpty() && private.isEmpty() && other.all { it.isEmpty() }

    /** Returns an ordering suitable for use in a sorted set. */
    fun totalCmp(otherExt: Extensions): Int = toString().compareTo(otherExt.toString())

    /** Retains the specified extension types, clearing all others. */
    fun retainByType(predicate: (ExtensionType) -> Boolean): Extensions {
        val newUnicode = if (predicate(ExtensionType.Unicode)) unicode else Unicode.empty()
        val newTransform = if (predicate(ExtensionType.Transform)) transform else Transform.empty()
        val newPrivate = if (predicate(ExtensionType.Private)) private else Private.empty()
        val newOther = other.filter { predicate(ExtensionType.Other(it.getExtByte())) }
        return Extensions(newUnicode, newTransform, newPrivate, newOther)
    }

    /** Iterates subtag strings for serialization. */
    fun forEachSubtagStr(f: (String) -> Unit) {
        var wroteTu = false

        for (o in other) {
            if (o.getExt() > TRANSFORM_EXT_CHAR && !wroteTu) {
                transform.forEachSubtagStr(f, true)
                unicode.forEachSubtagStr(f, true)
                wroteTu = true
            }
            o.forEachSubtagStr(f, true)
        }

        if (!wroteTu) {
            transform.forEachSubtagStr(f, true)
            unicode.forEachSubtagStr(f, true)
        }

        private.forEachSubtagStr(f, true)
    }

    override fun toString(): String = buildString {
        forEachSubtagStr { s ->
            if (isNotEmpty()) append("-")
            append(s)
        }
    }
}