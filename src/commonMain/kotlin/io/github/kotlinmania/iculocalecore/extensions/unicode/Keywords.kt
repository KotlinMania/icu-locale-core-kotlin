// port-lint: source extensions/unicode/keywords.rs
package io.github.kotlinmania.iculocalecore.extensions.unicode

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.parser.SubtagIterator
import io.github.kotlinmania.iculocalecore.shortvec.LiteMap
import io.github.kotlinmania.iculocalecore.shortvec.ShortBoxSlice
import io.github.kotlinmania.iculocalecore.subtags.Subtag

/**
 * A list of [Key]-[Value] pairs representing functional information
 * about locale's internationalization preferences.
 *
 * Here are examples of fields used in Unicode:
 * - `hc` - Hour Cycle (`h11`, `h12`, `h23`, `h24`)
 * - `ca` - Calendar (`buddhist`, `gregory`, ...)
 * - `fw` - First Day Of the Week (`sun`, `mon`, `sat`, ...)
 *
 * Examples
 * ```
 * val keywords = Keywords.fromPairs(listOf(Key.parse("hc").getOrThrow() to Value.parse("h23").getOrThrow()))
 * assertEquals(keywords.toString(), "hc-h23")
 * ```
 */
data class Keywords(
    val inner: LiteMap<Key, Value>,
) : Comparable<Keywords> {
    companion object {
        /** Returns a new empty list of key-value pairs. */
        fun empty(): Keywords = Keywords(LiteMap.empty())

        /** Creates a new [Keywords] with exactly one pair. */
        fun newSingle(key: Key, value: Value): Keywords {
            val map = LiteMap<Key, Value>()
            map.insert(key, value)
            return Keywords(map)
        }

        /** Creates a [Keywords] from a list of pairs, sorting and deduplicating. */
        fun fromPairs(pairs: List<Pair<Key, Value>>): Keywords {
            val map = LiteMap.fromIterable(pairs)
            return Keywords(map)
        }

        /** Parses a string into a well-formed [Keywords]. */
        fun tryFromStr(s: String): Result<Keywords> = tryFromUtf8(s.encodeToByteArray())

        /** Parses a UTF-8 byte array into a well-formed [Keywords]. */
        fun tryFromUtf8(codeUnits: ByteArray): Result<Keywords> {
            val iter = SubtagIterator(codeUnits)
            return tryFromIter(iter)
        }

        /** Parses a string into a well-formed [Keywords]. */
        fun parse(s: String): Result<Keywords> = tryFromStr(s)

        /** Parses keywords from a [SubtagIterator]. */
        fun tryFromIter(iter: SubtagIterator): Result<Keywords> {
            val keywords = LiteMap<Key, Value>()
            var currentKeyword: Key? = null
            var currentValue: MutableList<Subtag> = mutableListOf()

            while (true) {
                val subtag = iter.peek() ?: break
                val slen = subtag.size
                if (slen == 2) {
                    val kw = currentKeyword
                    if (kw != null) {
                        keywords.insert(kw, Value.fromShortSliceUnchecked(ShortBoxSlice.fromList(currentValue.toList())))
                        currentValue = mutableListOf()
                    }
                    val keyResult = Key.tryFromUtf8(subtag)
                    if (keyResult.isFailure) return Result.failure(keyResult.exceptionOrNull()!!)
                    currentKeyword = keyResult.getOrThrow()
                } else if (currentKeyword != null) {
                    val parseResult = Value.parseSubtagFromUtf8(subtag)
                    if (parseResult.isFailure) break
                    val t = parseResult.getOrThrow()
                    if (t != null) currentValue.add(t)
                } else {
                    break
                }
                iter.next()
            }

            val kw = currentKeyword
            if (kw != null) {
                keywords.insert(kw, Value.fromShortSliceUnchecked(ShortBoxSlice.fromList(currentValue.toList())))
            }

            return Result.success(Keywords(keywords))
        }
    }

    /** Returns whether there are no keywords. */
    fun isEmpty(): Boolean = inner.isEmpty()

    /** Returns whether the list contains a [Value] for the specified [Key]. */
    fun containsKey(key: Key): Boolean = inner.containsKey(key)

    /** Returns the [Value] corresponding to the [Key], or null if not present. */
    fun get(key: Key): Value? = inner.get(key)

    /** Sets the specified keyword, returning the old value if it existed. */
    fun set(key: Key, value: Value): Value? = inner.insert(key, value)

    /** Removes the specified keyword, returning the old value if it existed. */
    fun remove(key: Key): Value? = inner.remove(key)

    /** Clears all keywords. Returns the old keywords. */
    fun clear(): Keywords {
        val old = Keywords(inner)
        inner.clear()
        return old
    }

    /** Retains a subset of keywords as specified by the predicate. */
    fun retainByKey(predicate: (Key) -> Boolean) {
        inner.retain { k, _ -> predicate(k) }
    }

    /** Returns an ordered iterator over key-value pairs. */
    fun iter(): List<Pair<Key, Value>> = inner.iter()

    /** Extends the [Keywords] with values from another [Keywords]. */
    fun extendFromKeywords(other: Keywords) {
        for ((key, value) in other.inner.iter()) {
            inner.insert(key, value)
        }
    }

    /** Iterates subtag strings for serialization. */
    fun forEachSubtagStr(f: (String) -> Unit) {
        for ((k, v) in inner.iter()) {
            f(k.asString())
            v.forEachSubtagStr(f)
        }
    }

    /** Compare with BCP-47 bytes. */
    fun strictCmp(other: ByteArray): Int {
        val self = toString().encodeToByteArray()
        val lenCmp = self.size.compareTo(other.size)
        if (lenCmp != 0) return lenCmp
        for (i in self.indices) {
            val byteCmp = self[i].toInt().compareTo(other[i].toInt())
            if (byteCmp != 0) return byteCmp
        }
        return 0
    }

    override fun compareTo(other: Keywords): Int {
        val a = inner.iter()
        val b = other.inner.iter()
        val sizeCmp = a.size.compareTo(b.size)
        if (sizeCmp != 0) return sizeCmp
        for (i in a.indices) {
            val keyCmp = a[i].first.compareTo(b[i].first)
            if (keyCmp != 0) return keyCmp
            val valCmp = a[i].second.compareTo(b[i].second)
            if (valCmp != 0) return valCmp
        }
        return 0
    }

    override fun toString(): String =
        buildString {
            for ((k, v) in inner.iter()) {
                if (isNotEmpty()) append("-")
                append(k.asString())
                val vs = v.toString()
                if (vs.isNotEmpty()) {
                    append("-").append(vs)
                }
            }
        }
}
