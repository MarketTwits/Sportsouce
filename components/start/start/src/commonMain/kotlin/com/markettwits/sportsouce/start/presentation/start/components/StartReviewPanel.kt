package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.progress.SauceLinearProgressIndicator
import com.markettwits.core_ui.items.theme.*
import com.markettwits.sportsouce.start.domain.StartItem

@Composable
fun StartReviewPanel(modifier: Modifier = Modifier, reviewState: StartItem.ReviewState) {
    if (reviewState == StartItem.ReviewState.NoReviews) return
    StartContentBasePanel(modifier, label = "Рейтинг") {
        when (reviewState) {
            StartItem.ReviewState.LowRating -> Column(Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
                Icon(Icons.Outlined.StarOutline, null, Modifier.size(32.dp), tint = SauceTheme.colors.textMuted)
                Text("Недостаточно оценок", Modifier.fillMaxWidth(), style = MaterialTheme.typography.titleSmall,
                    color = SauceTheme.colors.textPrimary, textAlign = TextAlign.Center)
                Text("Будьте первым, кто поделится впечатлением", Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textSecondary, textAlign = TextAlign.Center)
                RatingStars(0f)
            }
            is StartItem.ReviewState.Rated -> RatedReviewContent(reviewState.scores)
            StartItem.ReviewState.NoReviews -> Unit
        }
    }
}

@Composable
private fun RatedReviewContent(scores: StartItem.ReviewScores) {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val summary: @Composable (Modifier) -> Unit = { modifier ->
            Column(modifier, horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
                Text(scores.review.asRatingText(), Modifier.fillMaxWidth(), style = MaterialTheme.typography.headlineMedium,
                    color = SauceTheme.colors.textPrimary, textAlign = TextAlign.Center)
                Text("Средняя оценка", Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodySmall,
                    color = SauceTheme.colors.textMuted, textAlign = TextAlign.Center)
                RatingStars(scores.review)
            }
        }
        val metrics: @Composable (Modifier) -> Unit = { modifier ->
            Column(modifier, verticalArrangement = Arrangement.spacedBy(SauceSpacing.medium)) {
                ReviewMetricRow("Локация", scores.location)
                ReviewMetricRow("Трасса", scores.road)
                ReviewMetricRow("Организация", scores.team)
            }
        }
        if (maxWidth >= 420.dp * LocalDensity.current.fontScale) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(SauceSpacing.extraLarge)) {
                summary(Modifier.width(132.dp)); metrics(Modifier.weight(1f))
            }
        } else Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(SauceSpacing.large)) {
            summary(Modifier.fillMaxWidth()); metrics(Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun ReviewMetricRow(label: String, value: Int) {
    SauceLinearProgressIndicator(label = label, progress = value.coerceIn(0, 5) / 5f, valueLabel = "$value/5")
}

@Composable
private fun RatingStars(rating: Float) {
    val normalized = rating.takeIf { it.isFinite() }?.coerceIn(0f, 5f) ?: 0f
    val starSize = 18.dp
    Row(Modifier.semantics { contentDescription = "${rating.asRatingText()} из 5" },
        horizontalArrangement = Arrangement.spacedBy(SauceSpacing.extraSmall)) {
        repeat(5) { index ->
            val portion = (normalized - index).coerceIn(0f, 1f)
            Box(Modifier.size(starSize)) {
                Icon(Icons.Filled.Star, null, Modifier.matchParentSize(), tint = SauceTheme.colors.border)
                if (portion > 0f) Icon(Icons.Filled.Star, null,
                    Modifier.matchParentSize().drawWithContent { clipRect(right = size.width * portion) { this@drawWithContent.drawContent() } },
                    tint = SauceTheme.colors.action)
            }
        }
    }
}

private fun Float.asRatingText(): String {
    val normalized = takeIf { it.isFinite() }?.coerceIn(0f, 5f) ?: 0f
    val rounded = kotlin.math.round(normalized * 100) / 100
    return if (rounded % 1f == 0f) rounded.toInt().toString() else rounded.toString()
}
