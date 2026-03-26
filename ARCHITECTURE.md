# WakeUp Warrior - Complete App Architecture

## 📱 Project Overview

**App Name:** WakeUp Warrior  
**Package:** com.wakeupwarrior.app  
**Platform:** Android (Kotlin + Jetpack Compose)  
**Min SDK:** API 26 (Android 8.0) - Covers 95%+ devices  
**Target SDK:** API 34 (Android 14)  
**Architecture:** MVVM + Clean Architecture  

---

## 🎨 Design System

### Color Palette (Dark Theme Primary)
```
Primary:        #6C5CE7 (Purple/Violet - Main accent)
Secondary:      #00CEC9 (Cyan/Teal - Secondary accent)
Tertiary:       #FD79A8 (Pink - Highlights)
Success:        #00B894 (Green - Achievements)
Warning:        #FDCB6E (Yellow - Warnings)
Error:          #E17055 (Red/Orange - Alarm)

Background:     #0D0D1A (Deep Dark Blue-Black)
Surface:        #1A1A2E (Card backgrounds)
SurfaceHigh:    #252542 (Elevated surfaces)
Glass:          rgba(255,255,255,0.08) (Glassmorphic overlay)
GlassBorder:    rgba(255,255,255,0.15) (Glass borders)

TextPrimary:    #FFFFFF
TextSecondary:  #B4B4C4
TextMuted:      #6C6C8A
```

### Glassmorphic Card Style
- Semi-transparent backgrounds with blur effect
- Subtle gradient borders
- Soft shadows with colored tints
- Rounded corners (24dp)

---

## 🏗️ Project Structure

