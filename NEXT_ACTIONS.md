# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 2/63 (3.2%)
- **Function parity:** 0/211 matched (target 2) — 0.0%
- **Class/type parity:** 2/35 matched (target 2) — 5.7%
- **Combined symbol parity:** 2/246 matched (target 4) — 0.8%
- **Average inline-code cosine:** 0.00 (function body across 2 matched files)
- **Average documentation cosine:** 0.56 (doc text across 2 matched files)
- **Cheat-zeroed Files:** 2
- **Critical Issues:** 2 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

1. **macros.enum_keyword** (13 deps)
   - Path: `preferences/extensions/unicode/macros/enum_keyword.rs`
   - Essential for 13 other files

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. parser.errors

- **Target:** `parser.Errors [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 110.0
- **Functions:** 0/0 matched (target 1)
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_

### 2. unicode.errors

- **Target:** `unicode.Errors [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 110.0
- **Functions:** 0/0 matched (target 1)
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

## Next Commands

```bash
# Initialize task queue for systematic porting
cd tools/ast_distance
./ast_distance --init-tasks ../../tmp/icu_locale_core/src rust ../../src/commonMain/kotlin/io/github/kotlinmania/iculocalecore kotlin tasks.json ../../AGENTS.md

# Get next high-priority task
./ast_distance --assign tasks.json <agent-id>
```
## Reexport / Wiring Modules

These files match `reexport_modules` patterns in `.ast_distance_config.json`. They are filtered out of
normal priority and missing-file ladders because they are wiring
modules, not direct logic ports. Consult them for call-site routing;
do not treat them as the next implementation target by default.

### Missing

| Source | Expected target | Deps | Source path | Expected path |
|--------|-----------------|------|-------------|---------------|
| `extensions.mod` | `extensions.Mod` | 0 | `extensions/mod.rs` | `extensions/Mod.kt` |
| `other.mod` | `extensions.other.Mod` | 0 | `extensions/other/mod.rs` | `extensions/other/Mod.kt` |
| `private.mod` | `extensions.private.Mod` | 0 | `extensions/private/mod.rs` | `extensions/private/Mod.kt` |
| `transform.mod` | `extensions.transform.Mod` | 0 | `extensions/transform/mod.rs` | `extensions/transform/Mod.kt` |
| `unicode.mod` | `extensions.unicode.Mod` | 0 | `extensions/unicode/mod.rs` | `extensions/unicode/Mod.kt` |
| `lib` | `Lib` | 0 | `lib.rs` | `Lib.kt` |
| `parser.mod` | `parser.Mod` | 0 | `parser/mod.rs` | `parser/Mod.kt` |
| `preferences.extensions.mod` | `preferences.extensions.Mod` | 0 | `preferences/extensions/mod.rs` | `preferences/extensions/Mod.kt` |
| `keywords.mod` | `preferences.extensions.unicode.keywords.Mod` | 0 | `preferences/extensions/unicode/keywords/mod.rs` | `preferences/extensions/unicode/keywords/Mod.kt` |
| `macros.mod` | `preferences.extensions.unicode.macros.Mod` | 0 | `preferences/extensions/unicode/macros/mod.rs` | `preferences/extensions/unicode/macros/Mod.kt` |
| `preferences.extensions.unicode.mod` | `preferences.extensions.unicode.Mod` | 0 | `preferences/extensions/unicode/mod.rs` | `preferences/extensions/unicode/Mod.kt` |
| `preferences.mod` | `preferences.Mod` | 0 | `preferences/mod.rs` | `preferences/Mod.kt` |
| `shortvec.mod` | `shortvec.Mod` | 0 | `shortvec/mod.rs` | `shortvec/Mod.kt` |
| `subtags.mod` | `subtags.Mod` | 0 | `subtags/mod.rs` | `subtags/Mod.kt` |

