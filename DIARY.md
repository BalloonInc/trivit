# Trivit — Dev Diary

Cross-platform fix/feature tracker. Each item shows status across platforms.
Status: done | pending | n/a (not applicable) | backlog

---

## Feb 22, 2026

### Android app (Kotlin + Jetpack Compose)
Full Android port with Room database, Hilt DI, Material 3 design. Matches iOS feature set: colored rows, tally marks, drag-to-reorder, swipe-to-delete.
| iOS | Android | Watch |
|-----|---------|-------|
| n/a | done | n/a |

---

## Feb 14, 2026

### Add deployment skills for Claude agents
Added `/deploy-testflight`, `/build-test`, and `/app-store-release` skill definitions for AI-assisted development workflows.
| iOS | Android | Watch |
|-----|---------|-------|
| done | n/a | n/a |

---

## Feb 6, 2026

### Bump to v5.0.1: statistics screenshot, iPad & watch support
Added statistics screenshot for App Store listing. Enabled iPad and Watch as supported destinations.
| iOS | Android | Watch |
|-----|---------|-------|
| done | backlog | done |

### Fix CI: pass secrets to shared iOS build workflow
CI builds failed because `APPLE_TEAM_ID` and other secrets weren't forwarded to the shared `ios-build` workflow. Fixed with explicit secret passthrough and `secrets: inherit`.
| iOS | Android | Watch |
|-----|---------|-------|
| done | n/a | n/a |

---

## Feb 4, 2026

### App Store v5.0 submission
Version 5.0 prepared and submitted. Automated App Store release workflow with Claude-generated release notes. PolyForm Noncommercial license added.
| iOS | Android | Watch |
|-----|---------|-------|
| done | backlog | done |

### Update support email and URLs
Changed support email and button text in Settings screen.
| iOS | Android | Watch |
|-----|---------|-------|
| done | pending | n/a |

---

## Feb 3, 2026

### Firebase Analytics integration
Added comprehensive Firebase Analytics tracking for all user interactions: taps, creates, deletes, reorders, settings changes.
| iOS | Android | Watch |
|-----|---------|-------|
| done | backlog | n/a |

### Watch sync: periodic sync overwriting watch changes
Periodic background sync from iPhone was overwriting changes made directly on the Watch. Fixed sync conflict resolution to respect watch-side edits.
| iOS | Android | Watch |
|-----|---------|-------|
| done | n/a | done |

### Watch: missing didReceiveMessage with replyHandler
Watch connectivity delegate was missing the `didReceiveMessage:replyHandler:` variant, causing silent sync failures when the watch expected a reply.
| iOS | Android | Watch |
|-----|---------|-------|
| done | n/a | done |

### Watch: schema migration crash
Watch app crashed on launch when SwiftData model schema changed. Added auto-delete of watch database on migration error, then reverted in favor of proper migration handling.
| iOS | Android | Watch |
|-----|---------|-------|
| n/a | n/a | done |

### Watch: expandable rows and full-screen tallies
Redesigned watch UI with expandable rows and full-screen tally mark display. Added debug Force Sync button in iOS settings.
| iOS | Android | Watch |
|-----|---------|-------|
| n/a | n/a | done |

### Localize example trivit names in 20 languages
50 random example names for new trivits, localized across 20 languages. Also added translations for all new UI features (soft delete, undo, tutorial, etc.) in 6 primary languages.
| iOS | Android | Watch |
|-----|---------|-------|
| done | pending | done |

### Tutorial overlay for first-time users
Added onboarding tutorial that shows on first launch, explaining swipe gestures and tap interactions.
| iOS | Android | Watch |
|-----|---------|-------|
| done | pending | n/a |

### Soft delete with undo toast
Swipe-to-delete now soft-deletes with a 5-second undo toast. Items retained for 30 days in "Recently Deleted" section in Settings. Replaced hard delete.
| iOS | Android | Watch |
|-----|---------|-------|
| done | pending | n/a |

### Hide counter when expanded (configurable)
New setting to hide the numeric counter when a trivit row is expanded, showing only tally marks.
| iOS | Android | Watch |
|-----|---------|-------|
| done | pending | n/a |

### Live reordering during drag
Drag-to-reorder now shows live visual feedback as items shift position, instead of snapping on drop.
| iOS | Android | Watch |
|-----|---------|-------|
| done | pending | n/a |

### Fix: simplify UI, smooth expand/collapse animation
Removed unnecessary animations, switched to ScrollView, removed minus button. Fixed jerky expand/collapse transition.
| iOS | Android | Watch |
|-----|---------|-------|
| done | pending | n/a |

