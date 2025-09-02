package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.extensions.noRippleClickable
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.sportsouce.start.cloud.model.start.fields.DistinctDistance
import com.markettwits.sportsouce.start.domain.StartItem
import kotlinx.coroutines.delay

@Composable
internal fun StartRegistrationPanel(
    modifier: Modifier,
    distance: List<DistinctDistance>,
    startStatus: StartItem.StartStatus,
    regLink: String,
    onClickRegistration: () -> Unit,
) {
    // Create a unique key for this registration data combination
    val dataKey = "${distance.joinToString { it.name }}_${startStatus.code}_${regLink}"

    var isVisible by rememberSaveable(dataKey) { mutableStateOf(false) }
    var hasAnimated by rememberSaveable(dataKey) { mutableStateOf(false) }

    LaunchedEffect(distance, regLink, startStatus.code) {
        if (!hasAnimated) {
            delay(100) // Small delay for smoother appearance
            isVisible = true
            hasAnimated = true
        } else {
            isVisible = true // Show immediately if already animated
        }
    }
    
    if (distance.isNotEmpty() && startStatus.code == 3 || regLink.isNotEmpty() && startStatus.code == 3) {
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(
                animationSpec = tween(durationMillis = 600, easing = EaseOutCubic)
            ) + slideInVertically(
                animationSpec = tween(durationMillis = 700, easing = EaseOutCubic),
                initialOffsetY = { it / 4 } // Smoother, shorter slide distance
            )
        ) {
            Box(
            modifier = modifier
                .noRippleClickable {
                    onClickRegistration()
                }
                .fillMaxWidth()

        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(76.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                shape = Shapes.medium,
            ) {
                Spacer(modifier = Modifier.height(50.dp))
                if (distance.isNotEmpty()) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = distance.joinToString(", ") { it.name },
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.8f),
                        fontSize = 12.sp,
                        fontFamily = FontNunito.regular(),
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                    )
                } else {
                    Spacer(modifier = Modifier.height(12.dp))
                }

            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = {
                    onClickRegistration()
                },
                elevation = ButtonDefaults.buttonElevation(2.dp, 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                shape = Shapes.medium,
            ) {
                Text(
                    text = "Зарегистрироваться",
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontSize = 16.sp,
                    fontFamily = FontNunito.bold(),
                    textAlign = TextAlign.Center,
                )
            }
        }
        }
    }
}