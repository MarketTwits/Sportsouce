package com.markettwits.news_event.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.news_list.domain.Hashtag


@Composable
internal fun NewsEventBadgeTags(
    modifier: Modifier = Modifier,
    hashtags: List<Hashtag>
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .clip(Shapes.medium)
                .background(color = SportSouceColor.SportSouceRegistryOpenGreen)
        ) {
            Text(
                modifier = Modifier.padding(6.dp),
                text = "Новости",
                fontSize = 16.sp,
                fontFamily = FontNunito.bold(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSecondary,
            )
        }
        if (hashtags.isNotEmpty()) {
            Text(
                modifier = Modifier.padding(4.dp),
                text = "# ${hashtags.joinToString(separator = ", ") { it.name }}",
                fontSize = 14.sp,
                fontFamily = FontNunito.regular(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }

}