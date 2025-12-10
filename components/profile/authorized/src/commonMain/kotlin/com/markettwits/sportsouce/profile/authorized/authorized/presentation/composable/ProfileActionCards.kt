package com.markettwits.sportsouce.profile.authorized.authorized.presentation.composable

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderPaymentStatus

private data class PaymentStatistics(
    val successCount: Int = 0,
    val freeCount: Int = 0,
    val onPlaceCount: Int = 0,
    val notPaidCount: Int = 0,
    val cancelledCount: Int = 0,
    val withoutStatusCount: Int = 0,
) {
    val total: Int
        get() = successCount + freeCount + onPlaceCount + notPaidCount + cancelledCount + withoutStatusCount
}

private data class PaymentSegment(
    val label: String,
    val count: Int,
    val color: Color,
)

@Composable
private fun PaymentStatistics.getSegments(): List<PaymentSegment> {
    val grayColor = MaterialTheme.colorScheme.outline
    return buildList {
        if (successCount > 0) add(PaymentSegment("Оплачено", successCount, SportSouceColor.SportSouceRegistryOpenGreen))
        if (freeCount > 0) add(PaymentSegment("Бесплатно", freeCount, SportSouceColor.SportSouceLighBlue))
        if (onPlaceCount > 0) add(
            PaymentSegment(
                "На месте",
                onPlaceCount,
                SportSouceColor.SportSouceRegistryCommingSoonYellow
            )
        )
        if (notPaidCount > 0) add(PaymentSegment("Не оплачено", notPaidCount, SportSouceColor.SportSouceLightRed))
        if (cancelledCount > 0) add(
            PaymentSegment(
                "Отменено",
                cancelledCount,
                SportSouceColor.SportSouceStartEndedPink
            )
        )
        if (withoutStatusCount > 0) add(PaymentSegment("Без статуса", withoutStatusCount, grayColor))
    }
}

@Composable
internal fun ProfileActionCards(
    modifier: Modifier = Modifier,
    startsCount: Int,
    registrations: List<com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo>,
    onClickStarts: () -> Unit,
    onClickOrders: () -> Unit,
    onClickFavorites: () -> Unit,
) {
    val startsDescription = if (startsCount != 0) "$startsCount стартов" else "Нет регистраций"

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ProfileActionCardLarge(
            modifier = Modifier.fillMaxWidth(),
            icon = Icons.Default.EventAvailable,
            title = "Мои старты",
            count = startsCount,
            description = startsDescription,
            registrations = registrations,
            accent = SportSouceColor.SportSouceRegistryOpenGreen,
            onClick = onClickStarts
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ProfileActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Favorite,
                title = "Избранное",
                description = "Сохраненные старты",
                accent = SportSouceColor.SportSouceStartEndedPink,
                onClick = onClickFavorites
            )

            ProfileActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Inventory2,
                title = "Мои заказы",
                description = "История и статусы",
                accent = SportSouceColor.SportSouceLighBlue,
                onClick = onClickOrders
            )
        }
    }
}

@Composable
private fun ProfileActionCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    description: String,
    accent: Color,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
        ),
        elevation = CardDefaults.cardElevation(2.dp, pressedElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                modifier = Modifier.size(22.dp),
                imageVector = icon,
                contentDescription = title,
                tint = accent
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontFamily = FontNunito.bold(),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = description,
                    fontSize = 12.sp,
                    fontFamily = FontNunito.regular(),
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Composable
private fun ProfileActionCardLarge(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    count: Int,
    description: String,
    registrations: List<com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo>,
    accent: Color,
    onClick: () -> Unit,
) {
    val statistics = remember(registrations) {
        PaymentStatistics(
            successCount = registrations.count { it.payment is StartOrderPaymentStatus.Success },
            freeCount = registrations.count { it.payment is StartOrderPaymentStatus.Free },
            onPlaceCount = registrations.count { it.payment is StartOrderPaymentStatus.OnPlace },
            notPaidCount = registrations.count { it.payment is StartOrderPaymentStatus.NotPaid },
            cancelledCount = registrations.count { it.payment is StartOrderPaymentStatus.PaymentCancelled },
            withoutStatusCount = registrations.count { it.payment is StartOrderPaymentStatus.WithoutStatus }
        )
    }

    Card(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.cardElevation(2.dp, pressedElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = icon,
                    contentDescription = title,
                    tint = accent
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (count != 0) {
                        Text(
                            text = "$count",
                            fontSize = 28.sp,
                            fontFamily = FontNunito.bold(),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontFamily = FontNunito.bold(),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = if (count != 0) "стартов" else description,
                        fontSize = 12.sp,
                        fontFamily = FontNunito.regular(),
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            if (count > 0) {
                val segments = statistics.getSegments()

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MiniDonutChart(
                        statistics = statistics,
                        segments = segments,
                        size = 60.dp
                    )

                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        segments.forEach { segment ->
                            LegendItem(
                                color = segment.color,
                                text = "${segment.label}: ${segment.count}",
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MiniDonutChart(
    statistics: PaymentStatistics,
    segments: List<PaymentSegment>,
    size: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier,
) {
    if (statistics.total == 0) return

    val animatedProgress = rememberSaveable { Animatable(0f) }

    LaunchedEffect(statistics.total) {
        animatedProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1200,
                delayMillis = 100
            )
        )
    }

    Canvas(
        modifier = modifier.size(size)
    ) {
        val strokeWidth = size.toPx() * 0.2f
        var currentStartAngle = -90f

        segments.forEach { segment ->
            val segmentAngle = 360f * segment.count / statistics.total
            val animatedSweepAngle = segmentAngle * animatedProgress.value

            drawArc(
                color = segment.color,
                startAngle = currentStartAngle,
                sweepAngle = animatedSweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            currentStartAngle += segmentAngle
        }
    }
}

@Composable
private fun LegendItem(
    color: Color,
    text: String,
    fontSize: androidx.compose.ui.unit.TextUnit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Canvas(modifier = Modifier.size(8.dp)) {
            drawCircle(color = color)
        }
        Text(
            text = text,
            fontSize = fontSize,
            fontFamily = FontNunito.regular(),
            color = MaterialTheme.colorScheme.outline
        )
    }
}

