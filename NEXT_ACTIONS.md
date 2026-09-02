# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 53/73 (72.6%)
- **Function parity:** 145/329 matched (target 585) — 44.1%
- **Class/type parity:** 24/69 matched (target 91) — 34.8%
- **Combined symbol parity:** 169/398 matched (target 676) — 42.5%
- **Average inline-code cosine:** 0.13 (function body across 42 matched files)
- **Average documentation cosine:** 0.72 (doc text across 42 matched files)
- **Cheat-zeroed Files:** 33
- **Critical Issues:** 52 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

1. **macros.enum_keyword** (13 deps)
   - Path: `icu_locale_core/src/preferences/extensions/unicode/macros/enum_keyword.rs`
   - Essential for 13 other files

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. icu_locale_core.locale

- **Target:** `iculocalecore.Locale [PROVENANCE-FALLBACK]`
- **Similarity:** 0.25
- **Dependents:** 5
- **Priority Score:** 5081707.5
- **Functions:** 8/15 matched (target 14)
- **Missing functions:** `test_sizes`, `as_tuple`, `try_from_utf8_with_single_variant_single_keyword_unicode_extension`, `from_str`, `from`, `fmt`, `test_writeable`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `Err`
- **Tests:** 0/1 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `locale.rs` vs expected `locale.rs`
- **Proposed provenance header:** `// port-lint: source locale.rs` (current: `// port-lint: source locale.rs`)
- **Lint issues:** 1

### 2. unicode.value

- **Target:** `unicode.Value [PROVENANCE-FALLBACK]`
- **Similarity:** 0.49
- **Dependents:** 4
- **Priority Score:** 4072605.2
- **Functions:** 18/22 matched (target 24)
- **Missing functions:** `into_iter`, `from_iter`, `extend`, `from_str`
- **Types:** 1/4 matched (target 1)
- **Missing types:** `Item`, `IntoIter`, `Err`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `extensions/unicode/value.rs` vs expected `extensions/unicode/value.rs`
- **Proposed provenance header:** `// port-lint: source extensions/unicode/value.rs` (current: `// port-lint: source extensions/unicode/value.rs`)
- **Lint issues:** 1

### 3. shortvec.litemap

- **Target:** `shortvec.LiteMap [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 3
- **Priority Score:** 3252510.0
- **Functions:** 0/21 matched (target 33)
- **Missing functions:** `lm_get_range`, `lm_len`, `lm_is_empty`, `lm_get`, `lm_last`, `lm_binary_search_by`, `lm_sort_from_iter`, `lm_with_capacity`, `lm_reserve`, `lm_get_mut`, `lm_push`, `lm_insert`, `lm_remove`, `lm_clear`, `lm_retain`, `lm_extend`, `lm_iter`, `lm_iter_mut`, `lm_into_iter`, `test_short_slice_impl`, `test_short_slice_impl_full`
- **Types:** 0/4 matched (target 3)
- **Missing types:** `Slice`, `KeyValueIter`, `KeyValueIterMut`, `KeyValueIntoIter`
- **Tests:** 0/2 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `shortvec/litemap.rs` vs expected `shortvec/litemap.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:shortvec/litemap.rs` vs expected `shortvec/litemap.rs`
- **Proposed provenance header:** `// port-lint: source shortvec/litemap.rs` (current: `// port-lint: source shortvec/litemap.rs`)
- **Proposed provenance header:** `// port-lint: tests shortvec/litemap.rs` (current: `// port-lint: tests shortvec/litemap.rs`)
- **Lint issues:** 2

### 4. subtags.variants

- **Target:** `subtags.Variants [PROVENANCE-FALLBACK]`
- **Similarity:** 0.30
- **Dependents:** 2
- **Priority Score:** 2041007.0
- **Functions:** 5/8 matched (target 15)
- **Missing functions:** `new`, `from_short_slice_unchecked`, `deref`
- **Types:** 1/2 matched
- **Missing types:** `Target`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `subtags/variants.rs` vs expected `subtags/variants.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:subtags/variants.rs` vs expected `subtags/variants.rs`
- **Proposed provenance header:** `// port-lint: source subtags/variants.rs` (current: `// port-lint: source subtags/variants.rs`)
- **Proposed provenance header:** `// port-lint: tests subtags/variants.rs` (current: `// port-lint: tests subtags/variants.rs`)
- **Lint issues:** 2

### 5. subtags.region

