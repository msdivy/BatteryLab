# BatteryLab

A minimalistic Android application to evaluate battery usage under different screen brightness conditions.

## Purpose
This app is designed for automated testing (Firebase Test Lab). It allows setting a countdown timer and screen brightness, while logging battery levels at the start and end of the timer.

## Features
- **Brightness Control**: Slider to adjust screen brightness (window level).
- **Battery Monitor**: Real-time battery percentage display.
- **Countdown Timer**: Configurable duration. Keeps screen on during execution.
- **Battery Logging**: Captures battery % at start and end of timer.

## Automated Testing
- **Package**: `com.example.batterylab`
- **Activity**: `.MainActivity`
- **UI Elements**:
    - `etDuration`: Input for timer seconds.
    - `btnStartTimer`: Button to start.
    - `sbBrightness`: Seekbar (0-100).
    - `tvStartBattery`: Displays "Battery at Start: X%".
    - `tvEndBattery`: Displays "Battery at End: X%".

## Build Instructions
Open in Android Studio and run:
```bash
./gradlew assembleDebug
```
The APK will be located in `app/build/outputs/apk/debug/app-debug.apk`.
