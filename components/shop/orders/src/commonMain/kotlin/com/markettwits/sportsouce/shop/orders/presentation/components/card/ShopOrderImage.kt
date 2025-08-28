package com.markettwits.sportsouce.shop.orders.presentation.components.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.markettwits.core_ui.items.components.progress.shimmer
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes

@Composable
internal fun ShopOrderImage(
    modifier: Modifier = Modifier,
    imageUrl: String?,
){
    if (imageUrl == null) {
        // Handle null imageUrl case with caption
        Column(
            modifier = modifier
                .clip(Shapes.medium)
                .size(60.dp)
                .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Photo,
                contentDescription = "No image",
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "Без изображения",
                fontSize = 8.sp,
                fontFamily = FontNunito.regular(),
                color = MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.Center,
                maxLines = 2,
                lineHeight = 9.sp
            )
        }
    } else {
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = "",
            modifier = modifier
                .clip(Shapes.medium)
                .size(60.dp)
                .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)),
            loading = {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .shimmer(
                            gradientColors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                                Color.Transparent,
                            ),
                            tiltAngle = 30
                        )
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.3f))
                )
            },
            error = {
                Column(
                    modifier = Modifier
                        .size(60.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Photo,
                        contentDescription = "Image load error",
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Без изображения",
                        fontSize = 8.sp,
                        fontFamily = FontNunito.regular(),
                        color = MaterialTheme.colorScheme.outline,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        lineHeight = 9.sp
                    )
                }
            }
        )
    }
}