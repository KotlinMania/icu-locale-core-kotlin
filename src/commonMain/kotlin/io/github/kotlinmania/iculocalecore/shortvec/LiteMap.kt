// port-lint: source shortvec/litemap.rs
package io.github.kotlinmania.iculocalecore.shortvec

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

/**
 * A sorted map backed by a flat list of key-value pairs.
 *
 * In the upstream Rust implementation this is backed by [ShortBoxSlice]
 * with niche optimization. In Kotlin we use a simple mutable list since
 * Kotlin does not have the same stack-vs-heap constraints.
 *
 * Keys are maintained in sorted order. Insertion uses binary search to
 * find the correct position, ensuring the map is always sorted.
 */
internal class LiteMap<K : Comparable<K>, V> private constructor(
    private val entries: MutableList<Pair<K, V>>,
) {
    companion object {
        /** Creates a new, empty [LiteMap]. */
        fun <K : Comparable<K>, V> empty(): LiteMap<K, V> = LiteMap(mutableListOf())

        /** Creates a [LiteMap] from an iterable of pairs, sorting and deduplicating. */
        fun <K : Comparable<K>, V> fromIterable(iter: Iterable<Pair<K, V>>): LiteMap<K, V> {
            val map = LiteMap<K, V>(mutableListOf())
            for ((k, v) in iter) {
                map.insert(k, v)
            }
            return map
        }
    }

    /** Creates a new, empty [LiteMap]. */
    constructor() : this(mutableListOf())

    /** Returns the number of entries. */
    fun size(): Int = entries.size

    /** Returns whether the map is empty. */
    fun isEmpty(): Boolean = entries.isEmpty()

    /** Returns whether the map contains the given key. */
    fun containsKey(key: K): Boolean = binarySearch(key) >= 0

    /** Returns the value for the given key, or null if not present. */
    fun get(key: K): V? {
        val idx = binarySearch(key)
        return if (idx >= 0) entries[idx].second else null
    }

    /** Returns the value for the given key, or null if not present. */
    fun getOrDefault(key: K, default: V): V = get(key) ?: default

    /** Inserts a key-value pair, replacing any existing value. Returns the old value if present. */
    fun insert(key: K, value: V): V? {
        val idx = binarySearch(key)
        if (idx >= 0) {
            val old = entries[idx].second
            entries[idx] = Pair(key, value)
            return old
        }
        entries.add(-(idx + 1), Pair(key, value))
        return null
    }

    /** Tries to insert a key-value pair. Returns false if the key already exists. */
    fun tryInsert(key: K, value: V): Boolean {
        val idx = binarySearch(key)
        if (idx >= 0) return false
        entries.add(-(idx + 1), Pair(key, value))
        return true
    }

    /** Removes the entry for the given key. Returns the old value if present. */
    fun remove(key: K): V? {
        val idx = binarySearch(key)
        if (idx < 0) return null
        return entries.removeAt(idx).second
    }

    /** Removes the entry at the given index. */
    fun removeAt(index: Int): Pair<K, V> = entries.removeAt(index)

    /** Retains only entries matching the predicate. */
    fun retain(predicate: (K, V) -> Boolean) {
        entries.retainAll { (k, v) -> predicate(k, v) }
    }

    /** Returns an iterator over key-value pairs. */
    fun iter(): List<Pair<K, V>> = entries.toList()

    /** Returns an iterator over key-value pairs (live view). */
    fun iterator(): Iterator<Pair<K, V>> = entries.iterator()

    /** Returns the key at the given index. */
    fun keyAt(index: Int): K = entries[index].first

    /** Returns the value at the given index. */
    fun valueAt(index: Int): V = entries[index].second

    /** Returns a mutable reference to the value for the given key, or null if not present. */
    fun getMut(key: K): MutableValue<V>? {
        val idx = binarySearch(key)
        if (idx < 0) return null
        @Suppress("UNCHECKED_CAST")
        return MutableValue(entries as MutableList<Pair<Any?, V>>, idx)
    }

    /** Clears all entries. */
    fun clear() = entries.clear()

    private fun binarySearch(key: K): Int =
        entries.binarySearchBy(key) { it.first }

    override fun equals(other: Any?): Boolean =
        other is LiteMap<*, *> && entries == other.entries

    override fun hashCode(): Int = entries.hashCode()

    override fun toString(): String = entries.toString()

    /** A mutable reference to a value in the [LiteMap]. */
    class MutableValue<V>(
        @Suppress("UNCHECKED_CAST")
        private val entries: MutableList<Pair<Any?, V>>,
        private val index: Int,
    ) {
        /** Gets the value. */
        fun get(): V = entries[index].second

        /** Sets the value. */
        fun set(value: V) {
            val key = entries[index].first
            entries[index] = Pair(key, value)
        }
    }
}
