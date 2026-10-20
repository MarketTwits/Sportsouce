package com.markettwits.sportsouce.club.info.presentation.components.questions

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.navigation.SauceLazyColumn
import androidx.compose.foundation.lazy.items
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.club.common.presentation.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.ui.Alignment
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription

@Composable
internal fun QuestionsContent(modifier: Modifier = Modifier, questions: List<QuestionUi>) {
    var expanded by rememberSaveable { mutableStateOf(emptyList<Int>()) }
    SauceLazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        if (questions.isEmpty()) item { ClubNotice("Вопросов пока нет", "Информацию можно уточнить при записи на тренировку.") }
        items(questions, key = { it.id }) { question ->
            val open = question.id in expanded
            OnBackgroundCard(modifier = Modifier.semantics { stateDescription = if (open) "Развёрнуто" else "Свёрнуто" },
                onClick = { expanded = if (open) expanded - question.id else expanded + question.id }) {
                Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(question.question, Modifier.weight(1f), style = MaterialTheme.typography.titleSmall, color = SauceTheme.colors.textPrimary)
                        Icon(if (open) Icons.Default.ExpandLess else Icons.Default.ExpandMore, null, tint = SauceTheme.colors.textMuted)
                    }
                    if (open) HtmlText(text = question.answer, style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary, showImages = false)
                }
            }
        }
    }
}
