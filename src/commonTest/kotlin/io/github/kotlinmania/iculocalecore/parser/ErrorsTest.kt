// port-lint: tests icu_locale_core/src/parser/errors.rs
package io.github.kotlinmania.iculocalecore.parser

import kotlin.test.Test
import kotlin.test.assertEquals

class ErrorsTest {
    @Test
    fun testParseErrorVariants() {
        assertEquals("The given language subtag is invalid", ParseError.InvalidLanguage.message)
        assertEquals("Invalid subtag", ParseError.InvalidSubtag.message)
        assertEquals("Invalid extension", ParseError.InvalidExtension.message)
        assertEquals("Duplicated extension", ParseError.DuplicatedExtension.message)

        val ex = ParseException(ParseError.InvalidSubtag)
        assertEquals("Invalid subtag", ex.message)
        assertEquals(ParseError.InvalidSubtag, ex.error)
    }
}
