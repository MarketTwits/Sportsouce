package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.theme.*
import com.markettwits.sportsouce.start.cloud.model.start.fields.Organizer
import com.markettwits.sportsouce.start.domain.StartItem
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import com.markettwits.sportsouce.starts.common.presentation.StartStatusBadge
import com.markettwits.sportsouce.starts.common.presentation.startKindOfSportsIcon
import com.markettwits.sportsouce.starts.common.presentation.startKindOfSportsColor

@Composable
internal fun StartStatus(modifier: Modifier = Modifier, organizers: List<Organizer>, kindOfSports: List<StartItem.KindOfSport>, startStatus: StartItem.StartStatus) {
    Row(modifier.fillMaxWidth().padding(horizontal = SauceSpacing.large, vertical = SauceSpacing.small),
        horizontalArrangement = Arrangement.spacedBy(SauceSpacing.small), verticalAlignment = Alignment.CenterVertically) {
        StartStatusBadge(startStatus.code, Modifier.weight(1f).heightIn(min = 40.dp), centered = true, singleLine = true)
        kindOfSports.take(2).forEach { sport ->
            val sportItem = StartsListItem.KindOfSport(sport.id, sport.name)
            val sportColor = startKindOfSportsColor(sportItem)
            Box(Modifier.size(40.dp).clip(CircleShape).background(sportColor.copy(alpha = 0.15f)), contentAlignment = Alignment.Center) {
                Icon(startKindOfSportsIcon(sportItem), sport.name,
                    Modifier.size(SauceSizes.icon), tint = sportColor)
            }
        }
        val organizer = organizers.find { it.isMain } ?: organizers.firstOrNull()
        val path = organizer?.photo?.fullPath
        if (!path.isNullOrBlank()) Box(Modifier.size(40.dp).clip(CircleShape).background(SauceTheme.colors.surfaceSubtle), contentAlignment = Alignment.Center) {
            SubcomposeAsyncImage(model = imageRequestCrossfade(path), contentDescription = "Логотип организатора ${organizer.name}",
                contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize(),
                error = { Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(Icons.Outlined.Business, null, Modifier.size(SauceSizes.icon), tint = SauceTheme.colors.textMuted)
                } }, success = { SubcomposeAsyncImageContent() })
        }
    }
}
