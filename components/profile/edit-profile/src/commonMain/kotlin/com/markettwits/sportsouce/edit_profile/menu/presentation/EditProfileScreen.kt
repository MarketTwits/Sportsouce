package com.markettwits.sportsouce.edit_profile.menu.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll as verticalScroll
import com.markettwits.core_ui.items.components.topbar.SauceScaffold as Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.topbar.TopBarWithClip
import com.markettwits.sportsouce.edit_profile.menu.presentation.component.EditProfileMenuComponentComponent
import com.markettwits.sportsouce.edit_profile.menu.presentation.components.EditProfileMenu
import com.markettwits.sportsouce.edit_profile.menu.presentation.components.profileMenu
import com.markettwits.sportsouce.edit_profile.menu.presentation.components.securityMenu

@Composable
fun EditProfileScreen(component: EditProfileMenuComponentComponent) {
    Scaffold(topBar = {
        TopBarWithClip(title = "Редактировать профиль", goBack = {
            component.obtainOutPut(EditProfileMenuComponentComponent.OutPut.GoBack)
        })
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize().padding(paddingValues).consumeWindowInsets(paddingValues)
                .verticalScroll(rememberScrollState()).padding(16.dp)
        ) {
            EditProfileMenu(
                modifier = Modifier.padding(bottom = 16.dp),
                menu = profileMenu(),
                onClickItem = {
                    when (it) {
                        "Мои социальные сети" -> component.obtainOutPut(
                            EditProfileMenuComponentComponent.OutPut.GoSocialNetwork
                        )

                        "Изменить учетную запись" -> component.obtainOutPut(
                            EditProfileMenuComponentComponent.OutPut.GoChangeProfileInfo
                        )

                        "Изменить фото профиля" -> component.obtainOutPut(
                            EditProfileMenuComponentComponent.OutPut.GoProfileImage
                        )

                        "Изменить статус" -> component.obtainOutPut(
                            EditProfileMenuComponentComponent.OutPut.GoProfileAbout
                        )
                    }
                })
            EditProfileMenu(
                modifier = Modifier.padding(bottom = 16.dp),
                menu = securityMenu(),
                onClickItem = {
                    when (it) {
                        "Сменить пароль" -> component.obtainOutPut(
                            EditProfileMenuComponentComponent.OutPut.GoChangePassword
                        )

                        "Выйти из аккаунта" -> component.obtainOutPut(
                            EditProfileMenuComponentComponent.OutPut.GoSignOut
                        )
                    }
                })
        }
    }
}