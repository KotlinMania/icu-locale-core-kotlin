// port-lint: source extensions/unicode/attributes.rs
package io.github.kotlinmania.iculocalecore.extensions.unicode

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.parser.SubtagIterator
import io.github.kotlinmania.iculocalecore.shortvec.ShortBoxSlice

/**
 * A set of [Attribute] elements as defined in Unicode Extension Attributes.
 *
 * The attributes are maintained in sorted order.
 *
 * Examples
 * ```
 * val attr1 = Attribute.parse("foobar").getOrThrow()
 * val attr2 = Attribute.parse("testing").getOrThrow()
 * val attributes = Attributes.fromVecUnchecked(listOf(attr1, attr2))
 * assertEquals(attributes.toString(), "foobar-testing")
 * ```
 */
class Attributes internal constructor(
    internal val inner: ShortBoxSlice<Attribute>,
) : Comparable<Attributes> {
    companion object {
        /** Returns a new empty set of attributes. */
        fun empty(): Attributes = Attributes(ShortBoxSlice.empty())

        /** Creates an [Attributes] from a pre-sorted list. */
        internal fun fromVecUnchecked(input: List<Attribute>): Attributes =
            Attributes(ShortBoxSlice.fromList(input))

        /** Parses a string into a well-formed [Attributes]. */
        fun tryFromStr(s: String): Result<Attributes> = tryFromUtf8(s.encodeToByteArray())

        /** Parses a string into a well-formed [Attributes]. */
        fun parse(s: String): Result<Attributes> = tryFromStr(s)

        /** Parses a UTF-8 byte array into a well-formed [Attributes]. */
        fun tryFromUtf8(codeUnits: ByteArray): Result<Attributes> {
            val iter = SubtagIterator(codeUnits)
            return tryFromIter(iter)
        }

        /** Parses attributes from a [SubtagIterator]. */
        fun tryFromIter(iter: SubtagIterator): Result<Attributes> {
            val attrs = mutableListOf<Attribute>()
            while (true) {
                val subtag = iter.peek() ?: break
                val attrResult = Attribute.tryFromUtf8(subtag)
                if (attrResult.isSuccess) {
                    val attr = attrResult.getOrThrow()
                    val searchResult = attrs.binarySearch(attr)
                    if (searchResult < 0) {
                        attrs.add(-(searchResult + 1), attr)
                    }
                } else {
                    break
                }
                iter.next()
            }
            return Result.success(Attributes(ShortBoxSlice.fromList(attrs)))
        }
    }

    /** Returns whether there are no attributes. */
    fun isEmpty(): Boolean = inner.isEmpty()

    /** Returns the number of attributes. */
    fun size(): Int = inner.size()

    /** Returns whether the set contains the given attribute. */
    fun contains(attr: Attribute): Boolean {
        for (a in inner) {
            if (a == attr) return true
        }
        return false
    }

    /** Returns the attribute at the given index. */
    operator fun get(index: Int): Attribute = inner[index]

    /** Returns an iterator over the attributes. */
    fun iter(): Iterator<Attribute> = inner.iterator()

    /** Empties the [Attributes] list. Returns the old list. */
    fun clear(): Attributes {
        val old = Attributes(inner)
        return old
    }

    /** Binary search for an attribute. Returns the index or -(insertionPoint + 1). */
    fun binarySearch(attr: Attribute): Int {
        val list = inner.toList()
        return list.binarySearch(attr)
    }

    /** Extends the [Attributes] with values from another [Attributes]. */
    fun extendFromAttributes(other: Attributes): Attributes {
        val current = inner.toList().toMutableList()
        for (attr in other.inner) {
            val searchResult = current.binarySearch(attr)
            if (searchResult < 0) {
                current.add(-(searchResult + 1), attr)
            }
        }
        return Attributes(ShortBoxSlice.fromList(current))
    }

    /** Iterates subtag strings for serialization. */
    fun forEachSubtagStr(f: (String) -> Unit) {
        for (a in inner) {
            f(a.asString())
        }
    }

    override fun compareTo(other: Attributes): Int {
        val a = inner.toList()
        val b = other.inner.toList()
        val sizeCmp = a.size.compareTo(b.size)
        if (sizeCmp != 0) return sizeCmp
        for (i in a.indices) {
            val cmp = a[i].compareTo(b[i])
            if (cmp != 0) return cmp
        }
        return 0
    }

    override fun equals(other: Any?): Boolean =
        other is Attributes && inner == other.inner

    override fun hashCode(): Int = inner.hashCode()

    override fun toString(): String = inner.toList().joinToString("-") { it.asString() }
}
