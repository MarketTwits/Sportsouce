package com.markettwits.registrations.start_order_profile.components.start

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.cloud.model.common.StartStatus
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.registrations.registrations.presentation.components.startStatusBackground
import dontPayment
import successPayment

@Composable
fun OrderDialogStatus(payment: Boolean, startStatus: StartStatus) {
    val order = mapOrderStatus(payment, startStatus)
    Image(
        painter = rememberVectorPainter(image = order.image),
        modifier = Modifier.size(80.dp),
        contentDescription = "",
        colorFilter = ColorFilter.tint(order.tint, blendMode = BlendMode.DstIn)
    )
    Text(
        text = order.title,
        fontFamily = FontNunito.bold,
        fontSize = 26.sp,
        color = order.tint
    )
    Text(
        text = order.description,
        textAlign = TextAlign.Center,
        fontFamily = FontNunito.bold,
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.outline
    )
}

data class OrderStatusDialog(
    val image: ImageVector,
    val tint: Color,
    val title: String,
    val description: String
)

private fun mapOrderStatus(payment: Boolean, startStatus: StartStatus): OrderStatusDialog {
    return if (payment)
        OrderStatusDialog(
            successPayment,
            startStatusBackground(startStatus.code),
            startStatus.name,
            "Будем ждать вас снова !"
        )
    else
        OrderStatusDialog(
            dontPayment,
            SportSouceColor.SportSouceLightRed,
            "Заказ не оплачен",
            "Оплатите заказ до окончания регистрации"
        )
}
