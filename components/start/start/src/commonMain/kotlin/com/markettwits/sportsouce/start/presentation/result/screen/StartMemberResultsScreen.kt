package com.markettwits.sportsouce.start.presentation.result.screen

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.markettwits.core_ui.items.components.toolbar.CollapsingToolbarScaffold
import com.markettwits.core_ui.items.components.toolbar.ScrollStrategy
import com.markettwits.core_ui.items.components.toolbar.rememberCollapsingToolbarScaffoldState
import com.markettwits.core_ui.items.window.calculateWindowSizeClass
import com.markettwits.sportsouce.start.presentation.result.component.StartMemberResultsComponent
import com.markettwits.sportsouce.start.presentation.result.model.MemberResult
import com.markettwits.sportsouce.start.presentation.result.components.MemberResultDetailedDialog
import com.markettwits.sportsouce.start.presentation.result.components.StartMemberMemberResultsTopbar
import com.markettwits.sportsouce.start.presentation.result.components.StartMemberResultsItemsContent

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun StartMemberResultsScreen(component: StartMemberResultsComponent) {

    val state by component.state.collectAsState()

    var currentMemberResult by remember { mutableStateOf<MemberResult?>(null) }

    CollapsingToolbarScaffold(
        modifier = Modifier,
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        state = rememberCollapsingToolbarScaffoldState(),
        toolbar = {
            StartMemberMemberResultsTopbar(
                state = state,
                windowSizeClass = calculateWindowSizeClass(),
                onIntent = component::obtainEvent
            )
        }
    ) {
        StartMemberResultsItemsContent(
            results = state.filteredMembers,
            onClickMemberResult = {
                currentMemberResult = it
            }
        )
        if (currentMemberResult != null) {
            Dialog(
                properties = DialogProperties(usePlatformDefaultWidth = true),
                onDismissRequest = {
                    currentMemberResult = null
                }
            ) {
                MemberResultDetailedDialog(
                    memberResult = currentMemberResult!!,
                    onBackClick = {
                        currentMemberResult = null
                    }
                )
            }
        }
    }
}