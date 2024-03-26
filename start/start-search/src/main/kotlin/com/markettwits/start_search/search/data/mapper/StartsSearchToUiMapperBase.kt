package com.markettwits.start_search.search.data.mapper

import com.markettwits.cloud.model.starts.StartsRemote
import com.markettwits.start_search.search.domain.StartsSearch
import com.markettwits.starts_common.data.mapper.StartsCloudToListMapper

class StartsSearchToUiMapperBase(
    private val startsMapper: StartsCloudToListMapper
) : StartsSearchToUiMapper {
    override fun map(searches: List<String>, start: StartsRemote): StartsSearch =
        StartsSearch(searches, startsMapper.mapSingle(start.rows))

    override fun map(value: String): Map<String, String> =
        mapOf("filterText" to value)
}