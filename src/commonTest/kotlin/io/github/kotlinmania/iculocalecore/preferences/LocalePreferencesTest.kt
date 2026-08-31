// port-lint: tests icu_locale_core/src/preferences/locale.rs
package io.github.kotlinmania.iculocalecore.preferences

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.LanguageIdentifier
import io.github.kotlinmania.iculocalecore.Locale
import io.github.kotlinmania.iculocalecore.subtags.Language
import io.github.kotlinmania.iculocalecore.subtags.Region
import io.github.kotlinmania.iculocalecore.subtags.Script
import io.github.kotlinmania.iculocalecore.subtags.Subtag
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LocalePreferencesTest {
    @Test
    fun testDefault() {
        val prefs = LocalePreferences()
        assertEquals(Language.UNKNOWN, prefs.language)
        assertNull(prefs.script)
        assertNull(prefs.region)
        assertNull(prefs.variant)
        assertNull(prefs.subdivision)
        assertNull(prefs.ueRegion)
    }

    @Test
    fun testFromLocale() {
        val loc = Locale.tryFromStr("en-Latn-US-u-sd-usca-rg-gbzzzz").getOrThrow()
        val prefs = LocalePreferences.from(loc)

        assertEquals(Language.tryFromStr("en").getOrThrow(), prefs.language)
        assertEquals(Script.tryFromStr("Latn").getOrThrow(), prefs.script)
        assertEquals(Region.tryFromStr("US").getOrThrow(), prefs.region)
        assertEquals(Subtag.tryFromStr("usca").getOrThrow(), prefs.subdivision)
        assertNull(prefs.ueRegion)
    }

    @Test
    fun testFromLanguageIdentifier() {
        val lid = LanguageIdentifier.tryFromStr("fr-CA").getOrThrow()
        val prefs = LocalePreferences.from(lid)

        assertEquals(Language.tryFromStr("fr").getOrThrow(), prefs.language)
        assertNull(prefs.script)
        assertEquals(Region.tryFromStr("CA").getOrThrow(), prefs.region)
        assertNull(prefs.subdivision)
        assertNull(prefs.ueRegion)
    }

    @Test
    fun testExtend() {
        val prefs1 =
            LocalePreferences(
                language = Language.tryFromStr("en").getOrThrow(),
                region = Region.tryFromStr("US").getOrThrow(),
            )
        val prefs2 =
            LocalePreferences(
                script = Script.tryFromStr("Latn").getOrThrow(),
                region = Region.tryFromStr("GB").getOrThrow(),
            )
        prefs1.extend(prefs2)

        assertEquals(Language.tryFromStr("en").getOrThrow(), prefs1.language)
        assertEquals(Script.tryFromStr("Latn").getOrThrow(), prefs1.script)
        assertEquals(Region.tryFromStr("GB").getOrThrow(), prefs1.region)
    }

    @Test
    fun testDataLocalePriorities() {
        val prefs =
            LocalePreferences(
                language = Language.tryFromStr("en").getOrThrow(),
                region = Region.tryFromStr("US").getOrThrow(),
                ueRegion = Region.tryFromStr("GB").getOrThrow(),
            )

        val dlRegionPriority = prefs.toDataLocaleRegionPriority()
        assertEquals(Region.tryFromStr("GB").getOrThrow(), dlRegionPriority.region)

        val dlLanguagePriority = prefs.toDataLocaleLanguagePriority()
        assertEquals(Region.tryFromStr("US").getOrThrow(), dlLanguagePriority.region)
    }

    @Test
    fun testToLocale() {
        val prefs =
            LocalePreferences(
                language = Language.tryFromStr("ja").getOrThrow(),
                subdivision = Subtag.tryFromStr("jptokyo").getOrNull(),
                ueRegion = Region.tryFromStr("JP").getOrThrow(),
            )
        val loc = prefs.toLocale()
        assertEquals("ja", loc.id.toString())
        assertEquals(Region.tryFromStr("JP").getOrThrow(), loc.id.region ?: prefs.ueRegion)
    }
}
