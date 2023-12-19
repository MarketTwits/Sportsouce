package com.markettwits.start.presentation.membres.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.start.presentation.membres.StartMembersScreen
import com.markettwits.start.presentation.membres.compoent.StartMembers
import com.markettwits.start.presentation.membres.compoent.StartMembersTopBar
import com.markettwits.start.presentation.membres.compoent.StartSearchMember

@Composable
fun StartMembersScreen(component: StartMembersScreen) {
    val members by component.members.subscribeAsState()
    Box(Modifier.fillMaxSize().background(Color.White)) {
        Column {
            StartMembersTopBar {
                component.back()
            }
            StartSearchMember(component = component)
            StartMembers(members = members)
        }
    }
}