- **Target:** `subtags.Region [PROVENANCE-FALLBACK]`
- **Similarity:** 0.27
- **Dependents:** 2
- **Priority Score:** 2000107.4
- **Functions:** 1/1 matched (target 14)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 2)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `subtags/region.rs` vs expected `subtags/region.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:subtags/region.rs` vs expected `subtags/region.rs`
- **Proposed provenance header:** `// port-lint: source subtags/region.rs` (current: `// port-lint: source subtags/region.rs`)
- **Proposed provenance header:** `// port-lint: tests subtags/region.rs` (current: `// port-lint: tests subtags/region.rs`)
- **Lint issues:** 2

### 6. unicode.key

- **Target:** `unicode.Key [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 2
- **Priority Score:** 2000010.0
- **Functions:** 0/0 matched (target 6)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `extensions/unicode/key.rs` vs expected `extensions/unicode/key.rs`
- **Proposed provenance header:** `// port-lint: source extensions/unicode/key.rs` (current: `// port-lint: source extensions/unicode/key.rs`)
- **Lint issues:** 1

### 7. unicode.keywords

- **Target:** `unicode.Keywords [PROVENANCE-FALLBACK]`
- **Similarity:** 0.48
- **Dependents:** 1
- **Priority Score:** 1082405.2
- **Functions:** 15/22 matched
- **Missing functions:** `new`, `get_mut`, `from_tuple_vec`, `from`, `from_iter`, `from_str`, `test_keywords_fromstr`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `Err`
- **Tests:** 0/2 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `extensions/unicode/keywords.rs` vs expected `extensions/unicode/keywords.rs`
- **Proposed provenance header:** `// port-lint: source extensions/unicode/keywords.rs` (current: `// port-lint: source extensions/unicode/keywords.rs`)
- **Lint issues:** 1

### 8. unicode.attributes

- **Target:** `unicode.Attributes [PROVENANCE-FALLBACK]`
- **Similarity:** 0.35
- **Dependents:** 1
- **Priority Score:** 1061406.5
- **Functions:** 7/11 matched (target 19)
- **Missing functions:** `new`, `from_str`, `deref`, `test_attributes_fromstr`
- **Types:** 1/3 matched (target 1)
- **Missing types:** `Err`, `Target`
- **Tests:** 0/1 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `extensions/unicode/attributes.rs` vs expected `extensions/unicode/attributes.rs`
- **Proposed provenance header:** `// port-lint: source extensions/unicode/attributes.rs` (current: `// port-lint: source extensions/unicode/attributes.rs`)
- **Lint issues:** 1

### 9. transform.fields

- **Target:** `transform.Fields [PROVENANCE-FALLBACK]`
- **Similarity:** 0.42
- **Dependents:** 1
- **Priority Score:** 1051305.8
- **Functions:** 7/11 matched (target 13)
- **Missing functions:** `new`, `from_tuple_vec`, `from`, `from_iter`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `Inner`
- **Tests:** 0/1 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `extensions/transform/fields.rs` vs expected `extensions/transform/fields.rs`
- **Proposed provenance header:** `// port-lint: source extensions/transform/fields.rs` (current: `// port-lint: source extensions/transform/fields.rs`)
- **Lint issues:** 1

### 10. subtags.script

- **Target:** `subtags.Script [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1010110.0
- **Functions:** 0/1 matched (target 12)
- **Missing functions:** `from`
- **Types:** 0/0 matched (target 2)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `subtags/script.rs` vs expected `subtags/script.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:subtags/script.rs` vs expected `subtags/script.rs`
- **Proposed provenance header:** `// port-lint: source subtags/script.rs` (current: `// port-lint: source subtags/script.rs`)
- **Proposed provenance header:** `// port-lint: tests subtags/script.rs` (current: `// port-lint: tests subtags/script.rs`)
- **Lint issues:** 2

### 11. subtags.language

- **Target:** `subtags.Language [PROVENANCE-FALLBACK]`
- **Similarity:** 0.64
- **Dependents:** 1
- **Priority Score:** 1000103.6
- **Functions:** 1/1 matched (target 15)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 2)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `subtags/language.rs` vs expected `subtags/language.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:subtags/language.rs` vs expected `subtags/language.rs`
- **Proposed provenance header:** `// port-lint: source subtags/language.rs` (current: `// port-lint: source subtags/language.rs`)
- **Proposed provenance header:** `// port-lint: tests subtags/language.rs` (current: `// port-lint: tests subtags/language.rs`)
- **Lint issues:** 2

