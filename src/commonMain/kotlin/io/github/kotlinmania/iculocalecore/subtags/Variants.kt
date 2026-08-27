// port-lint: source subtags/variants.rs
package io.github.kotlinmania.iculocalecore.subtags

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

/**
 * A list of variants (examples: `["macos", "posix"]`, etc.)
 *
 * [Variants] stores a list of [Variant] subtags in a canonical form
 * by sorting and deduplicating them.
 *
 * Examples
 * ```
 * val variants = Variants.fromVecUnchecked(listOf(Variant.parse("macos").getOrThrow(), Variant.parse("posix").getOrThrow()))
 * assertEquals(variants.toString(), "macos-posix")
 * ```
 */
data class Variants(
    val inner: List<Variant>,
) : Iterable<Variant> {
    init {
        // Variants are expected to be sorted and deduplicated by the caller.
        // We enforce this invariant here for safety.
        require(inner == inner.distinct().sorted()) {
            "Variants must be sorted and deduplicated"
        }
    }

    /** Returns a new empty list of variants. */
    companion object {
        val EMPTY: Variants = Variants(emptyList())

        /** Creates a new [Variants] set from a single [Variant]. */
        fun fromVariant(variant: Variant): Variants = Variants(listOf(variant))

        /**
         * Creates a new [Variants] set from a list.
         * The caller is expected to provide sorted and deduplicated input.
         */
        fun fromVecUnchecked(input: List<Variant>): Variants = Variants(input)

        /** Creates a [Variants] from a list, sorting and deduplicating. */
        fun fromSortedDeduped(input: List<Variant>): Variants =
            Variants(input.distinct().sorted())
    }

    /** Whether the list of variants is empty. */
    fun isEmpty(): Boolean = inner.isEmpty()

    /** Returns the number of variants. */
    fun size(): Int = inner.size

    /** Empties the [Variants] list. Returns the old list. */
    fun clear(): Variants = EMPTY

    /** Iterates over the variants. */
    override fun iterator(): Iterator<Variant> = inner.iterator()

    /** Gets the first variant, if any. */
    fun first(): Variant? = inner.firstOrNull()

    /** Iterates over variant strings. */
    fun forEachSubtagStr(f: (String) -> Unit) {
        inner.forEach { f(it.asString()) }
    }

    override fun toString(): String = inner.joinToString("-") { it.asString() }
}
