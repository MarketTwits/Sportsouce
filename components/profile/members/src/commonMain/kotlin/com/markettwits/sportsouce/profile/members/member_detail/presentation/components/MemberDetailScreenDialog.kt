package com.markettwits.sportsouce.profile.members.member_detail.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.markettwits.core_ui.items.components.bottom_sheet.DefaultModalBottomSheet
import com.markettwits.sportsouce.profile.members.member_detail.presentation.component.MemberDetailComponent
import com.markettwits.sportsouce.profile.members.member_detail.presentation.components.components.MemberDetailContent
import com.markettwits.sportsouce.profile.members.member_detail.presentation.store.MemberDetailStore

@Composable
fun MemberDetailScreenDialog(component: MemberDetailComponent) {
    val state by component.state.collectAsState()
    DefaultModalBottomSheet(
        title = "Участник",
        onDismissRequest = { component.obtainEvent(MemberDetailStore.Intent.Dismiss) },
    ) {
        MemberDetailContent(
            item = state.member,
            isDeleteLoading = state.isLoading,
            error = state.message.takeIf { state.isError },
            onClickEdit = { component.obtainEvent(MemberDetailStore.Intent.OnClickEdit) },
            onClickDelete = { component.obtainEvent(MemberDetailStore.Intent.OnClickDelete) },
        )
    }
}
