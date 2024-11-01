package com.markettwits.shop.order.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.OnBackgroundCard
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.shop.order.domain.model.ShopRecipient

@Composable
internal fun ShopOrderRecipient(
    modifier: Modifier = Modifier,
    shopRecipient: ShopRecipient
) {
    ShopBasicSectorContent(modifier = modifier, title = "Получатель"){

        OnBackgroundCard(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Face,
                    tint = MaterialTheme.colorScheme.tertiary,
                    contentDescription = ""
                )
                Column {
                    Text(
                        modifier = Modifier.padding(3.dp),
                        text = shopRecipient.name,
                        fontSize = 12.sp,
                        fontFamily = FontNunito.semiBoldBold(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        modifier = Modifier.padding(3.dp),
                        text = shopRecipient.phone,
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
}