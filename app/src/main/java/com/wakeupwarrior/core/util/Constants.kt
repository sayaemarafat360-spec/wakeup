package com.wakeupwarrior.core.util

object Constants {
    
    object Database {
        const val NAME = "wakeup_warrior_db"
        const val VERSION = 1
    }
    
    object Notification {
        const val CHANNEL_ID = "alarm_channel"
        const val REMINDER_CHANNEL_ID = "reminder_channel"
        const val ALARM_NOTIFICATION_ID = 1001
    }
    
    object Alarm {
        const val ACTION_TRIGGER = "com.wakeupwarrior.ALARM_TRIGGER"
        const val ACTION_DISMISS = "com.wakeupwarrior.ALARM_DISMISS"
        const val ACTION_SNOOZE = "com.wakeupwarrior.ALARM_SNOOZE"
        const val EXTRA_ALARM_ID = "alarm_id"
        const val SNOOZE_DURATION_MINUTES = 5L
        const val MAX_FREE_ALARMS = 3
    }
    
    object Challenge {
        const val MATH_EASY_OPERATIONS = 1
        const val MATH_MEDIUM_OPERATIONS = 2
        const val MATH_HARD_OPERATIONS = 3
        const val MATH_NIGHTMARE_OPERATIONS = 4
        
        const val SHAKE_THRESHOLD = 15f
        const val SHAKE_DEFAULT_COUNT = 20
        
        const val MEMORY_SEQUENCE_LENGTH_EASY = 3
        const val MEMORY_SEQUENCE_LENGTH_MEDIUM = 5
        const val MEMORY_SEQUENCE_LENGTH_HARD = 7
        const val MEMORY_SEQUENCE_LENGTH_NIGHTMARE = 10
        
        const val TYPING_MIN_LENGTH = 20
        const val TYPING_TIME_LIMIT_SECONDS = 60
    }
    
    object Gamification {
        const val COINS_FIRST_ALARM = 50
        const val COINS_DAILY_BASE = 10
        const val COINS_STREAK_BONUS = 5
        const val COINS_WEEK_STREAK = 200
        const val COINS_MONTH_STREAK = 1000
    }
    
    object Premium {
        const val MONTHLY_SKU = "premium_monthly"
        const val YEARLY_SKU = "premium_yearly"
        const val LIFETIME_SKU = "premium_lifetime"
        
        const val PRICE_MONTHLY = 2.99
        const val PRICE_YEARLY = 19.99
        const val PRICE_LIFETIME = 49.99
    }
    
    object AdMob {
        // Test Ad Unit IDs
        const val BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/9214589741"
        const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
        const val REWARDED_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"
    }
    
    object Preferences {
        const val DATASTORE_NAME = "wakeup_warrior_prefs"
        const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
        const val KEY_PREMIUM = "is_premium"
        const val KEY_PREMIUM_UNTIL = "premium_until"
        const val KEY_SELECTED_THEME = "selected_theme"
        const val KEY_DEFAULT_SOUND_URI = "default_sound_uri"
        const val KEY_VIBRATION_ENABLED = "vibration_enabled"
        const val KEY_ESCALATION_ENABLED = "escalation_enabled"
        const val KEY_DEFAULT_SNOOZE_LIMIT = "default_snooze_limit"
    }
    
    object Time {
        const val MORNING_START = 5
        const val AFTERNOON_START = 12
        const val EVENING_START = 17
        const val NIGHT_START = 21
    }
}
