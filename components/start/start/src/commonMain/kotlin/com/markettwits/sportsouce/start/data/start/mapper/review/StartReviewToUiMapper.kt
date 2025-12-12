package com.markettwits.sportsouce.start.data.start.mapper.review

import com.markettwits.sportsouce.start.domain.StartItem

interface StartReviewToUiMapper {
    fun map(
        averageLocationScore: String? = null,
        averageReviewScore: String? = null,
        averageRoadScore: String? = null,
        averageTeamScore: String? = null,
    ): StartItem.ReviewState
}