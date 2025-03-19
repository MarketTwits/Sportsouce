package com.markettwits.news_list.data

import com.markettwits.news_list.domain.NewsInfo

interface NewsDataSource {
    suspend fun news() : Result<List<NewsInfo>>
}