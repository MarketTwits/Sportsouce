package com.markettwits.members.member_edit.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.base_screen.FailedScreen
import com.markettwits.core_ui.base_screen.LoadingFullScreen
import com.markettwits.core_ui.components.top_bar.TopBarClipWithLabel
import com.markettwits.members.member_edit.presentation.component.MemberEditComponent
import com.markettwits.members.member_edit.presentation.components.components.EditMemberContent
import com.markettwits.members.member_edit.presentation.store.MemberEditStore


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberEditScreenDialog(component: MemberEditComponent) {

    val state by component.state.collectAsState()

    ModalBottomSheet(
        containerColor = MaterialTheme.colorScheme.primary,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        onDismissRequest = {
            component.obtainEvent(MemberEditStore.Intent.Dismiss)
        }) {
        Scaffold(topBar = {
            val title =
                if (state.mode is MemberEditComponent.Mode.Edit) "Редактировать участника" else "Добавить участника"
            TopBarClipWithLabel(title = title,
                onClickLabel = {
                    component.obtainEvent(MemberEditStore.Intent.Save)
                }, goBack = {
                    component.obtainEvent(MemberEditStore.Intent.Dismiss)
                })
        }) {
            EditMemberContent(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(top = it.calculateTopPadding()),
                onMemberChange = {
                    component.obtainEvent(MemberEditStore.Intent.OnValueChanged(it))
                },
                member = state.member,
                teams = state.teams,
                onClickSave = {

                })
            if (state.isLoading) {
                LoadingFullScreen()
            }
            if (state.isError && state.teams.isEmpty()) {
                FailedScreen(
                    onClickHelp = { /*TODO*/ },
                    message = state.message
                ) {

                }
            }
        }
    }


}
