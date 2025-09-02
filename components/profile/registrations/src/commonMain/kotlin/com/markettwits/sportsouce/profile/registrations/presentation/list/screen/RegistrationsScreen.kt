package com.markettwits.sportsouce.profile.registrations.presentation.list.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.toolbar.CollapsingToolbarScaffold
import com.markettwits.core_ui.items.components.toolbar.ScrollStrategy
import com.markettwits.core_ui.items.components.toolbar.rememberCollapsingToolbarScaffoldState
import com.markettwits.core_ui.items.screens.FailedScreen
import com.markettwits.core_ui.items.screens.LoadingFullScreen
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.sportsouce.profile.registrations.presentation.list.component.RegistrationsComponent
import com.markettwits.sportsouce.profile.registrations.presentation.list.components.filter.RegistrationsFilterDialog
import com.markettwits.sportsouce.profile.registrations.presentation.list.components.starts.RegistrationsStart
import com.markettwits.sportsouce.profile.registrations.presentation.list.components.toolbar.RegistrationsToolbar
import com.markettwits.sportsouce.profile.registrations.presentation.list.store.RegistrationsStore

@Composable
fun MyRegistrationsScreen(component: RegistrationsComponent) {

    val state by component.value.collectAsState()

    CollapsingToolbarScaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        state = rememberCollapsingToolbarScaffoldState(),
        toolbar = {
            RegistrationsToolbar(component = component)
        }
    ) {
        PullToRefreshScreen(
            isRefreshing = state.isLoading && state.base.isNotEmpty(),
            onRefresh = {
                component.obtainEvent(RegistrationsStore.Intent.LoadData)
            }
        ) {
            if (state.isSuccess) {
                RegistrationsStart(
                    modifier = Modifier.fillMaxSize(),
                    withoutFilterStarts = state.base,
                    withFilterStarts = state.filtered,
                    filter = state.filter,
                    onClick = {
                        component.obtainEvent(RegistrationsStore.Intent.OnClickItem(it))
                    },
                )
            }
            if (state.base.isEmpty() && state.isLoading) {
                LoadingFullScreen()
            }
            if (state.isError) {
                FailedScreen(
                    message = state.message,
                    onClickRetry = {
                        component.obtainEvent(RegistrationsStore.Intent.LoadData)
                    }
                )
            }
        }

        // Filter Dialog
        RegistrationsFilterDialog(component)
    }
}