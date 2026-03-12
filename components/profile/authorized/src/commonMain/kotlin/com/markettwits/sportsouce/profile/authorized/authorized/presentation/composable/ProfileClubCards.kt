package com.markettwits.sportsouce.profile.authorized.authorized.presentation.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.LocalDarkOrLightTheme
import com.markettwits.core_ui.items.theme.SportSouceColor
import kotlinx.coroutines.delay

@Composable
internal fun ProfileClubCards(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val cards = remember {
        listOf(
            ClubCardInfo(
                title = "Клубные тренировки",
                subtitle = "Зал, новые направления и быстрый доступ к расписанию",
                badge = "Тренировки",
                icon = Icons.Default.FitnessCenter,
                secondaryIcon = Icons.AutoMirrored.Filled.DirectionsRun,
                primaryFeature = "Зал",
                secondaryFeature = "Расписание",
                accent = SportSouceColor.SportSouceRegistryOpenGreen,
                secondaryAccent = SportSouceColor.SportSouceLighBlue
            ),
            ClubCardInfo(
                title = "Команда на старте",
                subtitle = "Клубные чаты, совместные выезды и беговое комьюнити",
                badge = "Команда",
                icon = Icons.Default.Groups,
                secondaryIcon = Icons.Default.EmojiEvents,
                primaryFeature = "Комьюнити",
                secondaryFeature = "Выезды",
                accent = SportSouceColor.SportSouceLightRed,
                secondaryAccent = SportSouceColor.SportSouceRegistryCommingSoonYellow
            ),
            ClubCardInfo(
                title = "Бонусы и привилегии",
                subtitle = "Скидки партнёров, ранний доступ и клубные предложения",
                badge = "Бонусы",
                icon = Icons.Default.EmojiEvents,
                secondaryIcon = Icons.Default.FitnessCenter,
                primaryFeature = "Скидки",
                secondaryFeature = "Партнёры",
                accent = SportSouceColor.SportSouceLighBlue,
                secondaryAccent = SportSouceColor.SportSouceRegistryOpenGreen
            ),
            ClubCardInfo(
                title = "Клубный сезон",
                subtitle = "Челленджи, цели команды и новые спортивные привычки",
                badge = "Сезон",
                icon = Icons.AutoMirrored.Filled.DirectionsRun,
                secondaryIcon = Icons.Default.EmojiEvents,
                primaryFeature = "Челлендж",
                secondaryFeature = "Рейтинг",
                accent = SportSouceColor.SportSouceRegistryCommingSoonYellow,
                secondaryAccent = SportSouceColor.SportSouceStartEndedPink
            ),
        )
    }

    val pagerState = rememberPagerState(pageCount = { cards.size })

    LaunchedEffect(pagerState.pageCount) {
        while (true) {
            delay(10_000)
            if (pagerState.pageCount > 1) {
                val nextPage = (pagerState.currentPage + 1) % pagerState.pageCount
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp, pressedElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 14.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Клуб",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = FontNunito.bold(),
                    fontSize = 18.sp
                )
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.08f)
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        text = "SportSouce Club",
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.74f),
                        fontFamily = FontNunito.medium(),
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            HorizontalPager(
                modifier = Modifier.fillMaxWidth(),
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 8.dp),
                pageSpacing = 10.dp,
            ) { page ->
                ProfileClubCard(
                    modifier = Modifier.fillMaxWidth(),
                    info = cards[page],
                    onClick = onClick,
                )
            }

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(cards.size) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .height(4.dp)
                            .width(if (isSelected) 18.dp else 6.dp)
                            .clip(CircleShape)
                            .background(
                                MaterialTheme.colorScheme.onPrimary.copy(
                                    alpha = if (isSelected) 0.78f else 0.24f
                                )
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileClubCard(
    modifier: Modifier = Modifier,
    info: ClubCardInfo,
    onClick: () -> Unit,
) {
    val isDarkTheme = LocalDarkOrLightTheme.current
    val containerAlpha = if (isDarkTheme) 0.18f else 0.12f
    val borderAlpha = if (isDarkTheme) 0.34f else 0.22f

    Card(
        modifier = modifier
            .padding(vertical = 1.dp)
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = info.accent.copy(alpha = containerAlpha)),
        border = BorderStroke(1.dp, info.accent.copy(alpha = borderAlpha))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 188.dp)
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Surface(
                        shape = RoundedCornerShape(999.dp),
                        color = info.accent.copy(alpha = 0.18f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier.size(14.dp),
                                imageVector = info.secondaryIcon,
                                contentDescription = info.badge,
                                tint = info.accent
                            )
                            Text(
                                text = info.badge,
                                color = info.accent,
                                fontFamily = FontNunito.bold(),
                                fontSize = 11.sp
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = info.accent.copy(alpha = 0.18f),
                        border = BorderStroke(1.dp, info.accent.copy(alpha = 0.28f))
                    ) {
                        Box(
                            modifier = Modifier.size(44.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier.size(22.dp),
                                imageVector = info.icon,
                                contentDescription = info.title,
                                tint = info.accent
                            )
                        }
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = info.title,
                        fontSize = 19.sp,
                        fontFamily = FontNunito.bold(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        lineHeight = 21.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = info.subtitle,
                        fontSize = 13.sp,
                        lineHeight = 16.sp,
                        fontFamily = FontNunito.medium(),
                        color = info.accent,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Spacer(modifier = Modifier.weight(1f))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ClubFeaturePill(
                    icon = info.icon,
                    label = info.primaryFeature,
                    accent = info.accent
                )
                ClubFeaturePill(
                    icon = info.secondaryIcon,
                    label = info.secondaryFeature,
                    accent = info.secondaryAccent
                )
            }
        }
    }
}

@Composable
private fun ClubFeaturePill(
    icon: ImageVector,
    label: String,
    accent: Color,
) {
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = accent.copy(alpha = 0.16f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(14.dp),
                imageVector = icon,
                contentDescription = label,
                tint = accent
            )
            Text(
                text = label,
                color = accent,
                fontFamily = FontNunito.medium(),
                fontSize = 11.sp
            )
        }
    }
}

private data class ClubCardInfo(
    val title: String,
    val subtitle: String,
    val badge: String,
    val icon: ImageVector,
    val secondaryIcon: ImageVector,
    val primaryFeature: String,
    val secondaryFeature: String,
    val accent: Color,
    val secondaryAccent: Color,
)
