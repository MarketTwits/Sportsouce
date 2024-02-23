package com.markettwits.registrations.paymant_dialog.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.base_extensions.openWebPage
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.event.EventEffect
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.registrations.paymant_dialog.store.RegistrationsPaymentStore
import com.markettwits.registrations.registrations.presentation.components.RegistrationsCardImageCard


@Composable
fun RegistrationsPaymentCard(
    state: RegistrationsPaymentStore.State,
    onClickPayment: (Int) -> Unit
) {
    LazyColumn {
        items(state.items.paymentList) {
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .height(height = 200.dp)
                    .padding(10.dp)
            ) {
                Row {
                    RegistrationsCardImageCard(
                        modifier = Modifier,
                        it.image,
//                        it.dateStart,
//                        it.payment,
//                        it.statusCode
                    )
                    RegistrationsCardInfoStatusInfo(
                        id = it.id,
                        title = it.startTitle,
                        cost = it.cost,
                        onClickPayment = {
                            onClickPayment(it)
                        }
                    )
                }
            }

        }
    }
    val context = LocalContext.current
    EventEffect(
        event = state.event,
        onConsumed = {
        },
    ) {
        openWebPage(it, context)
    }
}

@Composable
private fun RegistrationsCardInfoStatusInfo(
    modifier: Modifier = Modifier,
    id: Int,
    title: String,
    cost: String,
    onClickPayment: (Int) -> Unit
) {
    Column(modifier = modifier.padding(start = 10.dp, end = 10.dp)) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontFamily = FontNunito.bold,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            color = SportSouceColor.SportSouceBlue
        )

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontFamily = FontNunito.bold)) {
                    append("Цена: ")
                }
                withStyle(style = SpanStyle(fontFamily = FontNunito.regular)) {
                    append(cost + "₽")
                }
            },
            fontSize = 12.sp,
            overflow = TextOverflow.Ellipsis,
            fontFamily = FontNunito.medium,
            color = SportSouceColor.SportSouceBlue
        )
        Button(
            shape = Shapes.large,
            colors = ButtonDefaults.elevatedButtonColors(containerColor = SportSouceColor.SportSouceRegistryOpenGreen),
            onClick = { onClickPayment(id) }
        ) {
            Text(
                text = "Оплатить",
                fontSize = 12.sp,
                overflow = TextOverflow.Ellipsis,
                fontFamily = FontNunito.medium,
                color = Color.White
            )
        }
    }
}