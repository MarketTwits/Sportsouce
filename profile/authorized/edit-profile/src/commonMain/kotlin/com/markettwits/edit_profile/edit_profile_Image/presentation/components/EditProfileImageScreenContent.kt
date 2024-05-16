package com.markettwits.edit_profile.edit_profile_Image.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.OnBackgroundCard
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.components.buttons.ButtonContentBase
import com.markettwits.core_ui.items.components.progress.CircularProgressIndicatorBase
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.edit_profile.edit_profile_Image.presentation.store.EditProfileImageStore
import kotlinx.coroutines.delay

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
            fontFamily = FontNunito.bold(),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.tertiary
        )
        Column(modifier = Modifier.padding(20.dp)) {
            val stroke = Stroke(
                width = 4f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            )
            val color = Color.LightGray
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .drawBehind {
                        drawRoundRect(
                            color = color,
                            style = stroke,
                            cornerRadius = CornerRadius(10.dp.toPx())
                        )
                    }
                    .clip(Shapes.medium)
                    .clickable {
                        onClickImageBox()
                    },
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
                    fontFamily = FontNunito.bold(),
                    fontSize = 12.sp,
                    color = if (state.isFailed) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.tertiary
                )
            } else {
                Spacer(modifier = Modifier.padding(20.dp))
            }
            ButtonContentBase(
                modifier = Modifier.height(35.dp),
                title = "Отмена",
                borderStroke = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
                onClick = { dismiss() }
            )
        }
    }
    LaunchedEffect(key1 = state) {
        if (state.isSuccess) {
            delay(800)
            dismiss()
        }
    }
}