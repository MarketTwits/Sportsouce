package com.markettwits.sportsouce.starts.common.presentation

/**
 * @param 0 - Start close
 * @param 1 - The start has been postponed
 * @param 2 - Registration will start soon
 * @param 3 - Registration is open
 * @param 4 - Registration is closed, wait for the start
 * @param 5 - The start is taking place
 * @param 6 - Start ended
 */
fun startStatusCompactMessage(statusCode : Int) : String =
    when (statusCode) {
        1 -> "Отложен"
        2 -> "Скоро"
        3 -> "Регистрация"
        4 -> "Ожидание старта"
        5 -> "Проходит"
        6 -> "Завершен"
        else -> "Отложен"
    }

fun startStatusMessage(statusCode: Int): String =
    when (statusCode) {
        1 -> "Временно отложен"
        2 -> "Регистрация скоро начнется"
        3 -> "Регистрация открыта"
        4 -> "Регистрация закрыта, ожидайте начало старта"
        5 -> "Старт проходит"
        6 -> "Старт завершен"
        else -> "Временно отложен"
    }