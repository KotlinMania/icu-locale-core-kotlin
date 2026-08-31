// port-lint: tests icu_locale_core/src/preferences/extensions/unicode/keywords/mod.rs
package io.github.kotlinmania.iculocalecore.preferences.extensions.unicode.keywords

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.extensions.unicode.Key
import io.github.kotlinmania.iculocalecore.extensions.unicode.SubdivisionSuffix
import io.github.kotlinmania.iculocalecore.extensions.unicode.Value
import io.github.kotlinmania.iculocalecore.subtags.Region
import io.github.kotlinmania.iculocalecore.subtags.Script
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class KeywordsTest {
    @Test
    fun testCalendar() {
        val bud = CalendarAlgorithm.parse("buddhist").getOrThrow()
        assertEquals(CalendarAlgorithm.Buddhist, bud)
        assertEquals("buddhist", bud.unicodeExtensionValue().toString())
        assertEquals(Key("ca"), bud.unicodeExtensionKey())

        val hijriCivil = CalendarAlgorithm.parse("islamic-civil").getOrThrow()
        assertEquals(CalendarAlgorithm.Hijri(HijriCalendarAlgorithm.Civil), hijriCivil)
        assertEquals("islamic-civil", hijriCivil.unicodeExtensionValue().toString())

        val islamicAlias = CalendarAlgorithm.parse("islamicc").getOrThrow()
        assertEquals(CalendarAlgorithm.Hijri(HijriCalendarAlgorithm.Civil), islamicAlias)

        val hijriPlain = CalendarAlgorithm.parse("islamic").getOrThrow()
        assertEquals(CalendarAlgorithm.Hijri(null), hijriPlain)
        assertEquals("islamic", hijriPlain.unicodeExtensionValue().toString())

        assertTrue(CalendarAlgorithm.parse("invalid").isFailure)
    }

    @Test
    fun testCollation() {
        val col = CollationType.parse("pinyin").getOrThrow()
        assertEquals(CollationType.Pinyin, col)
        assertEquals("pinyin", col.asStr())
        assertEquals(Key("co"), col.unicodeExtensionKey())

        val caseFirst = CollationCaseFirst.parse("upper").getOrThrow()
        assertEquals(CollationCaseFirst.Upper, caseFirst)
        assertEquals(Key("kf"), caseFirst.unicodeExtensionKey())

        val numOrdering = CollationNumericOrdering.parse("true").getOrThrow()
        assertEquals(CollationNumericOrdering.True, numOrdering)
        assertEquals(Key("kn"), numOrdering.unicodeExtensionKey())
    }

    @Test
    fun testCurrency() {
        val cur = CurrencyType.parse("usd").getOrThrow()
        assertEquals("usd", cur.code)
        assertEquals(Key("cu"), cur.unicodeExtensionKey())
        assertEquals("usd", cur.unicodeExtensionValue().toString())

        assertTrue(CurrencyType.parse("toolong").isFailure)
        assertTrue(CurrencyType.parse("12").isFailure)
    }

    @Test
    fun testCurrencyFormat() {
        val cf = CurrencyFormatStyle.parse("account").getOrThrow()
        assertEquals(CurrencyFormatStyle.Account, cf)
        assertEquals(Key("cf"), cf.unicodeExtensionKey())
    }

    @Test
    fun testDictionaryBreak() {
        val dx = DictionaryBreakScriptExclusions.parse("latn-cyrl").getOrThrow()
        assertEquals(
            listOf(Script.tryFromStr("Latn").getOrThrow(), Script.tryFromStr("Cyrl").getOrThrow()),
            dx.scripts,
        )
        assertEquals(Key("dx"), dx.unicodeExtensionKey())
        assertEquals("latn-cyrl", dx.unicodeExtensionValue().toString())
    }

    @Test
    fun testEmoji() {
        val em = EmojiPresentationStyle.parse("emoji").getOrThrow()
        assertEquals(EmojiPresentationStyle.Emoji, em)
        assertEquals(Key("em"), em.unicodeExtensionKey())
    }

    @Test
    fun testFirstDay() {
        val fw = FirstDay.parse("mon").getOrThrow()
        assertEquals(FirstDay.Mon, fw)
        assertEquals(Key("fw"), fw.unicodeExtensionKey())
    }

    @Test
    fun testHourCycle() {
        val hc = HourCycle.parse("h23").getOrThrow()
        assertEquals(HourCycle.H23, hc)
        assertEquals(Key("hc"), hc.unicodeExtensionKey())
        assertEquals("h23", hc.unicodeExtensionValue().toString())
    }

    @Test
    fun testLineBreak() {
        val lb = LineBreakStyle.parse("strict").getOrThrow()
        assertEquals(LineBreakStyle.Strict, lb)
        assertEquals(Key("lb"), lb.unicodeExtensionKey())

        val lw = LineBreakWordHandling.parse("keepall").getOrThrow()
        assertEquals(LineBreakWordHandling.KeepAll, lw)
        assertEquals(Key("lw"), lw.unicodeExtensionKey())
    }

    @Test
    fun testMeasurementSystem() {
        val ms = MeasurementSystem.parse("metric").getOrThrow()
        assertEquals(MeasurementSystem.Metric, ms)
        assertEquals(Key("ms"), ms.unicodeExtensionKey())

        val mu = MeasurementUnitOverride.parse("celsius").getOrThrow()
        assertEquals(MeasurementUnitOverride.Celsius, mu)
        assertEquals(Key("mu"), mu.unicodeExtensionKey())
    }

    @Test
    fun testNumberingSystem() {
        val nu = NumberingSystem.parse("arab").getOrThrow()
        assertEquals("arab", nu.subtag.asString())
        assertEquals(Key("nu"), nu.unicodeExtensionKey())
        assertEquals("arab", nu.unicodeExtensionValue().toString())
    }

    @Test
    fun testRegionOverride() {
        val val1 = Value.tryFromStr("uksct").getOrThrow()
        val rg1 = RegionOverride.tryFromValue(val1).getOrThrow()
        assertEquals(Region.tryFromStr("UK").getOrThrow(), rg1.subdivisionId.region)
        assertEquals(SubdivisionSuffix.tryFromStr("sct").getOrThrow(), rg1.subdivisionId.suffix)

        val val2 = Value.tryFromStr("usca").getOrThrow()
        val rg2 = RegionOverride.tryFromValue(val2).getOrThrow()
        assertEquals(Region.tryFromStr("US").getOrThrow(), rg2.subdivisionId.region)
        assertEquals(SubdivisionSuffix.tryFromStr("ca").getOrThrow(), rg2.subdivisionId.suffix)

        val val3 = Value.tryFromStr("419bel").getOrThrow()
        val rg3 = RegionOverride.tryFromValue(val3).getOrThrow()
        assertEquals(Region.tryFromStr("419").getOrThrow(), rg3.subdivisionId.region)
        assertEquals(SubdivisionSuffix.tryFromStr("bel").getOrThrow(), rg3.subdivisionId.suffix)

        val val4 = Value.tryFromStr("uszzzz").getOrThrow()
        val rg4 = RegionOverride.tryFromValue(val4).getOrThrow()
        assertEquals(Region.tryFromStr("US").getOrThrow(), rg4.subdivisionId.region)
        assertEquals(SubdivisionSuffix.tryFromStr("zzzz").getOrThrow(), rg4.subdivisionId.suffix)

        for (invalid in listOf("4aabel", "a4bel", "ukabcde")) {
            assertTrue(RegionOverride.tryFromStr(invalid).isFailure)
        }
    }

    @Test
    fun testRegionalSubdivision() {
        val val1 = Value.tryFromStr("uksct").getOrThrow()
        val sd = RegionalSubdivision.tryFromValue(val1).getOrThrow()
        assertEquals(Region.tryFromStr("UK").getOrThrow(), sd.region)
        assertEquals(SubdivisionSuffix.tryFromStr("sct").getOrThrow(), sd.suffix)

        for (invalid in listOf("4aabel", "a4bel", "ukabcde")) {
            assertTrue(RegionalSubdivision.tryFromStr(invalid).isFailure)
        }
    }

    @Test
    fun testSentenceSupression() {
        val ss = SentenceBreakSupressions.parse("standard").getOrThrow()
        assertEquals(SentenceBreakSupressions.Standard, ss)
        assertEquals(Key("ss"), ss.unicodeExtensionKey())
    }

    @Test
    fun testTimeZoneShortId() {
        val tz = TimeZoneShortId.parse("uslax").getOrThrow()
        assertEquals("uslax", tz.subtag.asString())
        assertEquals(Key("tz"), tz.unicodeExtensionKey())
    }

    @Test
    fun testVariant() {
        val va = CommonVariantType.parse("posix").getOrThrow()
        assertEquals(CommonVariantType.Posix, va)
        assertEquals(Key("va"), va.unicodeExtensionKey())
    }
}
