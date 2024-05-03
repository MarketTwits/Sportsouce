package com.markettwits.core_ui.items.exception

import androidx.compose.ui.graphics.vector.ImageVector

sealed interface ExceptionAction {

    val title: String
    val message: String
    val icon: ImageVector

    class Network(
        override val title: String = "Проблема с интернет соединением",
        override val message: String,
        override val icon: ImageVector
    ) : ExceptionAction

    class Server(
        override val title: String = "Проблема с установки соединения с сервисом",
        override val message: String,
        override val icon: ImageVector
    ) : ExceptionAction

    class Unknown(
        override val title: String = "Что-то пошло не так",
        override val message: String = "Техническая проблема",
        override val icon: ImageVector
    ) : ExceptionAction
}