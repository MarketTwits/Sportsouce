package com.markettwits.sportsouce.profile.registrations.presentation.detail.components.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.components.progress.shimmer
import com.markettwits.core_ui.items.extensions.noRippleClickable
import com.markettwits.core_ui.items.image.DefaultImages
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderMember
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.start.mapOrderStatusColor
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.start.mapOrderStatusIcon
import com.markettwits.sportsouce.profile.registrations.presentation.detail.store.StartOrderStore

@Composable
fun OrderOverviewTab(
    orderInfo: StartOrderInfo,
    priceState: StartOrderStore.StartPriceResult,
    onClickStart: (Int) -> Unit,
    onClickPay: () -> Unit,
    onUpdatePrice: () -> Unit,
    onHelp: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StartInfoCard(
            orderInfo = orderInfo,
            onClickStart = onClickStart
        )

        PrimaryMemberCard(orderInfo = orderInfo)

        PaymentStatusCard(orderInfo = orderInfo)

        ActionsCard(
            priceState = priceState,
            onClickPay = onClickPay,
            onUpdatePrice = onUpdatePrice,
            onHelp = onHelp
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun StartInfoCard(
    orderInfo: StartOrderInfo,
    onClickStart: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(Shapes.medium)
            .background(MaterialTheme.colorScheme.primary)
            .noRippleClickable { onClickStart(orderInfo.startId) }
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(Shapes.medium)
                .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
        ) {
            SubcomposeAsyncImage(
                model = orderInfo.image.takeIf { it.isNotEmpty() },
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .shimmer(
                                tiltAngle = 30,
                                gradientColors = listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                                    Color.Transparent,
                                )
                            )
                    )
                },
                error = {
                    SubcomposeAsyncImageContent(
                        modifier = Modifier.fillMaxSize(),
                        painter = DefaultImages.EmptyImageStart()
                    )
                }
            )
        }

        Text(
            text = orderInfo.name,
            fontSize = 16.sp,
            fontFamily = FontNunito.bold(),
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InfoChip(
                icon = Icons.Default.CalendarToday,
                label = "Дата",
                value = orderInfo.dateStartPreview,
                modifier = Modifier.weight(1f)
            )
            InfoChip(
                icon = Icons.Default.Numbers,
                label = "ID заказа",
                value = "#${orderInfo.id}",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun InfoChip(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(Shapes.small)
            .background(MaterialTheme.colorScheme.outlineVariant)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = label,
                fontSize = 11.sp,
                fontFamily = FontNunito.medium(),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
        }
        Text(
            text = value,
            fontSize = 12.sp,
            fontFamily = FontNunito.semiBoldBold(),
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun PrimaryMemberCard(orderInfo: StartOrderInfo) {
    if (orderInfo.members.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(Shapes.medium)
                .background(MaterialTheme.colorScheme.primary)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Group,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "Участники",
                    fontSize = 14.sp,
                    fontFamily = FontNunito.semiBoldBold(),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${orderInfo.members.size}",
                    fontSize = 12.sp,
                    fontFamily = FontNunito.semiBoldBold(),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                orderInfo.members.forEach { member ->
                    CompactMemberRow(member = member)
                }
            }
        }
    }
}

@Composable
private fun PaymentStatusCard(orderInfo: StartOrderInfo) {
    val statusColor = mapOrderStatusColor(orderInfo.payment)
    val statusIcon = mapOrderStatusIcon(orderInfo.payment)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(Shapes.medium)
            .background(MaterialTheme.colorScheme.primary)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AccountBalanceWallet,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "Статус оплаты",
                fontSize = 14.sp,
                fontFamily = FontNunito.semiBoldBold(),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(Shapes.small)
                .background(MaterialTheme.colorScheme.outlineVariant)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = statusColor.copy(alpha = 0.1f),
                        shape = CircleShape
                    )
                    .padding(6.dp)
            ) {
                Icon(
                    imageVector = statusIcon,
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.size(16.dp)
                )
            }

            Text(
                text = orderInfo.payment.title,
                fontSize = 14.sp,
                fontFamily = FontNunito.semiBoldBold(),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun ActionsCard(
    priceState: StartOrderStore.StartPriceResult,
    onClickPay: () -> Unit,
    onUpdatePrice: () -> Unit,
    onHelp: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(Shapes.medium)
            .background(MaterialTheme.colorScheme.primary)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Действия",
            fontSize = 15.sp,
            fontFamily = FontNunito.bold(),
            color = MaterialTheme.colorScheme.onBackground
        )

        if (priceState is StartOrderStore.StartPriceResult.Success) {
            Button(
                onClick = onClickPay,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                shape = Shapes.small
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Payment,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Оплатить ${priceState.price} ₽",
                        fontSize = 14.sp,
                        fontFamily = FontNunito.semiBoldBold(),
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        } else if (priceState is StartOrderStore.StartPriceResult.Loading) {
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                ),
                shape = Shapes.small
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = MaterialTheme.colorScheme.onSecondary,
                        strokeWidth = 2.dp
                    )
                    Text(
                        text = "Загрузка...",
                        fontSize = 14.sp,
                        fontFamily = FontNunito.semiBoldBold(),
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        }

        Button(
            onClick = onUpdatePrice,
            modifier = Modifier.fillMaxWidth(),
            enabled = priceState !is StartOrderStore.StartPriceResult.Loading,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.outlineVariant,
                disabledContainerColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
            ),
            shape = Shapes.small
        ) {
            Row(
                modifier = Modifier.padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (priceState is StartOrderStore.StartPriceResult.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = MaterialTheme.colorScheme.onBackground,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Text(
                    text = if (priceState is StartOrderStore.StartPriceResult.Loading) "Обновление..." else "Обновить цену",
                    fontSize = 14.sp,
                    fontFamily = FontNunito.semiBoldBold(),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Button(
            onClick = onHelp,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.outlineVariant
            ),
            shape = Shapes.small
        ) {
            Row(
                modifier = Modifier.padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Help,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "Требуется помощь",
                    fontSize = 14.sp,
                    fontFamily = FontNunito.semiBoldBold(),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
private fun CompactMemberRow(member: StartOrderMember) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(Shapes.small)
            .background(MaterialTheme.colorScheme.outlineVariant)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        val fullName = "${member.surname} ${member.name}"
        val initials = fullName.split(" ")
            .take(2).joinToString("") { it.firstOrNull()?.toString() ?: "" }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(36.dp)
                .background(
                    color = MaterialTheme.colorScheme.outline,
                    shape = CircleShape
                )
        ) {
            Text(
                text = initials,
                color = MaterialTheme.colorScheme.onSecondary,
                fontSize = 13.sp,
                fontFamily = FontNunito.semiBoldBold()
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = fullName,
                fontSize = 14.sp,
                fontFamily = FontNunito.semiBoldBold(),
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${member.ageGroupName} • ${member.distanceName}",
                fontSize = 12.sp,
                fontFamily = FontNunito.medium(),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
