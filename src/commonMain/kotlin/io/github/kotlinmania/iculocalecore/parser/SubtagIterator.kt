// port-lint: source parser/mod.rs
package io.github.kotlinmania.iculocalecore.parser

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

/**
 * A helper iterator for [LanguageIdentifier] and [Locale] parsing.
 *
 * The iterator is eager and fallible, allowing it to reject invalid slices
 * such as `"-"`, `"-en"`, `"en-"` etc.
 */
class SubtagIterator(private var remaining: ByteArray) {
    private var current: ByteArray? = skipBeforeSeparator(remaining)

    companion object {
        private fun skipBeforeSeparator(slice: ByteArray): ByteArray {
            var end = 0
            while (end < slice.size && slice[end] != '-'.code.toByte()) {
                end++
            }
            return slice.copyOfRange(0, end)
        }
    }

    /** Returns the current subtag without advancing. */
    fun peek(): ByteArray? = current

    /** Returns the next subtag and advances the iterator. */
    fun next(): ByteArray? {
        val result = current ?: return null
        current = if (result.size < remaining.size) {
            remaining = remaining.copyOfRange(result.size + 1, remaining.size)
            skipBeforeSeparator(remaining)
        } else {
            null
        }
        return result
    }

    /** Converts the iterator to a list of subtag strings. */
    fun toList(): List<String> {
        val result = mutableListOf<String>()
        while (true) {
            val subtag = next() ?: break
            result.add(subtag.decodeToString())
        }
        return result
    }

    private fun skipBeforeSeparator(slice: ByteArray): ByteArray {
        var end = 0
        while (end < slice.size && slice[end] != '-'.code.toByte()) {
            end++
        }
        return slice.copyOfRange(0, end)
    }
}