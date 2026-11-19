package com.markettwits.sportsouce.club.info.presentation.components.trainings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.navigation.SauceLazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.club.common.presentation.*
import com.markettwits.sportsouce.club.info.domain.models.Training
import com.markettwits.core_ui.items.screens.FullImageScreen

@Composable
internal fun TrainingsContent(modifier: Modifier = Modifier, trainings: List<Training>, selectedTrainingId: Int? = null,
    onRegisterClick: (Training) -> Unit = {}) {
    var image by rememberSaveable { mutableStateOf("") }
    val listState = key(selectedTrainingId) {
        rememberLazyListState(initialFirstVisibleItemIndex = trainings.indexOfFirst { it.id == selectedTrainingId }.coerceAtLeast(0))
    }
    SauceLazyColumn(modifier.fillMaxSize(), state = listState, contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        if (trainings.isEmpty()) item { ClubNotice("Тренировок пока нет", "Расписание направлений появится здесь позже.") }
        items(trainings, key = { it.id }) { training ->
            val accent = clubDirectionAccent(training.type)
            OnBackgroundCard {
                ClubAccentStripe(accent)
                Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    if (training.imageUrl.isNotBlank()) ClubPhoto(training.imageUrl, training.type,
                        Modifier.fillMaxWidth().aspectRatio(16f / 9f), open = { image = training.imageUrl })
                    Text(training.type, style = MaterialTheme.typography.titleMedium, color = accent.clubColors().content)
                    if (training.htmlDescription.isNotBlank()) HtmlText(text = training.htmlDescription,
                        style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary, showImages = false)
                    SauceButton("Записаться", { onRegisterClick(training) }, modifier = Modifier.fillMaxWidth(), variant = SauceButtonVariant.Outlined)
                }
            }
        }
    }
    if (image.isNotBlank()) FullImageScreen(image) { image = "" }
}
