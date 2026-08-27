# port-lint Proposed Changes

**Generated:** 2026-08-27
**Source:** tmp/icu_locale_core/src
**Target:** src/commonMain/kotlin/io/github/kotlinmania/iculocalecore

These are review proposals only. They are emitted when a Rust -> Kotlin pair matches only after fallback normalization, so the existing `port-lint` header is not an exact provenance match.

| Target file | Current header | Proposed header | Source path | Reason |
|-------------|----------------|-----------------|-------------|--------|
| `src/commonTest/kotlin/io/github/kotlinmania/iculocalecore/preferences/extensions/unicode/keywords/KeywordsTest.kt` | `// port-lint: tests preferences/extensions/unicode/keywords/mod.rs` | `// port-lint: tests extensions/mod.rs` | `extensions/mod.rs` | `port-lint provenance header matched only by basename: 'tests:preferences/extensions/unicode/keywords/mod.rs' vs expected 'extensions/mod.rs'` |
