// port-lint: source shortvec/mod.rs
package io.github.kotlinmania.iculocalecore.shortvec

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

/**
 * A boxed slice that supports no-allocation, constant values if length 0 or 1.
 *
 * In the upstream Rust implementation this uses niche optimization via
 * `ShortBoxSliceInner<T>` to keep stack size at 16 bytes. In Kotlin we use
 * a simple list-backed approach since Kotlin does not have the same
 * stack-vs-heap constraints.
 */
class ShortBoxSlice<T> private constructor(
    private val items: List<T>,
) : Iterable<T> {
    companion object {
        /** Creates a new, empty [ShortBoxSlice]. */
        fun <T> empty(): ShortBoxSlice<T> = ShortBoxSlice(emptyList())

        /** Creates a new [ShortBoxSlice] containing a single element. */
        fun <T> of(item: T): ShortBoxSlice<T> = ShortBoxSlice(listOf(item))

        /** Creates a new [ShortBoxSlice] from a list. */
        fun <T> fromList(items: List<T>): ShortBoxSlice<T> = ShortBoxSlice(items.toList())
    }

    /** Returns the number of elements in the collection. */
    fun size(): Int = items.size

    /** Returns whether the collection is empty. */
    fun isEmpty(): Boolean = items.isEmpty()

    /** Gets the element at the specified index. */
    operator fun get(index: Int): T = items[index]

    /** Gets a single element from the [ShortBoxSlice]. Returns null if empty or more than one element. */
    fun single(): T? = if (items.size == 1) items[0] else null

    /** Destruct into a single element of the [ShortBoxSlice]. Returns null if empty or more than one element. */
    fun intoSingle(): T? = single()

    /** Pushes an element onto this [ShortBoxSlice], returning a new instance. */
    fun push(item: T): ShortBoxSlice<T> = ShortBoxSlice(items + item)

    /** Inserts an element at the specified index, returning a new instance. */
    fun insert(index: Int, item: T): ShortBoxSlice<T> {
        val mutable = items.toMutableList()
        mutable.add(index, item)
        return ShortBoxSlice(mutable.toList())
    }

    /** Removes the element at the specified index, returning a pair of the new slice and the removed element. */
    fun removeAt(index: Int): Pair<ShortBoxSlice<T>, T> {
        val mutable = items.toMutableList()
        val removed = mutable.removeAt(index)
        return ShortBoxSlice(mutable.toList()) to removed
    }

    /** Removes all elements, returning an empty slice. */
    fun clear(): ShortBoxSlice<T> = ShortBoxSlice(emptyList())

    /** Retains only elements matching the predicate, returning a new instance. */
    fun retain(predicate: (T) -> Boolean): ShortBoxSlice<T> =
        ShortBoxSlice(items.filter(predicate))

    /** Returns the underlying list. */
    fun toList(): List<T> = items

    override fun iterator(): Iterator<T> = items.iterator()

    override fun equals(other: Any?): Boolean =
        other is ShortBoxSlice<*> && items == other.items

    override fun hashCode(): Int = items.hashCode()

    override fun toString(): String = items.toString()
}
