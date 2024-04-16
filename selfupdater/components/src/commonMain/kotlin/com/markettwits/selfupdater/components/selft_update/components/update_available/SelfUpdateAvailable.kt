package com.markettwits.selfupdater.components.selft_update.components.update_available

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
fun SelfUpdateAvailable(modifier: Modifier = Modifier, isAvailable: Boolean) {
    val image =
        if (LocalDarkOrLightTheme.current) DefaultImages.SportSauceDarkLogo() else DefaultImages.SportSauceLightLogo()
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .size(200.dp),
            painter = image,
            contentDescription = "",
            contentScale = ContentScale.Crop
        )
        Text(
            modifier = Modifier.padding(4.dp),
            text = if (isAvailable) UpdatesAvailabelTitle else UpdatesDontAvailabelTitle,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            fontFamily = FontNunito.bold(),
            fontSize = 18.sp
        )
        Text(
            modifier = Modifier.padding(4.dp),
            text = if (isAvailable) UpdatesAvailabelDescription else UpdatesDontAvailabelDescription,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            fontFamily = FontNunito.regular(),
            fontSize = 14.sp
        )
    }
}

private val UpdatesAvailabelTitle = "Доступно обновление"
private val UpdatesAvailabelDescription =
    "Версия приложения устарела. Обновите его что бы иметь самую актуальную версию"
private val UpdatesDontAvailabelTitle = "Приложение обновлено"
private val UpdatesDontAvailabelDescription =
    "Установлена новейшая версия приложения.\nОбновление не требуется "
