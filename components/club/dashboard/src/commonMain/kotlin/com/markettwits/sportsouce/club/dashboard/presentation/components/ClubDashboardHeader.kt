package com.markettwits.sportsouce.club.dashboard.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.MotionDurationScale
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.club.common.presentation.ClubMetricsRow
import com.markettwits.sportsouce.club.info.domain.models.Statistic
import org.jetbrains.compose.resources.painterResource
import sportsouce.components.club.dashboard.generated.resources.Res
import sportsouce.components.club.dashboard.generated.resources.im_club_main_image

@Composable
internal fun ClubDashboardHeader(
    modifier: Modifier = Modifier,
    imageUrl: String,
    statistics: List<Statistic>,
    topPadding: Dp,
    scrollFraction: () -> Float,
    onSchedule: () -> Unit,
    onStatistics: () -> Unit,
) {
    var introPlayed by rememberSaveable { mutableStateOf(false) }
    val photoReveal = remember { Animatable(if (introPlayed) 1f else 0f) }
    var motionEnabled by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        val motionScale = coroutineContext[MotionDurationScale]
        snapshotFlow { (motionScale?.scaleFactor ?: 1f) > 0f }.collect { motionEnabled = it }
    }
    LaunchedEffect(photoReveal) {
        if (!introPlayed) {
            introPlayed = true
            photoReveal.animateTo(1f, tween(850))
        }
    }
    val gradientPhase = if (motionEnabled) {
        rememberInfiniteTransition(label = "Club cover gradient").animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(7600, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse,
            ),
            label = "Gradient drift",
        )
    } else {
        rememberUpdatedState(0f)
    }
    val description: @Composable () -> Unit = { ClubIntroduction(onSchedule) }
    val photo: @Composable (Modifier) -> Unit = { modifier ->
        ClubCoverPhoto(imageUrl, modifier.aspectRatio(1.18f)) {
            val reveal = if (motionEnabled) photoReveal.value else 1f
            val scroll = if (motionEnabled) scrollFraction().coerceIn(0f, 1f) else 0f
            val zoom = 1f + .065f * (1f - reveal) + .075f * scroll
            scaleX = zoom
            scaleY = zoom
            translationY = size.height * .03f * scroll
            alpha = .88f + .12f * reveal
        }
    }
    Column(
        modifier.fillMaxWidth().clubHeroBackground { gradientPhase.value }
            .padding(start = 20.dp, end = 20.dp, top = topPadding, bottom = 4.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        BoxWithConstraints(Modifier.fillMaxWidth()) {
            if (maxWidth >= 620.dp) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(28.dp)) {
                    photo(Modifier.weight(1.1f))
                    Column(Modifier.weight(1f)) { description() }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    photo(Modifier.fillMaxWidth())
                    description()
                }
            }
        }
        if (statistics.isNotEmpty()) ClubStatisticsStrip(statistics.take(2), onStatistics)
    }
}

@Composable
private fun ClubCoverPhoto(
    imageUrl: String,
    modifier: Modifier,
    motion: GraphicsLayerScope.() -> Unit,
) {
    val shape = SauceShapes.card
    val cropAlignment = BiasAlignment(horizontalBias = 0f, verticalBias = -.55f)
    Box(modifier.clip(shape).background(SauceTheme.colors.surfaceSubtle)) {
        Box(Modifier.matchParentSize().graphicsLayer(motion)) {
            Image(
                painter = painterResource(Res.drawable.im_club_main_image),
                contentDescription = "Тренировки и команда Спорт Союз",
                modifier = Modifier.fillMaxSize(),
                alignment = cropAlignment,
                contentScale = ContentScale.Crop,
            )
            if (imageUrl.isNotBlank()) {
                SubcomposeAsyncImage(
                    model = imageRequestCrossfade(imageUrl),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    alignment = cropAlignment,
                    contentScale = ContentScale.Crop,
                    loading = {},
                    error = {},
                    success = { SubcomposeAsyncImageContent() },
                )
            }
        }
    }
}

@Composable
private fun ClubIntroduction(onSchedule: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            Icon(Icons.Outlined.Place, null, Modifier.size(16.dp), tint = SauceTheme.colors.action)
            Text("Новосибирск", style = MaterialTheme.typography.labelLarge, color = SauceTheme.colors.action)
        }
        Text(
            "Клуб «Спорт Союз»",
            Modifier.semantics { heading() },
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = SauceTheme.colors.textPrimary,
        )
        Text(
            "Бег, плавание и лыжи. Тренируйтесь в группе и готовьтесь к стартам вместе.",
            style = MaterialTheme.typography.bodyLarge,
            color = SauceTheme.colors.textSecondary,
        )
        SauceButton("Расписание тренировок", onSchedule, modifier = Modifier.fillMaxWidth().padding(top = 6.dp))
    }
}

@Composable
private fun ClubStatisticsStrip(statistics: List<Statistic>, onClick: () -> Unit) {
    val colors = SauceTheme.colors
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = SauceShapes.card,
        color = colors.surface.copy(alpha = .82f),
        border = BorderStroke(1.dp, colors.border),
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    "Клуб в цифрах",
                    Modifier.weight(1f).semantics { heading() },
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.textPrimary,
                )
                Icon(Icons.AutoMirrored.Filled.ArrowForward, null, Modifier.size(20.dp), tint = colors.action)
            }
            BoxWithConstraints(Modifier.fillMaxWidth()) {
                val columns = if (maxWidth >= 220.dp * LocalDensity.current.fontScale) 2 else 1
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    statistics.chunked(columns).forEach { metrics ->
                        ClubMetricsRow(metrics, compact = true)
                    }
                }
            }
        }
    }
}
