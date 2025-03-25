package com.markettwits.sportsouce.shop.orders.presentation.components.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
internal fun ShopOrderItemRow(
    modifier: Modifier = Modifier,
    imageUrl : String,
    title : String,
    count : String,
    totalPrice : String
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ShopOrderImage(
                modifier = Modifier
                    .padding(2.dp)
                    .weight(0.8f),
                imageUrl = imageUrl
            )
            Text(
                modifier = Modifier
                    .padding(2.dp)
                    .weight(2f),
                text =  title,
                fontSize = 12.sp,
                fontFamily = FontNunito.medium(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                modifier = Modifier
                    .padding(2.dp)
                    .weight(1f),
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(
                        color = MaterialTheme.colorScheme.outline,
                        fontSize = 16.sp,
                        fontFamily = FontNunito.semiBoldBold(),
                    )
                    ) {
                        append("$count X ")
                    }
                    withStyle(style = SpanStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 16.sp,
                        fontFamily = FontNunito.bold(),
                    )) {
                        append("$totalPrice ₽")
                    }
                },
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
}