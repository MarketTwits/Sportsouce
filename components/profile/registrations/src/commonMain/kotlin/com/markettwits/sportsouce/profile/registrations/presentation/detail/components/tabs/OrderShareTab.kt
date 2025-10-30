package com.markettwits.sportsouce.profile.registrations.presentation.detail.components.tabs

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.markettwits.capturable.Capturable
import com.markettwits.capturable.CompressionFormat
import com.markettwits.capturable.rememberCaptureController
import com.markettwits.capturable.toByteArray
import com.markettwits.core_ui.items.image.DefaultImages
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.intent.composable.rememberIntentActionByPlatform
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo

@Composable
fun OrderShareTab(
    startOrderInfo: StartOrderInfo
) {
    ShareOrderTab(startOrderInfo)
}

@Composable
fun ShareOrderTab(
    orderInfo: StartOrderInfo,
    modifier: Modifier = Modifier
) {
    var selectedCardIndex by remember { mutableStateOf(0) }
    val pagerState = rememberPagerState(pageCount = { 5 })
    val capture = rememberCaptureController()
    val intent = rememberIntentActionByPlatform()

    LaunchedEffect(pagerState.currentPage) {
        selectedCardIndex = pagerState.currentPage
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Выберите дизайн карточки",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontFamily = FontNunito.bold()
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Создайте красивую карточку для публикации",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = FontNunito.regular()
            ),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 32.dp),
            pageSpacing = 16.dp,
            modifier = Modifier.weight(1f)
        ) { page ->
            val scale by animateFloatAsState(
                targetValue = if (pagerState.currentPage == page) 1f else 0.85f,
                animationSpec = tween(300)
            )
            Capturable(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Transparent)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    },
                captureController = capture,
                onCaptured = {
                    intent.shareImage(it.toByteArray(CompressionFormat.PNG, 100))
                }
            ) {

                when (page) {
                    0 -> ImageBackgroundCardVariant(orderInfo)
                    1 -> GradientCardVariant(orderInfo)
                    2 -> ModernVibrantCardVariant(orderInfo)
                    3 -> ResultsCompactCardVariant(orderInfo)
                    4 -> ResultsCardVariant(orderInfo)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Индикаторы страниц
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            repeat(4) { index ->
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
            onClick = { capture.capture() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Поделиться",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = FontNunito.semiBoldBold()
                )
            )
        }
    }
}