```
app/
├── build.gradle.kts (Module level)
├── proguard-rules.pro
└── src/
    └── main/
        ├── AndroidManifest.xml
        ├── java/com/wakeupwarrior/
        │   ├── WakeUpWarriorApp.kt (Application class)
        │   │
        │   ├── core/                    # Core utilities
        │   │   ├── di/                  # Dependency Injection modules
        │   │   │   ├── AppModule.kt
        │   │   │   ├── DatabaseModule.kt
        │   │   │   └── RepositoryModule.kt
        │   │   ├── util/                # Utility classes
        │   │   │   ├── Constants.kt
        │   │   │   ├── Extensions.kt
        │   │   │   └── TimeUtils.kt
        │   │   ├── alarm/               # Alarm management
        │   │   │   ├── AlarmScheduler.kt
        │   │   │   ├── AlarmReceiver.kt
        │   │   │   └── AlarmService.kt
        │   │   └── notification/         # Notification handling
        │   │       └── NotificationHelper.kt
        │   │
        │   ├── data/                    # Data layer
        │   │   ├── local/               # Local database
        │   │   │   ├── database/
        │   │   │   │   └── AppDatabase.kt
        │   │   │   ├── dao/
        │   │   │   │   ├── AlarmDao.kt
        │   │   │   │   ├── ChallengeDao.kt
        │   │   │   │   ├── AchievementDao.kt
        │   │   │   │   └── StatsDao.kt
        │   │   │   └── entity/
        │   │   │       ├── AlarmEntity.kt
        │   │   │       ├── ChallengeEntity.kt
        │   │   │       ├── AchievementEntity.kt
        │   │   │       └── UserStatsEntity.kt
        │   │   ├── repository/           # Repository implementations
        │   │   │   ├── AlarmRepositoryImpl.kt
        │   │   │   ├── ChallengeRepositoryImpl.kt
        │   │   │   ├── AchievementRepositoryImpl.kt
        │   │   │   └── StatsRepositoryImpl.kt
        │   │   └── model/                # Data models
        │   │       ├── Alarm.kt
        │   │       ├── Challenge.kt
        │   │       ├── ChallengeType.kt
        │   │       ├── Difficulty.kt
        │   │       └── UserStats.kt
        │   │
        │   ├── domain/                  # Domain layer
        │   │   ├── model/                # Domain models
        │   │   │   ├── Alarm.kt
        │   │   │   ├── Challenge.kt
        │   │   │   └── GameResult.kt
        │   │   ├── repository/           # Repository interfaces
        │   │   │   ├── AlarmRepository.kt
        │   │   │   ├── ChallengeRepository.kt
        │   │   │   └── StatsRepository.kt
        │   │   └── usecase/              # Business logic
        │   │       ├── alarm/
        │   │       │   ├── CreateAlarmUseCase.kt
        │   │       │   ├── UpdateAlarmUseCase.kt
        │   │       │   ├── DeleteAlarmUseCase.kt
        │   │       │   ├── GetAlarmsUseCase.kt
        │   │       │   └── TriggerAlarmUseCase.kt
        │   │       ├── challenge/
        │   │       │   ├── GenerateChallengeUseCase.kt
        │   │       │   ├── ValidateChallengeUseCase.kt
        │   │       │   └── GetChallengeByTypeUseCase.kt
        │   │       └── stats/
        │   │           ├── UpdateStreakUseCase.kt
        │   │           ├── RecordWakeUpUseCase.kt
        │   │           └── GetStatsUseCase.kt
        │   │
        │   ├── presentation/             # UI layer
        │   │   ├── theme/                # App theming
        │   │   │   ├── Theme.kt
        │   │   │   ├── Color.kt
        │   │   │   ├── Type.kt
        │   │   │   └── Shape.kt
        │   │   ├── components/           # Reusable UI components
        │   │   │   ├── GlassCard.kt
        │   │   │   ├── GlassButton.kt
        │   │   │   ├── GlassTextField.kt
        │   │   │   ├── GradientBackground.kt
        │   │   │   ├── AnimatedProgressBar.kt
        │   │   │   ├── StreakBadge.kt
        │   │   │   ├── ChallengeCard.kt
        │   │   │   ├── TimePicker.kt
        │   │   │   └── DaySelector.kt
        │   │   ├── navigation/           # Navigation
        │   │   │   ├── NavGraph.kt
        │   │   │   └── Screen.kt
        │   │   └── screens/              # Screen composables
        │   │       ├── splash/
        │   │       │   └── SplashScreen.kt
        │   │       ├── onboarding/
        │   │       │   ├── OnboardingScreen.kt
        │   │       │   └── OnboardingViewModel.kt
        │   │       ├── home/
        │   │       │   ├── HomeScreen.kt
        │   │       │   └── HomeViewModel.kt
        │   │       ├── alarm/
        │   │       │   ├── CreateAlarmScreen.kt
        │   │       │   ├── AlarmListScreen.kt
        │   │       │   ├── AlarmViewModel.kt
        │   │       │   └── components/
        │   │       │       ├── AlarmCard.kt
        │   │       │       └── AlarmTimeDisplay.kt
        │   │       ├── challenge/
        │   │       │   ├── ChallengeScreen.kt
        │   │       │   ├── ChallengeViewModel.kt
        │   │       │   └── games/
        │   │       │       ├── MathChallengeScreen.kt
        │   │       │       ├── MemoryChallengeScreen.kt
        │   │       │       ├── QRChallengeScreen.kt
        │   │       │       ├── ShakeChallengeScreen.kt
        │   │       │       ├── TypingChallengeScreen.kt
        │   │       │       ├── VoiceChallengeScreen.kt
        │   │       │       └── StepsChallengeScreen.kt
        │   │       ├── ringing/
        │   │       │   ├── RingingScreen.kt
        │   │       │   └── RingingViewModel.kt
        │   │       ├── stats/
        │   │       │   ├── StatsScreen.kt
        │   │       │   └── StatsViewModel.kt
        │   │       ├── achievements/
        │   │       │   ├── AchievementsScreen.kt
        │   │       │   └── AchievementsViewModel.kt
        │   │       ├── settings/
        │   │       │   ├── SettingsScreen.kt
        │   │       │   └── SettingsViewModel.kt
        │   │       └── premium/
        │   │           ├── PremiumScreen.kt
        │   │           └── PremiumViewModel.kt
        │   │
        │   └── service/                  # Background services
        │           ├── AlarmForegroundService.kt
        │           └── StepCounterService.kt
        │
        └── res/
            ├── drawable/
            ├── mipmap-*/                 # App icon variants
            ├── raw/                       # Lottie JSON files, sounds
            │   ├── splash_animation.json
            │   ├── alarm_sound.mp3
            │   ├── success.json
            │   ├── achievement_unlock.json
            │   └── morning_motivation.mp3
            └── values/
                ├── strings.xml
                ├── colors.xml
                └── themes.xml
```

