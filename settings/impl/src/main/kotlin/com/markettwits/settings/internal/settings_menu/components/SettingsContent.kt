package com.markettwits.settings.internal.settings_menu.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun SettingsContent(
    modifier: Modifier = Modifier,
    applicationsMenu: List<SettingsMenuElement>,
    socialMenu: List<SettingsMenuElement>,
    onClickMenu: (SettingsMenuElement) -> Unit
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(10.dp)
    ) {
        SettingsMenuList(menuItems = applicationsMenu, onClick = {
            onClickMenu(it)
        })
        Spacer(modifier = Modifier.padding(20.dp))
        SettingsMenuList(menuItems = socialMenu, onClick = {
            onClickMenu(it)
        })
        Spacer(modifier = Modifier.padding(20.dp))
        SettingsApplicationVersionElement()
    }
}