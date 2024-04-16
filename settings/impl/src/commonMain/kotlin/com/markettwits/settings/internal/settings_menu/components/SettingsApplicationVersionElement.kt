package com.markettwits.settings.internal.settings_menu.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.OnBackgroundCard
import com.markettwits.core_ui.items.image.DefaultImages
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.LocalDarkOrLightTheme


@Composable
fun SettingsApplicationVersionElement(
    modifier: Modifier = Modifier,
) {
    val image =
        if (LocalDarkOrLightTheme.current) DefaultImages.SportSauceDarkLogo() else DefaultImages.SportSauceLightLogo()

    OnBackgroundCard(modifier = modifier) {
        Row(
            modifier = modifier
                .padding(4.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(70.dp),
                painter = image,
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Column {
                Text(
                    text = "Спорт Союз",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontFamily = FontNunito.semiBoldBold(),
                )
                Text(
                    text = "Версия 1.2.1 Сборка 123123",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.outline,
                    fontFamily = FontNunito.semiBoldBold(),
                )
            }
        }
    }
}