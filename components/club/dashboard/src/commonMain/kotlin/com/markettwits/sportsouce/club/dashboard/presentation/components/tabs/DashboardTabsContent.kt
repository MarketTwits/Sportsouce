package com.markettwits.sportsouce.club.dashboard.presentation.components.tabs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DashboardTabsContent(
    onSmallTabClick: (SmallTab) -> Unit,
    onLargeTabClick: (LargeTab) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Маленькие табы (по центру) - открывают BottomSheet
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SmallTab.values().forEach { tab ->
                SmallTabItem(
                    tab = tab,
                    onClick = { onSmallTabClick(tab) }
                )
            }
        }

        // Большие табы - открывают отдельные экраны
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            LargeTab.values().forEach { tab ->
                LargeTabItem(
                    tab = tab,
                    onClick = { onLargeTabClick(tab) }
                )
            }
        }
    }
}

@Composable
private fun SmallTabItem(
    tab: SmallTab,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .size(80.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = tab.icon,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = tab.title,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun LargeTabItem(
    tab: LargeTab,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Иконка
            Card(
                modifier = Modifier.size(40.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF42A5F5)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tab.icon,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Текст
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = tab.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                if (tab.subtitle.isNotEmpty()) {
                    Text(
                        text = tab.subtitle,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

enum class SmallTab(
    val title: String,
    val icon: String,
) {
    PLAN("План", "📅"),
    TRAININGS("Тренировки и мероприятия", "🏃‍♂️"),
    CLUB_BONUSES("Бонусы клуба", "😊"),
    OUR_TEAM("Наша команда", "👥")
}

enum class LargeTab(
    val title: String,
    val subtitle: String,
    val icon: String,
) {
    SUBSCRIPTIONS("Абонементы и цены", "Выберите подходящий тариф", "💳"),
    SCHEDULE("Расписание тренировок", "График занятий на неделю", "📆")
}