# Android Developer Assessment - Sports App

## Overview

Welcome to the Android Developer Assessment! In this exercise, you will build a youth sports team/league browsing application using modern Android development practices.

**Expected Time:** 3-4 hours
**Target Level:** Senior Android Developer (5+ years experience)

## AI Usage Policy

You are welcome to use AI tools (e.g., ChatGPT, Claude, GitHub Copilot) during this assessment. However:

1. **Document your process** - Include a brief summary of how you used AI, including key prompts or conversations that shaped your implementation
2. **Understand your code** - You will be asked specific questions about your implementation choices, architecture decisions, and how particular pieces of code work during the follow-up interview
3. **Be prepared to modify** - You may be asked to make changes or explain alternatives during the review

We're evaluating your ability to effectively leverage tools *and* your understanding of the resulting code, not whether you wrote every line by hand.

## Your Task

Build a sports app that allows users to:
1. **Browse Leagues** - Display a list of available sports leagues
2. **View Teams** - Browse teams within a selected league
3. **Team Details** - View detailed information about a team

## Technical Requirements

### Architecture
- Implement **MVVM** architecture pattern
- Use clear separation of concerns between UI, domain, and data layers
- Demonstrate proper state management

### UI
- Use **Jetpack Compose** (already configured)
- Implement proper loading, error, and empty states

### Networking
- Integrate with [TheSportsDB API](https://www.thesportsdb.com/documentation)
- Handle network errors gracefully.
- Implement proper error messages for the user

### Modularization
- Implement features and other architectural components in separate modules.
- Maintain proper dependency direction
- Keep modules focused and single-purpose

### Testing
- Include unit tests for ViewModels and Repositories
- Demonstrate your testing approach (coverage threshold is flexible)

### Build Configuration
- Debug and release build types are configured
- ProGuard/R8 is enabled for release builds

---

## API Reference

**Base URL:** `https://www.thesportsdb.com/api/v1/json/3/`

| Endpoint | Description | Example |
|----------|-------------|---------|
| `all_leagues.php` | List all leagues (limited to 10 on free tier) | N/A |
| `search_all_leagues.php?c={country}` | List leagues by country | `?c=England` |
| `search_all_teams.php?l={league}` | Get teams in a league | `?l=English Premier League` |
| `searchteams.php?t={name}` | Search teams by name (recommended for team details) | `?t=Arsenal` |

**Rate Limit:** 30 requests per minute (free tier)

**Note:** Using `all_leagues.php` is acceptable for this assessment. However, if you want to see more familiar leagues and teams (e.g., English Premier League, La Liga), you can use `search_all_leagues.php?c={country}` to fetch leagues from specific countries like England, Spain, Germany, etc.

## Evaluation Criteria

### Must Have (Pass/Fail)
- [ ] App builds and runs without crashes
- [ ] Displays data from TheSportsDB API
- [ ] Clear MVVM architecture
- [ ] Feature module separation maintained
- [ ] At least some unit tests present
- [ ] README with setup instructions (update this file!)

### Scored Areas

| Category | Weight | What We Look For |
|----------|--------|------------------|
| **Architecture** | 35% | Clean separation, testability, proper state management |
| **Code Quality** | 25% | Kotlin idioms, readability, naming conventions |
| **Modularization** | 25% | Proper module boundaries, dependency direction |
| **UI/UX** | 5% | Compose best practices, responsive design, polish |
| **Testing** | 10% | Test coverage approach, meaningful tests |

### Bonus Points
None of this is required, but will help us with your overall assessment.

- Offline caching
- Pagination
- Animations/transitions
- Comprehensive error states
- Search functionality

---

## Questions?

If you have any questions about the requirements, please reach out to your contact at the company.

Good luck!
