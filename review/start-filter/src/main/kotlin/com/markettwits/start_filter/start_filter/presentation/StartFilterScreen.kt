package com.markettwits.start_filter.start_filter.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.magnifier
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.start_filter.start_filter.presentation.components.FilterButtonSelectionPanel
import com.markettwits.start_filter.start_filter.presentation.components.StartFilterList
import com.markettwits.start_filter.start_filter.presentation.components.StartFilterTopBar
import com.markettwits.start_filter.start_filter.presentation.components.dialog.BottomScreenContent
import com.markettwits.start_filter.start_filter.presentation.store.StartFilterStore

@Composable
fun StartFilterScreen(component: StartFilterComponent) {
    val state by component.value.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = SportSouceColor.SportSouceBlue,
                strokeCap = StrokeCap.Round
            )
        }
        if (state.filter.items.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(10.dp)
            ) {
                StartFilterTopBar(title = "Фильтр") {
                    component.obtainEvent(StartFilterStore.Intent.GoBack)
                }
                StartFilterList(startFilter = state.filter) { item, index, single ->
                    component.obtainEvent(
                        StartFilterStore.Intent.OnItemSelected(
                            item,
                            index,
                            single
                        )
                    )
                }
            }
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .align(Alignment.BottomCenter)
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
            Column(modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxSize()
                .background(Color.White)
                .padding(10.dp)) {
                StartFilterTopBar(
                    title = "Фильтр"
                ) {
                    component.obtainEvent(StartFilterStore.Intent.GoBack)
                }
            }

        }

    }
}