### 12. private.other

- **Target:** `private.PrivateSubtag [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1000010.0
- **Functions:** 0/0 matched (target 6)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `extensions/private/other.rs` vs expected `extensions/private/other.rs`
- **Proposed provenance header:** `// port-lint: source extensions/private/other.rs` (current: `// port-lint: source extensions/private/other.rs`)
- **Lint issues:** 1

### 13. subtags.variant

- **Target:** `subtags.Variant [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1000010.0
- **Functions:** 0/0 matched (target 14)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 2)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `subtags/variant.rs` vs expected `subtags/variant.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:subtags/variant.rs` vs expected `subtags/variant.rs`
- **Proposed provenance header:** `// port-lint: source subtags/variant.rs` (current: `// port-lint: source subtags/variant.rs`)
- **Proposed provenance header:** `// port-lint: tests subtags/variant.rs` (current: `// port-lint: tests subtags/variant.rs`)
- **Lint issues:** 2

### 14. unicode.attribute

- **Target:** `unicode.Attribute [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1000010.0
- **Functions:** 0/0 matched (target 6)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `extensions/unicode/attribute.rs` vs expected `extensions/unicode/attribute.rs`
- **Proposed provenance header:** `// port-lint: source extensions/unicode/attribute.rs` (current: `// port-lint: source extensions/unicode/attribute.rs`)
- **Lint issues:** 1

### 15. shortvec.mod

- **Target:** `shortvec.ShortBoxSlice [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 202810.0
- **Functions:** 7/21 matched (target 25)
- **Missing functions:** `default`, `new`, `new_single`, `new_double`, `len`, `remove`, `deref`, `deref_mut`, `from`, `from_iter`, `next`, `into_iter`, `test_new_single_const`, `test_get_single`
- **Types:** 1/7 matched (target 2)
- **Missing types:** `ShortBoxSliceInner`, `Target`, `ShortBoxSliceIntoIter`, `ShortBoxSliceIntoIterInner`, `Item`, `IntoIter`
- **Tests:** 0/2 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `shortvec/mod.rs` vs expected `shortvec/mod.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:shortvec/mod.rs` vs expected `shortvec/mod.rs`
- **Proposed provenance header:** `// port-lint: source shortvec/mod.rs` (current: `// port-lint: source shortvec/mod.rs`)
- **Proposed provenance header:** `// port-lint: tests shortvec/mod.rs` (current: `// port-lint: tests shortvec/mod.rs`)
- **Lint issues:** 2

### 16. icu_locale_core.data

- **Target:** `iculocalecore.DataLocale [PROVENANCE-FALLBACK]`
- **Similarity:** 0.09
- **Dependents:** 0
- **Priority Score:** 121709.1
- **Functions:** 4/14 matched (target 17)
- **Missing functions:** `default`, `fmt`, `from`, `from_str`, `try_from_str`, `for_each_subtag_str`, `as_tuple`, `into_locale`, `test_data_locale_to_string`, `test_data_locale_from_string`
- **Types:** 1/3 matched (target 2)
- **Missing types:** `Err`, `TestCase`
- **Tests:** 0/2 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `data.rs` vs expected `data.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:data.rs` vs expected `data.rs`
- **Proposed provenance header:** `// port-lint: source data.rs` (current: `// port-lint: source data.rs`)
- **Proposed provenance header:** `// port-lint: tests data.rs` (current: `// port-lint: tests data.rs`)
- **Lint issues:** 2

### 17. icu_locale_core.langid

- **Target:** `iculocalecore.LanguageIdentifier [PROVENANCE-FALLBACK]`
- **Similarity:** 0.19
- **Dependents:** 0
- **Priority Score:** 112008.1
- **Functions:** 8/18 matched (target 33)
- **Missing functions:** `try_from_utf8_with_single_variant`, `normalize_utf8`, `as_tuple`, `for_each_subtag_str`, `for_each_subtag_str_lowercased`, `write_lowercased_to`, `fmt`, `from_str`, `test_writeable`, `from`
- **Types:** 1/2 matched
- **Missing types:** `Err`
- **Tests:** 0/1 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `langid.rs` vs expected `langid.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:langid.rs` vs expected `langid.rs`
- **Proposed provenance header:** `// port-lint: source langid.rs` (current: `// port-lint: source langid.rs`)
- **Proposed provenance header:** `// port-lint: tests langid.rs` (current: `// port-lint: tests langid.rs`)
- **Lint issues:** 2

