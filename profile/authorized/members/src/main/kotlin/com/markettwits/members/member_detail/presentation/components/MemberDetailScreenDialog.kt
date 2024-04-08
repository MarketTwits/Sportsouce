package com.markettwits.members.member_detail.presentation.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.markettwits.members.member_detail.presentation.component.MemberDetailComponent
import com.markettwits.members.member_detail.presentation.components.components.MemberDetailContent
import com.markettwits.members.member_detail.presentation.store.MemberDetailStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberDetailScreenDialog(component: MemberDetailComponent) {
    val state by component.state.collectAsState()
    ModalBottomSheet(
        onDismissRequest = {
            component.obtainEvent(MemberDetailStore.Intent.Dismiss)
        },
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        MemberDetailContent(
            modifier = Modifier.padding(
                bottom = WindowInsets.navigationBars
                    .asPaddingValues()
                    .calculateBottomPadding()
            ),
            item = state.member,
            isDeleteLoading = state.isLoading,
            onClickEdit = {
                component.obtainEvent(MemberDetailStore.Intent.OnClickEdit)
            }, onClickDelete = {
                component.obtainEvent(MemberDetailStore.Intent.OnClickDelete)
            }
        )
    }
}
