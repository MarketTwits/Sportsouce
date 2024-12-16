package com.markettwits.registrations.detail.components.start

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.registrations.list.domain.StartOrderInfo

@Composable
fun OrderPaymentStatus(modifier: Modifier = Modifier, payment: StartOrderInfo.PaymentStatus) {
    val color = mapOrderStatus(payment)
    Card(
        modifier = modifier,
        shape = Shapes.medium,
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.2f))
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            text = payment.title,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            fontFamily = FontNunito.bold(),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = color
        )
    }
}

internal fun mapOrderStatus(payment: StartOrderInfo.PaymentStatus): Color {
    return when (payment) {
        is StartOrderInfo.PaymentStatus.NotPaid ->
            SportSouceColor.SportSouceLightRed

        is StartOrderInfo.PaymentStatus.Free ->
            SportSouceColor.SportSouceRegistryCommingSoonYellow

        is StartOrderInfo.PaymentStatus.PaymentCancelled ->
            SportSouceColor.SportSouceLightRed

        is StartOrderInfo.PaymentStatus.Success ->
            SportSouceColor.SportSouceRegistryOpenGreen

        is StartOrderInfo.PaymentStatus.WithoutStatus ->
            SportSouceColor.SportSouceLighBlue

        is StartOrderInfo.PaymentStatus.OnPlace ->
            SportSouceColor.SportSouceLighBlue
    }
}
