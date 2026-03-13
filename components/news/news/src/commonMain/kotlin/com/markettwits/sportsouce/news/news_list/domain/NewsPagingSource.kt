package com.markettwits.sportsouce.news.news_list.domain

import com.markettwits.core.paging.OffsetAndLimitPagingSourceNew
import com.markettwits.sportsouce.news.common.NewsRepository
import com.markettwits.sportsouce.news.common.model.NewsItem

class NewsPagingSource(
    private val repository: NewsRepository,
    private val categoryId: Int?,
    private val hashtag: String?,
    pageSize: Int,
) : OffsetAndLimitPagingSourceNew<NewsItem>(pageSize = pageSize) {

    override suspend fun getTotalCount(): Int = 0

    override suspend fun load(offset: Int, limit: Int): List<NewsItem> {
        return repository.news(
            categoryId = categoryId,
            hashtag = hashtag,
            limit = limit,
            offset = offset
        ).getOrElse {
            throw it
        }
    }
}
