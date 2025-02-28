package com.markettwits.start.presentation.start.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddComment
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material3.*
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
        Card(
            modifier = Modifier
                .weight(3f)
                .clip(Shapes.large),
            shape = Shapes.medium,
            colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.2f))
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
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
    }
}