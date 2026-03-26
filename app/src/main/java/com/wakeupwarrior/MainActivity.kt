package com.wakeupwarrior

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.wakeupwarrior.presentation.navigation.NavGraph
import com.wakeupwarrior.presentation.theme.WakeUpWarriorTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Check if launched from alarm
        val alarmId = intent.getLongExtra("alarmId", -1L)
        
        setContent {
            WakeUpWarriorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val navController = rememberNavController()
                    NavGraph(
                        navController = navController,
                        alarmId = if (alarmId != -1L) alarmId else null
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }
}
