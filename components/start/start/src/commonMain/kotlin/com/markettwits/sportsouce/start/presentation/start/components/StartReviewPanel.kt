package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.sportsouce.start.domain.StartItem

@Composable
fun StartReviewPanel(
    modifier: Modifier = Modifier,
    reviewState: StartItem.ReviewState,
) {
    when (reviewState) {
        StartItem.ReviewState.NoReviews -> return
        StartItem.ReviewState.LowRating -> {
            StartContentBasePanel(
                modifier = modifier,
                label = "Рейтинг"
            ) {
                UnRatedReviewContent()
            }
        }

        is StartItem.ReviewState.Rated -> {
            StartContentBasePanel(
                modifier = modifier,
                label = "Рейтинг"
            ) {
                RatedReviewContent(reviewState.scores)
            }
        }
    }
}

@Composable
private fun UnRatedReviewContent() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.outlineVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.StarOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(28.dp)
            )
        }

        Text(
            text = "Недостаточно оценок",
            color = MaterialTheme.colorScheme.onBackground,
            fontFamily = FontNunito.semiBoldBold(),
            fontSize = 16.sp
        )
        Text(
            text = "Будьте первым, кто поделится впечатлением",
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            fontFamily = FontNunito.regular(),
            fontSize = 12.sp
        )

        RatingStars(rating = 0f)
    }
}

@Composable
private fun RatedReviewContent(
    scores: StartItem.ReviewScores,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.width(120.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = scores.review.asRatingText(),
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = FontNunito.black(),
                fontSize = 36.sp
            )
            Text(
                text = "Средняя оценка",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                fontFamily = FontNunito.regular(),
                fontSize = 12.sp
            )
            RatingStars(rating = scores.review)
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ReviewMetricRow(
                label = "Локация",
                value = scores.location,
                color = MaterialTheme.colorScheme.secondary
            )
            ReviewMetricRow(
                label = "Трасса",
                value = scores.road,
                color = MaterialTheme.colorScheme.tertiary
            )
            ReviewMetricRow(
                label = "Организация",
                value = scores.team,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun ReviewMetricRow(
    label: String,
    value: Int,
    color: Color,
) {
    val progress = animateFloatAsState(
        targetValue = value.coerceIn(0, 5) / 5f,
        animationSpec = tween(durationMillis = 400),
        label = "review_progress_$label"
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = FontNunito.semiBoldBold(),
                fontSize = 14.sp
            )
            Text(
                text = "$value/5",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f),
                fontFamily = FontNunito.medium(),
                fontSize = 12.sp
            )
        }

        LinearProgressIndicator(
            progress = { progress.value },
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(Shapes.small),
            color = color,
            trackColor = MaterialTheme.colorScheme.outlineVariant
        )
    }
}

@Composable
private fun RatingStars(rating: Float) {
    val normalized = rating.coerceIn(0f, 5f)
    val starSize = 18.dp
    val activeColor = MaterialTheme.colorScheme.secondary
    val inactiveColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.15f)

    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        repeat(5) { index ->
            val filledPortion = (normalized - index).coerceIn(0f, 1f)

            Box(modifier = Modifier.size(starSize)) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = inactiveColor,
                    modifier = Modifier.matchParentSize()
                )
                if (filledPortion > 0f) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clipToBounds(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = activeColor,
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(starSize * filledPortion)
                        )
                    }
                }
            }
        }
    }
}

private fun Float.asRatingText(): String {
    val normalized = this.coerceIn(0f, 5f)
    val rounded = kotlin.math.round(normalized * 100) / 100
    return if (rounded % 1f == 0f) rounded.toInt().toString() else rounded.toString()
}
