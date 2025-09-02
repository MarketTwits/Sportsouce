package com.markettwits.sportsouce.profile.members.member_add_edit.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import com.markettwits.core_ui.items.components.topbar.TopBarWithClip
import com.markettwits.core_ui.items.event.EventEffect
import com.markettwits.core_ui.items.extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.items.screens.FailedScreen
import com.markettwits.core_ui.items.screens.LoadingFullScreen
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.sportsouce.profile.members.member_add_edit.presentation.component.MemberEditComponent
import com.markettwits.sportsouce.profile.members.member_add_edit.presentation.components.components.EditMemberContent
import com.markettwits.sportsouce.profile.members.member_add_edit.presentation.store.MemberEditStore


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberEditScreenDialog(component: MemberEditComponent) {

    val state by component.state.collectAsState()

    val snackBarHostState by remember {
        mutableStateOf(SnackbarHostState())
    }
    var snackBarColor by remember {
        mutableStateOf(SportSouceColor.SportSouceLighBlue)
    }
    val focusManager = LocalFocusManager.current
    val title = if (state.mode is MemberEditComponent.Mode.Edit) "Редактировать участника" else "Добавить участника"
    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.background,
        onDismissRequest = {
            component.obtainEvent(MemberEditStore.Intent.Dismiss)
        }) {
        Scaffold(
            topBar = {
                TopBarWithClip(
                    modifier = Modifier.background(MaterialTheme.colorScheme.background),
                    title = title,
                    isStatusBarHandle = false
                ) {
                    focusManager.clearFocus()
                    component.obtainEvent(MemberEditStore.Intent.Dismiss)
                }
            },
            snackbarHost = {
                SnackbarHost(
                    hostState = snackBarHostState,
                ) {
                    Snackbar(
                        contentColor = Color.White,
                        containerColor = snackBarColor,
                        snackbarData = it
                    )
                }
            }
        ) { paddingValues ->
            EditMemberContent(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(top = paddingValues.calculateTopPadding()),
                onMemberChange = {
                    component.obtainEvent(MemberEditStore.Intent.OnValueChanged(it))
                },
                member = state.member,
                teams = state.teams,
                onSave = {
                    focusManager.clearFocus()
                    component.obtainEvent(MemberEditStore.Intent.Save)
                }
            )
            if (state.isLoading) {
                LoadingFullScreen()
            }
            if (state.isError && state.teams.isEmpty()) {
                FailedScreen(
                    message = state.message,
                    onClickBack = {
                        component.obtainEvent(MemberEditStore.Intent.Dismiss)
                    },
                    onClickRetry = {
                        component.obtainEvent(MemberEditStore.Intent.Retry)
                    }
                )
            }
        }
    }
    EventEffect(
        event = state.event,
        onConsumed = {
            component.obtainEvent(MemberEditStore.Intent.OnConsumedEvent)
        },
    ) {
        snackBarColor = if (it.success)
            SportSouceColor.SportSouceLighBlue else SportSouceColor.SportSouceLightRed
        snackBarHostState.showLongMessageWithDismiss(message = it.message)
    }
}
