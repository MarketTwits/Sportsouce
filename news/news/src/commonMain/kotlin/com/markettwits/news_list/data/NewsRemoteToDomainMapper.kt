package com.markettwits.news_list.data

import com.markettwits.news.cloud.model.NetworkNews
import com.markettwits.news_list.domain.Hashtag
import com.markettwits.news_list.domain.NewsInfo
import com.markettwits.time.TimeMapper
import com.markettwits.time.TimePattern

interface NewsRemoteToDomainMapper {
    fun map(news: NetworkNews): List<NewsInfo>
    class Base(
        private val timeMapper: TimeMapper
    ) : NewsRemoteToDomainMapper {
        override fun map(news: NetworkNews): List<NewsInfo> {
            return news.rows.map {
                val imageUrl = it.mainImage?.fullPath ?: it.images.firstOrNull()?.fullPath ?: ""
                NewsInfo(
                    id = it.id,
                    title = it.title,
                    shortDescription = it.shortDescription,
                    fullDescription = it.fullDescription,
                    imageUrl = imageUrl,
                    createData = timeMapper.mapTime(TimePattern.FullWithEmptySpace, it.createdAt),
                    hashtags = it.hashtags.map { hashTag ->
                        Hashtag(
                            id = hashTag.id,
                            name = hashTag.name
                        )
                    }
                )
            }
        }
    }
}