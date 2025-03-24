package com.markettwits.news.common

import com.markettwits.news.common.model.NewsCategory
import com.markettwits.news.common.model.NewsHashtag
import com.markettwits.news.common.model.NewsItem

interface NewsRepository {

    suspend fun news(): Result<List<NewsItem>>

    suspend fun categories(): Result<List<NewsCategory>>

    suspend fun hashtags(): Result<List<NewsHashtag>>
}