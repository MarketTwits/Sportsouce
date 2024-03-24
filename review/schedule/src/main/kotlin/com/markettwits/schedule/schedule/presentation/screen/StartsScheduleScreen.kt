package com.markettwits.schedule.schedule.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.components.top_bar.TopBarWithClip
import com.markettwits.schedule.schedule.presentation.component.StartsScheduleComponent
import com.markettwits.schedule.schedule.presentation.components.StartsScheduleFailedContent
import com.markettwits.schedule.schedule.presentation.components.StartsScheduleLoadingContent
import com.markettwits.schedule.schedule.presentation.components.StartsScheduleSuccessContent
import com.markettwits.schedule.schedule.presentation.store.StartsScheduleStore

@Composable
fun StartsScheduleScreen(component: StartsScheduleComponent) {
    val state by component.value.collectAsState()
    Scaffold(
        containerColor = MaterialTheme.colorScheme.primary,
        topBar = {
            TopBarWithClip(title = "Расписание") {
                component.obtainEvent(StartsScheduleStore.Intent.Back)
            }
        }) {
        Column(
            modifier = Modifier
                .padding(top = it.calculateTopPadding())
                .fillMaxSize()
        ) {
            if (state.isError) {
                StartsScheduleFailedContent(onClick = {
                    component.obtainEvent(StartsScheduleStore.Intent.Launch)
                },
                    onClickPop = {
                        component.obtainEvent(StartsScheduleStore.Intent.Back)
                    }
                )
            }
            if (state.isLoading) {
                StartsScheduleLoadingContent()
            }
            if (state.actualStarts.isNotEmpty()) {
                StartsScheduleSuccessContent(
                    starts = state.actualStarts
                ) {
                    component.obtainEvent(StartsScheduleStore.Intent.OnClickItem(it))
                }
            }
        }
    }

}