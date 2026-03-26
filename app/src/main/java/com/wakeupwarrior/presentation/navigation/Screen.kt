package com.wakeupwarrior.presentation.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")
    object CreateAlarm : Screen("create_alarm?alarmId={alarmId}") {
        fun createRoute(alarmId: Long? = null) = 
            if (alarmId != null) "create_alarm?alarmId=$alarmId" else "create_alarm"
    }
    object Ringing : Screen("ringing?alarmId={alarmId}") {
        fun createRoute(alarmId: Long) = "ringing?alarmId=$alarmId"
    }
    object Challenge : Screen("challenge?alarmId={alarmId}&type={type}") {
        fun createRoute(alarmId: Long, type: String) = "challenge?alarmId=$alarmId&type=$type"
    }
    object Stats : Screen("stats")
    object Achievements : Screen("achievements")
    object Settings : Screen("settings")
    object Premium : Screen("premium")
}
