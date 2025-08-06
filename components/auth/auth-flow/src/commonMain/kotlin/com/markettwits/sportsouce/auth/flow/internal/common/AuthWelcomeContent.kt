package com.markettwits.sportsouce.auth.flow.internal.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.image.DefaultImages
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.LocalDarkOrLightTheme


@Composable
internal fun AuthWelcomeContent(content: String = "Добро пожаловать в SportSouce !") {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            Image(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(width = 160.dp, height = 60.dp),
                painter = if (LocalDarkOrLightTheme.current) DefaultImages.SportSauceDarkLogo()
                else DefaultImages.SportSauceLightLogo(),
                contentDescription = "welcome image",
                contentScale = ContentScale.Crop
            )
        }
        Text(
            modifier = Modifier.padding(bottom = 15.dp),
            text = content,
            fontFamily = FontNunito.bold(),
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}