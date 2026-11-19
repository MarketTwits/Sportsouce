package com.markettwits.sportsouce.profile.authorized.authorized.presentation.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.progress.SauceSkeleton
import com.markettwits.core_ui.items.image.DefaultImages
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.theme.*

@Composable
internal fun ProfileSectionHeader(title: String, action: String? = null, onClick: () -> Unit = {}) {
    FlowRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
        verticalArrangement = Arrangement.spacedBy(8.dp), itemVerticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
        Text(title, style = MaterialTheme.typography.titleSmall, color = SauceTheme.colors.textPrimary)
        if (action != null) SauceButton(action, onClick, variant = SauceButtonVariant.Text, fillWidth = false)
    }
}

@Composable
internal fun ProfilePreviewCard(
    title: String, image: String, date: String, modifier: Modifier = Modifier,
    place: String = "", onClick: () -> Unit, status: @Composable () -> Unit,
) {
    OnBackgroundCard(modifier, shape = SauceShapes.field, onClick = onClick) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SubcomposeAsyncImage(model = imageRequestCrossfade(image), contentDescription = null,
                    contentScale = ContentScale.Crop, modifier = Modifier.size(width = 72.dp, height = 96.dp).clip(SauceShapes.badge),
                    loading = { SauceSkeleton(Modifier.fillMaxSize()) },
                    error = { SubcomposeAsyncImageContent(painter = DefaultImages.EmptyImageStart()) },
                    success = { SubcomposeAsyncImageContent() })
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(title, style = MaterialTheme.typography.titleSmall, color = SauceTheme.colors.textPrimary,
                        maxLines = 2, overflow = TextOverflow.Ellipsis)
                    Text(date, style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textSecondary)
                    if (place.isNotBlank()) Text(place, style = MaterialTheme.typography.bodySmall,
                        color = SauceTheme.colors.textSecondary, maxLines = 2, overflow = TextOverflow.Ellipsis)
                }
            }
            status()
        }
    }
}
