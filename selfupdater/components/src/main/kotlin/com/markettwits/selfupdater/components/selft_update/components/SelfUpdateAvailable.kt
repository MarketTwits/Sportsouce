package com.markettwits.selfupdater.components.selft_update.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.selfupdater.components.R

@Composable
fun SelfUpdateAvailable(modifier: Modifier = Modifier) {
    val image =
        if (isSystemInDarkTheme())
            painterResource(id = com.markettwits.core_ui.R.drawable.ic_launcher_black_foreground)
        else painterResource(
            id = com.markettwits.core_ui.R.drawable.ic_launcher_light_foreground
        )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val imageSize = image.intrinsicSize / 2F
        Image(
            modifier = Modifier
                .size(200.dp),
            painter = image,
            contentDescription = "",
            contentScale = ContentScale.Crop
        )
        Text(
            modifier = Modifier.padding(4.dp),
            text = "Доступно обновление",
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            fontFamily = FontNunito.bold,
            fontSize = 18.sp
        )
        Text(
            modifier = Modifier.padding(4.dp),
            text = "Версия приложения устарела. Обновите его что бы иметь самую актуальную версию",
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            fontFamily = FontNunito.regular,
            fontSize = 14.sp
        )
    }

}