### 18. subtags.mod

- **Target:** `subtags.Subtag [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 91010.0
- **Functions:** 1/9 matched (target 10)
- **Missing functions:** `len`, `from_tinystr_unvalidated`, `as_tinystr`, `to_ascii_lowercase`, `try_from`, `eq`, `test_subtag`, `test_subtag_from_tinystr`
- **Types:** 0/1 matched
- **Missing types:** `Error`
- **Tests:** 0/2 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `subtags/mod.rs` vs expected `subtags/mod.rs`
- **Proposed provenance header:** `// port-lint: source subtags/mod.rs` (current: `// port-lint: source subtags/mod.rs`)
- **Lint issues:** 1

### 19. private.mod

- **Target:** `private.Private [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 81610.0
- **Functions:** 7/13 matched (target 17)
- **Missing functions:** `new`, `from_str`, `write_to`, `writeable_length_hint`, `deref`, `test_private_extension_fromstr`
- **Types:** 1/3 matched (target 1)
- **Missing types:** `Err`, `Target`
- **Tests:** 0/1 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `extensions/private/mod.rs` vs expected `extensions/private/mod.rs`
- **Proposed provenance header:** `// port-lint: source extensions/private/mod.rs` (current: `// port-lint: source extensions/private/mod.rs`)
- **Lint issues:** 1

### 20. unicode.mod

- **Target:** `unicode.Unicode [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 71610.0
- **Functions:** 8/14 matched (target 13)
- **Missing functions:** `new`, `as_tuple`, `from_str`, `write_to`, `writeable_length_hint`, `test_unicode_extension_fromstr`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `Err`
- **Tests:** 0/1 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `extensions/unicode/mod.rs` vs expected `extensions/unicode/mod.rs`
- **Proposed provenance header:** `// port-lint: source extensions/unicode/mod.rs` (current: `// port-lint: source extensions/unicode/mod.rs`)
- **Lint issues:** 1

### 21. transform.mod

- **Target:** `transform.Transform [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 71510.0
- **Functions:** 7/13 matched (target 12)
- **Missing functions:** `new`, `as_tuple`, `from_str`, `write_to`, `writeable_length_hint`, `test_transform_extension_fromstr`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `Err`
- **Tests:** 0/1 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `extensions/transform/mod.rs` vs expected `extensions/transform/mod.rs`
- **Proposed provenance header:** `// port-lint: source extensions/transform/mod.rs` (current: `// port-lint: source extensions/transform/mod.rs`)
- **Lint issues:** 1

### 22. parser.mod

- **Target:** `parser.SubtagIterator [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 71110.0
- **Functions:** 3/9 matched (target 10)
- **Missing functions:** `new`, `next_const`, `slice_to_str`, `subtag_iterator_peek_test`, `subtag_iterator_test`, `skip_before_separator_test`
- **Types:** 1/2 matched
- **Missing types:** `Item`
- **Tests:** 0/4 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `parser/mod.rs` vs expected `parser/mod.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:parser/mod.rs` vs expected `parser/mod.rs`
- **Proposed provenance header:** `// port-lint: source parser/mod.rs` (current: `// port-lint: source parser/mod.rs`)
- **Proposed provenance header:** `// port-lint: tests parser/mod.rs` (current: `// port-lint: tests parser/mod.rs`)
- **Lint issues:** 2

### 23. other.mod

- **Target:** `other.Other [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 51510.0
- **Functions:** 9/13 matched (target 20)
- **Missing functions:** `from_str`, `write_to`, `writeable_length_hint`, `test_other_extension_fromstr`
- **Types:** 1/2 matched
- **Missing types:** `Err`
- **Tests:** 0/1 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `extensions/other/mod.rs` vs expected `extensions/other/mod.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:extensions/other/mod.rs` vs expected `extensions/other/mod.rs`
- **Proposed provenance header:** `// port-lint: source extensions/other/mod.rs` (current: `// port-lint: source extensions/other/mod.rs`)
- **Proposed provenance header:** `// port-lint: tests extensions/other/mod.rs` (current: `// port-lint: tests extensions/other/mod.rs`)
- **Lint issues:** 2

### 24. unicode.subdivision

