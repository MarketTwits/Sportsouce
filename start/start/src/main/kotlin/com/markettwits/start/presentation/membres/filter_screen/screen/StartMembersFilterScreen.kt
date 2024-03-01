package com.markettwits.start.presentation.membres.filter_screen.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.core_ui.components.top_bar.TopBarWithClip
import com.markettwits.start.presentation.membres.filter_screen.StartMembersFilterScreen
import com.markettwits.start.presentation.membres.filter_screen.component.StartMembersFilterButtonSelectionPanel
import com.markettwits.start.presentation.membres.filter_screen.component.StartMembersSortWrapper2

@Composable
fun StartMembersFilterScreen(component: StartMembersFilterScreen) {
    val filter by component.filter.subscribeAsState()
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .verticalScroll(rememberScrollState())
        ) {
            TopBarWithClip(title = "Список участников") {
                component.goBack()
            }
            StartMembersSortWrapper2(filter, component)
        }
        StartMembersFilterButtonSelectionPanel(
            modifier = Modifier.align(Alignment.BottomCenter),
            onClickApply = {
                component.apply()
            },
            onClickReset = {
                component.reset()
            })
    }
}