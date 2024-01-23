package com.markettwits.profile.presentation.common.menu

sealed class MenuItem(val title : String) {
    class Base(title: String) : MenuItem(title)
    class Header(title: String) : MenuItem(title)
}
fun profileMenu() = listOf<MenuItem>(
    MenuItem.Header("Профиль"),
    MenuItem.Base("Редактировать"),
    MenuItem.Base("Сменить пароль"),
    MenuItem.Header("Старты"),
    MenuItem.Base("Мои регистрации"),
  //  MenuItem.Base("Другие регистрации"),
  //  MenuItem.Base("Участники"),
)