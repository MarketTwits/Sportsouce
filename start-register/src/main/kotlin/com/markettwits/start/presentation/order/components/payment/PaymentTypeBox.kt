package com.markettwits.start.presentation.order.components.payment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.OnBackgroundCard
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.start.domain.StartStatement

@Composable
fun PaymentTypeBox(
    modifier: Modifier = Modifier,
    paymentDisabled: Boolean,
    paymentType: String,
    payNow: Boolean,
    members: List<StartStatement>,
    distances: List<String>,
    onClickChangePayment: (Boolean) -> Unit,
) {
    OnBackgroundCard(modifier = modifier.padding(top = 10.dp)) {
        Text(
            modifier = modifier.padding(10.dp),
            text = "Как оплатить ?",
            fontSize = 16.sp,
            fontFamily = FontNunito.bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.tertiary
        )
        Row {
            if (paymentDisabled) {
                PaymentTypeChosen(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    value = paymentType, selected = true
                ) {}
            } else {
                PaymentTypeChosen(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    value = "Сейчас", selected = payNow
                ) {
                    onClickChangePayment(true)
                }
                if (members.size <= 1 && distances.size <= 1) {
                    PaymentTypeChosen(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        value = "В личном кабинете",
                        selected = !payNow
                    ) {
                        onClickChangePayment(false)
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentTypeChosen(
    modifier: Modifier = Modifier,
    selected: Boolean,
    value: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable {
            onClick()
        },
        shape = Shapes.medium,
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primary),
        border = if (selected) BorderStroke(2.dp, MaterialTheme.colorScheme.secondary) else null
    ) {
        Text(
            modifier = modifier.padding(vertical = 5.dp, horizontal = 10.dp),
            text = value,
            fontSize = 16.sp,
            fontFamily = FontNunito.bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}