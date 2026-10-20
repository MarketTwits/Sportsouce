package com.markettwits.sportsouce.edit_profile.image.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.progress.CircularProgressIndicatorBase
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.sportsouce.edit_profile.image.presentation.store.EditProfileImageStore
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun EditProfileImageScreenContent(
    state: EditProfileImageStore.State,
    dismiss: () -> Unit,
    onClickImageBox: () -> Unit
) {
    OnBackgroundCard {
        Text(
            modifier = Modifier.padding(start = 20.dp, top = 20.dp),
            text = "Изменить фото профиля",
            style = MaterialTheme.typography.titleMedium,
            color = SauceTheme.colors.textPrimary
        )
        Column(modifier = Modifier.padding(20.dp)) {
            val stroke = Stroke(
                width = 4f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            )
            val color = SauceTheme.colors.controlBorder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 140.dp)
                    .drawBehind {
                        drawRoundRect(
                            color = color,
                            style = stroke,
                            cornerRadius = CornerRadius(10.dp.toPx())
                        )
                    }
                    .clip(SauceShapes.field)
                    .clickable(enabled = !state.isLoading, onClickLabel = "Выбрать фото профиля", onClick = onClickImageBox),
            ) {
                if (state.isLoading)
                    CircularProgressIndicatorBase(modifier = Modifier.align(Alignment.Center))
                else
                    InBoxImageContent()
            }
            if (state.message.isNotEmpty()) {
                Text(
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 10.dp, start = 10.dp)
                        .align(Alignment.Start),
                    text = state.message,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (state.isFailed) SauceTheme.colors.danger.content else SauceTheme.colors.textSecondary
                )
            } else {
                Spacer(modifier = Modifier.padding(20.dp))
            }
            SauceButton(
                title = "Отмена",
                variant = SauceButtonVariant.Outlined,
                onClick = { dismiss() }
            )
        }
    }
    LaunchedEffect(key1 = state) {
        if (state.isSuccess) {
            delay(800.milliseconds)
            dismiss()
        }
    }
}
