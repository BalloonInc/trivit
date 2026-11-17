#!/bin/bash
# Trivit 5.0.6 (2026111601) signed archive + ASC upload. Run INSIDE the tmux
# session whose login keychain is unlocked.
set -u
P=~/dev/trivit
B=$P/build
mkdir -p "$B"
exec > "$B/upload-tf.out" 2>&1
KEY_ID=$(cat ~/.appstoreconnect/key_id)
ISSUER=$(cat ~/.appstoreconnect/issuer_id)
cd "$P" || exit 9
xcodebuild archive \
  -project Trivit.xcodeproj -scheme trivit -configuration Release \
  -destination 'generic/platform=iOS' \
  -archivePath "$B/Trivit.xcarchive" \
  -derivedDataPath "$B/dd" \
  -allowProvisioningUpdates \
  -authenticationKeyPath ~/.appstoreconnect/AuthKey_${KEY_ID}.p8 \
  -authenticationKeyID "$KEY_ID" \
  -authenticationKeyIssuerID "$ISSUER" \
  DEVELOPMENT_TEAM=N324UX8D9M CODE_SIGN_STYLE=Automatic \
  > "$B/archive.log" 2>&1
echo "archive rc=$?" > "$B/upload-tf-status.txt"
grep -m1 "ARCHIVE " "$B/archive.log" >> "$B/upload-tf-status.txt" || true
xcodebuild -exportArchive \
  -archivePath "$B/Trivit.xcarchive" \
  -exportPath "$B/export" \
  -exportOptionsPlist ~/columbia-testflight-2026-11-16/ExportOptions-upload.plist \
  -allowProvisioningUpdates \
  -authenticationKeyPath ~/.appstoreconnect/AuthKey_${KEY_ID}.p8 \
  -authenticationKeyID "$KEY_ID" \
  -authenticationKeyIssuerID "$ISSUER" \
  > "$B/export.log" 2>&1
echo "export rc=$?" >> "$B/upload-tf-status.txt"
grep -E "Upload succeeded|EXPORT " "$B/export.log" | tail -2 >> "$B/upload-tf-status.txt"
echo DONE >> "$B/upload-tf-status.txt"
