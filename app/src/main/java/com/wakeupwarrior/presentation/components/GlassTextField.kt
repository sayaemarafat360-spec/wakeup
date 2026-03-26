package com.wakeupwarrior.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wakeupwarrior.presentation.theme.*

/**
 * Glass-styled text input field.
 */
@Composable
fun GlassTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    label: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    val shape = RoundedCornerShape(12.dp)
    
    Column(modifier = modifier) {
        if (label != null) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = TextSecondary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled && !readOnly,
            readOnly = readOnly,
            singleLine = singleLine,
            maxLines = maxLines,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = TextPrimary,
                textAlign = TextAlign.Start
            ),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 56.dp)
                        .clip(shape)
                        .background(GlassBackground, shape)
                        .border(
                            width = 1.dp,
                            color = if (isError) Error else GlassBorder,
                            shape = shape
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    leadingIcon?.invoke()
                    
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 16.dp)
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextMuted
                            )
                        }
                        innerTextField()
                    }
                    
                    trailingIcon?.invoke()
                }
            }
        )
        
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = Error,
                modifier = Modifier.padding(top = 4.dp, start = 16.dp)
            )
        }
    }
}

/**
 * Large text field for typing challenges.
 */
@Composable
fun GlassTextFieldLarge(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    enabled: Boolean = true,
    maxLines: Int = 5
) {
    val shape = RoundedCornerShape(16.dp)
    
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        maxLines = maxLines,
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = TextPrimary,
            textAlign = TextAlign.Start
        ),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 150.dp)
                    .clip(shape)
                    .background(GlassBackground, shape)
                    .border(1.dp, GlassBorder, shape)
                    .padding(16.dp)
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextMuted
                    )
                }
                innerTextField()
            }
        }
    )
}

/**
 * Number input field for math challenges.
 */
@Composable
fun NumberInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "?"
) {
    val shape = RoundedCornerShape(12.dp)
    
    BasicTextField(
        value = value,
        onValueChange = { newValue ->
            if (newValue.isEmpty() || newValue.matches(Regex("-?\\d*"))) {
                onValueChange(newValue)
            }
        },
        singleLine = true,
        textStyle = MaterialTheme.typography.headlineLarge.copy(
            color = TextPrimary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        ),
        decorationBox = { innerTextField ->
            Box(
                modifier = modifier
                    .widthIn(min = 100.dp)
                    .height(80.dp)
                    .clip(shape)
                    .background(GlassBackground, shape)
                    .border(1.dp, Primary.copy(alpha = 0.5f), shape),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.headlineLarge,
                        color = TextMuted
                    )
                }
                innerTextField()
            }
        }
    )
}
