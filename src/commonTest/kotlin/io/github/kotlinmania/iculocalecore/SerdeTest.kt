// port-lint: tests serde.rs
package io.github.kotlinmania.iculocalecore

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.subtags.Language
import io.github.kotlinmania.iculocalecore.subtags.Region
import io.github.kotlinmania.iculocalecore.subtags.Script
import io.github.kotlinmania.serdetest.Token
import io.github.kotlinmania.serdetest.assertDeTokensError
import io.github.kotlinmania.serdetest.assertTokens
import kotlin.test.Test

class SerdeTest {
    @Test
    fun testLanguageIdentifierSerde() {
        val langid = LanguageIdentifier.parse("en-US").getOrThrow()
        assertTokens(
            langid,
            LanguageIdentifier,
            listOf(Token.Str("en-US")),
        )

        assertDeTokensError(
            LanguageIdentifier,
            listOf(Token.Str("2Xs")),
            "The given language subtag is invalid",
        )
    }

    @Test
    fun testLocaleSerde() {
        val locale = Locale.parse("en-US-u-hc-h12").getOrThrow()
        assertTokens(
            locale,
            Locale,
            listOf(Token.Str("en-US-u-hc-h12")),
        )

        assertDeTokensError(
            Locale,
            listOf(Token.Str("2Xs")),
            "The given language subtag is invalid",
        )
    }

    @Test
    fun testLanguageSerde() {
        val language = Language.parse("fr").getOrThrow()
        assertTokens(
            language,
            Language,
            listOf(Token.Str("fr")),
        )

        assertDeTokensError(
            Language,
            listOf(Token.Str("2Xs")),
            "The given language subtag is invalid",
        )
    }

    @Test
    fun testScriptSerde() {
        val script = Script.parse("Latn").getOrThrow()
        assertTokens(
            script,
            Script,
            listOf(Token.Str("Latn")),
        )

        assertDeTokensError(
            Script,
            listOf(Token.Str("2Xs")),
            "Invalid subtag",
        )
    }

    @Test
    fun testRegionSerde() {
        val region = Region.parse("US").getOrThrow()
        assertTokens(
            region,
            Region,
            listOf(Token.Str("US")),
        )

        assertDeTokensError(
            Region,
            listOf(Token.Str("2Xs")),
            "Invalid subtag",
        )
    }
}
