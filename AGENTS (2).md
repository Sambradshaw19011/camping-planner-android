# AGENTS.md — Camping Planner Android App

## Project Goal

Build a polished, beginner-friendly Android app called **Camping Planner** using **Kotlin, Android Studio, and Jetpack Compose**. The app helps someone prepare for a camping trip by recording trip details and managing a packing checklist.

Keep the implementation focused and achievable. Prioritize a complete, reliable core experience over unnecessary features or complex architecture.

## Before Making Changes

1. Inspect the existing repository and identify its current Android/Kotlin/Gradle setup.
2. Reuse the existing project structure and dependencies where practical. Do not replace working configuration without a clear reason.
3. If the repository is empty or only contains a starter project, create the minimum structure needed for a working Android app.
4. Check the available Android SDK and Gradle configuration before choosing compatible dependency versions.
5. Implement the app, then run the available build and tests. Fix errors introduced by your changes.

## Required Technology

- Kotlin
- Jetpack Compose for the user interface
- Material 3 components where supported by the project's existing setup
- Android Studio-compatible Gradle configuration
- Android's local persistence options (prefer **Preferences DataStore** or **Room** only if already configured or appropriate; otherwise use a simple, maintainable local-storage solution)

Do not add a backend, account system, network service, or unrelated framework for this MVP.

## Core Features

### 1. Main Camping Planner Screen

- Give the app a clear title: **Camping Planner**.
- Show a welcoming camping-themed visual, such as a bundled/local illustration or a suitable project asset. Do not make the app depend on a remote image loading successfully.
- Display trip details when available, including:
  - Trip name
  - Destination (optional)
  - Start date and end date (optional)
- Provide an obvious way to edit trip details.
- Use a clean, readable layout that works on typical Android phone sizes.

### 2. Packing Checklist

- Let the user add a packing item by entering its name.
- Show each item in a list with a checkbox.
- Let the user mark an item as packed or unpacked.
- Let the user delete an item.
- Prevent empty or whitespace-only item names from being added.
- Update the visible list immediately after an item is added, checked, unchecked, or deleted.
- Display a useful empty state when the packing list has no items.
- Include a small starter set of common camping items only if it improves the first-run experience; users must be able to remove them.

### 3. Packing Progress

- Show the number of packed items versus total items.
- Show a progress indicator that updates when checklist items change.
- Handle an empty list safely (do not divide by zero).

### 4. Save Data Locally

- Preserve trip details and packing items when the app is closed and reopened.
- Keep the storage implementation straightforward and separated from UI code.
- Handle missing or first-run data gracefully.

## Visual and Interaction Guidelines

- Use a simple outdoor-inspired design with restrained greens, warm neutrals, and strong contrast.
- Use consistent spacing, typography, and touch targets.
- Make the primary actions easy to discover.
- Include sensible labels and accessibility descriptions for interactive controls.
- Support scrolling when content exceeds the screen height.
- Show brief feedback for successful actions or validation errors when helpful.
- Avoid decorative complexity that makes the app harder to use or maintain.
- Create a simple custom app icon with a camping/outdoor theme if the project setup supports it. Ensure the app still builds if a custom image asset is unavailable.

## Suggested Data Model

Use simple Kotlin data classes, adapting them to the existing project as needed:

- `TripDetails`
  - `name: String`
  - `destination: String`
  - `startDate: String`
  - `endDate: String`
- `PackingItem`
  - Stable unique ID
  - `name: String`
  - `isPacked: Boolean`

Do not identify items by their list position; use stable IDs so checking or deleting an item affects the correct row.

## Architecture and Code Quality

- Keep composables focused and reasonably small.
- Separate state and business logic from presentation. Use a `ViewModel` if the project already supports it or adding it is straightforward.
- Use unidirectional state flow where practical: UI events update state, and the UI renders the current state.
- Avoid duplicating state in multiple places.
- Use clear names and add comments only where they explain non-obvious decisions.
- Handle errors and invalid input without crashing.
- Do not leave placeholder buttons, TODO-only implementations, or fake interactions in the finished app.
- Do not add dependencies unless they provide clear value and are compatible with the project.

## Testing and Verification

At minimum, verify the following:

1. The project compiles successfully.
2. The app opens to the main planner screen.
3. A user can add a packing item.
4. A user can mark an item packed and unpacked.
5. A user can delete an item.
6. Packing progress changes correctly.
7. Empty input is rejected safely.
8. Trip details and checklist data persist after app restart.
9. The layout remains usable on a typical phone-sized emulator.

Add unit tests for packing progress and relevant state/business logic if the existing project supports tests. Do not claim a build or test passed unless you actually ran it.

## Scope Priorities

**Required for completion**
- Main planner screen
- Trip details editing
- Add/check/uncheck/delete packing items
- Live packing progress
- Local persistence
- Clean, usable Compose UI
- Successful build

**Optional polish, only after the required features work**
- Group items into categories such as Shelter, Clothing, Food, Safety, and Miscellaneous
- A reset/clear-list action with confirmation
- Improved icon and local camping illustration
- Multiple saved trips

Do not implement multiple-trip management before the single-trip workflow is complete and tested.

## Completion Checklist

Before finishing:

- [ ] The app's core features are implemented end to end.
- [ ] State updates correctly after every user action.
- [ ] Data survives an app restart.
- [ ] Empty and invalid input states are handled.
- [ ] UI controls have meaningful labels and actions.
- [ ] The project builds successfully, or any environment limitation is clearly reported.
- [ ] The final response summarizes what was implemented, lists the build/test commands run and their results, and identifies any remaining limitations.
