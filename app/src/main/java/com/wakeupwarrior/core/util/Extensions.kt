package com.wakeupwarrior.core.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun Long.formatTime(): String {
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return sdf.format(this)
}

fun Long.formatDate(): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return sdf.format(this)
}

fun Long.formatDateTime(): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault())
    return sdf.format(this)
}

fun Int.toDayName(): String {
    return when (this) {
        Calendar.SUNDAY -> "Sun"
        Calendar.MONDAY -> "Mon"
        Calendar.TUESDAY -> "Tue"
        Calendar.WEDNESDAY -> "Wed"
        Calendar.THURSDAY -> "Thu"
        Calendar.FRIDAY -> "Fri"
        Calendar.SATURDAY -> "Sat"
        else -> ""
    }
}

fun Int.toDayNameFull(): String {
    return when (this) {
        Calendar.SUNDAY -> "Sunday"
        Calendar.MONDAY -> "Monday"
        Calendar.TUESDAY -> "Tuesday"
        Calendar.WEDNESDAY -> "Wednesday"
        Calendar.THURSDAY -> "Thursday"
        Calendar.FRIDAY -> "Friday"
        Calendar.SATURDAY -> "Saturday"
        else -> ""
    }
}

fun getTimeOfDay(): String {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when (hour) {
        in Constants.Time.MORNING_START until Constants.Time.AFTERNOON_START -> "morning"
        in Constants.Time.AFTERNOON_START until Constants.Time.EVENING_START -> "afternoon"
        in Constants.Time.EVENING_START until Constants.Time.NIGHT_START -> "evening"
        else -> "night"
    }
}

fun Long.toTimeUntilString(): String {
    val now = System.currentTimeMillis()
    val diff = this - now
    
    if (diff <= 0) return "Now"
    
    val hours = diff / (1000 * 60 * 60)
    val minutes = (diff % (1000 * 60 * 60)) / (1000 * 60)
    
    return when {
        hours > 24 -> {
            val days = hours / 24
            "$days day${if (days > 1) "s" else ""}"
        }
        hours > 0 -> "$hours hr $minutes min"
        else -> "$minutes min"
    }
}

fun calculateNextAlarmTime(hour: Int, minute: Int, repeatDays: List<Int>): Long {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    
    if (repeatDays.isEmpty()) {
        // One-time alarm
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return calendar.timeInMillis
    }
    
    // Repeating alarm
    val currentDay = calendar.get(Calendar.DAY_OF_WEEK)
    val sortedDays = repeatDays.sorted()
    
    // Find the next occurrence
    for (day in sortedDays) {
        if (day > currentDay) {
            calendar.set(Calendar.DAY_OF_WEEK, day)
            if (calendar.timeInMillis > System.currentTimeMillis()) {
                return calendar.timeInMillis
            }
        }
    }
    
    // If no day found this week, take first day of next week
    val firstDay = sortedDays.first()
    val daysUntilNext = (7 - currentDay + firstDay) % 7
    calendar.add(Calendar.DAY_OF_MONTH, if (daysUntilNext == 0) 7 else daysUntilNext)
    
    return calendar.timeInMillis
}

fun formatDuration(seconds: Int): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    
    return when {
        hours > 0 -> String.format("%d:%02d:%02d", hours, minutes, secs)
        minutes > 0 -> String.format("%d:%02d", minutes, secs)
        else -> String.format("0:%02d", secs)
    }
}
