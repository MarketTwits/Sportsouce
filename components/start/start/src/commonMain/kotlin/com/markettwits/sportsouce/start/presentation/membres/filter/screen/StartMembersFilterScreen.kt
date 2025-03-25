package com.markettwits.sportsouce.start.presentation.membres.filter.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.core_ui.items.components.topbar.TopBarWithClip
import com.markettwits.sportsouce.start.presentation.membres.filter.StartMembersFilterScreen
import com.markettwits.sportsouce.start.presentation.membres.filter.component.StartMembersFilterButtonSelectionPanel
import com.markettwits.sportsouce.start.presentation.membres.filter.component.StartMembersSortWrapper2

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
            Spacer(modifier = Modifier.padding(18.dp))
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