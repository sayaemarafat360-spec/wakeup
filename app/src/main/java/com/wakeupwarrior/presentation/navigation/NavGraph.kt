package com.wakeupwarrior.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.wakeupwarrior.presentation.screens.splash.SplashScreen
import com.wakeupwarrior.presentation.screens.onboarding.OnboardingScreen
import com.wakeupwarrior.presentation.screens.home.HomeScreen
import com.wakeupwarrior.presentation.screens.alarm.CreateAlarmScreen
import com.wakeupwarrior.presentation.screens.ringing.RingingScreen
import com.wakeupwarrior.presentation.screens.challenge.ChallengeScreen
import com.wakeupwarrior.presentation.screens.stats.StatsScreen
import com.wakeupwarrior.presentation.screens.achievements.AchievementsScreen
import com.wakeupwarrior.presentation.screens.settings.SettingsScreen
import com.wakeupwarrior.presentation.screens.premium.PremiumScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    alarmId: Long? = null,
    startDestination: String = Screen.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = if (alarmId != null) Screen.Ringing.route else startDestination
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToOnboarding = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onComplete = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToCreateAlarm = { alarmId ->
                    navController.navigate(Screen.CreateAlarm.createRoute(alarmId))
                },
                onNavigateToStats = {
                    navController.navigate(Screen.Stats.route)
                },
                onNavigateToAchievements = {
                    navController.navigate(Screen.Achievements.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onNavigateToPremium = {
                    navController.navigate(Screen.Premium.route)
                }
            )
        }
        
        composable(
            route = Screen.CreateAlarm.route,
            arguments = listOf(
                navArgument("alarmId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val alarmIdArg = backStackEntry.arguments?.getString("alarmId")?.toLongOrNull()
            CreateAlarmScreen(
                alarmId = alarmIdArg,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(
            route = Screen.Ringing.route,
            arguments = listOf(
                navArgument("alarmId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) { backStackEntry ->
            val alarmIdArg = backStackEntry.arguments?.getLong("alarmId") ?: -1L
            RingingScreen(
                alarmId = alarmIdArg,
                onDismiss = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToChallenge = { id, type ->
                    navController.navigate(Screen.Challenge.createRoute(id, type.name))
                }
            )
        }
        
        composable(
            route = Screen.Challenge.route,
            arguments = listOf(
                navArgument("alarmId") {
                    type = NavType.LongType
                },
                navArgument("type") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val alarmIdArg = backStackEntry.arguments?.getLong("alarmId") ?: -1L
            val typeArg = backStackEntry.arguments?.getString("type") ?: "MATH"
            ChallengeScreen(
                alarmId = alarmIdArg,
                challengeTypeName = typeArg,
                onComplete = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Stats.route) {
            StatsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Achievements.route) {
            AchievementsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToPremium = {
                    navController.navigate(Screen.Premium.route)
                }
            )
        }
        
        composable(Screen.Premium.route) {
            PremiumScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
