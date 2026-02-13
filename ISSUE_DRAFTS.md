# BatteryLab v1.1 — GitHub Issues Draft List

Use these as copy/paste issue drafts in `msdivy/BatteryLab`.

---

## 1) BL-101: Add structured run export (JSON + CSV)

**Title**
Add structured experiment result export (JSON + CSV)

**Description**
BatteryLab currently shows run output mostly in UI labels and screenshots. Add structured, machine-readable run artifacts to support Firebase Test Lab and repeatable analysis.

Create one JSON object and one CSV row per timer run with:
- runId
- startTimestamp / endTimestamp
- requestedDurationSec / actualDurationSec
- brightnessPercent
- batteryStartPercent / batteryEndPercent
- deviceModel
- androidVersion
- appVersionName / appVersionCode

Add a simple export/share action so users can retrieve results.

**Acceptance Criteria**
- A completed timer run writes both JSON and CSV output.
- JSON is valid and includes all required fields.
- CSV header is stable and documented.
- Export failure path shows clear user message.

**Labels**
`enhancement`, `data`, `testing`

**Estimate**
M

---

## 2) BL-102: Capture richer battery telemetry

**Title**
Capture additional battery telemetry (status/health/temp/voltage)

**Description**
Improve experiment quality by logging richer telemetry from battery intents where available.

Add fields:
- chargingStatus
- batteryHealth
- batteryTemperatureC
- batteryVoltageMv

Persist telemetry in run export artifacts.

**Acceptance Criteria**
- Telemetry values are saved for each run when available.
- Missing fields are represented safely (null/NA) without crashes.
- UI remains functional if a field is unavailable on a device.

**Labels**
`enhancement`, `data`

**Estimate**
S

---

## 3) BL-201: Expand instrumentation coverage for core flows

**Title**
Add instrumentation test suite for timer, validation, brightness, and routing

**Description**
Current coverage is minimal and includes a `Thread.sleep`-based timer assertion.

Add UI tests for:
- invalid duration validation message
- controls disabled while timer is running
- controls re-enabled on finish
- brightness percentage text updates with seekbar changes
- EULA routing: Splash → Eula/Main based on preference state

Avoid direct `Thread.sleep`; use synchronization/idling-friendly patterns.

**Acceptance Criteria**
- At least 5 instrumentation tests exist and pass locally.
- Tests are stable under repeated runs.
- No direct `Thread.sleep` in instrumentation tests.

**Labels**
`testing`, `androidTest`, `quality`

**Estimate**
M

---

## 4) BL-202: Document result schema and automation contract

**Title**
Document result artifact schema and automation usage in README

**Description**
Formalize a stable output contract for CI/device lab usage.

Update README with:
- JSON schema (required + optional fields)
- CSV header and field definitions
- sample JSON and CSV outputs
- retrieval instructions for exported files

**Acceptance Criteria**
- README includes schema and examples.
- Schema is versioned (e.g., `resultSchemaVersion`).
- Automation users can parse output without code inspection.

**Labels**
`documentation`, `testing`, `data`

**Estimate**
S

---

## 5) BL-301: Refactor MainActivity into focused components

**Title**
Refactor MainActivity responsibilities into provider/controller/logger components

**Description**
`MainActivity` currently mixes UI updates, battery receiver handling, timer flow, and screenshot/storage logic.

Refactor into:
- `BatteryProvider`
- `TimerController`
- `RunLogger`
- `ScreenshotManager`

Keep behavior unchanged while improving maintainability and testability.

**Acceptance Criteria**
- MainActivity shrinks to orchestration/UI wiring.
- Non-UI logic has unit-testable boundaries.
- No functional regressions in existing flows.

**Labels**
`refactor`, `architecture`, `tech-debt`

**Estimate**
L

---

## 6) BL-302: Remove hardcoded UI strings

**Title**
Move hardcoded user-facing strings to string resources

**Description**
Some layout/activity text is hardcoded (e.g., button text and units). Move all user-facing literals to `strings.xml` for consistency and localization readiness.

**Acceptance Criteria**
- No hardcoded user-facing text in XML layouts or Kotlin files.
- `strings.xml` contains all UI labels/messages.
- Lint passes for hardcoded text checks.

**Labels**
`enhancement`, `i18n`, `ui`

**Estimate**
S

---

## 7) BL-401: Modernize storage/permissions usage

**Title**
Modernize screenshot storage and remove legacy storage flags where possible

**Description**
The app already uses MediaStore, but manifest contains legacy storage settings. Align fully with scoped storage patterns for supported API levels.

Review and update:
- `WRITE_EXTERNAL_STORAGE` usage
- `requestLegacyExternalStorage`
- screenshot save path/behavior on Android 10+

**Acceptance Criteria**
- Screenshot save works on supported API levels.
- Unnecessary legacy permission/flags are removed.
- Behavior is documented in README.

**Labels**
`android`, `security`, `storage`

**Estimate**
M

---

## 8) BL-402: Complete privacy/legal text and analytics disclosure

**Title**
Finalize privacy policy link and analytics disclosure

**Description**
EULA currently includes a placeholder privacy policy link. Replace with a real URL and ensure analytics collection is clearly disclosed in docs/legal text.

**Acceptance Criteria**
- EULA contains valid privacy policy URL.
- README includes concise data collection disclosure.
- Text is consistent across app, README, and policy link destination.

**Labels**
`documentation`, `legal`, `privacy`

**Estimate**
S

---

## Suggested Milestone Grouping

### Milestone: v1.1 Data + Automation
- BL-101
- BL-102
- BL-201
- BL-202

### Milestone: v1.1 Maintainability + Compliance
- BL-301
- BL-302
- BL-401
- BL-402
