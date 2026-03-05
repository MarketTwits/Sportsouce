package com.markettwits.sportsouce.news.news_list.domain

import com.markettwits.core.paging.OffsetAndLimitPagingSourceNew
import com.markettwits.sportsouce.news.common.NewsRepository
import com.markettwits.sportsouce.news.common.model.NewsHashtag

class NewsHashtagsPagingSource(
    private val repository: NewsRepository,
    pageSize: Int,
) : OffsetAndLimitPagingSourceNew<NewsHashtag>(pageSize = pageSize) {

    override suspend fun getTotalCount(): Int = 0

    override suspend fun load(offset: Int, limit: Int): List<NewsHashtag> {
        return repository.hashtags(
            limit = limit,
            offset = offset
        ).getOrElse {
            throw it
        }
    }
}
