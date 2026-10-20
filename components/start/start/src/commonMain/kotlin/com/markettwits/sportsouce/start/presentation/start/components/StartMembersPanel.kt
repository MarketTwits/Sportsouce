package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.layout.PaddingValues
import com.markettwits.core_ui.items.theme.SauceSpacing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Group
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.list.SauceActionRow
import com.markettwits.sportsouce.start.presentation.common.OnClick

@Composable
internal fun StartMembersPanel(modifier: Modifier = Modifier, membersCount: Int, onClick: OnClick) {
    if (membersCount != 0) OnBackgroundCard(modifier) {
        SauceActionRow(title = "Участники", count = membersCount.toString(), icon = Icons.Outlined.Group, onClick = onClick,
            contentPadding = PaddingValues(horizontal = SauceSpacing.large, vertical = SauceSpacing.small))
    }
}
