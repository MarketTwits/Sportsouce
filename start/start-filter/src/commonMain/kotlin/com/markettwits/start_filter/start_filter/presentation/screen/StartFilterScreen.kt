package com.markettwits.start_filter.start_filter.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.top_bar.TopBarWithClip
import com.markettwits.start_filter.start_filter.presentation.component.StartFilterComponent
import com.markettwits.start_filter.start_filter.presentation.components.FilterButtonSelectionPanel
import com.markettwits.start_filter.start_filter.presentation.components.StartFilterList
import com.markettwits.start_filter.start_filter.presentation.store.StartFilterStore

@Composable
fun StartFilterScreen(component: StartFilterComponent) {
    val state by component.value.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.tertiary,
                strokeCap = StrokeCap.Round
            )
        }
        if (state.filter.items.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                TopBarWithClip(title = "Фильтр") {
                    component.obtainEvent(StartFilterStore.Intent.GoBack)
                }
                Column(modifier = Modifier.padding(10.dp)) {
                    StartFilterList(startFilter = state.filter) { item, index, single ->
                        component.obtainEvent(
                            StartFilterStore.Intent.OnItemSelected(
                                startFilter = item,
                                index = index,
                                singleChoice = single
                            )
                        )
                    }
                }
            }
            Box(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                FilterButtonSelectionPanel(
                    onClickReset = {
                        component.obtainEvent(StartFilterStore.Intent.Reset)
                    }, onClickApply = {
                        component.obtainEvent(StartFilterStore.Intent.Apply)
                    })
            }
        }
        if (!state.isLoading && state.filter.items.isEmpty()) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                TopBarWithClip(title = "Фильтр") {
                    component.obtainEvent(StartFilterStore.Intent.GoBack)
                }
            }

        }

    }
}