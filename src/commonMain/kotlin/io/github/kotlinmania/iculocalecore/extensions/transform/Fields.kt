// port-lint: source icu_locale_core/src/extensions/transform/fields.rs
package io.github.kotlinmania.iculocalecore.extensions.transform

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.shortvec.LiteMap

/**
 * A list of [Key]-[Value] pairs representing functional information
 * about content transformations.
 *
 * Examples
 * ```
 * val value = Value.parse("hybrid").getOrThrow()
 * val fields = Fields.fromPairs(listOf(Key.parse("h0").getOrThrow() to value))
 * assertEquals(fields.toString(), "h0-hybrid")
 * ```
 */
class Fields internal constructor(
    internal val inner: LiteMap<Key, Value>,
) : Comparable<Fields> {
    companion object {
        /** Returns a new empty list of key-value pairs. */
        fun empty(): Fields = Fields(LiteMap.empty())

        /** Creates a [Fields] from a list of pairs, sorting and deduplicating. */
        internal fun fromPairs(pairs: List<Pair<Key, Value>>): Fields {
            val map = LiteMap.fromIterable(pairs)
            return Fields(map)
        }
    }

    /** Returns whether there are no fields. */
    fun isEmpty(): Boolean = inner.isEmpty()

    /** Clears all fields. Returns the old fields. */
    fun clear(): Fields {
        val old = Fields(inner)
        inner.clear()
        return old
    }

    /** Returns whether the list contains a [Value] for the specified [Key]. */
    fun containsKey(key: Key): Boolean = inner.containsKey(key)

    /** Returns the [Value] corresponding to the [Key], or null if not present. */
    fun get(key: Key): Value? = inner.get(key)

    /** Sets the specified keyword, returning the old value if it existed. */
    fun set(key: Key, value: Value): Value? = inner.insert(key, value)

    /** Retains a subset of fields as specified by the predicate. */
    fun retainByKey(predicate: (Key) -> Boolean) {
        inner.retain { k, _ -> predicate(k) }
    }

    /** Iterates subtag strings for serialization. */
    fun forEachSubtagStr(f: (String) -> Unit) {
        for ((k, v) in inner.iter()) {
            f(k.asString())
            v.forEachSubtagStr(f)
        }
    }

    override fun compareTo(other: Fields): Int {
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

    override fun equals(other: Any?): Boolean =
        other is Fields && inner == other.inner

    override fun hashCode(): Int = inner.hashCode()

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
