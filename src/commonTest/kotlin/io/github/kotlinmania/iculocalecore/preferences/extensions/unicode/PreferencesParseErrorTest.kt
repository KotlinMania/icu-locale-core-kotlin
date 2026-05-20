package io.github.kotlinmania.iculocalecore.preferences.extensions.unicode

import kotlin.test.Test
import kotlin.test.assertEquals

class PreferencesParseErrorTest {
    @Test
    fun invalidKeywordValueUsesUpstreamDisplayMessage() {
        assertEquals(
            "The given keyword value is not a valid preference variant.",
            PreferencesParseError.InvalidKeywordValue.toString(),
        )
    }
}
