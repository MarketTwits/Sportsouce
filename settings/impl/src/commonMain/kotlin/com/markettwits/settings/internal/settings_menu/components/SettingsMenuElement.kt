package com.markettwits.settings.internal.settings_menu.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.filled.Palette
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
    SettingsMenuElement.Base(
        id = 1,
        title = "Внешний вид",
        icon = Icons.Default.Palette
    ),
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
    SettingsMenuElement.Base(
        id = 7,
        title = "Проверить обновления",
        icon = Icons.Outlined.Error
    ),
)