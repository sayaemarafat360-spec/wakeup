package com.wakeupwarrior

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wakeupwarrior.presentation.screens.ringing.RingingScreen
import com.wakeupwarrior.presentation.theme.WakeUpWarriorTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmActivity : ComponentActivity() {
    
    companion object {
        private const val EXTRA_ALARM_ID = "alarm_id"
        
        fun createIntent(context: Context, alarmId: Long): Intent {
            return Intent(context, AlarmActivity::class.java).apply {
                putExtra(EXTRA_ALARM_ID, alarmId)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Make the activity show over lock screen
        setShowWhenLocked(true)
        setTurnScreenOn(true)
        
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val alarmId = intent.getLongExtra(EXTRA_ALARM_ID, -1L)

        setContent {
            WakeUpWarriorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    RingingScreen(
                        alarmId = alarmId,
                        onDismiss = {
                            finish()
                        },
                        onNavigateToChallenge = { _, _ ->
                            // Handle challenge navigation
                        }
                    )
                }
            }
        }
    }

    override fun onBackPressed() {
        // Disable back button during alarm
    }
}
