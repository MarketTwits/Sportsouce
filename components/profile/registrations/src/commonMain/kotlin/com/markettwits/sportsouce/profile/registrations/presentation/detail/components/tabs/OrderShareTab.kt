package com.markettwits.sportsouce.profile.registrations.presentation.detail.components.tabs

import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll as verticalScroll

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.markettwits.IntentAction
import com.markettwits.capturable.*
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.selection.SauceRadioRow
import com.markettwits.core_ui.items.components.selection.SauceSegmentedTabs
import com.markettwits.core_ui.items.image.DefaultImages
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.SauceMotion
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceSizes
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.intent.composable.rememberIntentActionByPlatform
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo

private enum class ShareCardStyle(val title: String) {
    Poster("Постер"),
    Classic("Классика"),
    Ticket("Билет"),
}

private enum class ShareCardAction { Share, Save }

@Composable
fun OrderShareTab(
    startOrderInfo: StartOrderInfo,
    intent: IntentAction = rememberIntentActionByPlatform(),
) {
    val colors = SauceTheme.colors
    val captureController = rememberCaptureController()
    var styleIndex by rememberSaveable(startOrderInfo.id) { mutableIntStateOf(0) }
    var pendingAction by remember(startOrderInfo.id) { mutableStateOf<ShareCardAction?>(null) }
    var exportFailed by remember(startOrderInfo.id) { mutableStateOf(false) }
    val transition = updateTransition(ShareCardStyle.entries[styleIndex], label = "Registration card style")
    val canExport = pendingAction == null && transition.currentState == transition.targetState && !transition.isRunning
    val requestExport: (ShareCardAction) -> Unit = { action ->
        if (pendingAction == null && !transition.isRunning && transition.currentState.ordinal == styleIndex) {
            exportFailed = false
            pendingAction = action
            captureController.capture()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = SauceSpacing.large, vertical = SauceSpacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier.widthIn(max = 420.dp).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(SauceSpacing.medium),
        ) {
            Text(
                "Стиль карточки",
                modifier = Modifier.semantics { heading() },
                color = colors.textPrimary,
                style = MaterialTheme.typography.titleSmall,
            )
            ShareStyleSelector(styleIndex, enabled = pendingAction == null) { styleIndex = it }
            Capturable(
                modifier = Modifier.fillMaxWidth().clip(SauceShapes.card)
                    .background(colors.surface).border(1.dp, colors.border, SauceShapes.card),
                captureController = captureController,
                onCaptured = { bitmap ->
                    pendingAction?.let { action ->
                        val filename = "sportsauce-registration-${startOrderInfo.id}"
                        exportFailed = runCatching {
                            val bytes = bitmap.toByteArray(CompressionFormat.PNG, QualityFormat.MAX.value)
                            check(bytes.isNotEmpty())
                            when (action) {
                                ShareCardAction.Share -> intent.shareImage(bytes, filename)
                                ShareCardAction.Save -> intent.saveImage(bytes, filename)
                            }
                        }.isFailure
                        pendingAction = null
                    }
                },
            ) {
                transition.AnimatedContent(
                    transitionSpec = {
                        fadeIn(tween(SauceMotion.feedbackMillis)) togetherWith
                            fadeOut(tween(SauceMotion.feedbackMillis)) using
                            SizeTransform { _, _ -> tween(SauceMotion.feedbackMillis) }
                    },
                ) { style ->
                    when (style) {
                        ShareCardStyle.Poster -> ImageBackgroundCard(startOrderInfo)
                        ShareCardStyle.Classic -> CleanBackgroundCard(startOrderInfo)
                        ShareCardStyle.Ticket -> TicketShareCard(startOrderInfo)
                    }
                }
            }
            ShareCardActions(
                enabled = canExport,
                pendingAction = pendingAction,
                onExport = requestExport,
            )
            if (exportFailed) {
                Text(
                    "Не удалось подготовить карточку. Попробуйте ещё раз.",
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                    color = colors.danger.content,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Composable
private fun ShareStyleSelector(selectedIndex: Int, enabled: Boolean, onSelected: (Int) -> Unit) {
    val labels = ShareCardStyle.entries.map { it.title }
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        if (maxWidth >= 288.dp * LocalDensity.current.fontScale) {
            SauceSegmentedTabs(labels, selectedIndex, onSelected, enabled = enabled)
        } else {
            val colors = SauceTheme.colors
            Surface(shape = SauceShapes.field, color = colors.surface, border = BorderStroke(1.dp, colors.border)) {
                Column(Modifier.padding(SauceSpacing.extraSmall).selectableGroup()) {
                    labels.forEachIndexed { index, label ->
                        SauceRadioRow(label, selectedIndex == index, { onSelected(index) }, enabled = enabled)
                    }
                }
            }
        }
    }
}

@Composable
private fun ShareCardActions(
    enabled: Boolean,
    pendingAction: ShareCardAction?,
    onExport: (ShareCardAction) -> Unit,
) {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val stacked = maxWidth < 260.dp * LocalDensity.current.fontScale
        if (stacked) {
            Column(verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
                SauceButton("Поделиться карточкой", { onExport(ShareCardAction.Share) },
                    enabled = enabled, loading = pendingAction == ShareCardAction.Share)
                SauceButton("Скачать PNG", { onExport(ShareCardAction.Save) },
                    variant = SauceButtonVariant.Outlined, enabled = enabled, loading = pendingAction == ShareCardAction.Save)
            }
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
                SauceButton("Поделиться", { onExport(ShareCardAction.Share) }, modifier = Modifier.weight(1f),
                    enabled = enabled, loading = pendingAction == ShareCardAction.Share)
                SauceButton("Скачать PNG", { onExport(ShareCardAction.Save) }, modifier = Modifier.weight(1f),
                    variant = SauceButtonVariant.Outlined, enabled = enabled, loading = pendingAction == ShareCardAction.Save)
            }
        }
    }
}

@Composable
private fun ImageBackgroundCard(orderInfo: StartOrderInfo) {
    Box(Modifier.fillMaxWidth()) {
        Box(Modifier.matchParentSize()) {
            ShareCover(orderInfo.image, Modifier.fillMaxSize())
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.7f),
                                Color.Black.copy(alpha = 0.3f),
                                Color.Black.copy(alpha = 0.8f)
                            )
                        )
                    )
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 360.dp)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(SauceSpacing.extraLarge, Alignment.CenterVertically)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f, fill = false)
                            .background(
                                Color.White.copy(alpha = 0.25f),
                                RoundedCornerShape(12.dp)
                            )
                            .border(
                                1.dp,
                                Color.White.copy(alpha = 0.3f),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = orderInfo.dateStartPreview,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontFamily = FontNunito.bold(),
                                    color = Color.White
                                )
                            )
                        }
                    }
                    Image(
                        painter = DefaultImages.SportSauceLightLogo(),
                        contentDescription = null,
                        modifier = Modifier.size(width = 68.dp, height = 40.dp),
                        contentScale = ContentScale.Fit,
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = orderInfo.name,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = FontNunito.black(),
                        color = Color.White,
                        lineHeight = 28.sp,
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.5f),
                            offset = Offset(2f, 2f),
                            blurRadius = 8f
                        )
                    ),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            val hasResults = orderInfo.members.any { it.results.isNotEmpty() }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color.White.copy(alpha = 0.15f),
                        RoundedCornerShape(16.dp)
                    )
                    .border(
                        1.dp,
                        Color.White.copy(alpha = 0.3f),
                        RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = if (hasResults) "РЕЗУЛЬТАТЫ" else "УЧАСТНИКИ",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontFamily = FontNunito.extraBold(),
                        color = Color.White.copy(alpha = 0.9f),
                        letterSpacing = 1.sp
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                if (hasResults) {
                    orderInfo.members.filter { it.results.isNotEmpty() }.take(3)
                        .forEachIndexed { index, member ->
                            val result = member.results.firstOrNull()
                            if (result != null) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .background(
                                                    when (result.place) {
                                                        1 -> Brush.linearGradient(
                                                            colors = listOf(
                                                                Color(0xFFFFD700),
                                                                Color(0xFFFFA000)
                                                            )
                                                        )

                                                        2 -> Brush.linearGradient(
                                                            colors = listOf(
                                                                Color(0xFFC0C0C0),
                                                                Color(0xFF9E9E9E)
                                                            )
                                                        )

                                                        3 -> Brush.linearGradient(
                                                            colors = listOf(
                                                                Color(0xFFCD7F32),
                                                                Color(0xFF8D6E63)
                                                            )
                                                        )

                                                        else -> Brush.linearGradient(
                                                            colors = listOf(
                                                                SportSouceColor.SportSouceLighBlue,
                                                                SportSouceColor.SportSouceRegistryOpenGreen
                                                            )
                                                        )
                                                    },
                                                    CircleShape
                                                )
                                                .border(2.dp, Color.White.copy(alpha = 0.3f), CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "${result.place}",
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontFamily = FontNunito.extraBold(),
                                                    color = Color.White
                                                )
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "${member.name} ${member.surname}",
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontFamily = FontNunito.bold(),
                                                    color = Color.White
                                                ),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = member.ageGroupName,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontFamily = FontNunito.medium(),
                                                    color = Color.White.copy(alpha = 0.85f)
                                                )
                                            )
                                        }
                                    }

                                    Text(
                                        text = result.result,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontFamily = FontNunito.black(),
                                            color = Color.White
                                        )
                                    )
                                }
                                if (index < minOf(
                                        2,
                                        orderInfo.members.filter { it.results.isNotEmpty() }.size - 1
                                    )
                                ) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                }
                            }
                        }
                } else {
                    orderInfo.members.take(3).forEachIndexed { index, member ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(
                                        Brush.linearGradient(
                                            colors = listOf(
                                                SportSouceColor.SportSouceLighBlue,
                                                SportSouceColor.SportSouceRegistryOpenGreen
                                            )
                                        ),
                                        CircleShape
                                    )
                                    .border(2.dp, Color.White.copy(alpha = 0.3f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${member.name.firstOrNull()}${member.surname.firstOrNull()}",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontFamily = FontNunito.extraBold(),
                                        color = Color.White
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "${member.name} ${member.surname}",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontFamily = FontNunito.bold(),
                                        color = Color.White
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = member.ageGroupName,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontFamily = FontNunito.medium(),
                                        color = Color.White.copy(alpha = 0.85f)
                                    )
                                )
                            }
                        }
                        if (index < minOf(2, orderInfo.members.size - 1)) {
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }

                if (orderInfo.members.size > 3) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "+${orderInfo.members.size - 3} ${if (hasResults) "результатов" else "участников"}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = FontNunito.bold(),
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun CleanBackgroundCard(orderInfo: StartOrderInfo) {
    val hasResults = orderInfo.members.any { it.results.isNotEmpty() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2.5f)
        ) {
            ShareCover(orderInfo.image, Modifier.fillMaxSize())

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                            ),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
            )

            Image(
                painter = DefaultImages.SportSauceLightLogo(),
                contentDescription = "SportSauce Logo",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .height(48.dp),
                contentScale = ContentScale.Fit
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = orderInfo.dateStartPreview,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = FontNunito.bold(),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = orderInfo.name,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = FontNunito.black(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    lineHeight = 22.sp
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (hasResults) {
                val membersWithResults = orderInfo.members.filter { it.results.isNotEmpty() }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    InfoCard(
                        icon = Icons.Default.Person,
                        value = "${membersWithResults.size}",
                        label = "участников",
                        modifier = Modifier.weight(1f),
                        isCleanCard = true
                    )

                    val topPlace = membersWithResults.mapNotNull {
                        it.results.firstOrNull()?.place
                    }.minOrNull() ?: 0

                    if (topPlace > 0) {
                        InfoCard(
                            icon = Icons.Default.Star,
                            value = "$topPlace",
                            label = "место",
                            modifier = Modifier.weight(1f),
                            highlighted = topPlace <= 3,
                            isCleanCard = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                val topMember = membersWithResults.minByOrNull { it.results.first().place }
                if (topMember != null) {
                    val topResult = topMember.results.first()

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.tertiaryContainer,
                                RoundedCornerShape(10.dp)
                            )
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                                RoundedCornerShape(10.dp)
                            )
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "ЛУЧШИЙ РЕЗУЛЬТАТ",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontFamily = FontNunito.extraBold(),
                                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                                    letterSpacing = 0.3.sp,
                                    fontSize = 10.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .background(
                                            when (topResult.place) {
                                                1 -> Brush.linearGradient(
                                                    colors = listOf(Color(0xFFFFD700), Color(0xFFFFA000))
                                                )

                                                2 -> Brush.linearGradient(
                                                    colors = listOf(Color(0xFFC0C0C0), Color(0xFF9E9E9E))
                                                )

                                                3 -> Brush.linearGradient(
                                                    colors = listOf(Color(0xFFCD7F32), Color(0xFF8D6E63))
                                                )

                                                else -> Brush.linearGradient(
                                                    colors = listOf(
                                                        MaterialTheme.colorScheme.secondary,
                                                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)
                                                    )
                                                )
                                            },
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${topResult.place}",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontFamily = FontNunito.black(),
                                            color = Color.White
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.width(6.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${topMember.name} ${topMember.surname}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontFamily = FontNunito.bold(),
                                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                                            fontSize = 12.sp
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "${topMember.ageGroupName}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontFamily = FontNunito.medium(),
                                            color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f),
                                            fontSize = 10.sp
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            Text(
                                text = topResult.result,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontFamily = FontNunito.black(),
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            )
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.tertiaryContainer,
                            RoundedCornerShape(10.dp)
                        )
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                            RoundedCornerShape(10.dp)
                        )
                        .padding(10.dp)
                ) {
                    Text(
                        text = "УЧАСТНИКИ",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = FontNunito.extraBold(),
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            letterSpacing = 0.5.sp,
                            fontSize = 10.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    orderInfo.members.take(3).forEachIndexed { index, member ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .background(
                                        MaterialTheme.colorScheme.secondary,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${member.name.firstOrNull()}${member.surname.firstOrNull()}",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontFamily = FontNunito.extraBold(),
                                        color = Color.White,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "${member.name} ${member.surname}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontFamily = FontNunito.bold(),
                                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                                        fontSize = 12.sp
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = member.ageGroupName,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontFamily = FontNunito.medium(),
                                        color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f),
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }
                        if (index < minOf(2, orderInfo.members.size - 1)) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    if (orderInfo.members.size > 3) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "+${orderInfo.members.size - 3} участников",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontNunito.bold(),
                                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f),
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(SauceSpacing.medium))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.tertiaryContainer,
                        RoundedCornerShape(10.dp)
                    )
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                        RoundedCornerShape(10.dp)
                    )
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = orderInfo.dateStartPreview,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = FontNunito.bold(),
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun TicketShareCard(orderInfo: StartOrderInfo) {
    val colors = SauceTheme.colors
    val hasResults = orderInfo.members.any { it.results.isNotEmpty() }
    Column(Modifier.fillMaxWidth().background(colors.surface)) {
        Column(
            modifier = Modifier.fillMaxWidth().background(
                Brush.verticalGradient(listOf(colors.actionSoft, colors.surface)),
            ).padding(SauceSpacing.extraLarge),
            verticalArrangement = Arrangement.spacedBy(SauceSpacing.medium),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(SauceSpacing.small),
            ) {
                Icon(Icons.Default.ConfirmationNumber, null, Modifier.size(SauceSizes.icon), tint = colors.action)
                Text("Стартовый билет", style = MaterialTheme.typography.labelLarge, color = colors.onActionSoft)
            }
            Text(
                orderInfo.name.ifBlank { orderInfo.startTitle },
                style = MaterialTheme.typography.headlineSmall,
                color = colors.textPrimary,
            )
            if (orderInfo.dateStartPreview.isNotBlank()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(SauceSpacing.small),
                ) {
                    Icon(Icons.Default.DateRange, null, Modifier.size(18.dp), tint = colors.action)
                    Text(orderInfo.dateStartPreview, style = MaterialTheme.typography.bodyMedium, color = colors.textSecondary)
                }
            }
        }
        Canvas(Modifier.fillMaxWidth().height(SauceSpacing.medium)) {
            val center = size.height / 2
            val inset = SauceSpacing.extraLarge.toPx()
            drawLine(
                color = colors.border,
                start = Offset(inset, center),
                end = Offset(size.width - inset, center),
                strokeWidth = 1.dp.toPx(),
                cap = StrokeCap.Round,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(4.dp.toPx(), 4.dp.toPx())),
            )
            drawCircle(colors.background, radius = center, center = Offset(0f, center))
            drawCircle(colors.background, radius = center, center = Offset(size.width, center))
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(SauceSpacing.extraLarge),
            verticalArrangement = Arrangement.spacedBy(SauceSpacing.medium),
        ) {
            if (orderInfo.members.isNotEmpty()) {
                Text(
                    if (hasResults) "Участники и результаты" else "Участники",
                    style = MaterialTheme.typography.titleSmall,
                    color = colors.textPrimary,
                )
                orderInfo.members.take(3).forEach { member ->
                    Column(verticalArrangement = Arrangement.spacedBy(SauceSpacing.extraSmall)) {
                        Text("${member.name} ${member.surname}".trim(), style = MaterialTheme.typography.titleSmall,
                            color = colors.textPrimary)
                        val result = member.results.firstOrNull()
                        val details = listOf(member.distanceName, result?.result.orEmpty())
                            .filter(String::isNotBlank)
                            .joinToString(" · ")
                        if (details.isNotEmpty()) {
                            Text(details, style = MaterialTheme.typography.bodySmall, color = colors.textSecondary)
                        }
                    }
                }
                if (orderInfo.members.size > 3) {
                    Text("Ещё ${orderInfo.members.size - 3}", style = MaterialTheme.typography.labelMedium,
                        color = colors.textSecondary)
                }
            }
            Text("Спорт Союз", style = MaterialTheme.typography.labelLarge, color = colors.action)
        }
    }
}

