package com.markettwits.sportsouce.auth.flow.internal.sign_up.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.model.SignUpStage

@Composable
fun SignUpProgressIndicator(
    modifier: Modifier = Modifier,
    currentStage: SignUpStage,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Stage 1
        StageIndicator(
            stageNumber = 1,
            isActive = currentStage.index >= 1,
            isCompleted = currentStage.index > 1
        )

        // Connector line
        ConnectorLine(isCompleted = currentStage.index > 1)

        // Stage 2
        StageIndicator(
            stageNumber = 2,
            isActive = currentStage.index >= 2,
            isCompleted = currentStage.index > 2
        )

        // Connector line
        ConnectorLine(isCompleted = currentStage.index > 2)

        // Stage 3
        StageIndicator(
            stageNumber = 3,
            isActive = currentStage.index >= 3,
            isCompleted = false // Last stage is never "completed"
        )
    }
}

@Composable
private fun StageIndicator(
    stageNumber: Int,
    isActive: Boolean,
    isCompleted: Boolean,
) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(
                color = when {
                    isCompleted -> MaterialTheme.colorScheme.tertiary
                    isActive -> MaterialTheme.colorScheme.tertiary
                    else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stageNumber.toString(),
            fontFamily = FontNunito.bold(),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = when {
                isCompleted || isActive -> MaterialTheme.colorScheme.onTertiary
                else -> MaterialTheme.colorScheme.outline
            }
        )
    }
}

@Composable
private fun ConnectorLine(
    isCompleted: Boolean,
) {
    Box(
        modifier = Modifier
            .size(width = 40.dp, height = 2.dp)
            .background(
                color = if (isCompleted) {
                    MaterialTheme.colorScheme.tertiary
                } else {
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                }
            )
    )
}