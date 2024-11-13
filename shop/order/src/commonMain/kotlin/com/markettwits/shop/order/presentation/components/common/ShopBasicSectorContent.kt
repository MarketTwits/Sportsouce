package com.markettwits.shop.order.presentation.components.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
internal fun ShopBasicSectorContent(
    modifier: Modifier = Modifier,
    title : String,
    content : @Composable () -> Unit,
){
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(vertical = 4.dp),
            text = title,
            fontSize = 18.sp,
            fontFamily = FontNunito.bold(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onPrimary
        )
        content()
    }
}