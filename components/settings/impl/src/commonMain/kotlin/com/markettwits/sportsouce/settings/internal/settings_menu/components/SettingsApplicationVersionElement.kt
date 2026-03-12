package com.markettwits.sportsouce.settings.internal.settings_menu.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.image.DefaultImages
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.LocalDarkOrLightTheme


@Composable
fun SettingsApplicationVersionElement(
    modifier: Modifier = Modifier,
    versionName: String,
    versionBuildNumber: String
) {
    val image =
        if (LocalDarkOrLightTheme.current) DefaultImages.SportSauceDarkLogo() else DefaultImages.SportSauceLightLogo()

    OnBackgroundCard(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
    ) {
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
                    text = "Версия $versionName Сборка $versionBuildNumber",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.outline,
                    fontFamily = FontNunito.semiBoldBold(),
                )
            }
        }
    }
}