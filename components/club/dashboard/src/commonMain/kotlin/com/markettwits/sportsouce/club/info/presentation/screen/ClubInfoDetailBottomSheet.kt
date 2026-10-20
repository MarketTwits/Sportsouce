package com.markettwits.sportsouce.club.info.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.bottom_sheet.DefaultModalBottomSheet
import com.markettwits.sportsouce.club.info.presentation.component.ClubInfoComponent
import com.markettwits.sportsouce.club.info.presentation.components.bottomsheet.MenuBottomSheet
import com.markettwits.sportsouce.club.info.presentation.components.bottomsheet.title
import com.markettwits.sportsouce.club.info.presentation.store.ClubInfoStore

@Composable
fun ClubInfoDetailBottomSheet(component: ClubInfoComponent) {
    val state by component.state.collectAsState()
    // Material computes its bottom anchor from the surface size. Bound only the scroll body.
    val bodyHeight = with(LocalDensity.current) { LocalWindowInfo.current.containerSize.height.toDp() } - 180.dp
    DefaultModalBottomSheet(title = state.selectedTab?.title ?: "Клуб", edgeToEdgeContent = true,
        onDismissRequest = { component.obtainEvent(ClubInfoStore.Intent.Dismiss) }) {
        Box(Modifier.fillMaxWidth().heightIn(max = bodyHeight.coerceAtLeast(120.dp))) {
            MenuBottomSheet(state.selectedTab, state.bottomSheetData,
                onClickSubscribe = { component.obtainEvent(ClubInfoStore.Intent.OnClickSubscribe) },
                onTrainerRegister = { component.obtainEvent(ClubInfoStore.Intent.OnTrainerRegister(it)) },
                onTrainingRegister = { component.obtainEvent(ClubInfoStore.Intent.OnTrainingRegister(it)) })
        }
    }
}
