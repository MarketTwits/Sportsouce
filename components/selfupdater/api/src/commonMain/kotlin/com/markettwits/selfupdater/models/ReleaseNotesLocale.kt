package com.markettwits.selfupdater.models

/** Follow the app's content language, independently of the OS/browser and GitHub's public body. */
object ReleaseNotesLocale {
    const val Default = "ru-RU"

    fun isRussian(locale: String): Boolean =
        locale.lowercase().substringBefore('-').substringBefore('_') == "ru"

    fun unavailable(locale: String = Default): String = if (isRussian(locale))
        "Описание изменений на русском языке временно недоступно. Обновление доступно."
    else "Release notes are temporarily unavailable. The update is still available."
}
