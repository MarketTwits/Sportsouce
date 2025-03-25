package com.markettwits.sportsouce.shop.order.presentation.components.delivery

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
internal fun ShopDeliveryTypeDescriptionContent(
    modifier: Modifier = Modifier,
    deliveryTitle : String,
    deliveryPlace : String,
){
    OnBackgroundCard(
        modifier = Modifier.padding(10.dp),
    ) {
        Row(
            modifier = modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(4.dp),
                imageVector = Icons.Default.Map,
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = ""
            )
            Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            Column {
                    Text(
                        text = deliveryTitle,
                        fontSize = 12.sp,
                        fontFamily = FontNunito.semiBoldBold(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = deliveryPlace ,
                        fontSize = 12.sp,
                        fontFamily = FontNunito.semiBoldBold(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
        }
    }
}