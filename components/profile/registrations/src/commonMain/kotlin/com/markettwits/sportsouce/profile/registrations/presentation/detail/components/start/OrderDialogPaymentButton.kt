package com.markettwits.sportsouce.profile.registrations.presentation.detail.components.start

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.sportsouce.profile.registrations.presentation.detail.store.StartOrderStore

@Composable
internal fun OrderDialogPaymentButton(
    modifier: Modifier = Modifier,
    priceState: StartOrderStore.StartPriceResult,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Error state
        AnimatedVisibility(
            visible = priceState is StartOrderStore.StartPriceResult.Failed,
            enter = expandVertically(
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
            ) + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(Shapes.medium)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.1f),
                                MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.05f)
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Не удалось обновить стоимость",
                        fontSize = 14.sp,
                        fontFamily = FontNunito.semiBoldBold(),
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Payment button
        AnimatedVisibility(
            visible = priceState !is StartOrderStore.StartPriceResult.Free &&
                    priceState !is StartOrderStore.StartPriceResult.Failed,
            enter = expandVertically(
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
            ) + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            val buttonScale by animateFloatAsState(
                targetValue = if (priceState is StartOrderStore.StartPriceResult.Loading) 0.95f else 1f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(buttonScale),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    disabledContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
                ),
                shape = Shapes.large,
                enabled = priceState is StartOrderStore.StartPriceResult.Success,
                onClick = onClick
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    when (priceState) {
                        is StartOrderStore.StartPriceResult.Loading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onSecondary,
                                strokeCap = StrokeCap.Round,
                                strokeWidth = 2.dp
                            )
                            Text(
                                text = "Загрузка...",
                                fontSize = 16.sp,
                                fontFamily = FontNunito.semiBoldBold(),
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                        }

                        is StartOrderStore.StartPriceResult.Success -> {
                            Icon(
                                imageVector = Icons.Default.Payment,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Оплатить ${priceState.price} ₽",
                                fontSize = 16.sp,
                                fontFamily = FontNunito.bold(),
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}