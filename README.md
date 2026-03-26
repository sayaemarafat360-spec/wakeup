# WakeUp Warrior - Android Alarm App

<p align="center">
  <img src="app_icon.png" alt="WakeUp Warrior Logo" width="200"/>
</p>

## 📱 About

WakeUp Warrior is a revolutionary alarm clock app that **doesn't stop until you complete a challenge**. Say goodbye to oversleeping and hello to productive mornings!

### 🎯 Key Features

- **Multiple Challenge Types**: Math puzzles, Memory games, QR code scanning, Shake challenges, Typing tests, Voice commands, and Step counting
- **Smart Escalation**: Alarm gets louder over time if you don't respond
- **Gamification**: Daily streaks, achievements, and coin rewards
- **Glassmorphic UI**: Beautiful, modern dark theme with glass effects
- **Reliable Alarms**: Works even when app is killed or phone is in Doze mode
- **Premium Features**: Unlimited alarms, all challenge types, custom sounds, and more

## 🛠 Tech Stack

| Category | Technology |
|----------|------------|
| Language | Kotlin |
| UI Framework | Jetpack Compose |
| Architecture | MVVM + Clean Architecture |
| Dependency Injection | Hilt |
| Local Database | Room |
| Preferences | DataStore |
| Navigation | Compose Navigation |
| Animations | Lottie |
| Camera | CameraX |
| ML Kit | Barcode Scanning |
| Ads | AdMob |
| Billing | Google Play Billing |
| Background Work | WorkManager |

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 34
- Kotlin 1.9.22+

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/wakeup-warrior.git
   cd wakeup-warrior
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the project directory

3. **Sync Gradle**
   - Click "Sync Now" when prompted
   - Wait for all dependencies to download

4. **Run the app**
   - Connect an Android device or start an emulator
   - Click Run (green play button)

### Building for Production

```bash
# Debug APK
./gradlew assembleDebug

# Release APK
./gradlew assembleRelease

# Release Bundle (for Play Store)
./gradlew bundleRelease
```

## ☁️ Building with Codemagic

This project is configured for cloud builds on [Codemagic](https://codemagic.io):

### Quick Setup

1. **Connect your repository**
   - Log in to Codemagic
   - Click "Add application"
   - Select your Git provider and repository

2. **Select configuration**
   - Codemagic will auto-detect `codemagic.yaml`
   - Select the `android-debug` workflow

3. **Start building!**

### Workflows

| Workflow | Trigger | Output |
|----------|---------|--------|
| `android-debug` | Push to any branch | Debug APK |
| `android-release` | Tag creation (commented out) | Signed Release APK |

### For Release Builds

1. Upload your keystore to Codemagic as `WAKEUP_WARRIOR_KEYSTORE`
2. Uncomment the `android-release` workflow in `codemagic.yaml`
3. Add Google Play credentials

## 🔑 Configuration

### AdMob (Test IDs are pre-configured)

Replace with your real AdMob IDs in production:
```kotlin
// In Constants.kt
object AdMob {
    const val BANNER_AD_UNIT_ID = "ca-app-pub-xxxxx/xxxxx"
    const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-xxxxx/xxxxx"
}
```

### Signing Configuration

For release builds, you need:
- A keystore file
- Keystore password, key alias, and key password
- Configure these in Codemagic settings

## 🏗 Architecture

The app follows Clean Architecture with MVVM pattern:

```
┌─────────────────────────────────────────────────────────┐
│                    Presentation Layer                    │
│  (Compose UI, ViewModels, Navigation, Theme)            │
├─────────────────────────────────────────────────────────┤
│                     Domain Layer                         │
│  (Use Cases, Repository Interfaces, Domain Models)      │
├─────────────────────────────────────────────────────────┤
│                      Data Layer                          │
│  (Room Database, Repository Implementations, APIs)      │
└─────────────────────────────────────────────────────────┘
```

## 💰 Monetization

- **Free Tier**: 3 alarms, basic challenges (Math, Shake), ads
- **Premium Tier**: $2.99/month or $19.99/year
  - Unlimited alarms
  - All challenge types
  - Custom sounds
  - No ads
  - Premium themes
- **Lifetime**: $49.99 one-time

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License.

---

**WakeUp Warrior** - Rise. Conquer. Repeat. ⏰
