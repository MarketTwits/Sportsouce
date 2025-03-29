package com.markettwits.sportsouce.edit_profile.edit_menu.presentation.components

sealed class MenuItem(
    val title: String
) {
    class Base(title: String) : MenuItem(title)
    class Header(title: String) : MenuItem(title)
}

fun profileMenu() = listOf<MenuItem>(
    MenuItem.Header("Профиль"),
    MenuItem.Base("Изменить фото профиля"),
    MenuItem.Base("Изменить статус"),
    MenuItem.Base("Изменить учетную запись"),
    MenuItem.Base("Мои социальные сети"),
)

fun securityMenu() = listOf(
    MenuItem.Header("Безопасность"),
    MenuItem.Base("Сменить пароль"),
    MenuItem.Base("Выйти из аккаунта"),
)