package com.wakeupwarrior.presentation.screens.challenge

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wakeupwarrior.data.model.ChallengeType
import com.wakeupwarrior.presentation.components.GradientBackground
import com.wakeupwarrior.presentation.screens.challenge.games.*

@Composable
fun ChallengeScreen(
    alarmId: Long,
    challengeTypeName: String,
    onComplete: () -> Unit,
    viewModel: ChallengeViewModel = hiltViewModel()
) {
    val challengeType = remember { ChallengeType.valueOf(challengeTypeName) }
    val difficulty by viewModel.difficulty.collectAsStateWithLifecycle()
    val isCompleted by viewModel.isCompleted.collectAsStateWithLifecycle()
    
    LaunchedEffect(alarmId) {
        viewModel.loadChallenge(alarmId, challengeType)
    }
    
    LaunchedEffect(isCompleted) {
        if (isCompleted) {
            onComplete()
        }
    }
    
    GradientBackground {
        Box(modifier = Modifier.fillMaxSize()) {
            when (challengeType) {
                ChallengeType.MATH -> MathChallengeScreen(
                    difficulty = difficulty,
                    onComplete = { viewModel.completeChallenge() }
                )
                ChallengeType.SHAKE -> ShakeChallengeScreen(
                    difficulty = difficulty,
                    onComplete = { viewModel.completeChallenge() }
                )
                ChallengeType.MEMORY -> MemoryChallengeScreen(
                    difficulty = difficulty,
                    onComplete = { viewModel.completeChallenge() }
                )
                ChallengeType.QR -> QRChallengeScreen(
                    difficulty = difficulty,
                    onComplete = { viewModel.completeChallenge() }
                )
                ChallengeType.TYPING -> TypingChallengeScreen(
                    difficulty = difficulty,
                    onComplete = { viewModel.completeChallenge() }
                )
                ChallengeType.VOICE -> VoiceChallengeScreen(
                    difficulty = difficulty,
                    onComplete = { viewModel.completeChallenge() }
                )
                ChallengeType.STEPS -> StepsChallengeScreen(
                    difficulty = difficulty,
                    onComplete = { viewModel.completeChallenge() }
                )
            }
        }
    }
}
