package com.markettwits.sportsouce.profile.members.members_list.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.markettwits.core.errors.api.composable.SauceErrorScreen
import com.markettwits.core.errors.api.throwable.mapToString
import com.markettwits.core_ui.items.components.bottom_sheet.DefaultModalBottomSheet
import com.markettwits.core_ui.items.components.buttons.*
import com.markettwits.core_ui.items.components.selection.SauceScrollableTabs
import com.markettwits.core_ui.items.components.topbar.*
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.sportsouce.profile.members.members_list.presentation.component.MembersListComponent
import com.markettwits.sportsouce.profile.members.members_list.presentation.components.components.*
import com.markettwits.sportsouce.profile.members.members_list.presentation.store.store.MembersListStore

@Composable
fun MembersScreen(component: MembersListComponent) {
    val state by component.state.collectAsState()
    var adding by rememberSaveable { mutableStateOf(false) }
    var tab by rememberSaveable { mutableIntStateOf(0) }
    val refresh = { component.obtainEvent(MembersListStore.Intent.Retry) }
    val pending = state.team?.incoming.orEmpty().count { it.status == "pending" }
    SauceScaffold(topBar = {
        TopBarWithClip(title = "Мои участники") { component.obtainEvent(MembersListStore.Intent.GoBack) }
    }) { padding ->
        AdaptivePane(modifier = Modifier.fillMaxSize().padding(padding)) {
            Column(Modifier.fillMaxSize()) {
                SauceTopBarSection {
                    SauceScrollableTabs(labels = listOf("Участники", if (pending > 0) "Запросы ($pending)" else "Запросы", "Доступ"),
                        selectedIndex = tab, onSelected = { tab = it })
                }
                Box(Modifier.weight(1f)) {
                    when {
                        tab != 0 -> MemberTeamContent(state, requests = tab == 1, onRefresh = refresh, onIntent = component::obtainEvent)
                        state.isSuccess -> MembersList(
                            modifier = Modifier.fillMaxSize(), items = state.members, isRefreshing = state.isLoading,
                            team = state.team, teamError = state.teamError, refreshError = state.error?.mapToString(), onRefresh = refresh,
                            teamBusy = state.actingTeamKey != null || state.teamLoading,
                            pendingTrustedUsers = state.trustedChangesAwaitingRefresh, onTeamIntent = component::obtainEvent,
                            onClick = { component.obtainEvent(MembersListStore.Intent.OnClickMember(it)) },
                            onClickAddMember = { adding = true })
                        state.error != null -> state.error?.SauceErrorScreen(modifier = Modifier.fillMaxSize(), onClickRetry = refresh)
                        else -> MembersLoadingContent()
                    }
                }
            }
        }
    }
    if (adding) DefaultModalBottomSheet(title = "Добавить участника", onDismissRequest = { adding = false }) {
        Column(Modifier.padding(SauceSpacing.large), verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
            SauceButton("Найти на сайте", onClick = {
                adding = false; component.obtainEvent(MembersListStore.Intent.OnClickSearchMember)
            })
            SauceButton("Добавить без аккаунта", variant = SauceButtonVariant.Outlined, onClick = {
                adding = false; component.obtainEvent(MembersListStore.Intent.OnClickAddMember)
            })
        }
    }
}
