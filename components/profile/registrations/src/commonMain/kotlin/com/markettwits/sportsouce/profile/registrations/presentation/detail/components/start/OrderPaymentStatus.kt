package com.markettwits.sportsouce.profile.registrations.presentation.detail.components.start

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
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderPaymentStatus

@Composable
fun OrderPaymentStatus(modifier: Modifier = Modifier, payment: StartOrderPaymentStatus) {
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

internal fun mapOrderStatus(payment: StartOrderPaymentStatus): Color {
    return when (payment) {
        is StartOrderPaymentStatus.NotPaid ->
            SportSouceColor.SportSouceLightRed

        is StartOrderPaymentStatus.Free ->
            SportSouceColor.SportSouceRegistryCommingSoonYellow

        is StartOrderPaymentStatus.PaymentCancelled ->
            SportSouceColor.SportSouceLightRed

        is StartOrderPaymentStatus.Success ->
            SportSouceColor.SportSouceRegistryOpenGreen

        is StartOrderPaymentStatus.WithoutStatus ->
            SportSouceColor.SportSouceLighBlue

        is StartOrderPaymentStatus.OnPlace ->
            SportSouceColor.SportSouceLighBlue
    }
}
