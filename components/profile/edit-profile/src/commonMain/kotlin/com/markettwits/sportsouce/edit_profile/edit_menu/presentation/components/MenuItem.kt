package com.markettwits.sportsouce.edit_profile.edit_menu.presentation.components

sealed class MenuItem(
    val title: String,
    val isDangerZone : Boolean
) {
    class Base(title: String,isDangerZone : Boolean) : MenuItem(title, isDangerZone)
    class Header(title: String) : MenuItem(title,false)
}

fun profileMenu() = listOf<MenuItem>(
    MenuItem.Header("Профиль"),
    MenuItem.Base("Изменить фото профиля", false),
    MenuItem.Base("Изменить статус",false),
    MenuItem.Base("Изменить учетную запись",false),
    MenuItem.Base("Мои социальные сети",false),
)

fun securityMenu() = listOf(
    MenuItem.Header("Безопасность"),
    MenuItem.Base("Сменить пароль",false),
    MenuItem.Base("Выйти из аккаунта",true),
)