---

## 🎮 Challenge Types (Complete Implementation)

### 1. Math Challenge
**Difficulty Levels:**
- Easy: Single digit operations (5 + 3, 7 - 2)
- Medium: Two-digit operations with some complexity (15 + 27, 8 × 7)
- Hard: Multi-step equations ((12 + 8) × 3 - 5)
- Nightmare: Complex calculations with time pressure

**Implementation:**
- Random equation generation
- Adjustable difficulty based on user performance
- Keyboard input with validation
- Timer for added pressure

### 2. Memory/Pattern Challenge
**Game Types:**
- Sequence Memory: Remember and repeat color/light sequences
- Grid Pattern: Memorize pattern shown briefly, recreate it
- Number Sequence: Remember increasing number sequences

**Implementation:**
- Animated pattern display
- Progressive difficulty (longer sequences)
- Visual feedback for correct/incorrect

### 3. QR Code Challenge
**Mechanics:**
- User pre-registers a QR code location (bathroom, kitchen)
- Must scan that specific QR code to dismiss alarm
- Option to use any QR code or specific registered one

**Implementation:**
- CameraX integration
- QR code generation for testing
- Location-based hints

### 4. Shake Challenge
**Mechanics:**
- Shake phone X times (configurable: 10-50 shakes)
- Shake intensity threshold
- Visual counter with satisfying feedback

**Implementation:**
- Accelerometer sensor
- Haptic feedback on each counted shake
- Progress ring animation

### 5. Typing Challenge
**Game Types:**
- Word Scramble: Unscramble words within time limit
- Quote Typing: Type motivational quotes accurately
- Speed Typing: Type random words as fast as possible

**Implementation:**
- Word bank with motivational content
- WPM calculation
- Accuracy tracking

### 6. Voice Challenge
**Mechanics:**
- Say a specific phrase clearly
- Speech recognition with accuracy threshold
- Tone/volume detection for "energetic" wake-up

**Implementation:**
- Android SpeechRecognizer API
- Phrase matching with tolerance
- Audio visualization

### 7. Steps Challenge (Premium)
**Mechanics:**
- Walk X steps to dismiss alarm
- Step counter using accelerometer
- GPS verification optional

**Implementation:**
- Step counter sensor
- Progress display
- Anti-cheat detection

---

## 🔔 Alarm System Architecture

### Reliable Alarm Delivery
```
┌─────────────────────────────────────────────────────────────────┐
│                     ALARM FLOW DIAGRAM                          │
└─────────────────────────────────────────────────────────────────┘

User Sets Alarm
       │
       ▼
┌─────────────────┐
│  AlarmScheduler │ ──── Uses AlarmManager.setExactAndAllowWhileIdle()
│  (System API)   │      for precise delivery even in Doze mode
└─────────────────┘
       │
       ▼ (At scheduled time)
┌─────────────────┐
│ AlarmReceiver   │ ──── BroadcastReceiver (handles BOOT_COMPLETED)
│ (Entry Point)   │      Starts Foreground Service immediately
└─────────────────┘
       │
       ▼
┌─────────────────────┐
│ AlarmForeground     │ ──── Shows persistent notification
│ Service             │      Starts alarm sound/vibration
└─────────────────────┘      Launches RingingScreen
       │
       ▼
┌─────────────────┐
│  RingingScreen  │ ──── Full-screen alarm UI
│                 │      Escalation system (louder, more intense)
└─────────────────┘
       │
       ▼
┌─────────────────┐
│ ChallengeScreen │ ──── Random or configured challenge
│                 │      Cannot be dismissed without completion
└─────────────────┘
       │
       ▼
  Alarm Dismissed
       │
       ▼
┌─────────────────┐
│  Update Stats   │ ──── Streak count, achievements, wake-up time
│  & Achievements │      Schedule next alarm if recurring
└─────────────────┘
```

