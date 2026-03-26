package com.wakeupwarrior.presentation.screens.onboarding

import kotlinx.coroutines.launch
import androidx.compose.ui.draw.offset
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wakeupwarrior.presentation.components.GradientBackground
import com.wakeupwarrior.presentation.components.GlassButton
import com.wakeupwarrior.presentation.components.GlassButtonSecondary
import com.wakeupwarrior.presentation.theme.*

data class OnboardingPage(
    val title: String,
    val description: String,
    val emoji: String,
    val gradientColors: List<Color>
)

val onboardingPages = listOf(
    OnboardingPage(
        title = "Welcome to\nWakeUp Warrior",
        description = "The alarm app that won't let you snooze your life away. No more oversleeping, no more excuses.",
        emoji = "💪",
        gradientColors = listOf(Primary.copy(alpha = 0.3f), Secondary.copy(alpha = 0.2f))
    ),
    OnboardingPage(
        title = "Alarms That\nActually Work",
        description = "Our alarms are designed to wake you up, no matter what. Once set, there's no escaping.",
        emoji = "⏰",
        gradientColors = listOf(Secondary.copy(alpha = 0.3f), Tertiary.copy(alpha = 0.2f))
    ),
    OnboardingPage(
        title = "Complete Challenges\nto Wake Up",
        description = "Solve math puzzles, shake your phone, scan QR codes - challenges that force your brain to wake up fully.",
        emoji = "🧠",
        gradientColors = listOf(Tertiary.copy(alpha = 0.3f), Primary.copy(alpha = 0.2f))
    ),
    OnboardingPage(
        title = "Ready to\nConquer Mornings?",
        description = "Join thousands of warriors who've defeated their snooze habits forever. Your journey starts now!",
        emoji = "🏆",
        gradientColors = listOf(Success.copy(alpha = 0.3f), Warning.copy(alpha = 0.2f))
    )
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val currentPage by pagerState.currentPage.collectAsState()
    
    val scope = rememberCoroutineScope()
    
    // Animation for current page content
    var visiblePage by remember { mutableStateOf(0) }
    
    LaunchedEffect(currentPage) {
        visiblePage = currentPage
    }
    
    GradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Skip button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                if (currentPage < onboardingPages.size - 1) {
                    TextButton(onClick = onComplete) {
                        Text(
                            text = "Skip",
                            style = MaterialTheme.typography.labelLarge,
                            color = TextMuted
                        )
                    }
                }
            }
            
            // Pager content
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) { page ->
                OnboardingPageContent(
                    page = onboardingPages[page],
                    isVisible = page == visiblePage
                )
            }
            
            // Page indicators
            Row(
                modifier = Modifier.padding(vertical = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(onboardingPages.size) { index ->
                    PageIndicator(
                        isSelected = index == currentPage,
                        color = onboardingPages[index].gradientColors.first()
                    )
                }
            }
            
            // Navigation buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (currentPage > 0) {
                    GlassButtonSecondary(
                        text = "Back",
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(currentPage - 1)
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
                
                GlassButton(
                    text = if (currentPage == onboardingPages.size - 1) "Get Started" else "Next",
                    onClick = {
                        if (currentPage == onboardingPages.size - 1) {
                            viewModel.completeOnboarding()
                            onComplete()
                        } else {
                            scope.launch {
                                pagerState.animateScrollToPage(currentPage + 1)
                            }
                        }
                    },
                    modifier = Modifier.weight(if (currentPage > 0) 1f else 1f)
                )
            }
        }
    }
}

@Composable
fun OnboardingPageContent(
    page: OnboardingPage,
    isVisible: Boolean
) {
    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(400),
        label = "alpha"
    )
    
    val offsetY by animateDpAsState(
        targetValue = if (isVisible) 0.dp else 20.dp,
        animationSpec = tween(400),
        label = "offsetY"
    )
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .alpha(alpha)
            .offset(y = offsetY),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Animated emoji/illustration placeholder
        Box(
            modifier = Modifier
                .size(180.dp)
                .background(
                    brush = Brush.radialGradient(page.gradientColors),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = page.emoji,
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = MaterialTheme.typography.displayLarge.fontSize * 2.5
                )
            )
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // Title
        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            ),
            color = TextPrimary,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Description
        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun PageIndicator(
    isSelected: Boolean,
    color: Color
) {
    val width by animateDpAsState(
        targetValue = if (isSelected) 32.dp else 8.dp,
        animationSpec = tween(300),
        label = "width"
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.3f,
        animationSpec = tween(300),
        label = "alpha"
    )
    
    Box(
        modifier = Modifier
            .height(8.dp)
            .width(width)
            .alpha(alpha)
            .background(
                color = if (isSelected) color else TextMuted,
                shape = CircleShape
            )
    )
}
