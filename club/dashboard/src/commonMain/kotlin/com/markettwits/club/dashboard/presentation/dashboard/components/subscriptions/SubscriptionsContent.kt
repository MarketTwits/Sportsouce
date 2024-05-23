package com.markettwits.club.dashboard.presentation.dashboard.components.subscriptions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.club.dashboard.presentation.dashboard.store.SubscriptionUi
import com.markettwits.core_ui.items.components.OnBackgroundCard
import com.markettwits.core_ui.items.components.checkbox.CheckBoxBase
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
fun SubscriptionsContent(
    modifier: Modifier = Modifier,
    subscriptions: List<SubscriptionUi>,
    onClick: (SubscriptionUi) -> Unit
) {
    Column(modifier = modifier.padding(10.dp)) {
        subscriptions.forEachIndexed { index, subscriptionUi ->
            val firstItem = 0
            val lastItem = subscriptions.size - 1
            val shape = when (index) {
                firstItem -> RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                lastItem -> RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)
                else -> RoundedCornerShape(0)
            }
            OnBackgroundCard(
                shape = shape,
                onClick = {
                    onClick(subscriptionUi)
                }
            ) { _ ->
                SubscriptionContentItem(
                    modifier = Modifier.padding(4.dp),
                    isSelected = subscriptionUi.isSelected,
                    title = subscriptionUi.subscription.name,
                    description = subscriptionUi.subscription.description,
                    price = subscriptionUi.subscription.price.toString(),
                    discountInPercent = subscriptionUi.subscription.discount,
                    onClick = {
                        onClick(subscriptionUi)
                    }
                )
            }
        }
    }
}

@Composable
private fun SubscriptionContentItem(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    title: String,
    description: String,
    price: String,
    discountInPercent: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        CheckBoxBase(
            checked = isSelected,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.secondary,
                checkmarkColor = MaterialTheme.colorScheme.onSecondary,
            ),
            onValueChanged = {
                onClick()
            }
        )
        Column(
            modifier = modifier,
        ) {
            Text(
                textAlign = TextAlign.Start,
                text = title,
                fontSize = 14.sp,
                fontFamily = FontNunito.semiBoldBold(),
                color = MaterialTheme.colorScheme.onPrimary
            )
//            HtmlText(
//                textAlign = TextAlign.Start,
//                text = description,
//                fontSize = 14.sp,
//                fontFamily = FontNunito.medium(),
//                color = MaterialTheme.colorScheme.outline
//            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    textAlign = TextAlign.End,
                    text = "RUB $price в месяц",
                    fontSize = 14.sp,
                    fontFamily = FontNunito.regular(),
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.padding(4.dp))
                SubscriptionDiscountContent(discountInPercent = discountInPercent)
            }
        }
    }
}

@Composable
private fun SubscriptionDiscountContent(
    modifier: Modifier = Modifier,
    discountInPercent: Int
) {
    if (discountInPercent > 0) {
        SubscriptionDiscount(modifier, discountInPercent)
    }
}

@Composable
private fun SubscriptionDiscount(
    modifier: Modifier,
    discountInPercent: Int
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            text = "- $discountInPercent%",
            fontSize = 14.sp,
            fontFamily = FontNunito.bold(),
            color = Color.White
        )
    }
}