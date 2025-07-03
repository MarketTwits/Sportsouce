package com.markettwits.sportsouce.start.presentation.result.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.markettwits.core_ui.items.components.toolbar.CollapsingToolbarScaffold
import com.markettwits.core_ui.items.components.toolbar.ScrollStrategy
import com.markettwits.core_ui.items.components.toolbar.rememberCollapsingToolbarScaffoldState
import com.markettwits.core_ui.items.window.calculateWindowSizeClass
import com.markettwits.sportsouce.start.presentation.result.component.StartMemberResultsComponent
import com.markettwits.sportsouce.start.presentation.result.model.MemberResult
import com.markettwits.sportsouce.start.presentation.result.newcomponents.DetailedResultScreen
import com.markettwits.sportsouce.start.presentation.result.newcomponents.MemberResultsTopbar
import com.markettwits.sportsouce.start.presentation.result.newcomponents.RaceResultsScreen2
import com.markettwits.sportsouce.start.presentation.result.store.StartMemberResultsStore

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


            MemberResultsTopbar(
                state = state,
                windowSizeClass = calculateWindowSizeClass(),
                onIntent = component::obtainEvent
            )

//            StartSearchMemberResults(
//                query = state.textQuery,
//                onValueChange = {
//                    component.obtainEvent(StartMemberResultsStore.Intent.OnChangeQuery(it))
//                },
//                onClickGoBack = {
//                    component.obtainEvent(StartMemberResultsStore.Intent.OnClickGoBack)
//                },
//                onClickBrush = {
//                    component.obtainEvent(StartMemberResultsStore.Intent.OnClickBrush)
//                }
//            )
        }
    ) {

        RaceResultsScreen2(
            results = state.filteredMembers,
            onClickMemberResult = {
                currentMemberResult = it
            }
        )

//        FilterDialog(
//            state = state,
//            onIntent = { intent ->
//                component.obtainEvent(intent)
//            },
//            onDismiss = {
//                component.obtainEvent(StartMemberResultsStore.Intent.OnToggleFilterDialog)
//            }
//        )

        if (currentMemberResult != null) {
            Dialog(
                properties = DialogProperties(usePlatformDefaultWidth = false),
                onDismissRequest = {
                    currentMemberResult = null
                }
            ) {
                DetailedResultScreen(
                    memberResult = currentMemberResult!!,
                )
            }
        }
    }
}