package com.markettwits.sportsouce.shop.item.presentation.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.components.progress.shimmer
import com.markettwits.core_ui.items.extensions.noRippleClickable
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.screens.FullImageScreen
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes


@Composable
internal fun ShopItemImagePager(
    modifier: Modifier,
    imageUrl: List<String>,
) {
    var selectedIndex by remember { mutableStateOf(0) }
    var isFullImage by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize()) {
        if (imageUrl.isNotEmpty()) {
            SubcomposeAsyncImage(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxSize()
                    .clip(Shapes.large)
                    .noRippleClickable {
                        isFullImage = true
                    },
                model = imageRequestCrossfade(imageUrl[selectedIndex]),
                contentDescription = "",
                contentScale = ContentScale.Fit,
                loading = {
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.outlineVariant)
                            .fillMaxSize()
                            .shimmer(
                                tiltAngle = 30,
                                gradientColors = listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.1f),
                                    Color.Transparent,
                                )
                            )
                    )
                },
                success = {
                    SubcomposeAsyncImageContent()
                })
            Row(
                modifier = Modifier
                    .padding(top = 14.dp)
                    .horizontalScroll(rememberScrollState())
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                imageUrl.forEachIndexed { index, image ->
                    AsyncImage(
                        modifier = Modifier
                            .clip(Shapes.medium)
                            .clickable { selectedIndex = index }
                            .size(100.dp)
                            .padding(4.dp)
                            .border(
                                width = if (index == selectedIndex) 2.dp else 0.dp,
                                color = if (index == selectedIndex) MaterialTheme.colorScheme.secondary else Color.Transparent,
                                shape = Shapes.medium
                            ),
                        model = imageRequestCrossfade(image),
                        contentDescription = "Thumbnail",
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
                    .fillMaxSize()
                    .defaultMinSize(minHeight = 400.dp)
                    .clip(Shapes.large),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = "No image",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = "Изображение отсутствует",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline,
                        fontFamily = FontNunito.medium(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
        if (isFullImage) {
            FullImageScreen(
                image = imageUrl,
                selectedImageIndex = selectedIndex,
                onDismiss = {
                    isFullImage = false
                }
            )
        }
    }
}