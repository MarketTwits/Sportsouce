package com.markettwits.sportsauce.deeplink.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface Deeplink {

    @Serializable
    sealed interface SportSauce : Deeplink

    // Starts-specific deeplinks
    @Serializable
    sealed interface Starts : SportSauce {
        @Serializable
        data object StartsList : Starts

        @Serializable
        data class StartDetail(
            val startId: Int? = null,
            val startSlug: String? = null
        ) : Starts
    }

    // News-specific deeplinks
    @Serializable
    sealed interface News : SportSauce {
        @Serializable
        data class NewsDetail(
            val newsId: Int
        ) : News
    }

    // Clubs-specific deeplinks
    @Serializable
    sealed interface Clubs : SportSauce {
        @Serializable
        data object ClubsList : Clubs
    }
}
