package com.markettwits.profile.presentation.component.unauthorized

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.profile.presentation.ProfileScreen
import com.markettwits.profile.presentation.ProfileUiState
import com.markettwits.profile.presentation.common.ProfileScreenContent
import com.markettwits.profile.presentation.common.menu.MenuItem
import com.markettwits.profile.presentation.common.menu.SignInOrRegistryButton
import com.markettwits.profile.presentation.common.top_bar.ProfileTopBar


@Composable
fun UnAuthorizedProfileScreen(component: UnAuthorizedProfile) {
    val state by component.state.subscribeAsState()
    Column {
        when (state) {
            is ProfileUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = SportSouceColor.SportSouceBlue
                    )
                }
            }

            is ProfileUiState.Base -> {
                ProfileTopBar(userName = "")
                SignInOrRegistryButton {
                    component.signIn()
                }
                ProfileScreenContent(menu = unauthorizedProfileScreenMenu()) {
                }
            }

            is ProfileUiState.Error -> {

            }
        }

    }
}

private fun unauthorizedProfileScreenMenu() = listOf<MenuItem>(
    MenuItem.Header("Помощь"),
    MenuItem.Base("Написать в Telegram"),
    MenuItem.Base("Написать в WhatsApp"),
    MenuItem.Base("Карта магазина"),
    MenuItem.Base("О нас"),
)