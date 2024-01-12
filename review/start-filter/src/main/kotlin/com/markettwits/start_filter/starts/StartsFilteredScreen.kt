package com.markettwits.start_filter.starts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import com.markettwits.core_ui.failed_screen.FailedScreen
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.start_filter.start_filter.presentation.components.StartFilterTopBar
import com.markettwits.start_filter.starts.store.StartsFilteredStore
import com.markettwits.starts.components.StartCard

@Composable
fun StartsFilteredScreen(component: StartsFilteredComponent) {
    val state by component.value.collectAsState()
    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
    ) {
        StartFilterTopBar(title = "Фильтр") {
              component.obtainEvent(StartsFilteredStore.Intent.OnClickBack)
        }
        if (state.isLoading){
            Box(modifier = Modifier.fillMaxSize()){
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = SportSouceColor.SportSouceBlue,
                    strokeCap = StrokeCap.Round
                )
            }

        }
        if (state.isError){
            FailedScreen(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                message = state.message,
                onClickHelp = { /*TODO*/ }
            ) {

            }
        }
        LazyColumn{
            items(state.starts) {
                StartCard(start = it, onItemClick = {
                    component.obtainEvent(StartsFilteredStore.Intent.OnItemClick(it))
                })
            }
        }
    }
}