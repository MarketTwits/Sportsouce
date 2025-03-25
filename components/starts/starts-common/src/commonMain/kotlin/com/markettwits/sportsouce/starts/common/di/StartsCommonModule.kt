package com.markettwits.sportsouce.starts.common.di

import com.markettwits.core.time.BaseTimeMapper
import com.markettwits.core.time.TimeMapper
import com.markettwits.sportsouce.starts.cloud.di.sportSauceStartsCloudModule
import com.markettwits.sportsouce.starts.common.data.SportSauceStartsApiBase
import com.markettwits.sportsouce.starts.common.data.mapper.StartsCloudToListMapper
import com.markettwits.sportsouce.starts.common.data.mapper.StartsCloudToListMapperBase
import com.markettwits.sportsouce.starts.common.domain.SportSauceStartsApi
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val startsCommonModule = module {
    includes(sportSauceStartsCloudModule)
    singleOf(::SportSauceStartsApiBase) bind SportSauceStartsApi::class
    singleOf(::StartsCloudToListMapperBase) bind StartsCloudToListMapper::class
    singleOf(::BaseTimeMapper) bind TimeMapper::class
}