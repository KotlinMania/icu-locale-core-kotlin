// port-lint: source preferences/mod.rs
package io.github.kotlinmania.iculocalecore.preferences

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.extensions.unicode.Key
import io.github.kotlinmania.iculocalecore.extensions.unicode.Value

/**
 * A low-level interface implemented on each preference exposed in component preferences.
 *
 * [PreferenceKey] has to be implemented on preferences that are to be included
 * in Formatter preferences. The interface may be implemented to indicate that the
 * given preference has a Unicode key corresponding to it or be a custom one.
 */
interface PreferenceKey {
    /** Retrieve Unicode extension key corresponding to a given preference. */
    fun unicodeExtensionKey(): Key? = null

    /** Retrieve Unicode extension value corresponding to the given instance of the preference. */
    fun unicodeExtensionValue(): Value? = null
}
