package com.markettwits.schedule.schedule.presentation.components.detail.screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.markettwits.schedule.schedule.presentation.components.detail.component.StartDetailScheduleComponent
import com.markettwits.schedule.schedule.presentation.components.detail.components.StartDetailScheduleContent
import com.markettwits.schedule.schedule.presentation.components.detail.store.StartDetailScheduleStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartDetailScheduleScreen(component: StartDetailScheduleComponent) {
    val state = component.state.collectAsState()
    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.primary,
        onDismissRequest = {
            component.obtainEvent(StartDetailScheduleStore.Intent.OnClickBack)
        }) {
        StartDetailScheduleContent(
            starts = state.value.start
        ) {
            component.obtainEvent(StartDetailScheduleStore.Intent.OnClickItem(it))
        }
    }
}