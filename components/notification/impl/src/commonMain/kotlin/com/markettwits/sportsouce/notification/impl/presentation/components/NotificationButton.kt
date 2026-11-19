package com.markettwits.sportsouce.notification.impl.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
fun NotificationButton(count: Int, onClick: () -> Unit) {
    val colors = SauceTheme.colors
    IconButton(onClick, Modifier.size(48.dp).clip(SauceShapes.button).semantics {
        contentDescription = if (count > 0) "Уведомления, непрочитанных: $count" else "Уведомления"
    }) {
        BadgedBox(badge = {
            if (count > 0) Badge(containerColor = colors.action, contentColor = colors.onAction) {
                Text(if (count > 99) "99+" else count.toString())
            }
        }) { Icon(Icons.Outlined.Notifications, null, tint = colors.textSecondary) }
    }
}
