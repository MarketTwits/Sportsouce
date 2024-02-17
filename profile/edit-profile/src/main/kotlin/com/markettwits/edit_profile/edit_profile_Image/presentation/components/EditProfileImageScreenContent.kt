package com.markettwits.edit_profile.edit_profile_Image.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.components.buttons.ButtonContentBase
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceTheme

@Composable
fun EditProfileImageScreenContent(dismiss: () -> Unit, onClickImageBox: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
        shape = Shapes.medium
    ) {
        Text(
            modifier = Modifier.padding(start = 20.dp, top = 20.dp),
            text = "Изменить фото профиля",
            fontFamily = FontNunito.bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.tertiary
        )
        Column(modifier = Modifier.padding(40.dp)) {
            val stroke = Stroke(
                width = 4f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            )
            val color = MaterialTheme.colorScheme.tertiaryContainer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
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
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(10.dp)
                            .size(40.dp),
                        imageVector = Icons.Default.Image,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.tertiaryContainer
                    )
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = "Изображение",
                        fontFamily = FontNunito.bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.tertiaryContainer
                    )
                }
            }
            Spacer(modifier = Modifier.padding(10.dp))
            ButtonContentBase(
                modifier = Modifier.height(35.dp),
                title = "Отмена",
                borderStroke = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
                onClick = { dismiss() }
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun EditProfileAboutScreenContentPreview() {
    SportSouceTheme {
        EditProfileImageScreenContent(onClickImageBox = {}, dismiss = {})
    }
}