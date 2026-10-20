package com.markettwits.sportsouce.profile.members.member_add_edit.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll
import com.markettwits.core_ui.items.components.topbar.SauceScaffold
import com.markettwits.core_ui.items.components.topbar.TopBarWithClip
import com.markettwits.core_ui.items.event.EventEffect
import com.markettwits.core_ui.items.extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.screens.FailedScreen
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.profile.members.member_add_edit.presentation.component.MemberEditComponent
import com.markettwits.sportsouce.profile.members.member_add_edit.presentation.components.ImprovedEditMemberContent
import com.markettwits.sportsouce.profile.members.member_add_edit.presentation.store.MemberEditStore
import com.markettwits.sportsouce.profile.members.members_list.presentation.components.components.MembersLoadingContent

@Composable
fun MemberEditScreen(component: MemberEditComponent) {
    val state by component.state.collectAsState()
    val snackbar = remember { SnackbarHostState() }
    val focus = LocalFocusManager.current
    SauceScaffold(
        topBar = {
            TopBarWithClip(title = if (state.mode is MemberEditComponent.Mode.Edit) "Редактировать участника" else "Добавить участника") {
                focus.clearFocus(); component.obtainEvent(MemberEditStore.Intent.Dismiss)
            }
        },
        bottomBar = {
            SauceButton(title = "Сохранить участника", loading = state.isSaving,
                enabled = !state.isLoading && state.loadError == null,
                modifier = Modifier.imePadding().navigationBarsPadding().padding(SauceSpacing.large),
                onClick = { focus.clearFocus(); component.obtainEvent(MemberEditStore.Intent.Save) })
        },
        snackbarHost = {
            SnackbarHost(snackbar) { Snackbar(snackbarData = it, containerColor = SauceTheme.colors.danger.container,
                contentColor = SauceTheme.colors.danger.content, actionColor = SauceTheme.colors.danger.content) }
        },
    ) { padding ->
        AdaptivePane(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> MembersLoadingContent()
                state.loadError != null -> FailedScreen(message = state.loadError.orEmpty(),
                    onClickBack = { component.obtainEvent(MemberEditStore.Intent.Dismiss) },
                    onClickRetry = { component.obtainEvent(MemberEditStore.Intent.Retry) })
                else -> ImprovedEditMemberContent(
                    modifier = Modifier.fillMaxSize().sauceVerticalScroll(rememberScrollState()),
                    member = state.member, teams = state.teams, enabled = !state.isSaving,
                    teamsLoading = state.teamsLoading, teamsError = state.teamsError,
                    onRetryTeams = { component.obtainEvent(MemberEditStore.Intent.RetryTeams) },
                    onMemberChange = { component.obtainEvent(MemberEditStore.Intent.OnValueChanged(it)) })
            }
        }
    }
    EventEffect(event = state.event, onConsumed = { component.obtainEvent(MemberEditStore.Intent.OnConsumedEvent) }) {
        snackbar.showLongMessageWithDismiss(it.message)
    }
}
