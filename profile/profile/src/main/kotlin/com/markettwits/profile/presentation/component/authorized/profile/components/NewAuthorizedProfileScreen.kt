package com.markettwits.profile.presentation.component.authorized.profile.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.theme.SportSouceTheme
import com.markettwits.profile.presentation.component.authorized.profile.components.user_info.UserInfoCard
import com.markettwits.profile.presentation.component.authorized.profile.components.user_info.UserStarts
import com.markettwits.profile.presentation.component.authorized.profile.components.user_info.UserStatistics

@Composable
fun NewAuthorizedProfileScreen() {
    SportSouceTheme {
        Scaffold(
            topBar = {
                ProfileTopBar(goSettings = {

                })
            }) {
            Column(
                modifier = Modifier
                    .padding(top = it.calculateTopPadding())
                    .padding(10.dp)
            ) {
                UserInfoCard()
                UserStatistics(modifier = Modifier.padding(vertical = 10.dp))
                UserStarts(onClick = {})
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NewAuthorizedProfileScreenPreview() {
    NewAuthorizedProfileScreen()
}