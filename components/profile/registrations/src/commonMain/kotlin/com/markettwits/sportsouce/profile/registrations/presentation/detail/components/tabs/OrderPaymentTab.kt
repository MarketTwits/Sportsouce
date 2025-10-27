package com.markettwits.sportsouce.profile.registrations.presentation.detail.components.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderPaymentStatus
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.start.mapOrderStatusColor
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.start.mapOrderStatusIcon
import com.markettwits.sportsouce.profile.registrations.presentation.detail.store.StartOrderStore

@Composable
fun OrderPaymentTab(
    orderInfo: StartOrderInfo,
    priceState: StartOrderStore.StartPriceResult,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Информация об оплате",
            fontSize = 18.sp,
            fontFamily = FontNunito.bold(),
            color = MaterialTheme.colorScheme.onBackground
        )

        if (priceState !is StartOrderStore.StartPriceResult.Free &&
            !orderInfo.payment.isPaid
        ) {
            PaymentAmountCard(orderInfo = orderInfo, priceState = priceState)
        }

        PaymentStatusDetailCard(paymentStatus = orderInfo.payment)

        PaymentDetailsCard(
            orderInfo = orderInfo
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun PaymentAmountCard(
    orderInfo: StartOrderInfo,
    priceState: StartOrderStore.StartPriceResult,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(Shapes.medium)
            .background(MaterialTheme.colorScheme.primary)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.MonetizationOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "Сумма к оплате",
                fontSize = 16.sp,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(Shapes.small)
                .background(MaterialTheme.colorScheme.outlineVariant)
                .padding(18.dp),
            contentAlignment = Alignment.Center
        ) {
            when (priceState) {
                is StartOrderStore.StartPriceResult.Success -> {
                    Text(
                        text = "${priceState.price} ₽",
                        fontSize = 28.sp,
                        fontFamily = FontNunito.bold(),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                is StartOrderStore.StartPriceResult.Loading -> {
                    Text(
                        text = "Загрузка...",
                        fontSize = 18.sp,
                        fontFamily = FontNunito.medium(),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                }

                is StartOrderStore.StartPriceResult.Failed -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = orderInfo.cost.takeIf { it.isNotEmpty() && it != "0" }
                                ?.let { "$it ₽" } ?: "Не указана",
                            fontSize = 22.sp,
                            fontFamily = FontNunito.bold(),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Не удалось обновить",
                            fontSize = 12.sp,
                            fontFamily = FontNunito.medium(),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }

                else -> {}
            }
        }
    }
}

@Composable
private fun PaymentStatusDetailCard(paymentStatus: StartOrderPaymentStatus) {
    val statusColor = mapOrderStatusColor(paymentStatus)
    val statusIcon = mapOrderStatusIcon(paymentStatus)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(Shapes.medium)
            .background(MaterialTheme.colorScheme.primary)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AccountBalanceWallet,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = "Статус оплаты",
                fontSize = 16.sp,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(Shapes.small)
                .background(MaterialTheme.colorScheme.outlineVariant)
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = statusColor.copy(alpha = 0.1f),
                        shape = CircleShape
                    )
                    .padding(10.dp)
            ) {
                Icon(
                    imageVector = statusIcon,
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = paymentStatus.title,
                    fontSize = 15.sp,
                    fontFamily = FontNunito.bold(),
                    color = MaterialTheme.colorScheme.onBackground
                )

                val reason = when (paymentStatus) {
                    is StartOrderPaymentStatus.Success -> paymentStatus.paymentReason
                    is StartOrderPaymentStatus.Free -> paymentStatus.paymentReason
                    is StartOrderPaymentStatus.OnPlace -> paymentStatus.paymentReason
                    else -> ""
                }

                if (reason.isNotEmpty()) {
                    Text(
                        text = reason,
                        fontSize = 13.sp,
                        fontFamily = FontNunito.medium(),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun PaymentDetailsCard(orderInfo: StartOrderInfo) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(Shapes.medium)
            .background(MaterialTheme.colorScheme.primary)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Receipt,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = "Детали оплаты",
                fontSize = 16.sp,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(Shapes.small)
                .background(MaterialTheme.colorScheme.outlineVariant)
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (orderInfo.promo.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalOffer,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Промокод:",
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontFamily = FontNunito.medium(),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Text(
                        text = orderInfo.promo,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = FontNunito.bold(),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                if (orderInfo.costWithoutDiscount.isNotEmpty() &&
                    orderInfo.cost.isNotEmpty() &&
                    orderInfo.costWithoutDiscount != "0" &&
                    orderInfo.cost != "0" &&
                    orderInfo.costWithoutDiscount != orderInfo.cost
                ) {

                    val additionalCost = orderInfo.additionalFieldsCost.toIntOrNull() ?: 0
                    val original = orderInfo.costWithoutDiscount.toIntOrNull() ?: 0
                    val discounted = orderInfo.cost.toIntOrNull() ?: 0

                    val originalWithoutAdditional = original - additionalCost
                    val discountedWithoutAdditional = discounted - additionalCost

                    val discount = originalWithoutAdditional - discountedWithoutAdditional
                    val discountPercent = if (originalWithoutAdditional > 0) {
                        ((discount.toFloat() / originalWithoutAdditional.toFloat()) * 100).toInt()
                    } else 0

                    if (discount > 0) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Цена без скидки:",
                                fontSize = 13.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontFamily = FontNunito.medium(),
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                            )
                            Text(
                                text = "$originalWithoutAdditional ₽",
                                fontSize = 13.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontFamily = FontNunito.semiBoldBold(),
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Скидка:",
                                fontSize = 13.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontFamily = FontNunito.medium(),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "-$discount ₽ ($discountPercent%)",
                                fontSize = 14.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontFamily = FontNunito.bold(),
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 4.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                )
            }

            if (orderInfo.additionalFieldsCost.isNotEmpty() && orderInfo.additionalFieldsCost != "0") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AttachMoney,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Доп. поля:",
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontFamily = FontNunito.medium(),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Text(
                        text = "${orderInfo.additionalFieldsCost} ₽",
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = FontNunito.bold(),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 4.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Сумма :",
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = FontNunito.bold(),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = if (orderInfo.cost.isNotEmpty() && orderInfo.cost != "0") {
                        "${orderInfo.cost} ₽"
                    } else {
                        "Бесплатно"
                    },
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = FontNunito.bold(),
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}
