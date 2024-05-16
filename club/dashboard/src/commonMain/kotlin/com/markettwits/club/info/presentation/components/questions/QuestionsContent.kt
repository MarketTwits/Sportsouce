package com.markettwits.club.info.presentation.components.questions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.OnBackgroundCard
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.text.HtmlText

@Composable
fun QuestionsContent(
    modifier: Modifier = Modifier,
    questions: List<QuestionUi>
) {

    val questionsState = remember { questions.map { it.copy() }.toMutableStateList() }

    Column(modifier = modifier.padding(10.dp)) {
        Text(
            textAlign = TextAlign.Start,
            text = "Часто задаваемые вопросы",
            fontSize = 20.sp,
            fontFamily = FontNunito.bold(),
            color = MaterialTheme.colorScheme.onPrimary
        )
        Column {
            questionsState.forEachIndexed { index, item ->
                QuestionItemContent(
                    modifier = Modifier.padding(vertical = 10.dp),
                    question = item,
                    isSelected = item.isSelected,
                    onClick = {
                        questionsState[index] = item.onChecked()
                    }
                )
            }
        }
    }
}

@Composable
private fun QuestionItemContent(
    modifier: Modifier = Modifier,
    question: QuestionUi,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Column(modifier = modifier) {
        QuestionItemRow(isSelected = isSelected, question = question, onClick = onClick)
        AnimatedVisibility(isSelected) {
            AnswerItemRow(
                modifier = Modifier.offset(y = (-10).dp),
                answer = question.answer
            )
        }
    }
}

@Composable
private fun QuestionItemRow(
    modifier: Modifier = Modifier,
    question: QuestionUi,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val background =
        if (isSelected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
    val textColor =
        if (isSelected) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onPrimary
    OnBackgroundCard(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = background),
        onClick = { onClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(10.dp)
                    .weight(0.9f),
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                text = question.question,
                fontSize = 14.sp,
                fontFamily = FontNunito.semiBoldBold(),
                color = textColor
            )
            QuestionItemIcon(
                modifier = Modifier.weight(0.1f),
                isSelected = isSelected
            )
        }
    }
}

@Composable
private fun AnswerItemRow(modifier: Modifier = Modifier, answer: String) {
    OnBackgroundCard(
        modifier = modifier,
        shape = RoundedCornerShape(
            bottomStart = 10.dp,
            bottomEnd = 10.dp
        )
    ) {
        HtmlText(
            modifier = Modifier
                .padding(10.dp),
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis,
            text = answer,
            fontSize = 14.sp,
            fontFamily = FontNunito.semiBoldBold(),
            color = MaterialTheme.colorScheme.outline
        )
    }
}


@Composable
private fun QuestionItemIcon(
    modifier: Modifier = Modifier,
    isSelected: Boolean
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp))
            .background(MaterialTheme.colorScheme.secondary)
    ) {
        val icon = if (isSelected) "-" else "+"
        Text(
            modifier = Modifier
                .padding(4.dp)
                .align(Alignment.Center),
            textAlign = TextAlign.Center,
            text = icon,
            fontSize = 50.sp,
            fontFamily = FontNunito.bold(),
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}