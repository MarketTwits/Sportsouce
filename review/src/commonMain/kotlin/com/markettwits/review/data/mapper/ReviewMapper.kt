package com.markettwits.review.data.mapper

import com.markettwits.news.cloud.model.NetworkNews
import com.markettwits.review.domain.Review
import com.markettwits.starts_common.domain.StartsListItem

interface ReviewMapper {

    fun map(actual: List<StartsListItem>, archive: List<StartsListItem>, news: NetworkNews): Review

}