### Smart Escalation System
- **Phase 1 (0-30s):** Normal volume, gentle vibration pattern
- **Phase 2 (30-60s):** Increased volume, stronger vibration
- **Phase 3 (60s+):** Maximum volume, random vibration patterns
- **Phase 4 (90s+):** Add visual strobing effect, screen flashes

---

## 💎 Premium Features

### Free Tier
- 3 alarms maximum
- Basic challenges (Math, Shake)
- Standard alarm sounds (5 sounds)
- Basic statistics
- Ads displayed

### Premium Tier ($2.99/month or $19.99/year)
- Unlimited alarms
- All challenge types
- Custom sound upload
- Advanced statistics & analytics
- Sleep cycle suggestions
- No ads
- Premium themes
- Multiple alarm profiles

### Lifetime ($49.99)
- All Premium features forever
- Early access to new features
- Priority support

---

## 🏆 Gamification System

### Achievements
| Achievement | Description | Reward |
|-------------|-------------|--------|
| First Rise | Complete your first alarm | 50 coins |
| Week Warrior | 7-day streak | 200 coins + Badge |
| Month Master | 30-day streak | 1000 coins + Premium Theme |
| Early Bird | Wake up before 6 AM for a week | 300 coins |
| Challenge Champion | Complete all challenge types | 500 coins |
| No Snooze Hero | Never snooze for 30 days | Premium Week Free |
| Math Genius | Solve 100 math challenges | Math Master Badge |
| Memory King | Complete 50 memory challenges | Memory King Badge |

### Daily Rewards
- Day 1-3: 10-20 coins
- Day 4-6: 30-50 coins
- Day 7: 100 coins + Reward chest

### Rewards Store
- Unlock new alarm sounds (50-200 coins)
- Unlock new themes (100-500 coins)
- Unlock motivational quote packs (50 coins)
- Unlock challenge variations (100 coins)

---

## 📱 Screen Flow

```
Splash Screen
     │
     ▼
Onboarding (First Launch Only)
     │
     ▼
Home Screen (Dashboard)
     │
     ├──▶ Create Alarm Screen
     │         │
     │         └──▶ Challenge Selection
     │
     ├──▶ Statistics Screen
     │
     ├──▶ Achievements Screen
     │
     ├──▶ Settings Screen
     │         │
     │         └──▶ Premium Screen
     │
     └──▶ Ringing Screen (when alarm triggers)
               │
               └──▶ Challenge Screen
                         │
                         └──▶ Success Animation → Home
```

---

## 🔧 Technical Specifications

### Dependencies
```kotlin
// Core
implementation("androidx.core:core-ktx:1.12.0")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
implementation("androidx.activity:activity-compose:1.8.2")

// Compose
implementation(platform("androidx.compose:compose-bom:2024.01.00"))
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.ui:ui-graphics")
implementation("androidx.compose.ui:ui-tooling-preview")
implementation("androidx.compose.material3:material3")
implementation("androidx.compose.material:material-icons-extended")

// Navigation
implementation("androidx.navigation:navigation-compose:2.7.6")

// Room Database
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
ksp("androidx.room:room-compiler:2.6.1")

// Hilt DI
implementation("com.google.dagger:hilt-android:2.50")
kapt("com.google.dagger:hilt-compiler:2.50")
implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

// DataStore
implementation("androidx.datastore:datastore-preferences:1.0.0")

// Lottie
implementation("com.airbnb.android:lottie-compose:6.3.0")

// CameraX (for QR)
implementation("androidx.camera:camera-core:1.3.1")
implementation("androidx.camera:camera-camera2:1.3.1")
implementation("androidx.camera:camera-lifecycle:1.3.1")
implementation("androidx.camera:camera-view:1.3.1")
implementation("com.google.mlkit:barcode-scanning:17.2.0")

// AdMob
implementation("com.google.android.gms:play-services-ads:23.0.0")

// Billing
implementation("com.android.billingclient:billing-ktx:6.1.0")

// WorkManager (for background tasks)
implementation("androidx.work:work-runtime-ktx:2.9.0")
```

