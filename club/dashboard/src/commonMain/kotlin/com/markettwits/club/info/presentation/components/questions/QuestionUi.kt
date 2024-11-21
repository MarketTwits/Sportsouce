package com.markettwits.club.info.presentation.components.questions

import androidx.compose.runtime.Immutable
import com.markettwits.club.info.domain.models.Question

@Immutable
data class QuestionUi(
    val isSelected: Boolean,
    val answer: String,
    val id: Int,
    val question: String
) {
    fun onChecked() = this.copy(isSelected = !this.isSelected)
}

internal fun List<Question>.mapToQuestionUi(): List<QuestionUi> = map {
    QuestionUi(
        isSelected = false,
        answer = it.answer,
        id = it.id,
        question = it.question
    )
}