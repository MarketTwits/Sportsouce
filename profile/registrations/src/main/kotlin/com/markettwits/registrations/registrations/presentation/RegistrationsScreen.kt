package com.markettwits.registrations.registrations.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.markettwits.core_ui.base_screen.FailedScreen
import com.markettwits.core_ui.base_screen.LoadingFullScreen
import com.markettwits.core_ui.components.top_bar.TopBarWithClip
import com.markettwits.core_ui.theme.SportSouceTheme
import com.markettwits.registrations.registrations.presentation.component.RegistrationsComponent
import com.markettwits.registrations.registrations.presentation.component.RegistrationsComponentMock
import com.markettwits.registrations.registrations.presentation.components.filter.RegistrationsFilterList
import com.markettwits.registrations.registrations.presentation.components.starts.RegistrationsStart
import com.markettwits.registrations.registrations.presentation.store.RegistrationsStore

@Composable
fun MyRegistrationsScreen(component: RegistrationsComponent) {
    val state by component.value.collectAsState()

    Scaffold(containerColor = MaterialTheme.colorScheme.primary, topBar = {
        TopBarWithClip(title = "Мои регистрации") {
            component.obtainEvent(RegistrationsStore.Intent.Pop)
        }
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding())
        ) {
            RegistrationsFilterList(items = state.filter) {
                component.obtainEvent(RegistrationsStore.Intent.OnClickFilter(it))
            }
            if (state.base.isNotEmpty()) {
                RegistrationsStart(state.filtered, isRefreshing = state.isLoading, onRefresh = {
                    component.obtainEvent(RegistrationsStore.Intent.LoadData)
                }, onClick = {
                    component.obtainEvent(RegistrationsStore.Intent.OnClickItem(it))
                })
            }
        }
        if (state.base.isEmpty() && state.isLoading) {
            LoadingFullScreen()
        }
        if (state.isError) {
            FailedScreen(
                message = state.message,
                onClickHelp = { /*TODO*/ }) {
                component.obtainEvent(RegistrationsStore.Intent.LoadData)
            }
        }
    }
}

@Preview
@Composable
fun MyRegistrationsScreenPreview() {
    SportSouceTheme {
        MyRegistrationsScreen(RegistrationsComponentMock())
    }
}