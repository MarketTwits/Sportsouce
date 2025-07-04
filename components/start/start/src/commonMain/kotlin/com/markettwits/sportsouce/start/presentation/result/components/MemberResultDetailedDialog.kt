package com.markettwits.sportsouce.start.presentation.result.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.sportsouce.start.presentation.result.model.MemberResult
import kotlin.math.roundToInt

@Composable
fun MemberResultDetailedDialog(
    memberResult: MemberResult,
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {

    DetailedResultContent(
        memberResult = memberResult,
        onBackClick = onBackClick,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
private fun DetailedResultContent(
    memberResult: MemberResult,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    
    var isVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        isVisible = true
    }
    
    val animationSpec = tween<Float>(800, easing = EaseOutCubic)

    val slideDistance = with(LocalDensity.current) { 50.dp.toPx() }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .clip(Shapes.large)
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top App Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.tertiary,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .statusBarsPadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onTertiary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Результаты",
                    fontSize = 18.sp,
                    fontFamily = FontNunito.bold(),
                    color = MaterialTheme.colorScheme.onTertiary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Card with main result
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInVertically(
                        initialOffsetY = { -slideDistance.toInt() }
                    ) + fadeIn(animationSpec)
                ) {
                    HeaderResultCard(memberResult = memberResult)
                }
            }
            
            // Performance metrics
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInVertically(
                        animationSpec = tween(800, delayMillis = 200, easing = EaseOutCubic),
                        initialOffsetY = { slideDistance.toInt() }
                    ) + fadeIn(tween(800, delayMillis = 200))
                ) {
                    PerformanceMetricsCard(memberResult = memberResult)
                }
            }
            
            // Checkpoint details
            if (memberResult.circles.isNotEmpty()) {
                item {
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = slideInVertically(
                            animationSpec = tween(800, delayMillis = 400, easing = EaseOutCubic),
                            initialOffsetY = { slideDistance.toInt() }
                        ) + fadeIn(tween(800, delayMillis = 400))
                    ) {
                        CheckpointDetailsCard(memberResult = memberResult)
                    }
                }
            }
            
            // Additional info
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInVertically(
                        animationSpec = tween(800, delayMillis = 600, easing = EaseOutCubic),
                        initialOffsetY = { slideDistance.toInt() }
                    ) + fadeIn(tween(800, delayMillis = 600))
                ) {
                    AdditionalInfoCard(memberResult = memberResult)
                }
            }
        }
    }
}

@Composable
private fun HeaderResultCard(
    memberResult: MemberResult,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box {
            // Gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                                Color.Transparent
                            ),
                            radius = 400f
                        )
                    )
            )
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Place badge
                PlaceBadge(place = memberResult.place)
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Athlete name
                Text(
                    text = memberResult.name,
                    fontSize = 18.sp,
                    fontFamily = FontNunito.bold(),
                    color = MaterialTheme.colorScheme.onTertiary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Result time
                Text(
                    text = memberResult.result,
                    fontSize = 18.sp,
                    fontFamily = FontNunito.bold(),
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.ExtraBold
                )
                
                // Body number
                Text(
                    text = "#${memberResult.bodyNumber}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun PlaceBadge(place: Int) {
    val badgeColor = when (place) {
        1 -> Color(0xFFFFD700) // Gold
        2 -> Color(0xFFC0C0C0) // Silver  
        3 -> Color(0xFFCD7F32) // Bronze
        else -> MaterialTheme.colorScheme.secondary
    }
    
    val icon = when (place) {
        1 -> Icons.Default.Star
        2 -> Icons.Default.Star
        3 -> Icons.Default.Star
        else -> Icons.Default.EmojiEvents
    }
    
    Surface(
        modifier = Modifier.size(64.dp),
        shape = CircleShape,
        color = badgeColor,
        shadowElevation = 6.dp
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            if (place <= 3) {
                Icon(
                    imageVector = icon,
                    contentDescription = "Place $place",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            } else {
                Text(
                    text = "$place",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun PerformanceMetricsCard(
    memberResult: MemberResult,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Показатели",
                fontSize = 16.sp,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricItem(
                        icon = Icons.Default.Speed,
                        label = "Темп",
                        value = calculatePace(memberResult.result, memberResult.distance)
                    )
                    MetricItem(
                        icon = Icons.Default.DirectionsRun,
                        label = "Дистанция",
                        value = memberResult.distance
                    )
                    MetricItem(
                        icon = Icons.Default.Group,
                        label = "Категория",
                        value = memberResult.group
                    )
                }
            }
        }
}

@Composable
private fun MetricItem(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(40.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column {
            Text(
                text = label,
                fontSize = 16.sp,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
            )
            Text(
                text = value,
                fontSize = 14.sp,
                fontFamily = FontNunito.medium(),
                color = MaterialTheme.colorScheme.onTertiaryContainer,
            )
        }
    }
}

@Composable
private fun CheckpointDetailsCard(
    memberResult: MemberResult,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Контрольные точки",
                fontSize = 16.sp,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.onPrimary,
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(memberResult.circles.toList()) { (checkpoint, time) ->
                    CheckpointChip(
                        checkpoint = checkpoint,
                        time = time
                    )
                }
            }
        }
    }
}

@Composable
private fun CheckpointChip(
    checkpoint: Int,
    time: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "КП $checkpoint",
                fontSize = 16.sp,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.secondary,
            )
            Text(
                text = time,
                fontSize = 14.sp,
                fontFamily = FontNunito.medium(),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun AdditionalInfoCard(
    memberResult: MemberResult,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Дополнительная информация",
                fontSize = 16.sp,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.onPrimary,
            )
            
            Spacer(modifier = Modifier.height(16.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    InfoRow("Команда", memberResult.team)
                    InfoRow("Пол", memberResult.sex)
                    InfoRow("Смена", memberResult.shift)
                    InfoRow("Стартовый №", memberResult.startId.toString())
                    InfoRow("ID участника", memberResult.id.toString())
                }
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = FontNunito.bold(),
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            fontFamily = FontNunito.medium(),
            fontWeight = FontWeight.Medium
        )
    }
}

// Helper function to calculate pace
private fun calculatePace(result: String, distance: String): String {
    try {
        val timeParts = result.split(":")
        val totalSeconds = when (timeParts.size) {
            2 -> timeParts[0].toInt() * 60 + timeParts[1].toInt()
            3 -> timeParts[0].toInt() * 3600 + timeParts[1].toInt() * 60 + timeParts[2].toInt()
            else -> return "Н/Д"
        }
        
        val distanceKm = distance.replace("км", "").replace(",", ".").toFloatOrNull() ?: return "Н/Д"
        val paceSeconds = (totalSeconds / distanceKm).roundToInt()
        val paceMinutes = paceSeconds / 60
        val paceSecondsRemainder = paceSeconds % 60
        return ""
       // return String.format("%d:%02d /км", paceMinutes, paceSecondsRemainder)
    } catch (e: Exception) {
        return "Н/Д"
    }
}