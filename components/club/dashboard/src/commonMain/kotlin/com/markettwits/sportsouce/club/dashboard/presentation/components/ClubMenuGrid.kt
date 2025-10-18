package com.markettwits.sportsouce.club.dashboard.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.club.dashboard.presentation.screen.MenuItemColor

@Composable
internal fun ClubMenuGrid(
    onPlanClick: () -> Unit,
    onTrainingsClick: () -> Unit,
    onClubBonusesClick: () -> Unit,
    onOurTeamClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            MenuGridItem(
                title = "План",
                description = "Изучите наш план тренировок",
                icon = Icons.AutoMirrored.Filled.EventNote,
                colors = MenuItemColor.PLAN.colors,
                onClick = onPlanClick
            )
            MenuGridItem(
                title = "Бонусы клуба",
                description = "Специальные предложения",
                icon = Icons.Default.Star,
                colors = MenuItemColor.CLUB_BONUSES.colors,
                onClick = onClubBonusesClick
            )
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            MenuGridItem(
                title = "Тренировки",
                description = "Мероприятия и тренировки",
                icon = Icons.Default.FitnessCenter,
                colors = MenuItemColor.TRAININGS.colors,
                onClick = onTrainingsClick
            )
            MenuGridItem(
                title = "Наша команда",
                description = "Познакомьтесь с тренерами",
                icon = Icons.Default.Group,
                colors = MenuItemColor.OUR_TEAM.colors,
                onClick = onOurTeamClick
            )
        }
    }
}

@Composable
internal fun MenuGridItem(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    colors: List<Color>,
    onClick: () -> Unit,
) {
    OnBackgroundCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.radialGradient(
                            colors = colors.map { it.copy(alpha = 0.15f) },
                            radius = 50f
                        ),
                        shape = androidx.compose.foundation.shape.CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = colors.first(),
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f, fill = false),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center,
                    fontFamily = FontNunito.bold(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.outline,
                    textAlign = TextAlign.Center,
                    lineHeight = 12.sp,
                    fontFamily = FontNunito.regular(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}