package com.markettwits.members.member_add_edit.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import com.markettwits.core_ui.base_extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.base_screen.FailedScreen
import com.markettwits.core_ui.base_screen.LoadingFullScreen
import com.markettwits.core_ui.components.top_bar.TopBarClipWithLabel
import com.markettwits.core_ui.event.EventEffect
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.members.member_add_edit.presentation.component.MemberEditComponent
import com.markettwits.members.member_add_edit.presentation.components.components.EditMemberContent
import com.markettwits.members.member_add_edit.presentation.store.MemberEditStore


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
    ModalBottomSheet(
        containerColor = MaterialTheme.colorScheme.primary,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        onDismissRequest = {
            component.obtainEvent(MemberEditStore.Intent.Dismiss)
        }) {
        val focusManager = LocalFocusManager.current
        Scaffold(topBar = {
            val title =
                if (state.mode is MemberEditComponent.Mode.Edit) "Редактировать участника" else "Добавить участника"
            TopBarClipWithLabel(title = title,
                onClickLabel = {
                    focusManager.clearFocus()
                    component.obtainEvent(MemberEditStore.Intent.Save)

                }, goBack = {
                    focusManager.clearFocus()
                    component.obtainEvent(MemberEditStore.Intent.Dismiss)
                })
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
        ) {
            EditMemberContent(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(top = it.calculateTopPadding()),
                onMemberChange = {
                    component.obtainEvent(MemberEditStore.Intent.OnValueChanged(it))
                },
                member = state.member,
                teams = state.teams
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
