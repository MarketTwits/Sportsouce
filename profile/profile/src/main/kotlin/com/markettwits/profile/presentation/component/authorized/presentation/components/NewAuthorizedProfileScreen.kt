package com.markettwits.profile.presentation.component.authorized.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.base_screen.LoadingFullScreen
import com.markettwits.core_ui.failed_screen.FailedScreen
import com.markettwits.profile.presentation.component.authorized.presentation.component.AuthorizedProfileComponent
import com.markettwits.profile.presentation.component.authorized.presentation.components.top_bar.ProfileTopBar
import com.markettwits.profile.presentation.component.authorized.presentation.components.user_info.UserInfoCard
import com.markettwits.profile.presentation.component.authorized.presentation.components.user_info.UserStatistics
import com.markettwits.profile.presentation.component.authorized.presentation.components.user_info.starts.UserStarts


@Composable
fun NewAuthorizedProfileScreen(component: AuthorizedProfileComponent) {
    val state by component.state.collectAsState()
    state.user?.let { user ->
        Scaffold(
            containerColor = MaterialTheme.colorScheme.primary,
            topBar = {
                ProfileTopBar(goSettings = {
                    component.obtainOutput(AuthorizedProfileComponent.Output.EditProfile)
                })
            }) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(top = it.calculateTopPadding())
                    .padding(10.dp)
            ) {
                UserInfoCard(
                    user = user,
                    onClickAddSocialNetwork = {
                        component.obtainOutput(AuthorizedProfileComponent.Output.SocialNetwork)
                    },
                    onClickEdit = {
                        component.obtainOutput(AuthorizedProfileComponent.Output.EditProfile)
                    })
                UserStatistics(
                    modifier = Modifier.padding(vertical = 10.dp),
                    membersCount = user.activity.userMemberCount,
                    registryCount = user.activity.userRegistry.size
                )
                UserStarts(
                    starts = user.activity.userRegistry,
                    onClick = {
                        component.obtainOutput(AuthorizedProfileComponent.Output.Start(it))
                    })
            }
        }
    }
    if (state.isLoading) {
        LoadingFullScreen()
    }
    if (state.isError) {
        FailedScreen(onClickHelp = { }) {
            //TODO add retry
        }
    }
}

//@Preview
//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//private fun NewAuthorizedProfileScreenPreview() {
//     NewAuthorizedProfileScreen(AuthorizedProfileComponentMock)
//}