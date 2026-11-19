package com.markettwits.sportsouce.review.review.presentation.components.archive

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.progress.SauceSkeleton
import com.markettwits.core_ui.items.image.DefaultImages
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.starts.common.domain.StartsListItem

@Composable
fun StartCardSimple(
    modifier: Modifier = Modifier,
    cardWidth: Dp = 136.dp,
    start: StartsListItem,
    onItemClick: (StartsListItem) -> Unit,
) {
    OnBackgroundCard(modifier.width(cardWidth), onClick = { onItemClick(start) }) {
        SubcomposeAsyncImage(
            model = imageRequestCrossfade(start.image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().aspectRatio(4f / 5f).background(SauceTheme.colors.surfaceSubtle),
            loading = { SauceSkeleton(Modifier.fillMaxSize()) },
            error = { SubcomposeAsyncImageContent(painter = DefaultImages.EmptyImageStart()) },
            success = { SubcomposeAsyncImageContent() },
        )
        Column(Modifier.fillMaxWidth().padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(start.name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = SauceTheme.colors.textPrimary, minLines = 2, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Text(start.displayDate, style = MaterialTheme.typography.bodySmall,
                color = SauceTheme.colors.textMuted, minLines = 2, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
    }
}
