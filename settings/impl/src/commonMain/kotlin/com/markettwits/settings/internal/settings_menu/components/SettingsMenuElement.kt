package com.markettwits.settings.internal.settings_menu.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.Gite
import androidx.compose.material.icons.outlined.NightsStay
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.ImageVector

@Stable
sealed interface SettingsMenuElement {
    val id: Int
    val title: String
    val icon: ImageVector

    @Stable
    data class Base(
        override val id: Int,
        override val title: String,
        override val icon: ImageVector
    ) : SettingsMenuElement

    @Stable
    data class WithDescription(
        val description: String,
        override val id: Int,
        override val title: String,
        override val icon: ImageVector
    ) : SettingsMenuElement
}

internal fun defaultApplicationElements(
    isDarkTheme: Boolean
): List<SettingsMenuElement> = listOf(
    SettingsMenuElement.WithDescription(
        description = if (isDarkTheme) "Включена" else "Выключена",
        id = 0,
        title = "Темная тема",
        icon = Icons.Outlined.NightsStay
    ),
//    SettingsMenuElement.Base(
//        id = 1,
//        title = "Внешний вид",
//        icon = Icons.Outlined.Palette
//    ),
//    SettingsMenuElement.Base(
//        id = 2,
//        title = "Управление данными",
//        icon = Icons.Outlined.DataUsage
//    ),
//    SettingsMenuElement.Base(
//        id = 3,
//        title = "Уведомления",
//        icon = Icons.Outlined.Notifications
//    ),
)

internal fun defaultSocialElements(): List<SettingsMenuElement> = listOf(
    SettingsMenuElement.Base(
        id = 4,
        title = "Сообщество",
        icon = Icons.AutoMirrored.Outlined.Message
    ),
    SettingsMenuElement.Base(
        id = 5,
        title = "GitHub",
        icon = Icons.Outlined.Gite
    ),
//    SettingsMenuElement.Base(
//        id = 6,
//        title = "Сообщить об ошибке",
//        icon = Icons.Outlined.BugReport
//    ),
    SettingsMenuElement.Base(
        id = 7,
        title = "Проверить обновления",
        icon = Icons.Outlined.Error
    ),
)