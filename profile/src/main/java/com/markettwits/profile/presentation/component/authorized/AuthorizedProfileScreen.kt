package com.markettwits.profile.presentation.component.authorized

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.profile.presentation.ProfileUiState
import com.markettwits.profile.presentation.common.ProfileScreenContent
import com.markettwits.profile.presentation.common.menu.ExitButton
import com.markettwits.profile.presentation.common.menu.profileMenu
import com.markettwits.profile.presentation.common.top_bar.ProfileTopBar
import com.markettwits.profile.presentation.sign_in.AuthButton


@Composable
fun AuthorizedProfileScreen(component: AuthorizedProfile) {
    val name by component.profileName.subscribeAsState()
    Column {
        if (name is ProfileUiState.Base){
            ProfileTopBar(userName =  (name as ProfileUiState.Base).user.name)
            ProfileScreenContent(menu = profileMenu()){
                when(it){
                    "Редактировать" -> component.goEditScreen()
                    "Сменить пароль" -> component.goChangePasswordScreen()
                    "Мои регистрации" -> component.goMyRegistryScreen()
                    "Участники" -> component.goMyMembersScreen()
                }
            }
            ExitButton {
                component.signOut()
            }
        }

    }
}