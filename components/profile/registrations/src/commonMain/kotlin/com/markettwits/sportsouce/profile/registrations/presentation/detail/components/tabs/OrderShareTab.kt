package com.markettwits.sportsouce.profile.registrations.presentation.detail.components.tabs

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.SubcomposeAsyncImage
import com.markettwits.IntentAction
import com.markettwits.capturable.*
import com.markettwits.core_ui.items.components.buttons.BackFloatingActionButton
import com.markettwits.core_ui.items.image.DefaultImages
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.intent.composable.rememberIntentActionByPlatform
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo

@Composable
fun OrderShareTab(
    startOrderInfo: StartOrderInfo,
) {
    ShareOrderTab(startOrderInfo)
}

@Composable
fun ShareOrderTab(
    orderInfo: StartOrderInfo,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val intent = rememberIntentActionByPlatform()
    var showPreviewDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Выберите дизайн карточки",
            fontSize = 18.sp,
            fontFamily = FontNunito.bold(),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 500.dp),
            contentAlignment = Alignment.Center
        ) {
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 32.dp),
                pageSpacing = 16.dp,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val scale by animateFloatAsState(
                    targetValue = if (pagerState.currentPage == page) 1f else 0.85f,
                    animationSpec = tween(300)
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                ) {
                    when (page) {
                        0 -> ImageBackgroundCard(orderInfo, RoundedCornerShape(16.dp))
                        1 -> CleanBackgroundCard(orderInfo, RoundedCornerShape(16.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            repeat(2) { index ->
                val isSelected = index == pagerState.currentPage
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(
                            width = if (isSelected) 24.dp else 8.dp,
                            height = 8.dp
                        )
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            if (isSelected)
                                MaterialTheme.colorScheme.secondary
                            else
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        )
                        .animateContentSize()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                showPreviewDialog = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Поделиться",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = FontNunito.semiBoldBold()
                )
            )
        }
        Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
    }

    if (showPreviewDialog) {
        SharePreviewDialog(
            orderInfo = orderInfo,
            currentPage = pagerState.currentPage,
            onDismiss = { showPreviewDialog = false },
            intent = intent
        )
    }
}

@Composable
private fun ImageBackgroundCard(
    orderInfo: StartOrderInfo,
    shape: RoundedCornerShape = RoundedCornerShape(0.dp),
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            SubcomposeAsyncImage(
                model = orderInfo.image,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        SportSouceColor.SportSouceLighBlue,
                                        SportSouceColor.SportSouceBlue
                                    )
                                )
                            )
                    )
                }
            )

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

            Image(
                painter = DefaultImages.SportSauceLightLogo(),
                contentDescription = "SportSauce Logo",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .height(40.dp),
                contentScale = ContentScale.Fit
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Box(
                        modifier = Modifier
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
}

@Composable
private fun CleanBackgroundCard(
    orderInfo: StartOrderInfo,
    shape: RoundedCornerShape = RoundedCornerShape(0.dp),
) {
    val hasResults = orderInfo.members.any { it.results.isNotEmpty() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 650.dp)
            .clip(shape)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.35f)
            ) {
                SubcomposeAsyncImage(
                    model = orderInfo.image,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    loading = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.secondary,
                                            MaterialTheme.colorScheme.tertiary
                                        )
                                    )
                                )
                        )
                    }
                )

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
                    .weight(0.65f)
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

                Spacer(modifier = Modifier.weight(1f))

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

@Composable
private fun SharePreviewDialog(
    orderInfo: StartOrderInfo,
    currentPage: Int,
    onDismiss: () -> Unit,
    intent: IntentAction,
) {
    val captureController = rememberCaptureController()
    var capturedBytes by remember { mutableStateOf<ByteArray?>(null) }
    val filename = remember { "${orderInfo.startTitle}-${orderInfo.dateStartPreview}" }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Capturable(
                modifier = Modifier.fillMaxSize(),
                captureController = captureController,
                onCaptured = { bitmap ->
                    capturedBytes = bitmap.toByteArray(CompressionFormat.PNG, QualityFormat.MAX.value)
                }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    when (currentPage) {
                        0 -> ImageBackgroundCard(orderInfo, RoundedCornerShape(0.dp))
                        1 -> CleanBackgroundCard(orderInfo, RoundedCornerShape(0.dp))
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .align(Alignment.TopStart)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.5f),
                                Color.Transparent
                            )
                        )
                    )
            )

            BackFloatingActionButton(
                modifier = Modifier.align(Alignment.TopStart),
                back = onDismiss
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.6f)
                            )
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        captureController.capture()
                        capturedBytes?.let { bytes ->
                            intent.shareImage(bytes, filename)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Поделиться",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = FontNunito.semiBoldBold(),
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    )
                }

                OutlinedButton(
                    onClick = {
                        captureController.capture()
                        capturedBytes?.let { bytes ->
                            intent.saveImage(bytes, filename)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(3.dp, MaterialTheme.colorScheme.secondary)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Скачать",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = FontNunito.semiBoldBold(),
                            color = MaterialTheme.colorScheme.secondary
                        )
                    )
                }
            }
        }
    }
}