// port-lint: source preferences/extensions/unicode/errors.rs
package io.github.kotlinmania.iculocalecore.preferences.extensions.unicode

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

// Errors related to parsing preferences.

/**
 * Error returned by parsers of Unicode extensions as preferences.
 *
 * This enum is non-exhaustive: new variants may be added in future releases.
 */
enum class PreferencesParseError(val message: String) {
    /**
     * The given keyword value is not a valid preference variant.
     */
    InvalidKeywordValue("The given keyword value is not a valid preference variant.");

    override fun toString(): String = message
}
