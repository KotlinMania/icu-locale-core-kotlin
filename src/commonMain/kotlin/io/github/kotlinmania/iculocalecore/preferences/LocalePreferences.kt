// port-lint: source icu_locale_core/src/preferences/locale.rs
package io.github.kotlinmania.iculocalecore.preferences

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.DataLocale
import io.github.kotlinmania.iculocalecore.LanguageIdentifier
import io.github.kotlinmania.iculocalecore.Locale
import io.github.kotlinmania.iculocalecore.extensions.Extensions
import io.github.kotlinmania.iculocalecore.extensions.unicode.Key
import io.github.kotlinmania.iculocalecore.extensions.unicode.Value
import io.github.kotlinmania.iculocalecore.subtags.Language
import io.github.kotlinmania.iculocalecore.subtags.Region
import io.github.kotlinmania.iculocalecore.subtags.Script
import io.github.kotlinmania.iculocalecore.subtags.Subtag
import io.github.kotlinmania.iculocalecore.subtags.Variant
import io.github.kotlinmania.iculocalecore.subtags.Variants

/**
 * The structure storing locale subtags used in preferences.
 */
data class LocalePreferences(
    /** Preference of Language */
    var language: Language = Language.UNKNOWN,
    /** Preference of Script */
    var script: Script? = null,
    /** Preference of Region */
    var region: Region? = null,
    /** Preference of Variant */
    var variant: Variant? = null,
    /** Preference of Regional Subdivision */
    var subdivision: Subtag? = null,
    /** Preference of Unicode Extension Region */
    var ueRegion: Region? = null,
) {
    companion object {
        val DEFAULT: LocalePreferences = LocalePreferences()

        /**
         * Creates a [LocalePreferences] from a [Locale].
         */
        fun from(loc: Locale): LocalePreferences {
            val sd =
                loc.extensions.unicode.keywords
                    .get(Key("sd"))
                    ?.asSingleSubtag()
            val ueRegion =
                loc.extensions.unicode.keywords
                    .get(Key("rg"))
                    ?.let { v ->
                        v.asSingleSubtag()?.let { s ->
                            Region.tryFromStr(s.asString()).getOrNull()
                        }
                    }
            return LocalePreferences(
                language = loc.id.language,
                script = loc.id.script,
                region = loc.id.region,
                variant = loc.id.variants.firstOrNull(),
                subdivision = sd,
                ueRegion = ueRegion,
            )
        }

        /**
         * Creates a [LocalePreferences] from a [LanguageIdentifier].
         */
        fun from(lid: LanguageIdentifier): LocalePreferences =
            LocalePreferences(
                language = lid.language,
                script = lid.script,
                region = lid.region,
                variant = lid.variants.firstOrNull(),
                subdivision = null,
                ueRegion = null,
            )
    }

    private fun toDataLocaleMaybeRegionPriority(regionPriority: Boolean): DataLocale {
        val effectiveRegion =
            if (regionPriority && region != null && ueRegion != null) {
                ueRegion
            } else {
                region
            }
        return DataLocale(
            language = language,
            script = script,
            region = effectiveRegion,
            variant = variant,
            subdivision = subdivision,
        )
    }

    /**
     * Convert to a [DataLocale], with region-based fallback priority.
     */
    fun toDataLocaleRegionPriority(): DataLocale = toDataLocaleMaybeRegionPriority(true)

    /**
     * Convert to a [DataLocale], with language-based fallback priority.
     */
    fun toDataLocaleLanguagePriority(): DataLocale = toDataLocaleMaybeRegionPriority(false)

    /**
     * Extends the preferences with the values from another set of preferences.
     */
    fun extend(other: LocalePreferences) {
        if (!other.language.isUnknown()) {
            this.language = other.language
        }
        if (other.script != null) {
            this.script = other.script
        }
        if (other.region != null) {
            this.region = other.region
        }
        if (other.variant != null) {
            this.variant = other.variant
        }
        if (other.subdivision != null) {
            this.subdivision = other.subdivision
        }
        if (other.ueRegion != null) {
            this.ueRegion = other.ueRegion
        }
    }

    /**
     * Converts this [LocalePreferences] into a [Locale].
     */
    fun toLocale(): Locale {
        val lid =
            LanguageIdentifier(
                language = language,
                script = script,
                region = region,
                variants = variant?.let { Variants.fromVariant(it) } ?: Variants.EMPTY,
            )
        val ext = Extensions.empty()
        if (subdivision != null) {
            ext.unicode.keywords.set(
                Key("sd"),
                Value.fromSubtag(subdivision),
            )
        }
        if (ueRegion != null) {
            ext.unicode.keywords.set(
                Key("rg"),
                Value.tryFromStr(ueRegion!!.asString()).getOrThrow(),
            )
        }
        return Locale(lid, ext)
    }
}
