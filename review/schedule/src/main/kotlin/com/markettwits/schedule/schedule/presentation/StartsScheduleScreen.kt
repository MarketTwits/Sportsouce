package com.markettwits.schedule.schedule.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.markettwits.schedule.schedule.presentation.components.ScheduleTopBar
import com.markettwits.schedule.schedule.presentation.components.StartsScheduleFailedContent
import com.markettwits.schedule.schedule.presentation.components.StartsScheduleLoadingContent
import com.markettwits.schedule.schedule.presentation.components.StartsScheduleSuccessContent
import com.markettwits.schedule.schedule.presentation.store.StartsScheduleStore

@Composable
fun StartsScheduleScreen(component: StartsScheduleComponent) {
    val state by component.value.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        ScheduleTopBar(title = "Расписание", goBack = {
            component.obtainEvent(StartsScheduleStore.Intent.Back)
        })
        if (state.isError){
            StartsScheduleFailedContent(onClick = {
                component.obtainEvent(StartsScheduleStore.Intent.Launch)
            })
        }
        if (state.isLoading){
            StartsScheduleLoadingContent()
        }
        if (state.starts.isNotEmpty()){
            StartsScheduleSuccessContent(starts = state.starts, onClick = {
                component.obtainEvent(StartsScheduleStore.Intent.OnClickItem(it))
            })
        }
    }
}