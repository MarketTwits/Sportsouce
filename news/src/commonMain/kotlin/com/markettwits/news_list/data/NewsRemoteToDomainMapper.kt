package com.markettwits.news_list.data

import com.markettwits.cloud.model.news.NewsRemote
import com.markettwits.news_list.domain.Hashtag
import com.markettwits.news_list.domain.NewsInfo
import com.markettwits.time.TimeMapper
import com.markettwits.time.TimePattern

interface NewsRemoteToDomainMapper {
    fun map(news: NewsRemote): List<NewsInfo>
    class Base(
        private val timeMapper: TimeMapper
    ) : NewsRemoteToDomainMapper {
        override fun map(news: NewsRemote): List<NewsInfo> {
            return news.rows.map {
                NewsInfo(
                    id = it.id,
                    title = it.title,
                    shortDescription = it.short_description,
                    fullDescription = it.full_description,
                    imageUrl = if (it.images.isNotEmpty()) it.images[0].fullPath else "",
                    createData = timeMapper.mapTime(TimePattern.FullWithEmptySpace, it.createdAt),
                    hashtags = it.hashtags.map {
                        Hashtag(
                            id = it.id,
                            name = it.name
                        )
                    }
                )
            }
        }
    }
}