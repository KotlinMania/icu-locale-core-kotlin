// port-lint: source serde.rs
package io.github.kotlinmania.iculocalecore

// This file is part of ICU4X. For terms of use, please see the file
// called LICENSE at the top level of the ICU4X source tree
// (online at: https://github.com/unicode-org/icu4x/blob/main/LICENSE ).

import io.github.kotlinmania.iculocalecore.subtags.Language
import io.github.kotlinmania.iculocalecore.subtags.Region
import io.github.kotlinmania.iculocalecore.subtags.Script
import io.github.kotlinmania.iculocalecore.subtags.Variant
import io.github.kotlinmania.serde.SerdeError
import io.github.kotlinmania.serde.SerdeResult
import io.github.kotlinmania.serdecore.de.Deserializer
import io.github.kotlinmania.serdecore.de.Visitor
import io.github.kotlinmania.serdecore.ser.Serializer

fun <Ok> serializeLanguageIdentifier(
    langid: LanguageIdentifier,
    serializer: Serializer<Ok>,
): SerdeResult<Ok> = serializer.serializeStr(langid.toString())

fun <Ok> serializeLocale(
    locale: Locale,
    serializer: Serializer<Ok>,
): SerdeResult<Ok> = serializer.serializeStr(locale.toString())

fun <Ok> serializeLanguage(
    language: Language,
    serializer: Serializer<Ok>,
): SerdeResult<Ok> = serializer.serializeStr(language.asString())

fun <Ok> serializeScript(
    script: Script,
    serializer: Serializer<Ok>,
): SerdeResult<Ok> = serializer.serializeStr(script.asString())

fun <Ok> serializeRegion(
    region: Region,
    serializer: Serializer<Ok>,
): SerdeResult<Ok> = serializer.serializeStr(region.asString())

fun <Ok> serializeVariant(
    variant: Variant,
    serializer: Serializer<Ok>,
): SerdeResult<Ok> = serializer.serializeStr(variant.asString())

private class ParseVisitor<T>(
    private val expectedDescription: String,
    private val parser: (String) -> Result<T>,
) : Visitor<T> {
    override fun expecting(): String = expectedDescription

    override fun visitStr(v: String): SerdeResult<T> =
        parser(v).fold(
            onSuccess = { SerdeResult.success(it) },
            onFailure = { error ->
                SerdeResult.failure(SerdeError.custom(error.message ?: "failed to parse string"))
            },
        )
}

val LANGUAGE_IDENTIFIER_VISITOR: Visitor<LanguageIdentifier> =
    ParseVisitor("a valid Unicode Language or Locale Identifier") { s ->
        LanguageIdentifier.tryFromStr(s)
    }

val LOCALE_VISITOR: Visitor<Locale> =
    ParseVisitor("a valid Unicode Language or Locale Identifier") { s ->
        Locale.tryFromStr(s)
    }

val LANGUAGE_VISITOR: Visitor<Language> =
    ParseVisitor("a valid Language subtag") { s ->
        Language.tryFromStr(s)
    }

val SCRIPT_VISITOR: Visitor<Script> =
    ParseVisitor("a valid Script subtag") { s ->
        Script.tryFromStr(s)
    }

val REGION_VISITOR: Visitor<Region> =
    ParseVisitor("a valid Region subtag") { s ->
        Region.tryFromStr(s)
    }

val VARIANT_VISITOR: Visitor<Variant> =
    ParseVisitor("a valid Variant subtag") { s ->
        Variant.tryFromStr(s)
    }

fun <D : Deserializer> deserializeLanguageIdentifier(deserializer: D): SerdeResult<LanguageIdentifier> =
    deserializer.deserializeStr(LANGUAGE_IDENTIFIER_VISITOR)

fun <D : Deserializer> deserializeLocale(deserializer: D): SerdeResult<Locale> =
    deserializer.deserializeStr(LOCALE_VISITOR)

fun <D : Deserializer> deserializeLanguage(deserializer: D): SerdeResult<Language> =
    deserializer.deserializeStr(LANGUAGE_VISITOR)

fun <D : Deserializer> deserializeScript(deserializer: D): SerdeResult<Script> =
    deserializer.deserializeStr(SCRIPT_VISITOR)

fun <D : Deserializer> deserializeRegion(deserializer: D): SerdeResult<Region> =
    deserializer.deserializeStr(REGION_VISITOR)

fun <D : Deserializer> deserializeVariant(deserializer: D): SerdeResult<Variant> =
    deserializer.deserializeStr(VARIANT_VISITOR)
