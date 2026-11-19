package com.markettwits.sportsouce.profile.members.members_list.presentation.components.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.buttons.SauceButton

@Composable
internal fun AddMemberActionButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    SauceButton(title = "Добавить участника", modifier = modifier, onClick = onClick)
}
