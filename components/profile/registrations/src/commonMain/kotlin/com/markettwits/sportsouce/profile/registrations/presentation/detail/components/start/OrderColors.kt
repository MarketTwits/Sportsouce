package com.markettwits.sportsouce.profile.registrations.presentation.detail.components.start

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderPaymentStatus

@Composable
fun mapOrderStatusColor(payment: StartOrderPaymentStatus): Color {
    return when (payment) {
        is StartOrderPaymentStatus.Success -> SportSouceColor.SportSouceRegistryOpenGreen
        is StartOrderPaymentStatus.Free -> SportSouceColor.SportSouceLightBlueForDarkTheme
        is StartOrderPaymentStatus.OnPlace -> MaterialTheme.colorScheme.secondary
        is StartOrderPaymentStatus.NotPaid -> MaterialTheme.colorScheme.onErrorContainer
        is StartOrderPaymentStatus.PaymentCancelled -> SportSouceColor.InstagramIcon
        is StartOrderPaymentStatus.WithoutStatus -> MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)
    }
}

fun mapOrderStatusIcon(payment: StartOrderPaymentStatus): ImageVector {
    return when (payment) {
        is StartOrderPaymentStatus.Success -> Icons.Default.CheckCircle
        is StartOrderPaymentStatus.Free -> Icons.Default.CardGiftcard
        is StartOrderPaymentStatus.OnPlace -> Icons.Default.LocationOn
        is StartOrderPaymentStatus.NotPaid -> Icons.Default.Schedule
        is StartOrderPaymentStatus.PaymentCancelled -> Icons.Default.Cancel
        is StartOrderPaymentStatus.WithoutStatus -> Icons.AutoMirrored.Filled.Help
    }
}
