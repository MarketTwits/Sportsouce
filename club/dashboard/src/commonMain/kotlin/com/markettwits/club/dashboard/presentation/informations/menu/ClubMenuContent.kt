package com.markettwits.club.dashboard.presentation.informations.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.SportsRugby
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.OnBackgroundCard
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.theme.FontNunito


@Composable
fun ClubMenuContent(
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit
) {
    OnBackgroundCard(modifier = modifier.padding(10.dp)) {
        defaultClubsMenu().forEach {
            ClubMenuItem(
                modifier = Modifier
                    .padding(10.dp)
                    .clip(Shapes.medium)
                    .clickable { onClick(it.id) },
                icon = it.icon,
                title = it.title,
                description = it.description
            )
        }
    }
}

@Composable
fun ClubMenuItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    description: String,
) {
    Row(
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.fillMaxWidth().weight(0.9f)) {
            Icon(
                imageVector = icon,
                contentDescription = "Club menu",
                tint = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Column {
                Text(
                    textAlign = TextAlign.Start,
                    text = title,
                    fontSize = 14.sp,
                    fontFamily = FontNunito.semiBoldBold(),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    textAlign = TextAlign.Start,
                    text = description,
                    fontSize = 12.sp,
                    fontFamily = FontNunito.semiBoldBold(),
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
        Icon(
            modifier = Modifier
                .size(40.dp)
                .weight(0.1f),
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Click Right",
            tint = MaterialTheme.colorScheme.outline
        )
    }
}

@Immutable
data class ClubMenuUi(
    val id: Int,
    val icon: ImageVector,
    val title: String,
    val description: String
)

private fun defaultClubsMenu(): List<ClubMenuUi> = listOf(
    ClubMenuUi(
        0,
        Icons.Default.Groups,
        "Наша команда",
        "У нас работают профиссианальные тренеры, которые не оставят вас равнодушными",
    ),
    ClubMenuUi(
        1,
        Icons.Default.QuestionAnswer,
        "Часто задаваемые вопросы о школе",
        "Если не нашли здесь ответ на свой вопрос, напишите нам и мы ответим",
    ),
    ClubMenuUi(
        2,
        Icons.Default.SportsRugby,
        "Наш опыт и немного статистики",
        "Мы развиваемся, принимаем новые методики и анализируем прошлый опыт",
    ),
    ClubMenuUi(
        3,
        Icons.Default.SupportAgent,
        "Тренировки которые выбирают",
        "На любом этапе вы получаете полную поддержку",
    )
)