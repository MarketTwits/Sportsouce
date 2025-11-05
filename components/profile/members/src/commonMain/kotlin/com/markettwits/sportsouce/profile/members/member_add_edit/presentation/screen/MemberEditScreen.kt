package com.markettwits.sportsouce.profile.members.member_add_edit.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.topbar.TopBarWithClip
import com.markettwits.core_ui.items.event.EventEffect
import com.markettwits.core_ui.items.extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.items.screens.FailedScreen
import com.markettwits.core_ui.items.screens.LoadingFullScreen
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.sportsouce.profile.members.member_add_edit.presentation.component.MemberEditComponent
import com.markettwits.sportsouce.profile.members.member_add_edit.presentation.components.ImprovedEditMemberContent
import com.markettwits.sportsouce.profile.members.member_add_edit.presentation.store.MemberEditStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberEditScreen(component: MemberEditComponent) {

    val state by component.state.collectAsState()

    val snackBarHostState by remember {
        mutableStateOf(SnackbarHostState())
    }
    var snackBarColor by remember {
        mutableStateOf(SportSouceColor.SportSouceLighBlue)
    }
    val focusManager = LocalFocusManager.current
    val title = if (state.mode is MemberEditComponent.Mode.Edit) "Редактировать участника" else "Добавить участника"

    Scaffold(
        topBar = {
            TopBarWithClip(
                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                title = title,
                isStatusBarHandle = true
            ) {
                focusManager.clearFocus()
                component.obtainEvent(MemberEditStore.Intent.Dismiss)
            }
        },
        bottomBar = {
            if (!state.isLoading) {
                Button(
                    onClick = {
                        component.obtainEvent(MemberEditStore.Intent.Save)
                        focusManager.clearFocus()
                    },
                    modifier = Modifier
                        .imePadding()
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    )
                ) {
                    Text(
                        text = "Сохранить участника",
                        fontFamily = FontNunito.bold(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
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
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            ImprovedEditMemberContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding())
                    .padding(bottom = paddingValues.calculateBottomPadding())
                    .verticalScroll(rememberScrollState()),
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
