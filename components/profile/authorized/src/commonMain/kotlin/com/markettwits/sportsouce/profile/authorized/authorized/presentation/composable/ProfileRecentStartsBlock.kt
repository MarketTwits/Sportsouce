package com.markettwits.sportsouce.profile.authorized.authorized.presentation.composable

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import com.markettwits.sportsouce.starts.common.presentation.StartStatusBadge

@Composable
internal fun ProfileRecentStartsBlock(modifier: Modifier = Modifier, starts: List<StartsListItem>, onClickStart: (StartsListItem) -> Unit) {
    if (starts.isEmpty()) return
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(Modifier.padding(horizontal = 16.dp)) {
            ProfileSectionHeader("Вы смотрели")
        }
        BoxWithConstraints(Modifier.fillMaxWidth()) {
            val cardWidth = (maxWidth - 32.dp).coerceAtLeast(0.dp).coerceAtMost(320.dp)
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                starts.forEach { start ->
                    ProfilePreviewCard(start.name, start.image, start.displayDate, Modifier.width(cardWidth), start.place,
                        onClick = { onClickStart(start) }) { StartStatusBadge(statusCode = start.statusCode.id, compact = true) }
                }
            }
        }
    }
}