@Composable
private fun ImageBackgroundCardVariant(orderInfo: StartOrderInfo) {
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Фоновое изображение
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

            // Градиентный оверлей для читаемости
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

            // Контент
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Верхняя часть
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
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = orderInfo.dateStartPreview,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontFamily = FontNunito.bold(),
                                    color = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = orderInfo.name,
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontFamily = FontNunito.black(),
                            color = Color.White,
                            lineHeight = 36.sp,
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

                // Нижняя часть с участниками
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
                        .padding(20.dp)
                ) {
                    Text(
                        text = "УЧАСТНИКИ",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontFamily = FontNunito.extraBold(),
                            color = Color.White.copy(alpha = 0.9f),
                            letterSpacing = 1.5.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    orderInfo.members.take(3).forEachIndexed { index, member ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
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
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "${member.name} ${member.surname}",
                                    style = MaterialTheme.typography.bodyLarge.copy(
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
                            Spacer(modifier = Modifier.height(14.dp))
                        }
                    }

                    if (orderInfo.members.size > 3) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "+${orderInfo.members.size - 3} участников",
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
private fun GradientCardVariant(orderInfo: StartOrderInfo) {
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF1E88E5),
                            Color(0xFF6A1B9A),
                            Color(0xFFD81B60)
                        ),
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
        ) {
            // Декоративные элементы
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = Color.White.copy(alpha = 0.1f),
                    radius = size.width * 0.5f,
                    center = Offset(size.width * 1.1f, -size.height * 0.2f)
                )
                drawCircle(
                    color = Color.White.copy(alpha = 0.08f),
                    radius = size.width * 0.4f,
                    center = Offset(-size.width * 0.2f, size.height * 1.1f)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = orderInfo.name,
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontFamily = FontNunito.black(),
                            color = Color.White,
                            lineHeight = 36.sp
                        ),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Нижняя секция с участниками
                    Column {
                        Divider(
                            color = Color.White.copy(alpha = 0.3f),
                            thickness = 2.dp,
                            modifier = Modifier.fillMaxWidth(0.3f)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        orderInfo.members.take(3).forEach { member ->
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
                                            .size(12.dp)
                                            .background(
                                                SportSouceColor.SportSouceLighBlue,
                                                CircleShape
                                            )
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "${member.name} ${member.surname}",
                                            style = MaterialTheme.typography.bodyLarge.copy(
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
                                                color = Color.White.copy(alpha = 0.7f)
                                            )
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .border(
                                            2.dp,
                                            Color.White.copy(alpha = 0.3f),
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = SportSouceColor.SportSouceLighBlue,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        if (orderInfo.members.size > 3) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .background(
                                            Color.White.copy(alpha = 0.5f),
                                            CircleShape
                                        )
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Ещё ${orderInfo.members.size - 3} участников",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontFamily = FontNunito.semiBoldBold(),
                                        color = Color.White.copy(alpha = 0.8f)
                                    ),
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = null,
                                        tint = Color.White.copy(alpha = 0.9f),
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = orderInfo.dateStartPreview,
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontFamily = FontNunito.bold(),
                                            color = Color.White.copy(alpha = 0.95f)
                                        )
                                    )
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Color.White,
                                        RoundedCornerShape(20.dp)
                                    )
                                    .padding(20.dp)
                            ) {
                                Text(
                                    text = "УЧАСТНИКИ",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontFamily = FontNunito.extraBold(),
                                        color = Color(0xFF6A1B9A),
                                        letterSpacing = 1.5.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                orderInfo.members.take(3).forEachIndexed { index, member ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(44.dp)
                                                .background(
                                                    when (index) {
                                                        0 -> Brush.linearGradient(
                                                            colors = listOf(Color(0xFF1E88E5), Color(0xFF42A5F5))
                                                        )

                                                        1 -> Brush.linearGradient(
                                                            colors = listOf(Color(0xFF6A1B9A), Color(0xFF9C27B0))
                                                        )

                                                        else -> Brush.linearGradient(
                                                            colors = listOf(Color(0xFFD81B60), Color(0xFFF06292))
                                                        )
                                                    },
                                                    CircleShape
                                                ),
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
                                        Spacer(modifier = Modifier.width(14.dp))
                                        Column {
                                            Text(
                                                text = "${member.name} ${member.surname}",
                                                style = MaterialTheme.typography.bodyLarge.copy(
                                                    fontFamily = FontNunito.bold(),
                                                    color = Color.Black.copy(alpha = 0.9f)
                                                ),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = member.ageGroupName,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontFamily = FontNunito.medium(),
                                                    color = Color.Black.copy(alpha = 0.6f)
                                                )
                                            )
                                        }
                                    }
                                    if (index < minOf(2, orderInfo.members.size - 1)) {
                                        Spacer(modifier = Modifier.height(14.dp))
                                    }
                                }

                                if (orderInfo.members.size > 3) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "+${orderInfo.members.size - 3} участников",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontFamily = FontNunito.bold(),
                                            color = Color(0xFF6A1B9A)
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun ModernVibrantCardVariant(orderInfo: StartOrderInfo) {
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            SportSouceColor.SportSouceRegistryCommingSoonYellow,
                            Color(0xFFFF6F00),
                            SportSouceColor.SportSouceStartEndedPink
                        ),
                        center = Offset(100f, 100f),
                        radius = 1200f
                    )
                )
        ) {
            // Декоративная графика
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Большие круги
                drawCircle(
                    color = Color.White.copy(alpha = 0.15f),
                    radius = size.width * 0.4f,
                    center = Offset(size.width * 0.9f, size.height * 0.2f)
                )
                drawCircle(
                    color = Color.White.copy(alpha = 0.1f),
                    radius = size.width * 0.35f,
                    center = Offset(size.width * 0.1f, size.height * 0.8f)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                // Верхний бейдж
                Row(
                    modifier = Modifier
                        .background(
                            Color.Black.copy(alpha = 0.3f),
                            RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 18.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "СТАРТ",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = FontNunito.extraBold(),
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = orderInfo.name,
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontFamily = FontNunito.black(),
                        color = Color.White,
                        lineHeight = 36.sp,
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.3f),
                            offset = Offset(2f, 2f),
                            blurRadius = 6f
                        )
                    ),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(
                            Color.Black.copy(alpha = 0.25f),
                            RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = orderInfo.dateStartPreview,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = FontNunito.bold(),
                            color = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Белый блок с участниками
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color.White,
                            RoundedCornerShape(20.dp)
                        )
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "КОМАНДА",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = FontNunito.extraBold(),
                                color = Color(0xFFFF6F00),
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text = "${orderInfo.members.size} чел.",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = FontNunito.bold(),
                                color = Color.Black.copy(alpha = 0.5f)
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    orderInfo.members.take(3).forEachIndexed { index, member ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(
                                        when (index) {
                                            0 -> Brush.linearGradient(
                                                colors = listOf(
                                                    SportSouceColor.SportSouceRegistryCommingSoonYellow,
                                                    Color(0xFFFF6F00)
                                                )
                                            )

                                            1 -> Brush.linearGradient(
                                                colors = listOf(
                                                    Color(0xFFFF6F00),
                                                    SportSouceColor.SportSouceStartEndedPink
                                                )
                                            )

                                            else -> Brush.linearGradient(
                                                colors = listOf(
                                                    SportSouceColor.SportSouceStartEndedPink,
                                                    Color(0xFFD81B60)
                                                )
                                            )
                                        },
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${member.name.firstOrNull()}${member.surname.firstOrNull()}",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontFamily = FontNunito.extraBold(),
                                        color = Color.White
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = "${member.name} ${member.surname}",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontFamily = FontNunito.bold(),
                                        color = Color.Black.copy(alpha = 0.9f)
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = member.ageGroupName,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontFamily = FontNunito.medium(),
                                        color = Color.Black.copy(alpha = 0.6f)
                                    )
                                )
                            }
                        }
                        if (index < minOf(2, orderInfo.members.size - 1)) {
                            Spacer(modifier = Modifier.height(14.dp))
                        }
                    }

                    if (orderInfo.members.size > 3) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(color = Color.Black.copy(alpha = 0.1f), thickness = 1.dp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "И ещё ${orderInfo.members.size - 3} участников",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = FontNunito.semiBoldBold(),
                                color = Color(0xFFFF6F00)
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ResultsCardVariant(orderInfo: StartOrderInfo) {
    // Проверяем, есть ли результаты хотя бы у одного участника
    val hasResults = orderInfo.members.any { it.results.isNotEmpty() }

    if (!hasResults) return

    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF00C853),
                            Color(0xFF00897B),
                            Color(0xFF00695C)
                        )
                    )
                )
        ) {
            // Декоративные элементы - трофеи
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Полупрозрачные круги
                drawCircle(
                    color = Color.White.copy(alpha = 0.08f),
                    radius = size.width * 0.4f,
                    center = Offset(size.width * 0.85f, size.height * 0.15f)
                )
                drawCircle(
                    color = Color.White.copy(alpha = 0.06f),
                    radius = size.width * 0.35f,
                    center = Offset(size.width * 0.15f, size.height * 0.9f)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Верхняя секция
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = orderInfo.dateStartPreview,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontFamily = FontNunito.bold(),
                                        color = Color.White
                                    )
                                )
                            }
                        }

                        // Иконка трофея
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(
                                    Color.White.copy(alpha = 0.2f),
                                    CircleShape
                                )
                                .border(
                                    2.dp,
                                    Color.White.copy(alpha = 0.3f),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFD700), // Золотой цвет
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = orderInfo.name,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontFamily = FontNunito.black(),
                            color = Color.White,
                            lineHeight = 32.sp,
                            shadow = Shadow(
                                color = Color.Black.copy(alpha = 0.3f),
                                offset = Offset(2f, 2f),
                                blurRadius = 6f
                            )
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "РЕЗУЛЬТАТЫ",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = FontNunito.extraBold(),
                            color = Color(0xFFFFD700),
                            letterSpacing = 2.sp
                        )
                    )
                }

                // Нижняя секция - результаты участников
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color.White,
                            RoundedCornerShape(20.dp)
                        )
                        .padding(20.dp)
                ) {
                    orderInfo.members.filter { it.results.isNotEmpty() }.take(3).forEachIndexed { index, member ->
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
                                    // Место
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .background(
                                                when (result.place) {
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
                                                        colors = listOf(Color(0xFF00C853), Color(0xFF00897B))
                                                    )
                                                },
                                                CircleShape
                                            )
                                            .border(
                                                2.dp,
                                                Color.Black.copy(alpha = 0.1f),
                                                CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${result.place}",
                                            style = MaterialTheme.typography.headlineSmall.copy(
                                                fontFamily = FontNunito.black(),
                                                color = Color.White,
                                                shadow = Shadow(
                                                    color = Color.Black.copy(alpha = 0.3f),
                                                    offset = Offset(1f, 1f),
                                                    blurRadius = 2f
                                                )
                                            )
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(14.dp))

                                    Column {
                                        Text(
                                            text = "${member.name} ${member.surname}",
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                fontFamily = FontNunito.bold(),
                                                color = Color.Black.copy(alpha = 0.9f)
                                            ),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = member.ageGroupName,
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    fontFamily = FontNunito.medium(),
                                                    color = Color.Black.copy(alpha = 0.5f)
                                                )
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Box(
                                                modifier = Modifier
                                                    .size(4.dp)
                                                    .background(
                                                        Color.Black.copy(alpha = 0.3f),
                                                        CircleShape
                                                    )
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = result.distance,
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    fontFamily = FontNunito.medium(),
                                                    color = Color.Black.copy(alpha = 0.5f)
                                                )
                                            )
                                        }
                                    }
                                }

                                // Время
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = result.result,
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontFamily = FontNunito.black(),
                                            color = Color(0xFF00C853)
                                        )
                                    )
                                    if (result.bodyNumber.isNotEmpty()) {
                                        Text(
                                            text = "№${result.bodyNumber}",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontFamily = FontNunito.semiBoldBold(),
                                                color = Color.Black.copy(alpha = 0.4f)
                                            )
                                        )
                                    }
                                }
                            }

                            if (index < minOf(2, orderInfo.members.filter { it.results.isNotEmpty() }.size - 1)) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Divider(
                                    color = Color.Black.copy(alpha = 0.08f),
                                    thickness = 1.dp,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }
                    }

                    val membersWithResults = orderInfo.members.filter { it.results.isNotEmpty() }
                    if (membersWithResults.size > 3) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Color(0xFF00C853).copy(alpha = 0.1f),
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "И ещё ${membersWithResults.size - 3} результатов",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontFamily = FontNunito.bold(),
                                    color = Color(0xFF00C853)
                                ),
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ResultsCompactCardVariant(orderInfo: StartOrderInfo) {
    val hasResults = orderInfo.members.any { it.results.isNotEmpty() }

    if (!hasResults) return

    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Фоновое изображение
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
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFFFF6B6B),
                                        Color(0xFFEE5A6F),
                                        Color(0xFFC44569)
                                    )
                                )
                            )
                    )
                }
            )

            // Градиентный оверлей
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFFF6B6B).copy(alpha = 0.85f),
                                Color(0xFFEE5A6F).copy(alpha = 0.75f),
                                Color(0xFFC44569).copy(alpha = 0.9f)
                            )
                        )
                    )
            )

            // Декор
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = Color.White.copy(alpha = 0.1f),
                    radius = size.width * 0.45f,
                    center = Offset(size.width * 1.1f, size.height * 0.3f)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                // Логотип сверху по центру
                Image(
                    painter = DefaultImages.SportSauceLightLogo(),
                    contentDescription = "SportSauce Logo",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .height(48.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Заголовок
                Column {
                    Text(
                        text = orderInfo.name,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontFamily = FontNunito.black(),
                            color = Color.White,
                            lineHeight = 28.sp
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = orderInfo.dateStartPreview,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontFamily = FontNunito.medium(),
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Статистика
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val membersWithResults = orderInfo.members.filter { it.results.isNotEmpty() }

                    StatBadge(
                        icon = Icons.Default.Person,
                        value = "${membersWithResults.size}",
                        label = "Участников",
                        modifier = Modifier.weight(1f)
                    )

                    val topPlace = membersWithResults.mapNotNull {
                        it.results.firstOrNull()?.place
                    }.minOrNull() ?: 0

                    if (topPlace > 0) {
                        StatBadge(
                            icon = Icons.Default.Star,
                            value = "$topPlace",
                            label = "Место",
                            modifier = Modifier.weight(1f),
                            highlighted = topPlace <= 3
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Топ результат
                val topMember = orderInfo.members
                    .filter { it.results.isNotEmpty() }
                    .minByOrNull { it.results.first().place }

                if (topMember != null) {
                    val topResult = topMember.results.first()

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color.White,
                                RoundedCornerShape(20.dp)
                            )
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "ЛУЧШИЙ РЕЗУЛЬТАТ",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontFamily = FontNunito.extraBold(),
                                    color = Color(0xFFC44569),
                                    letterSpacing = 1.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

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
                                        .size(56.dp)
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
                                                    colors = listOf(Color(0xFFFF6B6B), Color(0xFFC44569))
                                                )
                                            },
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${topResult.place}",
                                        style = MaterialTheme.typography.headlineMedium.copy(
                                            fontFamily = FontNunito.black(),
                                            color = Color.White
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column {
                                    Text(
                                        text = "${topMember.name} ${topMember.surname}",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontFamily = FontNunito.bold(),
                                            color = Color.Black.copy(alpha = 0.9f)
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "${topMember.ageGroupName} • ${topResult.distance}",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontFamily = FontNunito.medium(),
                                            color = Color.Black.copy(alpha = 0.6f)
                                        )
                                    )
                                }
                            }

                            Text(
                                text = topResult.result,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontFamily = FontNunito.black(),
                                    color = Color(0xFFFF6B6B)
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun StatBadge(
    icon: ImageVector,
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    highlighted: Boolean = false
) {
    Column(
        modifier = modifier
            .background(
                if (highlighted)
                    Color(0xFFFFD700).copy(alpha = 0.3f)
                else
                    Color.White.copy(alpha = 0.2f),
                RoundedCornerShape(16.dp)
            )
            .border(
                1.dp,
                if (highlighted)
                    Color(0xFFFFD700).copy(alpha = 0.5f)
                else
                    Color.White.copy(alpha = 0.3f),
                RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (highlighted) Color(0xFFFFD700) else Color.White,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontFamily = FontNunito.black(),
                color = Color.White
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                fontFamily = FontNunito.medium(),
                color = Color.White.copy(alpha = 0.8f)
            )
        )
    }
}