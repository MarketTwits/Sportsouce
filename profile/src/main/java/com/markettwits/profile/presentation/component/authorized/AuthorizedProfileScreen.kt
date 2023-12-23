package com.markettwits.profile.presentation.component.authorized

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.markettwits.profile.presentation.common.ProfileScreenContent
import com.markettwits.profile.presentation.common.menu.ExitButton
import com.markettwits.profile.presentation.common.menu.profileMenu
import com.markettwits.profile.presentation.common.top_bar.ProfileTopBar
import com.markettwits.profile.presentation.sign_in.AuthButton

@Composable
fun AuthorizedProfileScreen(userName : String, exit : () -> Unit) {
    Column {
        ProfileTopBar(userName = userName)
        ProfileScreenContent(menu = profileMenu())
        ExitButton{
            exit()
        }
    }
}