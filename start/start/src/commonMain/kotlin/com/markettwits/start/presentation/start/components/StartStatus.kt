package com.markettwits.start.presentation.start.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddComment
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.start.domain.StartItem
import com.markettwits.start.presentation.common.startStatusBackground

@Composable
internal fun StartStatus(
    modifier: Modifier = Modifier,
    status: StartItem.StartStatus,
    membersCount: Int,
) {
    Row(modifier = modifier) {
        val color = startStatusBackground(status.code)
        Row(
            modifier = Modifier
                .weight(3f)
                .border(2.dp, color.copy(0.8f), Shapes.large)
                .clip(Shapes.large),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = status.name,
                color = color,
                fontFamily = FontNunito.bold(),
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.size(12.dp))
        Row(
            modifier = Modifier
                .weight(1f)
                .border(0.5.dp, MaterialTheme.colorScheme.outline.copy(0.8f), Shapes.large)
                .clip(Shapes.large),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Spacer(modifier = Modifier.size(4.dp))
            Icon(
                modifier = Modifier
                    .padding(8.dp),
                imageVector = Icons.Outlined.Group,
                tint = MaterialTheme.colorScheme.outline,
                contentDescription = null,
            )
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = membersCount.toString(),
                color = MaterialTheme.colorScheme.outline,
                fontFamily = FontNunito.medium(),
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.size(4.dp))
        }
        Spacer(modifier = Modifier.size(12.dp))
        Row(
            modifier = Modifier
                .weight(0.7f)
                .border(0.5.dp, MaterialTheme.colorScheme.outline.copy(0.8f), Shapes.large)
                .clip(Shapes.large),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Spacer(modifier = Modifier.size(4.dp))
            Icon(
                modifier = Modifier
                    .padding(8.dp),
                imageVector = Icons.Outlined.AddComment,
                tint = MaterialTheme.colorScheme.outline,
                contentDescription = null,
            )
            Spacer(modifier = Modifier.size(4.dp))
        }
    }
    Spacer(modifier = Modifier.size(4.dp))
}