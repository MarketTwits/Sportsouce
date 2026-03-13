package com.markettwits.sportsouce.news.news_list.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
fun NewsEmptyState(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "emptyNews")
    val iconScale by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100),
            repeatMode = RepeatMode.Reverse
        ),
        label = "emptyNewsScale"
    )

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .scale(iconScale)
                    .background(
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.16f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Newspaper,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(42.dp)
                )
            }

            Text(
                modifier = Modifier.padding(top = 18.dp),
                text = "Ничего не найдено",
                color = MaterialTheme.colorScheme.tertiary,
                fontFamily = FontNunito.bold(),
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier.padding(top = 8.dp, start = 24.dp, end = 24.dp),
                text = "Попробуйте выбрать другую категорию или убрать часть хештегов, чтобы увидеть больше новостей.",
                color = MaterialTheme.colorScheme.outline,
                fontFamily = FontNunito.regular(),
                fontSize = 15.sp,
                lineHeight = 20.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
