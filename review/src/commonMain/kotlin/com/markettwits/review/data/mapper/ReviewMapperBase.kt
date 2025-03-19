package com.markettwits.review.data.mapper

import com.markettwits.news.cloud.model.NetworkNews
import com.markettwits.news_list.data.NewsRemoteToDomainMapper
import com.markettwits.review.domain.Review
import com.markettwits.starts_common.domain.StartsListItem

internal class ReviewMapperBase(
    private val newsMapper: NewsRemoteToDomainMapper
) : ReviewMapper {
    override fun map(actual: List<StartsListItem>, archive: List<StartsListItem>, news: NetworkNews): Review =
        Review(
            news = newsMapper.map(news),
            actualStarts = actual,
            archiveStarts = archive.reversed()
        )
}