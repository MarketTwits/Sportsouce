package com.markettwits.sportsouce.profile.registrations.presentation.detail.components.start

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.image.DefaultImages
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderPaymentStatus

@Composable
fun OrderStartCard(
    modifier: Modifier = Modifier,
    item: StartOrderInfo,
    onClickStart: (Int) -> Unit,
) {
    var isPressed by remember { mutableStateOf(false) }
    var isHovered by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    val scale by animateFloatAsState(
        targetValue = when {
            isPressed -> 0.96f
            isHovered -> 1.02f
            else -> 1f
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    val elevation by animateFloatAsState(
        targetValue = if (isPressed) 2.dp.value else 8.dp.value,
        animationSpec = tween(200),
        label = "elevation"
    )

    Card(
        modifier = modifier
            .scale(scale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = {
                        onClickStart(item.startId)
                    }
                )
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = elevation.dp,
            pressedElevation = 2.dp,
            hoveredElevation = 12.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
                        )
                    )
                )
        ) {
            Column {
                Box {
                    RegistrationsCardImageCard(
                        image = item.image,
                        modifier = Modifier.fillMaxWidth()
                    )

                    PaymentStatusBadge(
                        paymentStatus = item.payment,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                    )

                    if (item.members.any { it.results.isNotEmpty() }) {
                        ResultsBadge(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(12.dp)
                        )
                    }
                }

                RegistrationsCardContentInfo(
                    title = item.startTitle,
                    startDate = item.dateStartPreview,
                    orderId = item.id,
                    cost = item.cost,
                    membersCount = item.members.size,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
private fun PaymentStatusBadge(
    paymentStatus: StartOrderPaymentStatus,
    modifier: Modifier = Modifier,
) {
    val statusColor = mapOrderStatusColor(paymentStatus)
    val statusIcon = mapOrderStatusIcon(paymentStatus)

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = statusColor.copy(alpha = 0.8f),
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = statusIcon,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = MaterialTheme.colorScheme.onSecondary
            )
            Text(
                text = paymentStatus.title,
                fontSize = 12.sp,
                fontFamily = FontNunito.bold(),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}

@Composable
private fun ResultsBadge(
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.9f),
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = "Есть результаты",
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSecondary
            )
            Text(
                text = "Есть результаты",
                fontSize = 12.sp,
                fontFamily = FontNunito.bold(),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}

@Composable
private fun RegistrationsCardContentInfo(
    modifier: Modifier = Modifier,
    title: String,
    startDate: String,
    orderId: Int,
    cost: String,
    membersCount: Int,
) {
    Column(
        modifier = modifier.animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontFamily = FontNunito.bold(),
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onBackground,
            lineHeight = 22.sp
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = startDate,
                fontSize = 14.sp,
                fontFamily = FontNunito.medium(),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                InfoChip(
                    icon = Icons.Default.Receipt,
                    text = "№ $orderId"
                )
                InfoChip(
                    icon = Icons.Default.Group,
                    text = "$membersCount участн."
                )
            }

            if (cost.isNotEmpty() && cost != "0") {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = "$cost ₽",
                        fontSize = 16.sp,
                        fontFamily = FontNunito.bold(),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoChip(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                tint = MaterialTheme.colorScheme.outline
            )
            Text(
                text = text,
                fontSize = 12.sp,
                fontFamily = FontNunito.medium(),
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
fun RegistrationsCardImageCard(
    modifier: Modifier = Modifier,
    image: String,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
    ) {
        SubcomposeAsyncImage(
            model = image,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            error = {
                SubcomposeAsyncImageContent(
                    modifier = Modifier.fillMaxSize(),
                    painter = DefaultImages.EmptyImageStart()
                )
            },
            success = {
                SubcomposeAsyncImageContent(modifier = Modifier.fillMaxSize())
            }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.3f),
                            Color.Transparent,
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.3f)
                        )
                    )
                )
        )
    }
}

private fun mapOrderStatusColor(payment: StartOrderPaymentStatus): Color {
    return when (payment) {
        is StartOrderPaymentStatus.Success -> Color(0xFF4CAF50) // Зеленый
        is StartOrderPaymentStatus.Free -> Color(0xFF2196F3) // Синий
        is StartOrderPaymentStatus.OnPlace -> Color(0xFFFF9800) // Оранжевый
        is StartOrderPaymentStatus.NotPaid -> Color(0xFFF44336) // Красный
        is StartOrderPaymentStatus.PaymentCancelled -> Color(0xFF9E9E9E) // Серый
        is StartOrderPaymentStatus.WithoutStatus -> Color(0xFF607D8B) // Сине-серый
    }
}

private fun mapOrderStatusIcon(payment: StartOrderPaymentStatus): ImageVector {
    return when (payment) {
        is StartOrderPaymentStatus.Success -> Icons.Default.CheckCircle
        is StartOrderPaymentStatus.Free -> Icons.Default.CardGiftcard
        is StartOrderPaymentStatus.OnPlace -> Icons.Default.LocationOn
        is StartOrderPaymentStatus.NotPaid -> Icons.Default.Schedule
        is StartOrderPaymentStatus.PaymentCancelled -> Icons.Default.Cancel
        is StartOrderPaymentStatus.WithoutStatus -> Icons.AutoMirrored.Filled.Help
    }
}
