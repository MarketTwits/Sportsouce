package com.markettwits.sportsouce.profile.registrations.detail.components.start

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.sportsouce.profile.registrations.detail.store.store.StartOrderStore

@Composable
internal fun OrderDialogPaymentButton(
    modifier: Modifier = Modifier,
    priceState: StartOrderStore.StartPriceResult,
    onClick: () -> Unit
) {
    AnimatedVisibility(priceState is StartOrderStore.StartPriceResult.Failed) {
        Box(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .clip(Shapes.medium)
                .background(SportSouceColor.SportSouceLightRed.copy(alpha = 0.1f))
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(10.dp),
                text = "Не удалось обновить стоимость заказа",
                fontSize = 14.sp,
                fontFamily = FontNunito.bold(),
                overflow = TextOverflow.Ellipsis,
                color = SportSouceColor.SportSouceLightRed
            )
        }
    }
    if (
        priceState !is StartOrderStore.StartPriceResult.Free
        &&
        priceState !is StartOrderStore.StartPriceResult.Failed
    ) {
        Button(
            modifier = modifier.fillMaxWidth(),
            colors = ButtonDefaults.textButtonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                disabledContainerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f)
            ),
            onClick = { onClick() }
        ) {
            if (priceState is StartOrderStore.StartPriceResult.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onTertiary,
                    strokeCap = StrokeCap.Round
                )
            }
            if (priceState is StartOrderStore.StartPriceResult.Success) {
                Text(
                    modifier = Modifier.padding(2.dp),
                    text = "Оплатить ${priceState.price} ₽",
                    fontSize = 16.sp,
                    fontFamily = FontNunito.bold(),
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onTertiary
                )
            }
        }
    }
}