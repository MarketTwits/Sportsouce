package com.markettwits.sportsouce.profile.registrations.list.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.topbar.TopBarWithClip
import com.markettwits.core_ui.items.screens.FailedScreen
import com.markettwits.core_ui.items.screens.LoadingFullScreen
import com.markettwits.sportsouce.profile.registrations.list.presentation.component.RegistrationsComponent
import com.markettwits.sportsouce.profile.registrations.list.presentation.components.starts.RegistrationsStart
import com.markettwits.sportsouce.profile.registrations.list.presentation.store.RegistrationsStore

@Composable
fun MyRegistrationsScreen(component: RegistrationsComponent) {
    val state by component.value.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopBarWithClip(title = "Мои регистрации") {
                component.obtainEvent(RegistrationsStore.Intent.Pop)
            }
        }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding())
        ) {
            if (state.isSuccess) {
                RegistrationsStart(
                    starts = state.filtered,
                    isRefreshing = state.isLoading,
                    filter = state.filter,
                    onRefresh = {
                        component.obtainEvent(RegistrationsStore.Intent.LoadData)
                    }, onClick = {
                        component.obtainEvent(RegistrationsStore.Intent.OnClickItem(it))
                    },
                    onClickFilter = {
                        component.obtainEvent(RegistrationsStore.Intent.OnClickFilter(it))
                    }
                )
            }
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