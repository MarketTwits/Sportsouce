package com.markettwits.sportsouce.start.data.start.mapper.review

import com.markettwits.sportsouce.start.domain.StartItem
import kotlin.math.roundToInt

class StartReviewToUiMapperBase : StartReviewToUiMapper {

    override fun map(
        averageLocationScore: String?,
        averageReviewScore: String?,
        averageRoadScore: String?,
        averageTeamScore: String?,
    ): StartItem.ReviewState {
        val review = averageReviewScore.toFloatReview()

        if (review == 0f) {
            return StartItem.ReviewState.NoReviews
        }

        if (review < 2f) {
            return StartItem.ReviewState.LowRating
        }

        return StartItem.ReviewState.Rated(
            StartItem.ReviewScores(
                review = review,
                location = averageLocationScore.toIntScore(),
                road = averageRoadScore.toIntScore(),
                team = averageTeamScore.toIntScore(),
            )
        )
    }

    private fun String?.toFloatReview(): Float =
        this
            ?.toFloatOrNull()
            ?.coerceIn(0f, 5f)
            ?: 0f

    private fun String?.toIntScore(): Int =
        this
            ?.toFloatOrNull()
            ?.roundToInt()
            ?.coerceIn(0, 5)
            ?: 0
}