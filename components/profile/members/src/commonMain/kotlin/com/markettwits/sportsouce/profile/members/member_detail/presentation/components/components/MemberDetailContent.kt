package com.markettwits.sportsouce.profile.members.member_detail.presentation.components.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember
import com.markettwits.sportsouce.profile.members.member_common.presentation.MemberItemCard

@Composable
fun MemberDetailContent(
    modifier: Modifier = Modifier,
    item: ProfileMember,
    isDeleteLoading: Boolean,
    onClickEdit: () -> Unit,
    onClickDelete: () -> Unit,
    error: String? = null,
) {
    Column(modifier.fillMaxWidth().sauceVerticalScroll(rememberScrollState()).padding(SauceSpacing.large),
        verticalArrangement = Arrangement.spacedBy(SauceSpacing.large)) {
        error?.let { androidx.compose.material3.Text(it, color = com.markettwits.core_ui.items.theme.SauceTheme.colors.danger.content,
            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium) }
        MemberItemCard(full = true, item = item, onClick = {})
        MemberDetailButtons(deleteLoading = isDeleteLoading, onClickEdit = onClickEdit, onClickDelete = onClickDelete)
    }
}
