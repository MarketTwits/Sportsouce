package com.markettwits.sportsouce.club.info.presentation.components.comands

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
import com.markettwits.sportsouce.club.info.domain.models.Trainer
import com.markettwits.core_ui.items.screens.FullImageScreen

@Composable
internal fun CommandContent(modifier: Modifier = Modifier, trainers: List<Trainer>, selectedTrainerId: Int? = null,
    onRegisterClick: (Trainer) -> Unit = {}) {
    var image by rememberSaveable { mutableStateOf("") }
    val listState = key(selectedTrainerId) {
        rememberLazyListState(initialFirstVisibleItemIndex = trainers.indexOfFirst { it.id == selectedTrainerId }.coerceAtLeast(0))
    }
    SauceLazyColumn(modifier.fillMaxSize(), state = listState, contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        if (trainers.isEmpty()) item { ClubNotice("Тренеры пока не добавлены", "Информация появится здесь позже.") }
        items(trainers, key = { it.id }) { trainer ->
            OnBackgroundCard {
                Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    if (trainer.imageUrl.isNotBlank()) ClubPhoto(trainer.imageUrl, trainer.fullName(),
                        Modifier.fillMaxWidth().aspectRatio(1f), open = { image = trainer.imageUrl })
                    Text(trainer.fullName(), style = MaterialTheme.typography.titleMedium, color = SauceTheme.colors.textPrimary)
                    if (trainer.kindOfSports.isNotEmpty()) Text(trainer.kindOfSports.joinToString(" · "),
                        style = MaterialTheme.typography.bodySmall, color = clubDirectionAccent(trainer.kindOfSports.first()).clubColors().content)
                    if (trainer.description.isNotBlank()) HtmlText(text = trainer.description, style = MaterialTheme.typography.bodyMedium,
                        color = SauceTheme.colors.textSecondary, showImages = false)
                    if (trainer.isAcceptingRequests) SauceButton("Записаться", { onRegisterClick(trainer) }, variant = SauceButtonVariant.Outlined,
                        modifier = Modifier.fillMaxWidth())
                    else Text("Набор временно закрыт", style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textMuted)
                }
            }
        }
    }
    if (image.isNotBlank()) FullImageScreen(image) { image = "" }
}
