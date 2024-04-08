package com.markettwits.edit_profile.edit_social_network.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito

@Composable
fun ProfileSocialNetworkInfo(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = Shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(12.dp),
            text = socialNetworkLabel,
            color = MaterialTheme.colorScheme.outline,
            fontFamily = FontNunito.medium,
            lineHeight = 14.sp,
            fontSize = 12.sp
        )
    }
}

private const val socialNetworkLabel =
    "Укажите ссылки на свои социальные сети, чтобы организаторы могли с вами связаться"