package com.wakeupwarrior.presentation.screens.alarm

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.wakeupwarrior.data.model.ChallengeType
import com.wakeupwarrior.presentation.components.GlassCard
import com.wakeupwarrior.presentation.theme.*
import java.util.Calendar

@Composable
fun TimePickerDialog(
    initialHour: Int,
    initialMinute: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit
) {
    var selectedHour by remember { mutableStateOf(initialHour) }
    var selectedMinute by remember { mutableStateOf(initialMinute) }
    
    Dialog(onDismissRequest = onDismiss) {
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Set Alarm Time",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = TextPrimary
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Time picker
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Hour picker
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Hour",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        var hourInput by remember { mutableStateOf(initialHour.toString().padStart(2, '0')) }
                        
                        OutlinedTextField(
                            value = hourInput,
                            onValueChange = { newValue ->
                                if (newValue.length <= 2) {
                                    val hour = newValue.toIntOrNull()
                                    if (hour == null || hour in 0..23) {
                                        hourInput = newValue
                                        hour?.let { selectedHour = it }
                                    }
                                }
                            },
                            modifier = Modifier.width(80.dp),
                            textStyle = LocalTextStyle.current.copy(
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    
                    Text(
                        text = ":",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Primary
                    )
                    
                    // Minute picker
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Minute",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        var minuteInput by remember { mutableStateOf(initialMinute.toString().padStart(2, '0')) }
                        
                        OutlinedTextField(
                            value = minuteInput,
                            onValueChange = { newValue ->
                                if (newValue.length <= 2) {
                                    val minute = newValue.toIntOrNull()
                                    if (minute == null || minute in 0..59) {
                                        minuteInput = newValue
                                        minute?.let { selectedMinute = it }
                                    }
                                }
                            },
                            modifier = Modifier.width(80.dp),
                            textStyle = LocalTextStyle.current.copy(
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel", color = TextSecondary)
                    }
                    
                    Button(
                        onClick = { onConfirm(selectedHour, selectedMinute) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary
                        )
                    ) {
                        Text("Set Time")
                    }
                }
            }
        }
    }
}

@Composable
fun ChallengePickerDialog(
    selectedType: ChallengeType,
    isPremium: Boolean,
    onDismiss: () -> Unit,
    onSelect: (ChallengeType) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Select Challenge",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = TextPrimary
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Choose how you want to wake up",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                ChallengeType.values().forEach { type ->
                    ChallengeOption(
                        type = type,
                        isSelected = selectedType == type,
                        isPremium = isPremium,
                        onClick = { onSelect(type) }
                    )
                    
                    if (type != ChallengeType.values().last()) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ChallengeOption(
    type: ChallengeType,
    isSelected: Boolean,
    isPremium: Boolean,
    onClick: () -> Unit
) {
    val isLocked = type.isPremium && !isPremium
    
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = if (isSelected) Primary.copy(alpha = 0.2f) else GlassBackground,
        borderColor = if (isSelected) Primary else GlassBorder,
        onClick = { if (!isLocked) onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = when (type.icon) {
                    "calculator" -> "🧮"
                    "shake" -> "📳"
                    "brain" -> "🧠"
                    "qr_code" -> "📷"
                    "keyboard" -> "⌨️"
                    "mic" -> "🎤"
                    "directions_walk" -> "🚶"
                    else -> "❓"
                },
                style = MaterialTheme.typography.headlineMedium
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = type.displayName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    ),
                    color = if (isLocked) TextMuted else TextPrimary
                )
                
                Text(
                    text = type.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
            
            if (isLocked) {
                Lock.let { icon ->
                    Icon(
                        imageVector = icon,
                        contentDescription = "Locked",
                        tint = Warning
                    )
                }
            } else if (isSelected) {
                Icon(
                    imageVector = Check,
                    contentDescription = "Selected",
                    tint = Primary
                )
            }
        }
    }
}
