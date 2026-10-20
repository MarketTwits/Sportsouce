package com.markettwits.sportsouce.club.dashboard.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.club.common.presentation.ClubPhoto
import com.markettwits.sportsouce.club.common.presentation.clubColors
import com.markettwits.sportsouce.club.common.presentation.clubDirectionAccent
import com.markettwits.sportsouce.club.info.domain.models.Trainer
import com.markettwits.sportsouce.club.info.domain.models.Training

@Composable
internal fun ClubTrainingPreview(training: Training, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val descriptionStyle = MaterialTheme.typography.bodySmall
    val descriptionHeight = with(LocalDensity.current) { descriptionStyle.lineHeight.toDp() * 3 }
    Surface(onClick = onClick, modifier = modifier.width(248.dp),
        color = SauceTheme.colors.surface, shape = SauceShapes.card,
        border = BorderStroke(1.dp, SauceTheme.colors.border)) {
        Column {
            ClubPhoto(training.imageUrl, training.type, Modifier.fillMaxWidth().aspectRatio(1.6f),
                shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp))
            Text(training.type, Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium,
                minLines = 2, maxLines = 2, overflow = TextOverflow.Ellipsis, color = SauceTheme.colors.textPrimary)
            // Reserve three scaled lines even for a short or absent description. LazyRow must
            // not change its height when a taller card leaves the viewport.
            HtmlText(text = training.htmlDescription,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp).heightIn(min = descriptionHeight),
                style = descriptionStyle, maxLines = 3, overflow = TextOverflow.Ellipsis,
                color = SauceTheme.colors.textSecondary, showImages = false)
        }
    }
}

@Composable
internal fun ClubTrainerPreview(trainer: Trainer, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(modifier.width(184.dp).clip(SauceShapes.card)
        .clickable(onClickLabel = "Подробнее о тренере", onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        ClubPhoto(trainer.imageUrl, trainer.fullName(), Modifier.fillMaxWidth().height(220.dp))
        Text(trainer.fullName(), style = MaterialTheme.typography.titleSmall, minLines = 2,
            maxLines = 2, overflow = TextOverflow.Ellipsis, color = SauceTheme.colors.textPrimary)
        Text(trainer.kindOfSports.joinToString(" · "), style = MaterialTheme.typography.bodySmall, minLines = 2,
            maxLines = 2, overflow = TextOverflow.Ellipsis,
            color = clubDirectionAccent(trainer.kindOfSports.firstOrNull().orEmpty()).clubColors().content)
    }
}
