package com.markettwits.sportsouce.profile.authorized.authorized.presentation.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant

@Composable
fun ProfileExtraInfoContent(modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current
    Column(modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf("Контакты" to "https://sportsauce.ru/contacts", "Оплата" to "https://sportsauce.ru/payment",
            "Политика конфиденциальности" to "https://sportsauce.ru/confidentiality")
            .forEach { (title, url) ->
                SauceButton(title, { uriHandler.openUri(url) }, variant = SauceButtonVariant.Text, fillWidth = false)
            }
    }
}