- **Target:** `unicode.SubdivisionId [PROVENANCE-FALLBACK]`
- **Similarity:** 0.32
- **Dependents:** 0
- **Priority Score:** 51006.8
- **Functions:** 4/8 matched (target 13)
- **Missing functions:** `write_to`, `writeable_length_hint`, `from_str`, `test_subdivisionid_fromstr`
- **Types:** 1/2 matched
- **Missing types:** `Err`
- **Tests:** 0/1 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `extensions/unicode/subdivision.rs` vs expected `extensions/unicode/subdivision.rs`
- **Proposed provenance header:** `// port-lint: source extensions/unicode/subdivision.rs` (current: `// port-lint: source extensions/unicode/subdivision.rs`)
- **Lint issues:** 1

### 25. transform.value

- **Target:** `transform.Value [PROVENANCE-FALLBACK]`
- **Similarity:** 0.40
- **Dependents:** 0
- **Priority Score:** 41106.0
- **Functions:** 6/9 matched (target 11)
- **Missing functions:** `from_str`, `test_writeable`, `test_short_tvalue`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `Err`
- **Tests:** 0/2 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `extensions/transform/value.rs` vs expected `extensions/transform/value.rs`
- **Proposed provenance header:** `// port-lint: source extensions/transform/value.rs` (current: `// port-lint: source extensions/transform/value.rs`)
- **Lint issues:** 1

### 26. extensions.mod

- **Target:** `extensions.Extensions [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 31410.0
- **Functions:** 9/12 matched (target 11)
- **Missing functions:** `new`, `as_tuple`, `test_writeable`
- **Types:** 2/2 matched (target 6)
- **Missing types:** _none_
- **Tests:** 0/1 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `extensions/mod.rs` vs expected `extensions/mod.rs`
- **Proposed provenance header:** `// port-lint: source extensions/mod.rs` (current: `// port-lint: source extensions/mod.rs`)
- **Lint issues:** 1

### 27. preferences.locale

- **Target:** `preferences.LocalePreferences [PROVENANCE-FALLBACK]`
- **Similarity:** 0.47
- **Dependents:** 0
- **Priority Score:** 30905.3
- **Functions:** 5/8 matched (target 13)
- **Missing functions:** `default`, `language`, `region`
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `preferences/locale.rs` vs expected `preferences/locale.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:preferences/locale.rs` vs expected `preferences/locale.rs`
- **Proposed provenance header:** `// port-lint: source preferences/locale.rs` (current: `// port-lint: source preferences/locale.rs`)
- **Proposed provenance header:** `// port-lint: tests preferences/locale.rs` (current: `// port-lint: tests preferences/locale.rs`)
- **Lint issues:** 2

### 28. parser.langid

- **Target:** `parser.LanguageIdParser [PROVENANCE-FALLBACK]`
- **Similarity:** 0.38
- **Dependents:** 0
- **Priority Score:** 20606.2
- **Functions:** 2/4 matched (target 3)
- **Missing functions:** `parse_locale_with_single_variant_single_keyword_unicode_extension_from_iter`, `parse_language_identifier_with_single_variant`
- **Types:** 2/2 matched
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `parser/langid.rs` vs expected `parser/langid.rs`
- **Proposed provenance header:** `// port-lint: source parser/langid.rs` (current: `// port-lint: source parser/langid.rs`)
- **Lint issues:** 1

### 29. preferences.mod

- **Target:** `preferences.PreferenceKey [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10410.0
- **Functions:** 2/3 matched (target 2)
- **Missing functions:** `try_from_key_value`
- **Types:** 1/1 matched
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `preferences/mod.rs` vs expected `preferences/mod.rs`
- **Proposed provenance header:** `// port-lint: source preferences/mod.rs` (current: `// port-lint: source preferences/mod.rs`)
- **Lint issues:** 1

### 30. parser.locale

- **Target:** `parser.LocaleParser [PROVENANCE-FALLBACK]`
- **Similarity:** 0.36
- **Dependents:** 0
- **Priority Score:** 10206.4
- **Functions:** 1/2 matched (target 1)
- **Missing functions:** `parse_locale_with_single_variant_single_keyword_unicode_keyword_extension`
- **Types:** 0/0 matched
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `parser/locale.rs` vs expected `parser/locale.rs`
- **Proposed provenance header:** `// port-lint: source parser/locale.rs` (current: `// port-lint: source parser/locale.rs`)
- **Lint issues:** 1

### 31. keywords.region_override

