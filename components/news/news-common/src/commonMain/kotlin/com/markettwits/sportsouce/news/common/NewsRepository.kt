package com.markettwits.sportsouce.news.common

import com.markettwits.sportsouce.news.common.model.NewsCategory
import com.markettwits.sportsouce.news.common.model.NewsHashtag
import com.markettwits.sportsouce.news.common.model.NewsItem

interface NewsRepository {

    suspend fun news(): Result<List<NewsItem>>

    suspend fun categories(): Result<List<NewsCategory>>

    suspend fun hashtags(): Result<List<NewsHashtag>>
}