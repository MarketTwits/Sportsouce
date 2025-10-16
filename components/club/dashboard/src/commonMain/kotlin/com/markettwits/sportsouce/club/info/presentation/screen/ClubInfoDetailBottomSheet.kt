package com.markettwits.sportsouce.club.info.presentation.screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.markettwits.sportsouce.club.info.presentation.component.ClubInfoComponent
import com.markettwits.sportsouce.club.info.presentation.components.bottomsheet.MenuBottomSheet
import com.markettwits.sportsouce.club.info.presentation.store.ClubInfoStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClubInfoDetailBottomSheet(
    component: ClubInfoComponent,
) {
    val state by component.state.collectAsState()

    ModalBottomSheet(
        containerColor = MaterialTheme.colorScheme.background,
        sheetState = rememberModalBottomSheetState(true),
        onDismissRequest = {
            component.obtainEvent(ClubInfoStore.Intent.Dismiss)
        }
    ) {
        MenuBottomSheet(
            selectedTab = state.selectedTab,
            bottomSheetData = state.bottomSheetData,
            onDismiss = {
                component.obtainEvent(ClubInfoStore.Intent.Dismiss)
            },
            onClickSubscribe = {
                component.obtainEvent(ClubInfoStore.Intent.OnClickSubscribe)
            },
            onTrainerRegister = { trainer ->
                component.obtainEvent(ClubInfoStore.Intent.OnTrainerRegister(trainer))
            },
            onTrainingRegister = { training ->
                component.obtainEvent(ClubInfoStore.Intent.OnTrainingRegister(training))
            },
            onMenuItemClick = { /* Handle menu item clicks if needed */ }
        )
    }
}