- **Target:** `keywords.RegionOverride [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10110.0
- **Functions:** 0/1 matched (target 6)
- **Missing functions:** `region_override_test`
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Tests:** 0/1 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `preferences/extensions/unicode/keywords/region_override.rs` vs expected `preferences/extensions/unicode/keywords/region_override.rs`
- **Proposed provenance header:** `// port-lint: source preferences/extensions/unicode/keywords/region_override.rs` (current: `// port-lint: source preferences/extensions/unicode/keywords/region_override.rs`)
- **Lint issues:** 1

### 32. keywords.regional_subdivision

- **Target:** `keywords.RegionalSubdivision [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10110.0
- **Functions:** 0/1 matched (target 6)
- **Missing functions:** `region_subdivision_test`
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Tests:** 0/1 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `preferences/extensions/unicode/keywords/regional_subdivision.rs` vs expected `preferences/extensions/unicode/keywords/regional_subdivision.rs`
- **Proposed provenance header:** `// port-lint: source preferences/extensions/unicode/keywords/regional_subdivision.rs` (current: `// port-lint: source preferences/extensions/unicode/keywords/regional_subdivision.rs`)
- **Lint issues:** 1

### 33. parser.errors

- **Target:** `parser.Errors [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 110.0
- **Functions:** 0/0 matched (target 2)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 3)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `parser/errors.rs` vs expected `parser/errors.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:parser/errors.rs` vs expected `parser/errors.rs`
- **Proposed provenance header:** `// port-lint: source parser/errors.rs` (current: `// port-lint: source parser/errors.rs`)
- **Proposed provenance header:** `// port-lint: tests parser/errors.rs` (current: `// port-lint: tests parser/errors.rs`)
- **Lint issues:** 2

### 34. unicode.errors

- **Target:** `unicode.Errors [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 110.0
- **Functions:** 0/0 matched (target 2)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `preferences/extensions/unicode/errors.rs` vs expected `preferences/extensions/unicode/errors.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:preferences/extensions/unicode/errors.rs` vs expected `preferences/extensions/unicode/errors.rs`
- **Proposed provenance header:** `// port-lint: source preferences/extensions/unicode/errors.rs` (current: `// port-lint: source preferences/extensions/unicode/errors.rs`)
- **Proposed provenance header:** `// port-lint: tests preferences/extensions/unicode/errors.rs` (current: `// port-lint: tests preferences/extensions/unicode/errors.rs`)
- **Lint issues:** 2

### 35. keywords.measurement_unit_override

- **Target:** `keywords.MeasurementUnitOverride [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 7)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `preferences/extensions/unicode/keywords/measurement_unit_override.rs` vs expected `preferences/extensions/unicode/keywords/measurement_unit_override.rs`
- **Proposed provenance header:** `// port-lint: source preferences/extensions/unicode/keywords/measurement_unit_override.rs` (current: `// port-lint: source preferences/extensions/unicode/keywords/measurement_unit_override.rs`)
- **Lint issues:** 1

### 36. keywords.emoji

- **Target:** `keywords.Emoji [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 7)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `preferences/extensions/unicode/keywords/emoji.rs` vs expected `preferences/extensions/unicode/keywords/emoji.rs`
- **Proposed provenance header:** `// port-lint: source preferences/extensions/unicode/keywords/emoji.rs` (current: `// port-lint: source preferences/extensions/unicode/keywords/emoji.rs`)
- **Lint issues:** 1

### 37. keywords.variant

- **Target:** `keywords.Variant [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 7)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `preferences/extensions/unicode/keywords/variant.rs` vs expected `preferences/extensions/unicode/keywords/variant.rs`
- **Proposed provenance header:** `// port-lint: source preferences/extensions/unicode/keywords/variant.rs` (current: `// port-lint: source preferences/extensions/unicode/keywords/variant.rs`)
- **Lint issues:** 1

### 38. keywords.calendar

- **Target:** `keywords.Calendar [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 8)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 16)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `preferences/extensions/unicode/keywords/calendar.rs` vs expected `preferences/extensions/unicode/keywords/calendar.rs`
- **Proposed provenance header:** `// port-lint: source preferences/extensions/unicode/keywords/calendar.rs` (current: `// port-lint: source preferences/extensions/unicode/keywords/calendar.rs`)
- **Lint issues:** 1

### 39. keywords.currency

