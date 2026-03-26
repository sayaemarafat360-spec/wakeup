# WakeUp Warrior - Development Setup Guide

## 📋 Prerequisites Checklist

Before you start, ensure you have:

- [ ] Android Studio Hedgehog (2023.1.1) or later
- [ ] JDK 17 installed
- [ ] Android SDK 34
- [ ] A Google Play Developer account (for publishing)
- [ ] An AdMob account (for ads)
- [ ] A Codemagic account (for CI/CD)

## 🔧 Initial Setup

### 1. Development Environment

```bash
# Check Java version (should be 17)
java -version

# Check Kotlin version
kotlinc -version

# Set JAVA_HOME if not set
export JAVA_HOME=/path/to/jdk-17
```

### 2. Clone and Open Project

```bash
git clone https://github.com/yourusername/wakeup-warrior.git
cd wakeup-warrior
```

Open in Android Studio:
1. File > Open
2. Select the project directory
3. Wait for Gradle sync to complete

### 3. Configure Signing

Create a keystore for release builds:

```bash
keytool -genkey -v -keystore wakeup-warrior.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias wakeup-warrior
```

Store the keystore securely - you'll need it for Codemagic and Play Store.

### 4. Configure AdMob

1. Go to [AdMob Console](https://apps.admob.com/)
2. Create a new app
3. Create ad units:
   - Banner ad
   - Interstitial ad
   - Rewarded ad
4. Update `Constants.kt` with your ad unit IDs

### 5. Configure Google Play Billing

1. Go to [Google Play Console](https://play.google.com/console)
2. Create in-app products:
   - `premium_monthly` - $2.99/month subscription
   - `premium_yearly` - $19.99/year subscription  
   - `premium_lifetime` - $49.99 one-time purchase

## ☁️ Codemagic Setup

### 1. Connect Repository

1. Log in to [Codemagic](https://codemagic.io)
2. Click "Add application"
3. Select your Git provider
4. Select the `wakeup-warrior` repository

### 2. Configure Signing

1. Encode your keystore to base64:
   ```bash
   base64 -i wakeup-warrior.jks | pbcopy
   ```

2. In Codemagic:
   - Go to App Settings > Code signing
   - Add a new Android keystore
   - Paste the base64 keystore
   - Name it `WAKEUP_WARRIOR_KEYSTORE`
   - Enter keystore password, key alias, and key password

### 3. Configure Environment Variables

In Codemagic, add these variables:

| Variable | Description |
|----------|-------------|
| `WAKEUP_WARRIOR_KEYSTORE` | Reference to keystore |
| `GCLOUD_SERVICE_ACCOUNT_CREDENTIALS` | Google Play service account JSON |

### 4. Create Service Account for Google Play

1. Go to Google Cloud Console
2. Create a service account with these permissions:
   - `Android Publisher`
3. Create a JSON key
4. Add the JSON content to Codemagic as `GCLOUD_SERVICE_ACCOUNT_CREDENTIALS`

## 🚀 Building

### Local Debug Build

```bash
./gradlew assembleDebug
```

Output: `app/build/outputs/apk/debug/app-debug.apk`

### Local Release Build

```bash
./gradlew assembleRelease
```

Output: `app/build/outputs/apk/release/app-release.apk`

### Generate Bundle for Play Store

```bash
./gradlew bundleRelease
```

Output: `app/build/outputs/bundle/release/app-release.aab`

## 📱 Testing

### Run Unit Tests

```bash
./gradlew test
```

### Run Instrumented Tests

```bash
# Connect a device or start an emulator first
./gradlew connectedAndroidTest
```

### Run Lint

```bash
./gradlew lint
```

Report: `app/build/reports/lint-results.html`

## 🐛 Debugging

### View Logs

```bash
# All logs
adb logcat

# Filter by package
adb logcat | grep com.wakeupwarrior

# Filter by tag
adb logcat -t AlarmService
```

### Test Alarm Scheduling

```bash
# Set system time (requires root or emulator)
adb shell date 010110002024.00  # Jan 1, 2024 10:00 AM

# Force Doze mode
adb shell dumpsys battery unplug
adb shell dumpsys deviceidle force-idle

# Exit Doze
adb shell dumpsys deviceidle unforce
adb shell dumpsys battery reset
```

## 📦 Release Checklist

Before releasing:

- [ ] Update version code in `build.gradle.kts`
- [ ] Update version name
- [ ] Test all challenge types
- [ ] Test alarm scheduling
- [ ] Test premium purchase flow
- [ ] Test ad loading (with test ads)
- [ ] Update CHANGELOG.md
- [ ] Create git tag
- [ ] Build release AAB
- [ ] Upload to Play Store
- [ ] Update store listing

## 🔐 Security Notes

1. **Never commit**:
   - `keystore.jks`
   - `local.properties`
   - Service account JSON
   - API keys

2. **Use environment variables** for sensitive data in CI/CD

3. **Enable ProGuard/R8** for release builds (already configured)

4. **Verify permissions** in `AndroidManifest.xml` are necessary

## 📚 Resources

- [Android Developer Docs](https://developer.android.com/)
- [Jetpack Compose Docs](https://developer.android.com/jetpack/compose)
- [Kotlin Docs](https://kotlinlang.org/docs/)
- [Room Database](https://developer.android.com/training/data-storage/room)
- [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager)
- [Google Play Billing](https://developer.android.com/google/play/billing)
- [AdMob](https://developers.google.com/admob/android)

## ❓ Troubleshooting

### Build Errors

**"SDK location not found"**
```bash
# Create local.properties
echo "sdk.dir=$HOME/Android/Sdk" > local.properties
```

**"Keystore not found"**
- Ensure keystore path is correct in Codemagic
- Check keystore was uploaded correctly

### Runtime Errors

**"Alarm not triggering"**
- Check battery optimization is disabled for the app
- Verify exact alarm permission is granted
- Check device isn't in Doze mode during testing

**"Camera not working"**
- Grant camera permission
- Test on physical device (emulator camera can be unreliable)

---

For additional help, open an issue on GitHub.
