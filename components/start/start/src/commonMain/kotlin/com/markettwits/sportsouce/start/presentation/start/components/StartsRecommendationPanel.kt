package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.image.DefaultImages
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.sportsouce.starts.common.domain.StartsListItem

@Composable
internal fun StartsRecommendationPanel(
    modifier: Modifier = Modifier,
    items: List<StartsListItem>,
    onItemClick: (Int) -> Unit
) {
    if (items.isNotEmpty()) {
        Column(
            modifier = Modifier
                .padding(vertical = 14.dp)
                .background(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.07f))
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Также рекомендуем",
                    fontSize = 18.sp,
                    fontFamily = FontNunito.semiBoldBold(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
            LazyRow {
                items(items) { item ->
                    Spacer(modifier = Modifier.width(8.dp))
                    ImageCard(
                        name = item.name,
                        image = item.image,
                        onClick = { onItemClick(item.id) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}

@Composable
private fun ImageCard(
    modifier: Modifier = Modifier,
    name: String,
    image: String,
    onClick: () -> Unit,
) {
    Column(modifier = modifier.width(125.dp)) {
        SubcomposeAsyncImage(
            model = imageRequestCrossfade(model = image),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.height(height = 160.dp)
                .clip(Shapes.large)
                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), Shapes.large)
                .clickable { onClick() },
            error = {
                SubcomposeAsyncImageContent(
                    modifier = Modifier,
                    painter = DefaultImages.EmptyImageStart()
                )
            },
            success = {
                SubcomposeAsyncImageContent(modifier = modifier)
            }
        )
        Text(
            modifier = Modifier.padding(4.dp),
            text = name,
            color = MaterialTheme.colorScheme.onPrimary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            fontFamily = FontNunito.bold(),
            fontSize = 12.sp,
            lineHeight = 14.sp,
            textAlign = TextAlign.Start
        )
    }

}