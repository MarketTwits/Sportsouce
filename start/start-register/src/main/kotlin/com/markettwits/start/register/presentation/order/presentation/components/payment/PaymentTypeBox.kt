package com.markettwits.start.register.presentation.order.presentation.components.payment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.markettwits.start.register.domain.StartStatement

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
                    modifier = Modifier.padding(),
                    value = paymentType, selected = true
                ) {}
            } else {
                PaymentTypeChosen(
                    modifier = Modifier.padding(),
                    value = "Сейчас", selected = payNow
                ) {
                    onClickChangePayment(true)
                }
                if (members.size <= 1 && distances.size <= 1) {
                    PaymentTypeChosen(
                        modifier = Modifier.padding(),
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
    Button(
        modifier = modifier.padding(horizontal = 10.dp, vertical = 5.dp),
        shape = Shapes.medium,
        colors = ButtonDefaults.elevatedButtonColors(containerColor = MaterialTheme.colorScheme.primary),
        border = if (selected) BorderStroke(2.dp, MaterialTheme.colorScheme.secondary) else null,
        onClick = {
            onClick()
        }
    ) {
        Text(
            modifier = modifier.padding(vertical = 1.dp, horizontal = 2.dp),
            text = value,
            fontSize = 16.sp,
            fontFamily = FontNunito.bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}