### Permissions Required
```xml
<!-- Core Permissions -->
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
<uses-permission android:name="android.permission.VIBRATE" />
<uses-permission android:name="android.permission.WAKE_LOCK" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_SPECIAL_USE" />
<uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM" />
<uses-permission android:name="android.permission.USE_EXACT_ALARM" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />

<!-- Challenge Permissions -->
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.ACTIVITY_RECOGNITION" />

<!-- Optional -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

---

## 📊 Database Schema

### AlarmEntity
| Column | Type | Description |
|--------|------|-------------|
| id | Long | Primary key |
| label | String | Alarm name/label |
| hour | Int | Hour (0-23) |
| minute | Int | Minute (0-59) |
| repeatDays | String | JSON array of days [1,2,3,4,5] |
| isEnabled | Boolean | Active status |
| challengeType | String | Challenge enum value |
| difficulty | String | Difficulty enum value |
| soundUri | String | Custom sound URI |
| vibrationEnabled | Boolean | Vibration toggle |
| escalationEnabled | Boolean | Escalation toggle |
| snoozeLimit | Int | Max snoozes allowed |
| createdAt | Long | Creation timestamp |
| updatedAt | Long | Last update timestamp |

### UserStatsEntity
| Column | Type | Description |
|--------|------|-------------|
| id | Long | Primary key (always 1) |
| currentStreak | Int | Current wake streak |
| longestStreak | Int | Best streak ever |
| totalWakeUps | Int | Total alarms completed |
| coins | Int | Currency balance |
| premiumUntil | Long? | Premium expiration timestamp |
| totalChallengesCompleted | Int | All-time challenges |
| averageWakeTime | Long | Average wake-up time |
| createdAt | Long | First app launch |

### AchievementEntity
| Column | Type | Description |
|--------|------|-------------|
| id | Long | Primary key |
| achievementId | String | Achievement identifier |
| unlockedAt | Long | Unlock timestamp |
| progress | Int | Progress toward unlock |
| isViewed | Boolean | Has user seen it |

---

## 🎬 Lottie Animations Needed

1. **splash_animation.json** - Logo reveal animation (3-4 seconds)
2. **onboarding_welcome.json** - Person waking up cheerfully
3. **onboarding_alarm.json** - Alarm clock animation
4. **onboarding_challenge.json** - Brain/puzzle animation
5. **onboarding_ready.json** - Success/ready celebration
6. **alarm_ringing.json** - Pulsing alarm animation (loop)
7. **success.json** - Checkmark celebration
8. **achievement_unlock.json** - Trophy/medal reveal
9. **streak_fire.json** - Fire animation for streaks
10. **coins.json** - Coin collect animation
11. **empty_state.json** - Empty list placeholder
12. **loading.json** - Loading spinner

---

## 🚀 Codemagic Configuration

```yaml
# codemagic.yaml
workflows:
  android-app:
    name: WakeUp Warrior
    max_build_duration: 30
    environment:
      android_signing:
        - keystore_reference: YOUR_KEYSTORE_REFERENCE
          keystore_path: keystore.jks
      vars:
        PACKAGE_NAME: "com.wakeupwarrior.app"
    scripts:
      - name: Build Debug APK
        script: ./gradlew assembleDebug
      - name: Build Release APK
        script: ./gradlew assembleRelease
    artifacts:
      - app/build/outputs/**/*.apk
    publishing:
      email:
        recipients:
          - your-email@example.com
```

---

## 📈 Development Phases

### Phase 1: Foundation (Completed in this build)
- [x] Project structure setup
- [x] Theme and design system
- [x] Database layer
- [x] DI configuration
- [x] Splash screen
- [x] Onboarding flow

### Phase 2: Core Features
- [x] Alarm CRUD operations
- [x] Alarm scheduling system
- [x] Basic challenges (Math, Shake)
- [x] Ringing screen
- [x] Statistics tracking

### Phase 3: Advanced Features
- [x] All challenge types
- [x] Gamification system
- [x] Premium system
- [x] AdMob integration

### Phase 4: Polish
- [x] Lottie animations
- [x] Sound effects
- [x] Performance optimization
- [x] Edge case handling

---

This architecture document serves as the complete blueprint for WakeUp Warrior. All code will follow this structure precisely.
