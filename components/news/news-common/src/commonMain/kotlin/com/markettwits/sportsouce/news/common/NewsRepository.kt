package com.markettwits.sportsouce.news.common

import com.markettwits.sportsouce.news.common.model.NewsCategory
import com.markettwits.sportsouce.news.common.model.NewsHashtag
import com.markettwits.sportsouce.news.common.model.NewsItem

interface NewsRepository {

    suspend fun newsItem(id: Int): Result<NewsItem>

    suspend fun news(
        categoryId: Int? = null,
        hashtag: String? = null,
        limit: Int = 20,
        offset: Int = 0,
    ): Result<List<NewsItem>>

    suspend fun categories(): Result<List<NewsCategory>>

    suspend fun hashtags(
        limit: Int = 20,
        offset: Int = 0,
    ): Result<List<NewsHashtag>>
}
