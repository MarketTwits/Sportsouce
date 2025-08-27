package com.markettwits.sportsouce.profile.registrations.presentation.list.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.markettwits.core_ui.items.components.topbar.TopBarWithClip
import com.markettwits.core_ui.items.screens.CollapsingToolbarRefreshScaffold
import com.markettwits.core_ui.items.screens.FailedScreen
import com.markettwits.core_ui.items.screens.LoadingFullScreen
import com.markettwits.sportsouce.profile.registrations.presentation.list.component.RegistrationsComponent
import com.markettwits.sportsouce.profile.registrations.presentation.list.components.starts.RegistrationsStart
import com.markettwits.sportsouce.profile.registrations.presentation.list.store.RegistrationsStore

@Composable
fun MyRegistrationsScreen(component: RegistrationsComponent) {

    val state by component.value.collectAsState()

    CollapsingToolbarRefreshScaffold(
        isRefreshing = state.isLoading && state.base.isNotEmpty(),
        toolbar = {
            TopBarWithClip(title = "Мои регистрации") {
                component.obtainEvent(RegistrationsStore.Intent.Pop)
            }
        },
        onRefresh = {
            component.obtainEvent(RegistrationsStore.Intent.LoadData)
        },
    ) { modifier ->
        if (state.isSuccess) {
            RegistrationsStart(
                modifier = modifier.fillMaxSize(),
                withoutFilterStarts = state.base,
                withFilterStarts = state.filtered,
                filter = state.filter,
                onClick = {
                    component.obtainEvent(RegistrationsStore.Intent.OnClickItem(it))
                },
                onClickFilter = {
                    component.obtainEvent(RegistrationsStore.Intent.OnClickFilter(it))
                }
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
                })
        }
    }
}