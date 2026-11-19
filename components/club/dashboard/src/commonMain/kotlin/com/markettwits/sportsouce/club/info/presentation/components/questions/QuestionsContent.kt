package com.markettwits.sportsouce.club.info.presentation.components.questions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.SauceClickTarget
import com.markettwits.core_ui.items.components.navigation.SauceLazyColumn
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.club.common.presentation.ClubNotice
import com.markettwits.sportsouce.club.info.domain.models.Question

@Composable
internal fun QuestionsContent(modifier: Modifier = Modifier, questions: List<Question>) {
    var expanded by rememberSaveable { mutableStateOf(emptyList<Int>()) }
    SauceLazyColumn(
        modifier.fillMaxWidth(),
        contentPadding = PaddingValues(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        if (questions.isEmpty()) {
            item { ClubNotice("Вопросов пока нет", "Информацию можно уточнить при записи на тренировку.") }
        }
        itemsIndexed(questions, key = { _, question -> question.id }) { index, question ->
            val open = question.id in expanded
            val top = if (index == 0) 22.dp else 8.dp
            val bottom = if (index == questions.lastIndex) 22.dp else 8.dp
            val shape = RoundedCornerShape(topStart = top, topEnd = top, bottomStart = bottom, bottomEnd = bottom)
            ClubQuestion(question, shape, open) {
                expanded = if (open) expanded - question.id else expanded + question.id
            }
        }
    }
}

@Composable
private fun ClubQuestion(question: Question, shape: RoundedCornerShape, open: Boolean, onClick: () -> Unit) {
    val colors = SauceTheme.colors
    val angle by animateFloatAsState(if (open) 180f else 0f, label = "Question arrow")
    val headerShape = if (open) shape.copy(bottomStart = CornerSize(0.dp), bottomEnd = CornerSize(0.dp)) else shape
    Column(Modifier.fillMaxWidth().background(colors.surface, shape).border(1.dp, colors.border, shape)) {
        SauceClickTarget(
            onClick = onClick,
            shape = headerShape,
            modifier = Modifier.fillMaxWidth()
                .background(if (open) colors.actionSoft else colors.surfaceSubtle, headerShape)
                .semantics { stateDescription = if (open) "Развёрнуто" else "Свёрнуто" },
            onClickLabel = if (open) "Свернуть ответ" else "Показать ответ",
        ) {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(question.question, Modifier.weight(1f), style = MaterialTheme.typography.titleSmall, color = colors.textPrimary)
                Box(Modifier.size(30.dp).background(colors.surface, SauceShapes.badge), contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.ExpandMore,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp).rotate(angle),
                        tint = if (open) colors.action else colors.textSecondary,
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = open,
            enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
            exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut(),
        ) {
            Column {
                HorizontalDivider(Modifier.padding(horizontal = 16.dp), color = colors.border)
                HtmlText(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                    text = question.answer,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.textSecondary,
                    showImages = false,
                )
            }
        }
    }
}
