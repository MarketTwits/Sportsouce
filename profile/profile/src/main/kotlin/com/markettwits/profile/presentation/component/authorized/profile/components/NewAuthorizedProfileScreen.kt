package com.markettwits.profile.presentation.component.authorized.profile.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.theme.SportSouceTheme
import com.markettwits.profile.presentation.common.menu.ExitButton
import com.markettwits.profile.presentation.component.authorized.profile.AuthorizedProfile
import com.markettwits.profile.presentation.component.authorized.profile.components.top_bar.ProfileTopBar
import com.markettwits.profile.presentation.component.authorized.profile.components.user_info.UserInfoCard
import com.markettwits.profile.presentation.component.authorized.profile.components.user_info.UserStatistics
import com.markettwits.profile.presentation.component.authorized.profile.components.user_info.starts.UserStarts

@Composable
fun NewAuthorizedProfileScreen(component: AuthorizedProfile) {
    SportSouceTheme {
        Scaffold(
            topBar = {
                ProfileTopBar(goSettings = {
                    component.goEditScreen()
                })
            }) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(top = it.calculateTopPadding())
                    .padding(10.dp)
            ) {
                UserInfoCard(onClickEdit = {
                    component.goEditScreen()
                })
                UserStatistics(modifier = Modifier.padding(vertical = 10.dp))
                UserStarts(onClick = {})
                //TODO
                ExitButton {
                    component.signOut()
                }
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NewAuthorizedProfileScreenPreview() {
    // NewAuthorizedProfileScreen()
}