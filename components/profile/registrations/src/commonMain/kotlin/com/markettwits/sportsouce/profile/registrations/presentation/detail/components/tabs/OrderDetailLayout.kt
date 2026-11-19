package com.markettwits.sportsouce.profile.registrations.presentation.detail.components.tabs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Wallet
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceStatusColors
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.profile.cloud.model.registrations.RegistrationOptions
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderPaymentStatus
import com.markettwits.sportsouce.profile.registrations.presentation.management.inlinePaymentAvailable
import com.markettwits.sportsouce.profile.registrations.presentation.management.payRegistrationOptionsLabel
import com.markettwits.sportsouce.profile.registrations.presentation.management.unpaidAmount
import com.markettwits.sportsouce.profile.registrations.presentation.management.unpaidOptions

@Composable
internal fun OrderDetailPage(content: @Composable ColumnScope.() -> Unit) {
    Column(
        Modifier.fillMaxSize().sauceVerticalScroll(rememberScrollState()).padding(SauceSpacing.large),
        verticalArrangement = Arrangement.spacedBy(SauceSpacing.large),
        content = content,
    )
}

@Composable
internal fun OrderSectionHeading(title: String) {
    Text(title, Modifier.semantics { heading() }, style = MaterialTheme.typography.titleMedium, color = SauceTheme.colors.textPrimary)
}

@Composable
internal fun OrderSectionCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    val colors = SauceTheme.colors
    Surface(modifier.fillMaxWidth(), color = colors.surface, shape = SauceShapes.card, border = BorderStroke(1.dp, colors.border)) {
        Column(content = content)
    }
}

@Composable
internal fun OrderSectionDivider() {
    HorizontalDivider(Modifier.padding(horizontal = SauceSpacing.large), color = SauceTheme.colors.border)
}

@Composable
internal fun OrderInfoLine(label: String, value: String) {
    val colors = SauceTheme.colors
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        if (maxWidth < 230.dp * LocalDensity.current.fontScale) {
            Column(verticalArrangement = Arrangement.spacedBy(SauceSpacing.extraSmall)) {
                Text(label, style = MaterialTheme.typography.bodySmall, color = colors.textSecondary)
                Text(value, style = MaterialTheme.typography.bodyMedium, color = colors.textPrimary)
            }
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(SauceSpacing.medium)) {
                Text(label, Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, color = colors.textSecondary)
                Text(value, Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, color = colors.textPrimary,
                    textAlign = TextAlign.End)
            }
        }
    }
}

@Composable
internal fun RegistrationStatusBanner(
    order: StartOrderInfo,
    archived: Boolean = false,
    description: String? = null,
    actions: (@Composable ColumnScope.() -> Unit)? = null,
) {
    val colors = SauceTheme.colors
    val (title, palette, icon) = when {
        order.isRefunded -> Triple("Возврат оформлен", colors.neutral, Icons.Outlined.Info)
        archived -> Triple("Старт завершён", colors.neutral, Icons.Outlined.Info)
        order.paymentCode == 3 || order.payment is StartOrderPaymentStatus.PaymentCancelled ->
            Triple(order.payment.title, colors.danger, Icons.Outlined.Info)
        order.inlinePaymentAvailable() || order.payment is StartOrderPaymentStatus.NotPaid ||
            order.hasUnpaidOptions || order.paymentCode == 7 ->
            Triple(order.payment.title, colors.warning, Icons.Outlined.Wallet)
        order.payment is StartOrderPaymentStatus.Success || order.payment is StartOrderPaymentStatus.Free ->
            Triple(order.payment.title, colors.success, Icons.Outlined.CheckCircle)
        else -> Triple(order.payment.title, colors.neutral, Icons.Outlined.Info)
    }
    OrderDetailNotice(title, palette, icon, description, actions)
}

@Composable
internal fun RegistrationOptionsDueBanner(
    order: StartOrderInfo,
    options: RegistrationOptions?,
    onPayOptions: () -> Unit,
) {
    val description = if (order.paymentCode == 7) "Слот оплачен. Осталось оплатить выбранные опции." else null
    OrderDetailNotice(
        title = "К оплате за дополнительные опции",
        palette = SauceTheme.colors.warning,
        icon = Icons.Outlined.Wallet,
        description = description,
        actions = {
            val unpaid = options?.unpaidOptions.orEmpty()
            unpaid.take(2).forEach { Text(it.name, style = MaterialTheme.typography.bodyMedium) }
            if (unpaid.size > 2) Text("Ещё опций: ${unpaid.size - 2}", style = MaterialTheme.typography.bodySmall)
            SauceButton(payRegistrationOptionsLabel(options?.unpaidAmount()), onPayOptions)
        },
    )
}

@Composable
private fun OrderDetailNotice(
    title: String,
    palette: SauceStatusColors,
    icon: ImageVector,
    description: String?,
    actions: (@Composable ColumnScope.() -> Unit)?,
) {
    Surface(Modifier.fillMaxWidth(), shape = SauceShapes.card, color = palette.container, contentColor = palette.content) {
        Column(Modifier.padding(SauceSpacing.large), verticalArrangement = Arrangement.spacedBy(SauceSpacing.medium)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(SauceSpacing.medium)) {
                Icon(icon, null, Modifier.size(22.dp))
                Column(verticalArrangement = Arrangement.spacedBy(SauceSpacing.extraSmall)) {
                    Text(title, style = MaterialTheme.typography.titleMedium)
                    if (!description.isNullOrBlank()) Text(description, style = MaterialTheme.typography.bodySmall)
                }
            }
            actions?.invoke(this)
        }
    }
}
