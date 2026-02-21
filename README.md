# GameChanger

A youth sports league and team browsing app for Android, built as a senior developer assessment.

## Setup

1. Clone the repository
2. Open the project in Android Studio (Hedgehog or newer recommended)
3. Sync Gradle — all dependencies are managed via `gradle/libs.versions.toml`
4. No API key required — the app uses the free tier of [TheSportsDB API](https://www.thesportsdb.com/documentation)

## Build & Run

**Debug build:**
```
./gradlew assembleDebug
```

**Release build (R8 minification enabled):**
```
./gradlew assembleRelease
```

**Run unit tests:**
```
./gradlew test
```

**Run all checks:**
```
./gradlew check
```

## Module Structure

```
:app                  → Application wiring only (MainActivity, NavGraph, HiltApplication)
:core:common          → Shared utilities (Result<T>, UiState<T>)
:core:domain          → Domain models, repository interfaces, use cases (pure Kotlin)
:core:network         → Retrofit API, Moshi DTOs, OkHttp, NetworkModule
:core:data            → Room database, entities, DAOs, repository implementations
:feature:leagues      → Leagues list screen + ViewModel
:feature:teams        → Teams grid screen + ViewModel
:feature:teamdetails  → Team detail screen + ViewModel
```

**Dependency rule:**
- Feature modules → `:core:domain` only
- `:core:data` → `:core:domain` + `:core:network`
- `:app` → everything (wiring only)

## API

Base URL: `https://www.thesportsdb.com/api/v1/json/3/`

No authentication required for the free tier endpoints used:
- `all_leagues.php` — list all leagues
- `search_all_teams.php?l={leagueName}` — teams in a league
- `lookupteam.php?id={teamId}` — full team detail

## Architecture

**MVVM + Clean Architecture + Repository pattern**

- ViewModels expose `StateFlow<UiState>` consumed by Composable screens
- Use cases delegate to repository interfaces (defined in `:core:domain`)
- Repositories implement the SSOT pattern: UI always observes the Room database;
  network fetches populate the database, which then notifies observers via Flow
- Hilt manages dependency injection throughout
