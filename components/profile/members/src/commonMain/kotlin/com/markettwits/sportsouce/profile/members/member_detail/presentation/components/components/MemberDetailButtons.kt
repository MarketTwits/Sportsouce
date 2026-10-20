package com.markettwits.sportsouce.profile.members.member_detail.presentation.components.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.theme.SauceSpacing

@Composable
fun MemberDetailButtons(
    modifier: Modifier = Modifier,
    deleteLoading: Boolean,
    onClickEdit: () -> Unit,
    onClickDelete: () -> Unit,
) {
    // Full-width actions also fit narrow windows and large accessibility text.
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
        SauceButton(title = "Редактировать", onClick = onClickEdit, enabled = !deleteLoading)
        SauceButton(title = "Удалить", onClick = onClickDelete, variant = SauceButtonVariant.Outlined,
            enabled = !deleteLoading, loading = deleteLoading)
    }
}
