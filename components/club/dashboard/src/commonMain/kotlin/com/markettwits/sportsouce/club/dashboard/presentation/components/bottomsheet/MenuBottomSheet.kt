package com.markettwits.sportsouce.club.dashboard.presentation.components.bottomsheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.club.dashboard.presentation.store.ClubDashboardStore
import com.markettwits.sportsouce.club.info.domain.models.Trainer
import com.markettwits.sportsouce.club.info.domain.models.Training
import com.markettwits.sportsouce.club.info.presentation.components.comands.CommandContent
import com.markettwits.sportsouce.club.info.presentation.components.features.ClubFeaturesContent
import com.markettwits.sportsouce.club.info.presentation.components.plan.PlanContent
import com.markettwits.sportsouce.club.info.presentation.components.questions.QuestionUi
import com.markettwits.sportsouce.club.info.presentation.components.questions.QuestionsContent
import com.markettwits.sportsouce.club.info.presentation.components.trainings.TrainingsContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuBottomSheet(
    isVisible: Boolean,
    selectedTab: MenuBottomSheetType?,
    bottomSheetData: ClubDashboardStore.BottomSheetData,
    onDismiss: () -> Unit,
    onClickSubscribe: () -> Unit,
    onMenuItemClick: (MenuBottomSheetItem) -> Unit,
) {
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    LaunchedEffect(isVisible) {
        if (isVisible) {
            bottomSheetState.show()
        } else {
            bottomSheetState.hide()
        }
    }

    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = bottomSheetState,
            containerColor = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            when (selectedTab) {
                MenuBottomSheetType.PLAN -> {
                    PlanContent(
                        onDismiss = onDismiss,
                        onClickSubscribe = onClickSubscribe
                    )
                }

                MenuBottomSheetType.TRAININGS -> {
                    TrainingsBottomSheetContent(
                        trainings = bottomSheetData.trainings,
                        onDismiss = onDismiss
                    )
                }

                MenuBottomSheetType.CLUB_BONUSES -> {
                    ClubBonusesContent(
                        features = bottomSheetData.features,
                        onDismiss = onDismiss
                    )
                }

                MenuBottomSheetType.OUR_TEAM -> {
                    TeamBottomSheetContent(
                        trainers = bottomSheetData.trainers,
                        onDismiss = onDismiss
                    )
                }

                MenuBottomSheetType.FAQ -> {
                    FAQBottomSheetContent(
                        questions = bottomSheetData.questions.map { question ->
                            QuestionUi(
                                id = question.id,
                                question = question.question,
                                answer = question.answer,
                                isSelected = false
                            )
                        },
                        onDismiss = onDismiss
                    )
                }

                null -> {
                    DefaultMenuContent(onMenuItemClick = onMenuItemClick, onDismiss = onDismiss)
                }
            }
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

            // Заголовок и описание
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

enum class MenuBottomSheetType {
    PLAN,
    TRAININGS,
    CLUB_BONUSES,
    OUR_TEAM,
    FAQ
}

@Composable
private fun DefaultMenuContent(
    onMenuItemClick: (MenuBottomSheetItem) -> Unit,
    onDismiss: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Заголовок
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
private fun TrainingsBottomSheetContent(
    trainings: List<Training>,
    onDismiss: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .padding(horizontal = 20.dp)
            .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Тренировки и мероприятия",
                fontSize = 20.sp,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.onPrimary
            )
            IconButton(onClick = onDismiss) {
                Text("✕", fontSize = 18.sp)
            }
        }

        TrainingsContent(
            trainings = trainings
        )
    }
}

@Composable
private fun ClubBonusesContent(
    features: List<com.markettwits.sportsouce.club.info.domain.models.ClubFeature>,
    onDismiss: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .padding(horizontal = 20.dp)
            .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Бонусы клуба",
                fontSize = 20.sp,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.onPrimary
            )
            IconButton(onClick = onDismiss) {
                Text("✕", fontSize = 18.sp)
            }
        }

        ClubFeaturesContent(
            features = features
        )
    }
}

@Composable
private fun TeamBottomSheetContent(
    trainers: List<Trainer>,
    onDismiss: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .padding(horizontal = 20.dp)
            .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Наша команда",
                fontSize = 20.sp,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.onPrimary
            )
            IconButton(onClick = onDismiss) {
                Text("✕", fontSize = 18.sp)
            }
        }

        CommandContent(
            trainers = trainers
        )
    }
}

@Composable
private fun FAQBottomSheetContent(
    questions: List<QuestionUi>,
    onDismiss: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .padding(horizontal = 20.dp)
            .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Вопросы и ответы",
                fontSize = 20.sp,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.onPrimary
            )
            IconButton(onClick = onDismiss) {
                Text("✕", fontSize = 18.sp)
            }
        }

        QuestionsContent(
            questions = questions
        )
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
    MORE("Еще", "Дополнительные возможности", "❓"),
    FAQ("Вопросы и ответы", "Часто задаваемые вопросы", "❓")
}