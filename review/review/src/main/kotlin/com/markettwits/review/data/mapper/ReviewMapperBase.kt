package com.markettwits.review.data.mapper

import com.markettwits.cloud.model.news.NewsRemote
import com.markettwits.cloud.model.starts.StartsRemote
import com.markettwits.news_list.data.NewsRemoteToDomainMapper
import com.markettwits.review.domain.Review
import com.markettwits.starts_common.data.StartsCloudToListMapper
import com.markettwits.starts_common.domain.StartsListItem

class ReviewMapperBase(
    private val startsMapper: StartsCloudToListMapper,
    private val newsMapper: NewsRemoteToDomainMapper
) : ReviewMapper {
    override fun map(actual: StartsRemote, archive: StartsRemote, news: NewsRemote): Review =
        Review(
            news = newsMapper.map(news),
            actualStarts = startsMapper.mapSingle(actual.rows),
            archiveStarts = sortArchive(archive)
        )

    private fun sortArchive(archive: StartsRemote): List<StartsListItem> {
        val items = startsMapper.mapSingle(archive.rows)
        return items.reversed()
//        val sortedList = items.sortedByDescending { it.statusCode.id }
//        val filteredList = sortedList.filter { it.statusCode.id == 6 }
//        return filteredList + sortedList.filter { it.statusCode.id != 6 }
    }
}