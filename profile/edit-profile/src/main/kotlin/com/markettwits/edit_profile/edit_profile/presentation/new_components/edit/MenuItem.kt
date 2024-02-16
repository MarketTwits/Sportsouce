package com.markettwits.edit_profile.edit_profile.presentation.new_components.edit

sealed class MenuItem(val title: String) {
    class Base(title: String) : MenuItem(title)
    class Header(title: String) : MenuItem(title)
}

fun profileMenu() = listOf<MenuItem>(
    MenuItem.Header("Профиль"),
    MenuItem.Base("Редактировать"),
    MenuItem.Base("Сменить пароль"),
    MenuItem.Header("Старты"),
    MenuItem.Base("Мои регистрации"),
)

fun securityMenu() = listOf(
    MenuItem.Header("Безопасность"),
    MenuItem.Base("Сменить пароль"),
    MenuItem.Base("Выйти из аккаунта"),
)