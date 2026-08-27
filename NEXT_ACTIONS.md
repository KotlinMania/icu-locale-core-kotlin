# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 51/73 (69.9%)
- **Function parity:** 145/341 matched (target 585) — 42.5%
- **Class/type parity:** 24/71 matched (target 91) — 33.8%
- **Combined symbol parity:** 169/412 matched (target 676) — 41.0%
- **Average inline-code cosine:** 0.10 (function body across 42 matched files)
- **Average documentation cosine:** 0.72 (doc text across 42 matched files)
- **Cheat-zeroed Files:** 37
- **Critical Issues:** 51 files with <0.60 function similarity

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

- **Target:** `iculocalecore.Locale [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 5
- **Priority Score:** 5081710.0
- **Functions:** 8/15 matched (target 14)
- **Missing functions:** `test_sizes`, `as_tuple`, `try_from_utf8_with_single_variant_single_keyword_unicode_extension`, `from_str`, `from`, `fmt`, `test_writeable`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `Err`
- **Tests:** 0/1 matched

### 2. unicode.value

- **Target:** `unicode.Value`
- **Similarity:** 0.49
- **Dependents:** 4
- **Priority Score:** 4072605.2
- **Functions:** 18/22 matched (target 24)
- **Missing functions:** `into_iter`, `from_iter`, `extend`, `from_str`
- **Types:** 1/4 matched (target 1)
- **Missing types:** `Item`, `IntoIter`, `Err`

### 3. shortvec.litemap

- **Target:** `shortvec.LiteMap`
- **Similarity:** 0.00
- **Dependents:** 3
- **Priority Score:** 3252510.0
- **Functions:** 0/21 matched (target 33)
- **Missing functions:** `lm_get_range`, `lm_len`, `lm_is_empty`, `lm_get`, `lm_last`, `lm_binary_search_by`, `lm_sort_from_iter`, `lm_with_capacity`, `lm_reserve`, `lm_get_mut`, `lm_push`, `lm_insert`, `lm_remove`, `lm_clear`, `lm_retain`, `lm_extend`, `lm_iter`, `lm_iter_mut`, `lm_into_iter`, `test_short_slice_impl`, `test_short_slice_impl_full`
- **Types:** 0/4 matched (target 3)
- **Missing types:** `Slice`, `KeyValueIter`, `KeyValueIterMut`, `KeyValueIntoIter`
- **Tests:** 0/2 matched

### 4. subtags.variants

- **Target:** `subtags.Variants`
- **Similarity:** 0.30
- **Dependents:** 2
- **Priority Score:** 2041007.0
- **Functions:** 5/8 matched (target 15)
- **Missing functions:** `new`, `from_short_slice_unchecked`, `deref`
- **Types:** 1/2 matched
- **Missing types:** `Target`

### 5. subtags.region

- **Target:** `subtags.Region [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 2
- **Priority Score:** 2000110.0
- **Functions:** 1/1 matched (target 14)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 2)
- **Missing types:** _none_

### 6. unicode.key

