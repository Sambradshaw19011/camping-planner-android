# Camping Planner

A simple Android camping planner built with Kotlin and Jetpack Compose. Plan one
trip, manage a packing checklist, and keep trip details and checklist progress
saved locally on your device.

## Features

- Edit a trip name, destination, and optional start and end dates.
- Add, check off, and remove packing-list items.
- View packing progress as items are checked.
- Save planner data locally between app launches.

## Build and run

Open this project in Android Studio with the Android SDK installed, allow Gradle
to sync, then run the `app` configuration on an emulator or Android device.
The project uses Java 17, Android Gradle Plugin 8.6.1, and compile/target SDK 35.

To run the unit tests from Android Studio, use the test action for
`PlannerDataTest`. The tests cover empty-list progress and packed-item progress.
