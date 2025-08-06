package com.markettwits.sportsouce.shop.orders.presentation.components.states

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.theme.FontNunito
import kotlinx.coroutines.delay

@Composable
internal fun ShopUserOrdersWithoutItemsContent(
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(300)
        isVisible = true
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        OnBackgroundCard(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(600)) + scaleIn(
                    animationSpec = tween(600),
                    initialScale = 0.8f
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Пустая корзина",
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    Text(
                        text = "У вас пока нет заказов",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = FontNunito.bold(),
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Здесь будут отображаться все ваши заказы из магазина спортивных товаров",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.outline,
                        fontFamily = FontNunito.medium(),
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )

                    Text(
                        text = "Перейдите в магазин и сделайте первый заказ!",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.secondary,
                        fontFamily = FontNunito.medium(),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Text(
                        text = "💡 Заказы будут автоматически сохраняться в истории",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f),
                        fontFamily = FontNunito.regular(),
                        fontSize = 11.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}
