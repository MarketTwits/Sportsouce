package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.sportsouce.start.domain.StartItem

@Composable
internal fun StartRegistrationButton(
    modifier: Modifier = Modifier,
    startStatus: StartItem.StartStatus,
    isLoading: Boolean = false,
    hasError: Boolean = false,
    onClickRegistration: () -> Unit,
) {
    if (startStatus.code == 3 || startStatus.code == 2) {
        val infiniteTransition = rememberInfiniteTransition()

        val dotCount by infiniteTransition.animateValue(
            initialValue = 0,
            targetValue = 3,
            typeConverter = Int.VectorConverter,
            animationSpec = infiniteRepeatable(
                animation = tween(900, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )

        val baseColor = if (startStatus.code == 3) {
            MaterialTheme.colorScheme.secondary
        } else {
            SportSouceColor.SportSouceRegistryCommingSoonYellow
        }

        val isUpcoming = startStatus.code == 2

        val containerColor by animateColorAsState(
            targetValue = when {
                isLoading || hasError -> MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                isUpcoming -> baseColor.copy(alpha = 0.7f)
                else -> baseColor
            },
            animationSpec = tween(300, easing = FastOutSlowInEasing)
        )

        val isEnabled = !isLoading && !hasError && !isUpcoming
        val buttonText = if (startStatus.code == 3) {
            if (isLoading) "Загрузка${".".repeat(dotCount)}" else "Зарегестрироваться"
        } else {
            "Регистрация скоро начнется"
        }

        Button(
            modifier = modifier
                .height(55.dp)
                .fillMaxWidth(),
            onClick = { if (isEnabled) onClickRegistration() },
            enabled = isEnabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = MaterialTheme.colorScheme.onSecondary,
                disabledContainerColor = containerColor,
                disabledContentColor = MaterialTheme.colorScheme.onSecondary
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 6.dp,
                focusedElevation = 4.dp,
                hoveredElevation = 5.dp,
                disabledElevation = 0.dp
            ),
            shape = RoundedCornerShape(16.dp),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.padding(2.dp),
                    text = buttonText,
                    color = MaterialTheme.colorScheme.onSecondary,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    fontSize = 16.sp,
                    fontFamily = FontNunito.bold(),
                )
            }
        }
    }
}
