package com.markettwits.sportsouce.club.info.presentation.components.bottomsheet

import androidx.compose.runtime.Composable
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

@Composable
internal fun MenuBottomSheet(selectedTab: MenuBottomSheetType?, bottomSheetData: ClubDashboardStore.BottomSheetData,
    onClickSubscribe: () -> Unit, onTrainerRegister: (Trainer) -> Unit, onTrainingRegister: (Training) -> Unit) {
    when (selectedTab) {
        MenuBottomSheetType.Plan -> ClubPlanContent(onClickSubscribe, bottomSheetData.steps)
        MenuBottomSheetType.Trainings -> TrainingsContent(trainings = bottomSheetData.trainings, onRegisterClick = onTrainingRegister)
        is MenuBottomSheetType.TrainingDetails -> TrainingsContent(trainings = bottomSheetData.trainings,
            selectedTrainingId = selectedTab.id, onRegisterClick = onTrainingRegister)
        MenuBottomSheetType.ClubBonuses -> ClubFeaturesContent(features = bottomSheetData.features)
        MenuBottomSheetType.OurTeam -> CommandContent(trainers = bottomSheetData.trainers, onRegisterClick = onTrainerRegister)
        is MenuBottomSheetType.TrainerDetails -> CommandContent(trainers = bottomSheetData.trainers,
            selectedTrainerId = selectedTab.id, onRegisterClick = onTrainerRegister)
        MenuBottomSheetType.Faq -> QuestionsContent(questions = bottomSheetData.questions.map { QuestionUi(answer = it.answer, id = it.id, question = it.question, isSelected = false) })
        MenuBottomSheetType.Statistics -> StatisticContents(statistics = bottomSheetData.statistics)
        null -> Unit
    }
}

@Serializable
sealed interface MenuBottomSheetType {
    @Serializable
    data object Plan : MenuBottomSheetType

    @Serializable
    data object Trainings : MenuBottomSheetType

    @Serializable
    data class TrainingDetails(val id: Int) : MenuBottomSheetType

    @Serializable
    data object ClubBonuses : MenuBottomSheetType

    @Serializable
    data object OurTeam : MenuBottomSheetType

    @Serializable
    data class TrainerDetails(val id: Int) : MenuBottomSheetType

    @Serializable
    data object Faq : MenuBottomSheetType

    @Serializable
    data object Statistics : MenuBottomSheetType
}

internal val MenuBottomSheetType.title: String get() = when (this) {
    MenuBottomSheetType.Plan -> "Как начать"
    MenuBottomSheetType.Trainings, is MenuBottomSheetType.TrainingDetails -> "Направления тренировок"
    MenuBottomSheetType.ClubBonuses -> "Возможности клуба"
    MenuBottomSheetType.OurTeam, is MenuBottomSheetType.TrainerDetails -> "Наши тренеры"
    MenuBottomSheetType.Faq -> "Вопросы и ответы"
    MenuBottomSheetType.Statistics -> "Клуб в цифрах"
}