- **Target:** `keywords.Currency [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 6)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `preferences/extensions/unicode/keywords/currency.rs` vs expected `preferences/extensions/unicode/keywords/currency.rs`
- **Proposed provenance header:** `// port-lint: source preferences/extensions/unicode/keywords/currency.rs` (current: `// port-lint: source preferences/extensions/unicode/keywords/currency.rs`)
- **Lint issues:** 1

### 40. keywords.timezone

- **Target:** `keywords.Timezone [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 6)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `preferences/extensions/unicode/keywords/timezone.rs` vs expected `preferences/extensions/unicode/keywords/timezone.rs`
- **Proposed provenance header:** `// port-lint: source preferences/extensions/unicode/keywords/timezone.rs` (current: `// port-lint: source preferences/extensions/unicode/keywords/timezone.rs`)
- **Lint issues:** 1

### 41. keywords.first_day

- **Target:** `keywords.FirstDay [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 7)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `preferences/extensions/unicode/keywords/first_day.rs` vs expected `preferences/extensions/unicode/keywords/first_day.rs`
- **Proposed provenance header:** `// port-lint: source preferences/extensions/unicode/keywords/first_day.rs` (current: `// port-lint: source preferences/extensions/unicode/keywords/first_day.rs`)
- **Lint issues:** 1

### 42. keywords.collation

- **Target:** `keywords.Collation [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 21)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 3)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `preferences/extensions/unicode/keywords/collation.rs` vs expected `preferences/extensions/unicode/keywords/collation.rs`
- **Proposed provenance header:** `// port-lint: source preferences/extensions/unicode/keywords/collation.rs` (current: `// port-lint: source preferences/extensions/unicode/keywords/collation.rs`)
- **Lint issues:** 1

### 43. keywords.hour_cycle

- **Target:** `keywords.HourCycle [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 7)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `preferences/extensions/unicode/keywords/hour_cycle.rs` vs expected `preferences/extensions/unicode/keywords/hour_cycle.rs`
- **Proposed provenance header:** `// port-lint: source preferences/extensions/unicode/keywords/hour_cycle.rs` (current: `// port-lint: source preferences/extensions/unicode/keywords/hour_cycle.rs`)
- **Lint issues:** 1

### 44. keywords.line_break

- **Target:** `keywords.LineBreak [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 7)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `preferences/extensions/unicode/keywords/line_break.rs` vs expected `preferences/extensions/unicode/keywords/line_break.rs`
- **Proposed provenance header:** `// port-lint: source preferences/extensions/unicode/keywords/line_break.rs` (current: `// port-lint: source preferences/extensions/unicode/keywords/line_break.rs`)
- **Lint issues:** 1

### 45. keywords.line_break_word

- **Target:** `keywords.LineBreakWord [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 7)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `preferences/extensions/unicode/keywords/line_break_word.rs` vs expected `preferences/extensions/unicode/keywords/line_break_word.rs`
- **Proposed provenance header:** `// port-lint: source preferences/extensions/unicode/keywords/line_break_word.rs` (current: `// port-lint: source preferences/extensions/unicode/keywords/line_break_word.rs`)
- **Lint issues:** 1

### 46. keywords.currency_format

- **Target:** `keywords.CurrencyFormat [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 7)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `preferences/extensions/unicode/keywords/currency_format.rs` vs expected `preferences/extensions/unicode/keywords/currency_format.rs`
- **Proposed provenance header:** `// port-lint: source preferences/extensions/unicode/keywords/currency_format.rs` (current: `// port-lint: source preferences/extensions/unicode/keywords/currency_format.rs`)
- **Lint issues:** 1

### 47. keywords.mod

- **Target:** `keywords.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 16)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `preferences/extensions/unicode/keywords/mod.rs` vs expected `preferences/extensions/unicode/keywords/mod.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:preferences/extensions/unicode/keywords/mod.rs` vs expected `preferences/extensions/unicode/keywords/mod.rs`
- **Proposed provenance header:** `// port-lint: source preferences/extensions/unicode/keywords/mod.rs` (current: `// port-lint: source preferences/extensions/unicode/keywords/mod.rs`)
- **Proposed provenance header:** `// port-lint: tests preferences/extensions/unicode/keywords/mod.rs` (current: `// port-lint: tests preferences/extensions/unicode/keywords/mod.rs`)
- **Lint issues:** 2

### 48. keywords.numbering_system

