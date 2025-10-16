package com.markettwits.sportsouce.club.info.presentation.components.questions

import androidx.compose.runtime.Immutable

@Immutable
internal data class QuestionUi(
    val isSelected: Boolean,
    val answer: String,
    val id: Int,
    val question: String,
) {
    fun onChecked() = this.copy(isSelected = !this.isSelected)
}