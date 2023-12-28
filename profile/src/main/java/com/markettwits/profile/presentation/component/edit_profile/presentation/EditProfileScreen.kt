package com.markettwits.profile.presentation.component.edit_profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.profile.presentation.common.menu.SignInOrRegistryButton
import com.markettwits.profile.presentation.common.top_bar.MyProfileTopBar
import com.markettwits.profile.presentation.component.edit_profile.presentation.components.MyInfoPage
import com.markettwits.profile.presentation.component.edit_profile.presentation.components.MySocialNetworkPage
import com.markettwits.profile.presentation.component.edit_profile.presentation.components.SaveChangesButton
import com.markettwits.profile.presentation.component.edit_profile.presentation.components.TabBar
import com.markettwits.profile.presentation.component.edit_profile.presentation.components.UserDataPage

@Composable
fun EditProfileScreen(component: EditProfile) {
    val page by component.page.subscribeAsState()
    Box(
        modifier = Modifier
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter)
                .background(Color.White)
             //   .verticalScroll(rememberScrollState())
        ) {
            MyProfileTopBar {
                component.pop()
            }
            TabBar(modifier = Modifier.fillMaxSize()) {
                when (it) {
                    0 -> MySocialNetworkPage(
                        user = page[0] as EditProfileUiPage.MySocialNetwork,
                        onUserChange = { updatedUser -> component.obtainTextFiled(updatedUser) }
                    )

                    1 -> UserDataPage(
                        page = page[1] as EditProfileUiPage.UserData,
                        onUserChange = { updatedUser -> component.obtainTextFiled(updatedUser) }
                    )

                    2 -> MyInfoPage(
                        page = page[2] as EditProfileUiPage.MyInfo,
                        onUserChange = { updatedUser -> component.obtainTextFiled(updatedUser) }
                    )
                }
            }

        }
            SaveChangesButton(modifier = Modifier.align(Alignment.BottomCenter)) {
                component.saveChanges()
            }
//        SaveChangesButton(modifier = Modifier.align(Alignment.BottomCenter)) {
//            component.saveChanges()
//        }
    }
}

@Preview
@Composable
private fun EditProfileScreenPreview() {
    EditProfileScreen(MockEditProfileScreen())
}