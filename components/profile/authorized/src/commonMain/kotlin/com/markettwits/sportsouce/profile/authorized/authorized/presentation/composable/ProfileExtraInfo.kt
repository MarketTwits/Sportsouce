package com.markettwits.sportsouce.profile.authorized.authorized.presentation.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes

@Composable
fun ProfileExtraInfoContent(
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current
    val items = listOf(
        ProfileExtraInfo(
            "Политика конфиденциальности",
            "https://sportsauce.ru/confidentiality"
        ),
        ProfileExtraInfo(
            "Контакты",
            "https://sportsauce.ru/contacts"
        ),
        ProfileExtraInfo(
            "Оплата",
            "https://sportsauce.ru/payment"
        ),

        )
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
    ) {
        items.forEach {
            ProfileExtraInfoItem(
                modifier = Modifier.padding(8.dp),
                item = it, onClick = {
                    uriHandler.openUri(it.url)
                })
        }
    }
}

@Composable
private fun ProfileExtraInfoItem(
    modifier: Modifier = Modifier,
    item: ProfileExtraInfo,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(Shapes.medium)
            .clickable(onClick = onClick),
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            text = item.title,
            color = MaterialTheme.colorScheme.outline,
            fontFamily = FontNunito.medium(),
            fontSize = 14.sp
        )
    }
}

private data class ProfileExtraInfo(
    val title: String,
    val url: String,
)