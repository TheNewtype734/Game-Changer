# Architectural Decisions

This document explains the key architectural choices made during this assessment and the reasoning behind each.

---

## 1. MVVM + Clean Architecture

**Decision:** Use MVVM for the UI layer and Clean Architecture layering (domain/data/presentation).

**Why:**
- MVVM is the Google-recommended pattern for Jetpack Compose, with first-class support via `@HiltViewModel`, `SavedStateHandle`, and `StateFlow`
- Clean Architecture keeps business logic (use cases) decoupled from both the UI and data sources, making each independently testable
- The assessment explicitly requires clear separation of concerns — this structure makes the boundaries visible and enforced by module dependencies

---

## 2. Repository Pattern with Single Source of Truth (SSOT)

**Decision:** Repositories always expose a `Flow` backed by Room. The network is only used to refresh the local database. UI never observes the network directly.

**Why:**
- SSOT is the canonical pattern recommended by Google's architecture guidelines
- Room's `Flow<List<Entity>>` automatically emits when the underlying data changes — the UI gets updates without polling
- Network failures don't disrupt the cached data; errors are surfaced as `Result.Error` alongside the cached emission
- This enables seamless offline support: if the device is offline, the last-cached data is still shown

**Trade-off:** The initial load has a brief moment where cached data may be empty before the network response arrives and populates Room. This is intentional and surfaces as a `Loading` → `Success(emptyList)` → `Success(data)` transition.

---

## 3. Moshi over Gson

**Decision:** Use Moshi with KSP codegen (`@JsonClass(generateAdapter = true)`) instead of Gson.

**Why:**
- Moshi is null-safe and Kotlin-first — it respects non-nullable types at compile time
- KSP codegen generates adapters at compile time, avoiding reflection at runtime (faster, smaller APK, R8-friendly)
- Gson requires `@SerializedName` everywhere and has known issues with Kotlin data classes (nullable vs non-nullable)
- Moshi integrates cleanly with the existing KSP setup already configured in the project

---

## 4. Hilt for Dependency Injection

**Decision:** Use Hilt throughout (pre-selected by the project skeleton).

**Why:**
- Hilt is the recommended DI framework for Android, built on Dagger 2
- `@HiltViewModel` + `@Inject constructor` makes ViewModels automatically injectable without manual factory boilerplate
- `@AndroidEntryPoint` on Activity and Application is minimal ceremony
- `@BindsOptionalOf`, `@Binds`, and `@Provides` in `@Module` classes give explicit control over the object graph
- Test doubles can be provided via `@TestInstallIn` in unit tests without modifying production code

---

## 5. Module Structure and Dependency Direction

**Decision:** Strict module boundaries enforced by Gradle project dependencies.

```
:feature:* → :core:domain (interfaces + domain models only)
:core:data → :core:domain + :core:network
:core:network → (no project deps)
:core:common → (no project deps)
:app → all modules (wiring only)
```

**Why:**
- Feature modules cannot accidentally depend on data implementation details (Room entities, Retrofit DTOs)
- `:core:domain` has zero Android dependencies — it's a pure Kotlin module, making use cases trivially testable
- `:core:network` is isolated: swapping Retrofit for another HTTP client, or Moshi for another serializer, only affects this one module
- `:app` is intentionally thin — it wires modules together but contains no business logic

**Enforced by:** Gradle's `implementation(project(...))` declarations. If a feature module tried to import a Room entity, it would fail to compile.

---

## 6. Sealed Interface for UiState

**Decision:** Each feature module defines a `sealed interface XUiState` with `Loading`, `Success(data)`, `Error(message)` variants.

**Why:**
- `sealed interface` (vs `sealed class`) allows `data object` members with zero allocation overhead for stateless variants like `Loading`
- The ViewModel exposes `StateFlow<UiState>` which Compose collects via `collectAsStateWithLifecycle` — this correctly handles the Activity/Fragment lifecycle
- Exhaustive `when` expressions in Compose ensure all states are handled at compile time
- Keeping UiState in the feature module (not in domain) means it can hold UI-specific data (formatted strings, etc.) without polluting domain models

---

## 7. `collectAsStateWithLifecycle` over `collectAsState`

**Decision:** Use `androidx.lifecycle.compose.collectAsStateWithLifecycle` in all Compose screens.

**Why:**
- `collectAsStateWithLifecycle` stops collecting when the UI is not visible (background/stopped), stopping unnecessary work
- `collectAsState` collects unconditionally, even when the screen is in the background
- The `SharingStarted.WhileSubscribed(5_000)` on the ViewModel's `stateIn` works in tandem: the `StateFlow` upstream is stopped 5 seconds after the last subscriber drops, and resumes when the screen comes back

---

## 8. String-Based Navigation (not Navigation 2.8 type-safe)

**Decision:** Use string-based Navigation Compose routes rather than the Navigation 2.8 type-safe `@Serializable` route objects.

**Why:**
- The project's Compose BOM is `2024.06.00`, which predates the stable release of Navigation 2.8's type-safe API
- Adding Navigation 2.8 would require upgrading the BOM and potentially introducing compatibility issues with other Compose dependencies
- String-based routes are fully supported, well-understood, and sufficient for a 3-screen app
- The route definitions are centralized in `Screen` sealed class in `NavGraph.kt`, so the string literals are not scattered across the codebase

---

## 9. Room Database in `:core:data`

**Decision:** The Room database (entities, DAOs, `GameChangerDatabase`) lives entirely in `:core:data`, not in a separate `:core:database` module.

**Why:**
- The database is an implementation detail of the data layer — it has no reason to be visible to any other module
- Feature modules and domain use cases only see `Flow<Result<DomainModel>>`, never Room entities or DAOs
- A separate `:core:database` module would only make sense if multiple modules needed direct database access, which violates the layering rule
- Keeping it in `:core:data` enforces the boundary: DTOs (network) and entities (local) are both implementation details that never escape

---

## 10. R8/ProGuard on Release

**Decision:** Enable `isMinifyEnabled = true` on release builds with ProGuard rules for Moshi, Retrofit, OkHttp, and Hilt.

**Why:**
- The assessment requirements explicitly call for ProGuard/R8 on release builds
- R8 significantly reduces APK size and obfuscates the bytecode
- Without keep rules, R8 would strip Moshi-generated adapters, Retrofit service interfaces, and Hilt-generated components
- The keep rules target only what's necessary — not a blanket `-keep class *` which would defeat the purpose of R8
