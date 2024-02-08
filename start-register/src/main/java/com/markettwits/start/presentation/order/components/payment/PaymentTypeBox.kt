package com.markettwits.start.presentation.order.components.payment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.start.domain.StartStatement

@Composable
fun PaymentTypeBox(
    modifier: Modifier = Modifier,
    paymentDisabled: Boolean,
    paymentType: String,
    payNow: Boolean,
    members: List<StartStatement>,
    onClickChangePayment: (Boolean) -> Unit,
) {
    var selectedNow by remember {
        mutableStateOf(true)
    }
    var selectedLater by remember {
        mutableStateOf(false)
    }
    var selectedWithoutPayment by remember {
        mutableStateOf(false)
    }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        shape = Shapes.medium,
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
    ) {
        Text(
            modifier = modifier.padding(10.dp),
            text = "Как оплатить ?",
            fontSize = 16.sp,
            fontFamily = FontNunito.bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black
        )
        Row {
            if (paymentDisabled) {
                PaymentTypeChosen(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    value = paymentType, selected = true
                ) {
                    //  onClickChangePayment()
                    //  selectedWithoutPayment = true
                }
            } else {
                PaymentTypeChosen(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    value = "Сейчас", selected = payNow
                ) {
                    onClickChangePayment(true)
//                    selectedNow = true
//                    selectedLater = false
                }
                if (members.size < 2) {
                    PaymentTypeChosen(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        value = "В личном кабинете",
                        selected = !payNow
                    ) {
                        onClickChangePayment(false)
//                        selectedNow = false
//                        selectedLater = true
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
        colors = CardDefaults.elevatedCardColors(containerColor = SportSouceColor.VeryLighBlue),
        border = if (selected) BorderStroke(2.dp, SportSouceColor.SportSouceBlue) else null
    ) {
        Text(
            modifier = modifier.padding(vertical = 5.dp, horizontal = 10.dp),
            text = value,
            fontSize = 16.sp,
            fontFamily = FontNunito.bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = SportSouceColor.SportSouceLighBlue
        )
    }
}