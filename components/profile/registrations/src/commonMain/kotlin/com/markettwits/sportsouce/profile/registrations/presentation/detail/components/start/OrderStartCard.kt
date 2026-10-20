package com.markettwits.sportsouce.profile.registrations.presentation.detail.components.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.PeopleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.progress.SauceSkeleton
import com.markettwits.core_ui.items.components.status.SauceStatusBadge
import com.markettwits.core_ui.items.components.status.SauceStatusTone
import com.markettwits.core_ui.items.image.DefaultImages
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderPaymentStatus

@Composable
fun OrderStartCard(
    modifier: Modifier = Modifier,
    item: StartOrderInfo,
    onClickStart: (Int) -> Unit,
) {
    OnBackgroundCard(modifier = modifier, onClick = { onClickStart(item.startId) }) {
        Column(Modifier.padding(SauceSpacing.medium), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            BoxWithConstraints(Modifier.fillMaxWidth()) {
                if (maxWidth < 240.dp * LocalDensity.current.fontScale) {
                    // Large system text gets the full card width instead of a narrow photo column.
                    Column(verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
                        RegistrationTitle(item.startTitle)
                        Row(horizontalArrangement = Arrangement.spacedBy(SauceSpacing.medium)) {
                            RegistrationsCardImageCard(image = item.image, modifier = Modifier.size(56.dp, 64.dp))
                            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(SauceSpacing.extraSmall)) {
                                RegistrationNumber(item.id)
                                RegistrationDate(item.dateStartPreview, maxLines = 3)
                            }
                            RegistrationChevron(Modifier.align(Alignment.CenterVertically))
                        }
                    }
                } else Row(horizontalArrangement = Arrangement.spacedBy(SauceSpacing.medium)) {
                    RegistrationsCardImageCard(image = item.image, modifier = Modifier.size(72.dp, 88.dp))
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(SauceSpacing.extraSmall)) {
                        RegistrationNumber(item.id)
                        RegistrationTitle(item.startTitle)
                        RegistrationDate(item.dateStartPreview)
                    }
                    RegistrationChevron(Modifier.align(Alignment.CenterVertically))
                }
            }

            RegistrationMembersSummary(item)
            HorizontalDivider(color = SauceTheme.colors.border)
            RegistrationPaymentSummary(item)
        }
    }
}

@Composable
private fun RegistrationTitle(title: String) {
    Text(title, style = MaterialTheme.typography.titleSmall, color = SauceTheme.colors.textPrimary,
        maxLines = 2, overflow = TextOverflow.Ellipsis)
}

@Composable
private fun RegistrationNumber(id: Int) {
    Text("№ $id", style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textMuted,
        maxLines = 1, overflow = TextOverflow.Ellipsis)
}

@Composable
private fun RegistrationDate(date: String, maxLines: Int = 2) {
    if (date.isNotBlank()) Text(date, style = MaterialTheme.typography.bodySmall,
        color = SauceTheme.colors.textSecondary, maxLines = maxLines, overflow = TextOverflow.Ellipsis)
}

@Composable
private fun RegistrationChevron(modifier: Modifier = Modifier) {
    Icon(Icons.Default.ChevronRight, null, modifier.size(18.dp), tint = SauceTheme.colors.textMuted)
}

/** Team entries stay a summary in the list; every participant remains available in the details. */
@Composable
private fun RegistrationMembersSummary(item: StartOrderInfo) {
    if (item.members.isEmpty()) return
    val colors = SauceTheme.colors
    val member = item.members.singleOrNull()
    val names = member?.let { listOf(it.surname, it.name).filter(String::isNotBlank).joinToString(" ") }
    val title = names?.takeIf(String::isNotBlank) ?: "Участники: ${item.members.size}"
    val distances = item.members.map { it.distanceName.trim() }.filter(String::isNotBlank).distinct()
    Row(horizontalArrangement = Arrangement.spacedBy(SauceSpacing.small), verticalAlignment = Alignment.Top) {
        Icon(Icons.Outlined.PeopleOutline, null, Modifier.padding(top = 2.dp).size(16.dp), tint = colors.textMuted)
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(title, style = MaterialTheme.typography.bodyMedium, color = colors.textSecondary,
                maxLines = 1, overflow = TextOverflow.Ellipsis)
            if (distances.isNotEmpty()) Text(distances.joinToString(", "),
                style = MaterialTheme.typography.bodySmall, color = colors.textMuted,
                maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun RegistrationPaymentSummary(item: StartOrderInfo) {
    val cost = item.cost.takeIf { it.isNotBlank() && it != "0" }
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        // At large text sizes the price moves below the badges instead of squeezing their labels.
        val stackPrice = maxWidth < 280.dp * LocalDensity.current.fontScale
        if (stackPrice) Column(verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
            OrderStatusRow(item)
            if (cost != null) RegistrationPrice(cost)
        } else Row(horizontalArrangement = Arrangement.spacedBy(SauceSpacing.small), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.weight(1f)) { OrderStatusRow(item) }
            if (cost != null) RegistrationPrice(cost)
        }
    }
}

@Composable
private fun RegistrationPrice(cost: String) {
    Text("$cost ₽", style = MaterialTheme.typography.titleSmall, color = SauceTheme.colors.textPrimary)
}

@Composable
private fun OrderStatusRow(item: StartOrderInfo) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        SauceStatusBadge(item.payment.title, tone = when (item.payment) {
            is StartOrderPaymentStatus.Success, is StartOrderPaymentStatus.Free -> SauceStatusTone.Success
            is StartOrderPaymentStatus.NotPaid -> SauceStatusTone.Warning
            is StartOrderPaymentStatus.PaymentCancelled -> SauceStatusTone.Danger
            is StartOrderPaymentStatus.OnPlace, is StartOrderPaymentStatus.WithoutStatus -> SauceStatusTone.Neutral
        })
        if (item.members.any { it.results.isNotEmpty() }) SauceStatusBadge("Результаты", showIndicator = false)
    }
}

@Composable
fun RegistrationsCardImageCard(modifier: Modifier = Modifier, image: String) {
    SubcomposeAsyncImage(
        model = imageRequestCrossfade(image.takeIf(String::isNotBlank)),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier.clip(SauceShapes.badge).background(SauceTheme.colors.surfaceSubtle),
        loading = { SauceSkeleton(Modifier.fillMaxSize(), SauceShapes.badge) },
        error = { SubcomposeAsyncImageContent(painter = DefaultImages.EmptyImageStart()) },
        success = { SubcomposeAsyncImageContent() },
    )
}
