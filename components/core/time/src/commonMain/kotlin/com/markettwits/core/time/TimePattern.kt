package com.markettwits.core.time

interface TimePattern {

    fun map(): String

    /** 20.05.2024 00:00 */
    object Full : TimePattern {
        override fun map() = "dd.MM.yyyy HH:mm"
    }

    /** 20 may 2024 */
    object FullWithEmptySpace : TimePattern {
        override fun map() = "dd MMMM yyyy"
    }

    /** 20.05.2024 */
    object FullWithDots : TimePattern {
        override fun map() = "dd.MM.yyyy"
    }

    /** 2024-07-31T17:00:00.000Z */
    object Remote : TimePattern {
        override fun map() = "yyyy-MM-dd'T'HH:mm:ss.SSSX"
    }
}