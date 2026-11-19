package com.markettwits.sportsouce.profile.registrations.presentation.list.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.markettwits.core.errors.api.composable.SauceErrorScreen
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.checkbox.FilterChipBase
import com.markettwits.core_ui.items.components.snackbar.SauceSnackbarHost
import com.markettwits.core_ui.items.components.selection.SauceSegmentedTabs
import com.markettwits.core_ui.items.components.toolbar.CollapsingToolbarScaffold
import com.markettwits.core_ui.items.components.toolbar.ScrollStrategy
import com.markettwits.core_ui.items.components.toolbar.rememberCollapsingToolbarScaffoldState
import com.markettwits.core_ui.items.components.topbar.SauceTopBarSection
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.screens.LoadingFullScreen
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.sportsouce.profile.registrations.presentation.list.component.RegistrationsComponent
import com.markettwits.sportsouce.profile.registrations.presentation.list.components.filter.RegistrationsFilterDialog
import com.markettwits.sportsouce.profile.registrations.presentation.list.components.starts.RegistrationsStart
import com.markettwits.sportsouce.profile.registrations.presentation.list.components.toolbar.RegistrationsToolbar
import com.markettwits.sportsouce.profile.registrations.presentation.list.store.RegistrationsStore
import com.markettwits.sportsouce.profile.registrations.presentation.management.*
import com.markettwits.sportsouce.profile.registrations.presentation.management.RegistrationManagementComponent.Intent
import com.markettwits.sportsouce.profile.registrations.presentation.management.RegistrationManagementComponent.Sheet

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MyRegistrationsScreen(component: RegistrationsComponent) {
    val state by component.value.collectAsState()
    val management = component.management
    val managementState = management?.state?.collectAsState()?.value
    val refunded = managementState?.refundedOrderIds.orEmpty()
    val snackbar = remember { SnackbarHostState() }
    var external by rememberSaveable { mutableStateOf(false) }
    val siteScroll = rememberLazyGridState()
    val externalScroll = rememberLazyGridState()
    var owner by rememberSaveable { mutableStateOf(RegistrationOwner.All) }
    val selectedFilters = state.filter.filter { it.checked }

    val orders = remember(state.filtered, state.sharedUser, owner, refunded) {
        val filtered = when (owner) {
            RegistrationOwner.All -> state.filtered
            RegistrationOwner.Mine, RegistrationOwner.Others -> state.filtered.filter { order ->
                order.members.any { member ->
                    member.id != null && member.isCurrentUser(state.sharedUser) == (owner == RegistrationOwner.Mine)
                }
            }
        }
        filtered.map { if (it.id in refunded) it.copy(isRefunded = true) else it }
    }

    Box(Modifier.fillMaxSize()) {
        CollapsingToolbarScaffold(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            state = rememberCollapsingToolbarScaffoldState(),
            scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
            toolbar = {
                RegistrationsToolbar(component = component, activeFilters = selectedFilters.size, showFilters = !external)
            },
        ) {
            Column(Modifier.fillMaxSize()) {
                if (management != null) {
                    SauceTopBarSection {
                        AdaptivePane(fillHeight = false) {
                            SauceSegmentedTabs(
                                labels = listOf("На сайте", "Внешние"),
                                selectedIndex = if (external) 1 else 0,
                                onSelected = { external = it == 1 },
                                modifier = Modifier.padding(
                                    start = SauceSpacing.large,
                                    end = SauceSpacing.large,
                                    top = SauceSpacing.extraSmall,
                                    bottom = SauceSpacing.small,
                                ),
                            )
                        }
                    }
                }
                AdaptivePane {
                    key(external) {
                        if (external && management != null) {
                            val data = requireNotNull(managementState)
                            LaunchedEffect(management) { management.accept(Intent.ExternalList()) }
                            PullToRefreshScreen(
                                isRefreshing = data.externalRefreshing,
                                onRefresh = { management.accept(Intent.ExternalList(refresh = true)) },
                            ) {
                                ExternalRegistrationList(data, externalScroll, management::accept)
                            }
                        } else {
                            PullToRefreshScreen(isRefreshing = state.isRefreshing,
                                onRefresh = {
                                    management?.accept(Intent.ReloadPreviews)
                                    component.obtainEvent(RegistrationsStore.Intent.LoadData)
                                }) {
                                Box(Modifier.fillMaxSize()) {
                                    if (state.isSuccess) {
                                        RegistrationsStart(
                                            modifier = Modifier.fillMaxSize(),
                                            gridState = siteScroll,
                                            withoutFilterStarts = state.base,
                                            withFilterStarts = orders,
                                            filter = state.filter,
                                            ownerFiltered = owner != RegistrationOwner.All,
                                            user = state.sharedUser,
                                            orderExtras = management?.let { manager ->
                                                { order ->
                                                    RegistrationOptionPreview(order, manager)
                                                    RegistrationCardActions(order, manager)
                                                }
                                            },
                                            onClick = { component.obtainEvent(RegistrationsStore.Intent.OnClickItem(it)) },
                                            header = management?.let {
                                                {
                                                    Column(verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
                                                        if (selectedFilters.isNotEmpty()) {
                                                            FlowRow(horizontalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
                                                                selectedFilters.forEach { filter ->
                                                                    FilterChipBase(
                                                                        selected = true,
                                                                        label = filter.value,
                                                                        onClick = { component.toggleFilter(filter) },
                                                                    )
                                                                }
                                                            }
                                                        }
                                                        if (state.sharedUser != null) {
                                                            FlowRow(horizontalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
                                                                RegistrationOwner.entries.forEach { filter ->
                                                                    FilterChipBase(
                                                                        selected = owner == filter,
                                                                        label = filter.label,
                                                                        onClick = { owner = filter },
                                                                    )
                                                                }
                                                            }
                                                        }
                                                        SauceButton("Найти мои регистрации", variant = SauceButtonVariant.Soft,
                                                            onClick = { management.accept(Intent.Open(Sheet.Find)) })
                                                    }
                                                }
                                            },
                                        )
                                    }
                                    if (state.base.isEmpty() && state.isLoading) LoadingFullScreen()
                                    state.exception?.SauceErrorScreen(
                                        onClickRetry = { component.obtainEvent(RegistrationsStore.Intent.LoadData) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        RegistrationsFilterDialog(component)
        SauceSnackbarHost(snackbar, modifier = Modifier.align(Alignment.BottomCenter).navigationBarsPadding())
        if (management != null) RegistrationManagementFeedback(management, snackbar)
    }
}