@Composable
private fun ShareCover(url: String, modifier: Modifier) {
    val fallback = DefaultImages.EmptyImageStart()
    AsyncImage(
        model = imageRequestCrossfade(url.takeIf(String::isNotBlank)),
        contentDescription = null,
        modifier = modifier.background(SauceTheme.colors.actionSoft),
        contentScale = ContentScale.Crop,
        placeholder = fallback,
        fallback = fallback,
        error = fallback,
    )
}

@Composable
private fun InfoCard(
    icon: ImageVector,
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    highlighted: Boolean = false,
    isCleanCard: Boolean = false,
) {
    Column(
        modifier = modifier
            .background(
                if (highlighted)
                    Color(0xFFFFD700).copy(alpha = 0.15f)
                else if (isCleanCard)
                    MaterialTheme.colorScheme.tertiaryContainer
                else
                    MaterialTheme.colorScheme.secondaryContainer,
                RoundedCornerShape(12.dp)
            )
            .border(
                1.dp,
                if (highlighted)
                    Color(0xFFFFD700).copy(alpha = 0.3f)
                else
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (highlighted) Color(0xFFFFD700) else MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(
                fontFamily = FontNunito.black(),
                color = if (isCleanCard) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.onSecondaryContainer
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                fontFamily = FontNunito.medium(),
                color = if (isCleanCard)
                    MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                else
                    MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
            )
        )
    }
}