- **Target:** `keywords.NumberingSystem [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 6)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `preferences/extensions/unicode/keywords/numbering_system.rs` vs expected `preferences/extensions/unicode/keywords/numbering_system.rs`
- **Proposed provenance header:** `// port-lint: source preferences/extensions/unicode/keywords/numbering_system.rs` (current: `// port-lint: source preferences/extensions/unicode/keywords/numbering_system.rs`)
- **Lint issues:** 1

### 49. keywords.dictionary_break

- **Target:** `keywords.DictionaryBreak [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 6)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `preferences/extensions/unicode/keywords/dictionary_break.rs` vs expected `preferences/extensions/unicode/keywords/dictionary_break.rs`
- **Proposed provenance header:** `// port-lint: source preferences/extensions/unicode/keywords/dictionary_break.rs` (current: `// port-lint: source preferences/extensions/unicode/keywords/dictionary_break.rs`)
- **Lint issues:** 1

### 50. keywords.measurement_system

- **Target:** `keywords.MeasurementSystem [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 7)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `preferences/extensions/unicode/keywords/measurement_system.rs` vs expected `preferences/extensions/unicode/keywords/measurement_system.rs`
- **Proposed provenance header:** `// port-lint: source preferences/extensions/unicode/keywords/measurement_system.rs` (current: `// port-lint: source preferences/extensions/unicode/keywords/measurement_system.rs`)
- **Lint issues:** 1

### 51. keywords.sentence_supression

- **Target:** `keywords.SentenceSupression [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 7)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `preferences/extensions/unicode/keywords/sentence_supression.rs` vs expected `preferences/extensions/unicode/keywords/sentence_supression.rs`
- **Proposed provenance header:** `// port-lint: source preferences/extensions/unicode/keywords/sentence_supression.rs` (current: `// port-lint: source preferences/extensions/unicode/keywords/sentence_supression.rs`)
- **Lint issues:** 1

### 52. transform.key

- **Target:** `transform.Key [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 6)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `extensions/transform/key.rs` vs expected `extensions/transform/key.rs`
- **Proposed provenance header:** `// port-lint: source extensions/transform/key.rs` (current: `// port-lint: source extensions/transform/key.rs`)
- **Lint issues:** 1

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

## Reexport / Wiring Modules

These files match `reexport_modules` patterns in `.ast_distance_config.json`. They are filtered out of
normal priority and missing-file ladders because they are wiring
modules, not direct logic ports. Consult them for call-site routing;
do not treat them as the next implementation target by default.

### Matched

| Source | Target | Path |
|--------|--------|------|
| `icu_locale_core.lib` | `iculocalecore.Lib` | `icu_locale_core/src/lib` |

### Missing

| Source | Expected target | Deps | Source path | Expected path |
|--------|-----------------|------|-------------|---------------|
| `icu_locale_core.databake` | `iculocalecore.src.Databake` | 0 | `icu_locale_core/src/databake.rs` | `iculocalecore/src/Databake.kt` |
| `icu_locale_core.helpers` | `iculocalecore.src.Helpers` | 0 | `icu_locale_core/src/helpers.rs` | `iculocalecore/src/Helpers.kt` |
| `icu_locale_core.macros` | `iculocalecore.src.Macros` | 0 | `icu_locale_core/src/macros.rs` | `iculocalecore/src/Macros.kt` |
| `icu_locale_core.preferences.extensions.mod` | `iculocalecore.src.preferences.extensions.Mod` | 0 | `icu_locale_core/src/preferences/extensions/mod.rs` | `iculocalecore/src/preferences/extensions/Mod.kt` |
| `macros.mod` | `iculocalecore.src.preferences.extensions.unicode.macros.Mod` | 0 | `icu_locale_core/src/preferences/extensions/unicode/macros/mod.rs` | `iculocalecore/src/preferences/extensions/unicode/macros/Mod.kt` |
| `icu_locale_core.preferences.extensions.unicode.mod` | `iculocalecore.src.preferences.extensions.unicode.Mod` | 0 | `icu_locale_core/src/preferences/extensions/unicode/mod.rs` | `iculocalecore/src/preferences/extensions/unicode/Mod.kt` |
| `icu_locale_core.serde` | `iculocalecore.src.Serde` | 0 | `icu_locale_core/src/serde.rs` | `iculocalecore/src/Serde.kt` |
| `icu_locale_core.zerovec` | `iculocalecore.src.Zerovec` | 0 | `icu_locale_core/src/zerovec.rs` | `iculocalecore/src/Zerovec.kt` |

