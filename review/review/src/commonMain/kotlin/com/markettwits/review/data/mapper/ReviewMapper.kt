package com.markettwits.review.data.mapper

import com.markettwits.cloud.model.news.NewsRemote
import com.markettwits.cloud.model.starts.StartsRemote
import com.markettwits.review.domain.Review

interface ReviewMapper {

    fun map(actual: StartsRemote, archive: StartsRemote, news: NewsRemote): Review

}