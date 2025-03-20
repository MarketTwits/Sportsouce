package com.markettwits.news.news_event.components

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.extensions.noRippleClickable
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.core_ui.items.image.DefaultImages

@Composable
internal fun NewsEventMainImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    onClickImage: () -> Unit
) {
    Card(
        modifier = Modifier
            .noRippleClickable {
                onClickImage()
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .wrapContentSize()
                .clip(Shapes.medium),
            error = {
                SubcomposeAsyncImageContent(
                    painter = DefaultImages.EmptyImageStart()
                )
            },
            success = {
                SubcomposeAsyncImageContent(modifier = modifier)
            }
        )
    }
}