### Automated App Store release workflow
Fastlane SnapshotHelper for automated screenshots. Manual code signing to prevent CI certificate creation issues. Sample data mode for consistent screenshots.
| iOS | Android | Watch |
|-----|---------|-------|
| done | backlog | done |

---

## Jan 30, 2026

### CloudKit sync for iPhone and iPad
Full CloudKit sync so trivits stay in sync across iPhone and iPad. Combined with WatchConnectivity for real-time iPhone-to-Watch sync.
| iOS | Android | Watch |
|-----|---------|-------|
| done | n/a | done |

### Statistics and history tracking
Comprehensive statistics view showing creation dates, increment history, and activity timeline. History events aggregated by minute instead of individual events.
| iOS | Android | Watch |
|-----|---------|-------|
| done | pending | n/a |

### Firebase Crashlytics integration
Added Firebase Crashlytics with dSYM upload build phase. Fixed crash in debug builds by ensuring FirebaseCore import.
| iOS | Android | Watch |
|-----|---------|-------|
| done | backlog | n/a |

### Fix: vertical scrolling blocked by swipe gesture
Swipe-to-delete gesture was intercepting vertical scroll. Changed to `simultaneousGesture` to allow both directions.
| iOS | Android | Watch |
|-----|---------|-------|
| done | n/a | n/a |

### Fix: main list not scrollable, no auto-scroll to new items
List wasn't scrollable when many items added. Fixed layout and added auto-scroll to newly created trivits.
| iOS | Android | Watch |
|-----|---------|-------|
| done | pending | n/a |

### Watch app: missing icons and model
Watch app failed App Store validation due to missing asset catalog icons. Also fixed missing `Trivit` model in watch target and added `WKApplication` key for device installation.
| iOS | Android | Watch |
|-----|---------|-------|
| n/a | n/a | done |

### Match original app design with two-tone row layout
Restored the classic Trivit look: flat colored rows with cream/off-white title bar and two-tone design. Triangle indicator for expanded state.
| iOS | Android | Watch |
|-----|---------|-------|
| done | done | n/a |

### Add translations for 20 languages
Full localization for Dutch, French, German, Spanish, Portuguese, Italian, Japanese, Chinese, Korean, Arabic, Hindi, Russian, Turkish, Polish, Swedish, Norwegian, Danish, Finnish, Thai, and Vietnamese.
| iOS | Android | Watch |
|-----|---------|-------|
| done | pending | done |

### Migrate to shared ios-infra workflows
Moved CI/CD to shared workflow repo. Multiple attempts to get secret passthrough working correctly. Switched to GitHub-hosted macOS runners.
| iOS | Android | Watch |
|-----|---------|-------|
| done | n/a | n/a |

---

## Jan 28, 2026

### Complete SwiftUI modernization
Full rewrite from Objective-C to Swift/SwiftUI. Removed all legacy code, CocoaPods, DataKit framework, and Objective-C files. SwiftData for persistence, iOS 17+ deployment target.
| iOS | Android | Watch |
|-----|---------|-------|
| done | n/a | n/a |

### Redesign UI: flat colored rows with tally marks
Matched original app design. Fixed 5th tally mark diagonal strike-through. Added Chinese tally style (zheng character) for underscore-prefixed names.
| iOS | Android | Watch |
|-----|---------|-------|
| done | done | n/a |

### Configure App Groups for iOS/watchOS data sharing
Set up App Groups container for shared data between iPhone app and Watch app.
| iOS | Android | Watch |
|-----|---------|-------|
| done | n/a | done |

### Modernize Watch app to SwiftUI with real-time sync
Rebuilt Watch app from WatchKit to pure SwiftUI. Added WatchConnectivity for real-time bidirectional sync with iPhone.
| iOS | Android | Watch |
|-----|---------|-------|
| n/a | n/a | done |

### Fix: bundle IDs and project cleanup
Unified all bundle IDs to `com.wouterdevriendt.trivit`. Removed legacy WatchKit Extension, Google Analytics, NewRelicAgent framework, and unused targets.
| iOS | Android | Watch |
|-----|---------|-------|
| done | n/a | done |

---

## Backlog

### Widget support
Home screen widget showing top trivits with current counts.
| iOS | Android | Watch |
|-----|---------|-------|
| backlog | backlog | n/a |

### Siri shortcuts
"Hey Siri, increment Coffee" voice commands.
| iOS | Android | Watch |
|-----|---------|-------|
| backlog | n/a | backlog |

### Watch complications
Show trivit counts on watch face complications.
| iOS | Android | Watch |
|-----|---------|-------|
| n/a | n/a | backlog |

### Export to CSV
Export trivit data and history as CSV file.
| iOS | Android | Watch |
|-----|---------|-------|
| backlog | backlog | n/a |
