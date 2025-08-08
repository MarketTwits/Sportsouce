package com.markettwits.sportsouce.profile.registrations.presentation.detail.components.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderPaymentStatus

@Composable
fun OrderDialogPaymentStatus(
    modifier: Modifier = Modifier,
    paymentStatus: StartOrderPaymentStatus,
) {

    val statusColor = mapOrderStatusColor(paymentStatus)
    val statusIcon = mapOrderStatusIcon(paymentStatus)

    Column(
        modifier = modifier
            .clip(Shapes.large)
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AccountBalanceWallet,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "Статус оплаты",
                fontSize = 16.sp,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = Shapes.medium
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = statusColor.copy(alpha = 0.1f),
                        shape = CircleShape
                    )
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = statusIcon,
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = paymentStatus.title,
                fontSize = 16.sp,
                fontFamily = FontNunito.semiBoldBold(),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}