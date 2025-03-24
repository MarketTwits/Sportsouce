package com.markettwits.start_search.filter.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.markettwits.core_ui.items.components.bottom_sheet.DefaultModalBottomSheet
import com.markettwits.start_search.filter.presentation.component.StartFilterComponent
import com.markettwits.start_search.filter.presentation.components.StartFilterContent
import com.markettwits.start_search.filter.presentation.store.StartFilterStore


@Composable
fun StartFilterScreen(component: StartFilterComponent) {

    val state by component.value.collectAsState()

    DefaultModalBottomSheet(
        onDismissRequest = {
            component.obtainEvent((StartFilterStore.Intent.GoBack))
        }
    ) {
        StartFilterContent(
            filterUi = state.filter,
            isLoading = state.isLoading,
            onClickBack = {
                component.obtainEvent(StartFilterStore.Intent.GoBack)
            },
            onClickApply = {
                component.obtainEvent(StartFilterStore.Intent.Apply)
            },
            onItemSelected = { item, index, single ->
                component.obtainEvent(StartFilterStore.Intent.OnItemSelected(item, index, single))
            },
            onClickReset = {
                component.obtainEvent(StartFilterStore.Intent.Reset)
            }
        )
    }
}