package com.markettwits.sportsouce.club.info.presentation.components.bottomsheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.club.dashboard.presentation.store.ClubDashboardStore
import com.markettwits.sportsouce.club.info.domain.models.Trainer
import com.markettwits.sportsouce.club.info.domain.models.Training
import com.markettwits.sportsouce.club.info.presentation.components.comands.CommandContent
import com.markettwits.sportsouce.club.info.presentation.components.features.ClubFeaturesContent
import com.markettwits.sportsouce.club.info.presentation.components.plan.ClubPlanContent
import com.markettwits.sportsouce.club.info.presentation.components.questions.QuestionUi
import com.markettwits.sportsouce.club.info.presentation.components.questions.QuestionsContent
import com.markettwits.sportsouce.club.info.presentation.components.statistics.StatisticContents
import com.markettwits.sportsouce.club.info.presentation.components.trainings.TrainingsContent
import kotlinx.serialization.Serializable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MenuBottomSheet(
    selectedTab: MenuBottomSheetType?,
    bottomSheetData: ClubDashboardStore.BottomSheetData,
    onDismiss: () -> Unit,
    onClickSubscribe: () -> Unit,
    onMenuItemClick: (MenuBottomSheetItem) -> Unit,
    onTrainerRegister: (Trainer) -> Unit = {},
    onTrainingRegister: (Training) -> Unit = {},
) {
    when (selectedTab) {
        MenuBottomSheetType.Plan -> {
            InfoContent(
                title = "План как начать",
                onDismiss = onDismiss,
                content = {
                    ClubPlanContent(onClickSubscribe)
                }
            )
        }

        MenuBottomSheetType.Trainings -> {
            InfoContent(
                title = "Тренировки и мероприятия",
                onDismiss = onDismiss,
                content = {
                    TrainingsContent(
                        trainings = bottomSheetData.trainings,
                        onRegisterClick = onTrainingRegister
                    )
                })
        }

        MenuBottomSheetType.ClubBonuses -> {
            InfoContent(
                title = "Бонусы клуба",
                onDismiss = onDismiss,
                content = {
                    ClubFeaturesContent(
                        features = bottomSheetData.features,
                    )
                })
        }

        MenuBottomSheetType.OurTeam -> {
            InfoContent(
                title = "Наша команда",
                onDismiss = onDismiss,
                content = {
                    CommandContent(
                        trainers = bottomSheetData.trainers,
                        onRegisterClick = onTrainerRegister
                    )
                })
        }

        MenuBottomSheetType.Faq -> {
            InfoContent(
                title = "Вопросы и ответы",
                onDismiss = onDismiss,
                content = {
                    QuestionsContent(
                        questions = bottomSheetData.questions.map { question ->
                            QuestionUi(
                                id = question.id,
                                question = question.question,
                                answer = question.answer,
                                isSelected = false
                            )
                        }
                    )
                })
        }

        MenuBottomSheetType.Statistics -> {
            InfoContent(
                title = "Статистика и достижения",
                onDismiss = onDismiss,
                content = {
                    StatisticContents(
                        statistics = bottomSheetData.statistics,
                    )
                })
        }

        null -> {
            DefaultMenuContent(onMenuItemClick = onMenuItemClick)
        }
    }
}

@Composable
private fun MenuItemCard(
    item: MenuBottomSheetItem,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.outlineVariant
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Иконка
            Card(
                modifier = Modifier.size(40.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.icon,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = item.description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.outline,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

@Serializable
sealed interface MenuBottomSheetType {
    @Serializable
    data object Plan : MenuBottomSheetType

    @Serializable
    data object Trainings : MenuBottomSheetType

    @Serializable
    data object ClubBonuses : MenuBottomSheetType

    @Serializable
    data object OurTeam : MenuBottomSheetType

    @Serializable
    data object Faq : MenuBottomSheetType

    @Serializable
    data object Statistics : MenuBottomSheetType
}

@Composable
private fun DefaultMenuContent(
    onMenuItemClick: (MenuBottomSheetItem) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Еще",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MenuBottomSheetItem.values().forEach { item ->
                MenuItemCard(
                    item = item,
                    onClick = { onMenuItemClick(item) }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
internal fun InfoContent(
    modifier: Modifier = Modifier,
    title: String,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .padding(horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(0.9f),
                text = title,
                fontSize = 20.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.onPrimary
            )
            IconButton(
                modifier = Modifier.weight(0.1f),
                onClick = onDismiss
            ) {
                Text("✕", fontSize = 18.sp)
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        content()
    }
}

enum class MenuBottomSheetItem(
    val title: String,
    val description: String,
    val icon: String,
) {
    PLAN("План", "Тренировки и мероприятия", "📅"),
    EVENTS("Тренировки и мероприятия", "Расписание и события", "🏃‍♂️"),
    CLUB_BONUSES("Бонусы клуба", "Специальные предложения", "😊"),
    OUR_TEAM("Наша команда", "Информация о тренерах", "👥"),
    TRAINING_SCHEDULE("Расписание тренировок", "График занятий", "📆"),
    STATISTICS("Статистика", "Достижения и показатели клуба", "📊"),
    MORE("Еще", "Дополнительные возможности", "❓"),
    FAQ("Вопросы и ответы", "Часто задаваемые вопросы", "❓")
}