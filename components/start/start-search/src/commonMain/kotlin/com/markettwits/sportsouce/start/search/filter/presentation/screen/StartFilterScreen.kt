package com.markettwits.sportsouce.start.search.filter.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.markettwits.core_ui.items.components.bottom_sheet.DefaultModalBottomSheet
import com.markettwits.sportsouce.start.search.filter.presentation.component.StartFilterComponent
import com.markettwits.sportsouce.start.search.filter.presentation.components.StartFilterContent
import com.markettwits.sportsouce.start.search.filter.presentation.store.StartFilterStore


@Composable
fun StartFilterScreen(component: StartFilterComponent) {

    val state by component.value.collectAsState()

    DefaultModalBottomSheet(
        title = "Фильтры",
        onDismissRequest = {
            component.obtainEvent((StartFilterStore.Intent.GoBack))
        }
    ) {
        StartFilterContent(state, component::obtainEvent)
    }
}
