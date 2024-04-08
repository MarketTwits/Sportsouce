package com.markettwits.edit_profile.edit_menu.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.components.top_bar.TopBarWithClip
import com.markettwits.core_ui.theme.SportSouceTheme
import com.markettwits.edit_profile.edit_menu.presentation.component.EditProfileMenuComponentComponent
import com.markettwits.edit_profile.edit_menu.presentation.components.EditProfileMenu
import com.markettwits.edit_profile.edit_menu.presentation.components.profileMenu
import com.markettwits.edit_profile.edit_menu.presentation.components.securityMenu

@Composable
fun EditProfileScreen(component: EditProfileMenuComponentComponent) {
    SportSouceTheme {
        Scaffold(topBar = {
            TopBarWithClip(title = "Редактировать профиль", goBack = {
                component.obtainOutPut(EditProfileMenuComponentComponent.OutPut.GoBack)
            })
        }) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(top = it.calculateTopPadding())
                    .padding(10.dp)
            ) {
                EditProfileMenu(menu = profileMenu(), onClickItem = {
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
                    modifier = Modifier.padding(vertical = 10.dp),
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
}