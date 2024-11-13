package com.markettwits.shop.order.presentation.components.recipient

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
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
import com.markettwits.core_ui.items.components.textField.mapSimpleRuPhoneNumberToFull
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.shop.order.domain.model.ShopRecipient
import com.markettwits.shop.order.presentation.components.common.ShopBasicSectorContent

@Composable
internal fun ShopOrderRecipient(
    modifier: Modifier = Modifier,
    shopRecipient: ShopRecipient,
    onClick: () -> Unit,
) {
    ShopBasicSectorContent(
        modifier = modifier, title = "Получатель"
    ) {
        OnBackgroundCard(
            modifier = Modifier.padding(10.dp),
            onClick = onClick
        ) {
            Row(
                modifier = modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.padding(4.dp),
                    imageVector = Icons.Default.Face,
                    tint = MaterialTheme.colorScheme.secondary,
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
                Column {
                    AnimatedContent(shopRecipient.name) {
                        Text(
                            text = shopRecipient.name,
                            fontSize = 12.sp,
                            fontFamily = FontNunito.semiBoldBold(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    AnimatedContent(shopRecipient.phone) {
                        Text(
                            text = mapSimpleRuPhoneNumberToFull(shopRecipient.phone),
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
}