- **Target:** `unicode.Key [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 2
- **Priority Score:** 2000010.0
- **Functions:** 0/0 matched (target 6)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 7. unicode.keywords

- **Target:** `unicode.Keywords`
- **Similarity:** 0.48
- **Dependents:** 1
- **Priority Score:** 1082405.2
- **Functions:** 15/22 matched
- **Missing functions:** `new`, `get_mut`, `from_tuple_vec`, `from`, `from_iter`, `from_str`, `test_keywords_fromstr`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `Err`
- **Tests:** 0/2 matched

### 8. unicode.attributes

- **Target:** `unicode.Attributes`
- **Similarity:** 0.35
- **Dependents:** 1
- **Priority Score:** 1061406.5
- **Functions:** 7/11 matched (target 19)
- **Missing functions:** `new`, `from_str`, `deref`, `test_attributes_fromstr`
- **Types:** 1/3 matched (target 1)
- **Missing types:** `Err`, `Target`
- **Tests:** 0/1 matched

### 9. transform.fields

- **Target:** `transform.Fields`
- **Similarity:** 0.42
- **Dependents:** 1
- **Priority Score:** 1051305.8
- **Functions:** 7/11 matched (target 13)
- **Missing functions:** `new`, `from_tuple_vec`, `from`, `from_iter`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `Inner`
- **Tests:** 0/1 matched

### 10. subtags.script

- **Target:** `subtags.Script [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1010110.0
- **Functions:** 0/1 matched (target 12)
- **Missing functions:** `from`
- **Types:** 0/0 matched (target 2)
- **Missing types:** _none_

### 11. subtags.language

- **Target:** `subtags.Language [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1000110.0
- **Functions:** 1/1 matched (target 15)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 2)
- **Missing types:** _none_

### 12. private.other

- **Target:** `private.PrivateSubtag [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1000010.0
- **Functions:** 0/0 matched (target 6)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 13. subtags.variant

- **Target:** `subtags.Variant [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1000010.0
- **Functions:** 0/0 matched (target 14)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 2)
- **Missing types:** _none_

### 14. unicode.attribute

- **Target:** `unicode.Attribute [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1000010.0
- **Functions:** 0/0 matched (target 6)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 15. shortvec.mod

- **Target:** `shortvec.ShortBoxSlice [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 202810.0
- **Functions:** 7/21 matched (target 25)
- **Missing functions:** `default`, `new`, `new_single`, `new_double`, `len`, `remove`, `deref`, `deref_mut`, `from`, `from_iter`, `next`, `into_iter`, `test_new_single_const`, `test_get_single`
- **Types:** 1/7 matched (target 2)
- **Missing types:** `ShortBoxSliceInner`, `Target`, `ShortBoxSliceIntoIter`, `ShortBoxSliceIntoIterInner`, `Item`, `IntoIter`
- **Tests:** 0/2 matched

### 16. icu_locale_core.data

- **Target:** `iculocalecore.DataLocale`
- **Similarity:** 0.09
- **Dependents:** 0
- **Priority Score:** 121709.1
- **Functions:** 4/14 matched (target 17)
- **Missing functions:** `default`, `fmt`, `from`, `from_str`, `try_from_str`, `for_each_subtag_str`, `as_tuple`, `into_locale`, `test_data_locale_to_string`, `test_data_locale_from_string`
- **Types:** 1/3 matched (target 2)
- **Missing types:** `Err`, `TestCase`
- **Tests:** 0/2 matched

### 17. icu_locale_core.langid

- **Target:** `iculocalecore.LanguageIdentifier [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 112010.0
- **Functions:** 8/18 matched (target 33)
- **Missing functions:** `try_from_utf8_with_single_variant`, `normalize_utf8`, `as_tuple`, `for_each_subtag_str`, `for_each_subtag_str_lowercased`, `write_lowercased_to`, `fmt`, `from_str`, `test_writeable`, `from`
- **Types:** 1/2 matched
- **Missing types:** `Err`
- **Tests:** 0/1 matched

### 18. subtags.mod

- **Target:** `subtags.Subtag [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 91010.0
- **Functions:** 1/9 matched (target 10)
- **Missing functions:** `len`, `from_tinystr_unvalidated`, `as_tinystr`, `to_ascii_lowercase`, `try_from`, `eq`, `test_subtag`, `test_subtag_from_tinystr`
- **Types:** 0/1 matched
- **Missing types:** `Error`
- **Tests:** 0/2 matched

### 19. private.mod

- **Target:** `private.Private [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 81610.0
- **Functions:** 7/13 matched (target 17)
- **Missing functions:** `new`, `from_str`, `write_to`, `writeable_length_hint`, `deref`, `test_private_extension_fromstr`
- **Types:** 1/3 matched (target 1)
- **Missing types:** `Err`, `Target`
- **Tests:** 0/1 matched

### 20. unicode.mod

- **Target:** `unicode.Unicode [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 71610.0
- **Functions:** 8/14 matched (target 13)
- **Missing functions:** `new`, `as_tuple`, `from_str`, `write_to`, `writeable_length_hint`, `test_unicode_extension_fromstr`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `Err`
- **Tests:** 0/1 matched

### 21. transform.mod

- **Target:** `transform.Transform [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 71510.0
- **Functions:** 7/13 matched (target 12)
- **Missing functions:** `new`, `as_tuple`, `from_str`, `write_to`, `writeable_length_hint`, `test_transform_extension_fromstr`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `Err`
- **Tests:** 0/1 matched

### 22. parser.mod

- **Target:** `parser.SubtagIterator [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 71110.0
- **Functions:** 3/9 matched (target 10)
- **Missing functions:** `new`, `next_const`, `slice_to_str`, `subtag_iterator_peek_test`, `subtag_iterator_test`, `skip_before_separator_test`
- **Types:** 1/2 matched
- **Missing types:** `Item`
- **Tests:** 0/4 matched

### 23. other.mod

- **Target:** `other.Other [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 51510.0
- **Functions:** 9/13 matched (target 20)
- **Missing functions:** `from_str`, `write_to`, `writeable_length_hint`, `test_other_extension_fromstr`
- **Types:** 1/2 matched
- **Missing types:** `Err`
- **Tests:** 0/1 matched

### 24. unicode.subdivision

- **Target:** `unicode.SubdivisionId`
- **Similarity:** 0.32
- **Dependents:** 0
- **Priority Score:** 51006.8
- **Functions:** 4/8 matched (target 13)
- **Missing functions:** `write_to`, `writeable_length_hint`, `from_str`, `test_subdivisionid_fromstr`
- **Types:** 1/2 matched
- **Missing types:** `Err`
- **Tests:** 0/1 matched

### 25. transform.value

- **Target:** `transform.Value`
- **Similarity:** 0.40
- **Dependents:** 0
- **Priority Score:** 41106.0
- **Functions:** 6/9 matched (target 11)
- **Missing functions:** `from_str`, `test_writeable`, `test_short_tvalue`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `Err`
- **Tests:** 0/2 matched

### 26. extensions.mod

- **Target:** `extensions.Extensions [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 31410.0
- **Functions:** 9/12 matched (target 27)
- **Missing functions:** `new`, `as_tuple`, `test_writeable`
- **Types:** 2/2 matched (target 7)
- **Missing types:** _none_
- **Tests:** 0/1 matched
- **Provenance warning:** port-lint provenance header matched only by basename: `tests:icu_locale_core/src/preferences/extensions/unicode/keywords/mod.rs` vs expected `extensions/mod.rs`
- **Proposed provenance header:** `// port-lint: tests extensions/mod.rs` (current: `// port-lint: tests icu_locale_core/src/preferences/extensions/unicode/keywords/mod.rs`)
- **Lint issues:** 1

### 27. preferences.locale

- **Target:** `preferences.LocalePreferences`
- **Similarity:** 0.47
- **Dependents:** 0
- **Priority Score:** 30905.3
- **Functions:** 5/8 matched (target 13)
- **Missing functions:** `default`, `language`, `region`
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_

### 28. parser.langid

- **Target:** `parser.LanguageIdParser`
- **Similarity:** 0.38
- **Dependents:** 0
- **Priority Score:** 20606.2
- **Functions:** 2/4 matched (target 3)
- **Missing functions:** `parse_locale_with_single_variant_single_keyword_unicode_extension_from_iter`, `parse_language_identifier_with_single_variant`
- **Types:** 2/2 matched
- **Missing types:** _none_

### 29. preferences.mod

- **Target:** `preferences.PreferenceKey [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10410.0
- **Functions:** 2/3 matched (target 2)
- **Missing functions:** `try_from_key_value`
- **Types:** 1/1 matched
- **Missing types:** _none_

### 30. parser.locale

- **Target:** `parser.LocaleParser`
- **Similarity:** 0.36
- **Dependents:** 0
- **Priority Score:** 10206.4
- **Functions:** 1/2 matched (target 1)
- **Missing functions:** `parse_locale_with_single_variant_single_keyword_unicode_keyword_extension`
- **Types:** 0/0 matched
- **Missing types:** _none_

### 31. keywords.regional_subdivision

- **Target:** `keywords.RegionalSubdivision`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10110.0
- **Functions:** 0/1 matched (target 6)
- **Missing functions:** `region_subdivision_test`
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Tests:** 0/1 matched

### 32. keywords.region_override

- **Target:** `keywords.RegionOverride`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10110.0
- **Functions:** 0/1 matched (target 6)
- **Missing functions:** `region_override_test`
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Tests:** 0/1 matched

### 33. parser.errors

- **Target:** `parser.Errors [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 110.0
- **Functions:** 0/0 matched (target 2)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 3)
- **Missing types:** _none_

### 34. unicode.errors

- **Target:** `unicode.Errors [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 110.0
- **Functions:** 0/0 matched (target 2)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_

### 35. keywords.calendar

- **Target:** `keywords.Calendar [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 8)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 16)
- **Missing types:** _none_

### 36. keywords.currency

- **Target:** `keywords.Currency [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 6)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 37. keywords.first_day

- **Target:** `keywords.FirstDay [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 7)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 38. keywords.variant

- **Target:** `keywords.Variant [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 7)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 39. keywords.timezone

- **Target:** `keywords.Timezone [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 6)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 40. keywords.hour_cycle

- **Target:** `keywords.HourCycle [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 7)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 41. keywords.line_break

- **Target:** `keywords.LineBreak [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 7)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 42. keywords.collation

- **Target:** `keywords.Collation [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 21)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 3)
- **Missing types:** _none_

### 43. keywords.line_break_word

- **Target:** `keywords.LineBreakWord [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 7)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 44. keywords.currency_format

- **Target:** `keywords.CurrencyFormat [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 7)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 45. keywords.emoji

- **Target:** `keywords.Emoji [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 7)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 46. keywords.dictionary_break

- **Target:** `keywords.DictionaryBreak [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 6)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 47. keywords.numbering_system

- **Target:** `keywords.NumberingSystem [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 6)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 48. keywords.measurement_system

- **Target:** `keywords.MeasurementSystem [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 7)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 49. keywords.sentence_supression

- **Target:** `keywords.SentenceSupression [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 7)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 50. transform.key

- **Target:** `transform.Key [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 6)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 51. keywords.measurement_unit_override

- **Target:** `keywords.MeasurementUnitOverride [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 7)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

