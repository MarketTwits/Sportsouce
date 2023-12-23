package com.markettwits.profile.presentation.component.unauthorized

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.markettwits.profile.presentation.ProfileScreen
import com.markettwits.profile.presentation.common.ProfileScreenContent
import com.markettwits.profile.presentation.common.menu.MenuItem
import com.markettwits.profile.presentation.common.menu.SignInOrRegistryButton
import com.markettwits.profile.presentation.common.top_bar.ProfileTopBar

@Composable
fun UnAuthorizedProfileScreen(component : ProfileScreen) {
  //  val state by component.nameState.subscribeAsState()
    Column {
        ProfileTopBar(userName = "")
        SignInOrRegistryButton{
            component.goToSignInScreen()
        }
        ProfileScreenContent(menu = unauthorizedProfileScreenMenu())
    }
}
private fun unauthorizedProfileScreenMenu() =listOf<MenuItem>(
    MenuItem.Header("Помощь"),
    MenuItem.Base("Написать в Telegram"),
    MenuItem.Base("Написать в WhatsApp"),
    MenuItem.Base("Карта магазина"),
    MenuItem.Base("Мои регистрации"),
    MenuItem.Base("О нас"),
)