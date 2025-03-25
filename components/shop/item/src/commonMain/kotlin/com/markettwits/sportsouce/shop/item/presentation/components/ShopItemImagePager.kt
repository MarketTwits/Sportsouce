package com.markettwits.sportsouce.shop.item.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.extensions.noRippleClickable
import com.markettwits.core_ui.items.screens.FullImageScreen
import com.markettwits.core_ui.items.theme.Shapes


@Composable
internal fun ShopItemImagePager(
    modifier: Modifier,
    imageUrl: List<String>,
) {
    var selectedIndex by remember { mutableStateOf(0) }
    var isFullImage by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize()) {
        SubcomposeAsyncImage(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .clip(Shapes.large)
                .noRippleClickable {
                    isFullImage = true
                },
            model = imageUrl[selectedIndex],
            contentDescription = "",
            contentScale = ContentScale.Fit,
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
                    model = image,
                    contentDescription = "Thumbnail",
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
