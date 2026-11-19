package com.markettwits.sportsouce.club.dashboard.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.components.buttons.SauceClickTarget
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.club.common.presentation.ClubPhoto
import com.markettwits.sportsouce.club.common.presentation.clubColors
import com.markettwits.sportsouce.club.common.presentation.clubDirectionAccent
import com.markettwits.sportsouce.club.info.domain.models.Trainer
import com.markettwits.sportsouce.club.info.domain.models.Training

private val PreviewPhotoShape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp)

@Composable
internal fun ClubTrainingPreview(training: Training, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val colors = SauceTheme.colors
    val accent = clubDirectionAccent(training.type).clubColors()
    val titleStyle = MaterialTheme.typography.titleMedium
    val descriptionStyle = MaterialTheme.typography.bodySmall
    val captionHeight = with(LocalDensity.current) { (titleStyle.lineHeight.toDp() + descriptionStyle.lineHeight.toDp()) * 2 } + 6.dp
    ClubPreviewCard(modifier.width(248.dp), onClick, "Подробнее о направлении") {
        ClubPhoto(training.imageUrl, null, Modifier.fillMaxWidth().aspectRatio(1.62f), shape = PreviewPhotoShape)
        Column(
            Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp).heightIn(min = captionHeight),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    training.type, Modifier.weight(1f), style = titleStyle,
                    maxLines = 2, overflow = TextOverflow.Ellipsis, color = colors.textPrimary,
                )
                Box(Modifier.size(30.dp).background(accent.container, SauceShapes.badge), contentAlignment = Alignment.Center) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, Modifier.size(19.dp), tint = accent.content)
                }
            }
            HtmlText(
                text = training.htmlDescription,
                style = descriptionStyle, maxLines = 2, overflow = TextOverflow.Ellipsis,
                color = colors.textSecondary, showImages = false,
            )
        }
    }
}

@Composable
internal fun ClubTrainerPreview(trainer: Trainer, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val titleStyle = MaterialTheme.typography.titleSmall
    val sportsStyle = MaterialTheme.typography.bodySmall
    val captionHeight = with(LocalDensity.current) { (titleStyle.lineHeight.toDp() + sportsStyle.lineHeight.toDp()) * 2 } + 6.dp
    ClubPreviewCard(modifier.width(184.dp), onClick, "Подробнее о тренере") {
        ClubPhoto(trainer.imageUrl, null, Modifier.fillMaxWidth().height(200.dp), shape = PreviewPhotoShape)
        Column(
            Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 12.dp).heightIn(min = captionHeight),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(trainer.fullName(), style = titleStyle,
                maxLines = 2, overflow = TextOverflow.Ellipsis, color = SauceTheme.colors.textPrimary)
            Text(trainer.kindOfSports.joinToString(" · "), style = sportsStyle,
                maxLines = 2, overflow = TextOverflow.Ellipsis,
                color = clubDirectionAccent(trainer.kindOfSports.firstOrNull().orEmpty()).clubColors().content)
        }
    }
}

@Composable
private fun ClubPreviewCard(
    modifier: Modifier,
    onClick: () -> Unit,
    onClickLabel: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = SauceTheme.colors
    val shape = SauceShapes.card
    SauceClickTarget(onClick = onClick, modifier = modifier, shape = shape, onClickLabel = onClickLabel) {
        Column(
            Modifier.fillMaxWidth().background(colors.surface, shape).border(1.dp, colors.border, shape),
            content = content,
        )
    }
}
