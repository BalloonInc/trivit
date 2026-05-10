# Trivit

<p align="center">
  <a href="https://apps.apple.com/app/trivit/id960459126"><img alt="App Store" src="https://img.shields.io/badge/App_Store-Download-007AFF?style=flat-square&logo=apple&logoColor=white" /></a>
  <a href="https://ballooninc.be"><img alt="Website" src="https://img.shields.io/badge/Website-ballooninc.be-2E7D32?style=flat-square" /></a>
  <a href="https://github.com/BalloonInc/trivit/issues"><img alt="Issues" src="https://img.shields.io/github/issues/BalloonInc/trivit?style=flat-square" /></a>
</p>

A tally counter for iOS and watchOS. Tap to count up, swipe to count down,
swipe across to reset. Comes in 10 flat colors. No analytics, no accounts,
no dependencies.

Originally Objective-C / UIKit. Fully rewritten in Swift / SwiftUI / SwiftData
in January 2026.

## Gestures

- Tap a row to **increment** the counter
- Swipe **left** to decrement
- Swipe **right** to **reset** to zero
- Long-press for the context menu (rename, change color, delete)

Haptic feedback on every interaction; warning haptic on destructive actions.

## Stack

| Component          | Tech                                  |
| ------------------ | ------------------------------------- |
| UI                 | SwiftUI                               |
| Persistence        | SwiftData (`@Model`, `@Query`)        |
| Architecture       | Lightweight MVVM; views own their state |
| Watch app          | watchOS 10+, WatchConnectivity        |
| Language           | Swift 5+                              |
| Minimum iOS / watchOS | 17.0 / 10.0                        |
| Dependencies       | None — pure Apple frameworks          |

See [`ARCHITECTURE.md`](ARCHITECTURE.md) for a deeper walkthrough of the
project layout, data model, and patterns.

## Getting started

You need: macOS with **Xcode 15+** and an Apple Developer account (for
device testing).

```bash
# 1. Clone
git clone https://github.com/BalloonInc/trivit.git
cd trivit

# 2. Open in Xcode
open trivit.xcodeproj   # or trivit.xcworkspace if you see one
```

In Xcode:

1. Pick the **trivit** scheme and a simulator (iPhone 17 Pro works fine).
2. Hit **⌘R**. It should build and launch the app — no extra config needed
   for the simulator.
3. To run on a real device:
   - Open **Signing & Capabilities** for the `trivit` target.
   - Set your own **Team** (this repo's Team ID is for the original
     maintainers; you'll need to replace it).
   - Change the **bundle identifier** to something unique under your team,
     e.g. `com.yourname.trivit`.
   - Plug in your iPhone, select it as the run destination, ⌘R.

Useful command-line build / run:

```bash
# Build for a simulator
xcodebuild -scheme trivit \
  -destination 'platform=iOS Simulator,name=iPhone 17 Pro' build

# Install on a connected device (replace the device UDID)
xcrun devicectl device install app --device <UDID> \
  ~/Library/Developer/Xcode/DerivedData/trivit-*/Build/Products/Debug-iphoneos/trivit.app
```

## Watch app

The watchOS companion is in `trivit Watch App/`. It mirrors the iPhone's list
and lets you increment from the wrist. Cross-device sync is via
WatchConnectivity, with App Groups configured for shared SwiftData storage
(group: `group.com.wouterdevriendt.trivit`).

If you fork, you'll need to either:

- create your own App Group identifier on the Apple Developer Portal and
  swap it into the entitlements, or
- run iPhone-only and skip the watch target.

## TestFlight & releases

The `fastlane/` directory has the release automation. There are GitHub
Actions workflows for TestFlight internal, TestFlight external, and App
Store. See `fastlane/README.md` and `CLAUDE.md` for details.

## Maintainers

- Wouter Devriendt — wouter@ballooninc.be
- Pieterjan Criel — crielpieterjan@gmail.com
- Balloon Inc support — support@ballooninc.be

## License

See [`LICENSE`](LICENSE) — Polyform Noncommercial 1.0.0. Free for personal
and non-commercial use.
