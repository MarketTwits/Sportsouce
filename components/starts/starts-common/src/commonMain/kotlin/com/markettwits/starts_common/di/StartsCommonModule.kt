package com.markettwits.starts_common.di

import com.markettwits.cloud.di.sportSauceStartsCloudModule
import com.markettwits.starts_common.data.SportSauceStartsApiBase
import com.markettwits.starts_common.data.mapper.StartsCloudToListMapper
import com.markettwits.starts_common.data.mapper.StartsCloudToListMapperBase
import com.markettwits.starts_common.domain.SportSauceStartsApi
import com.markettwits.time.BaseTimeMapper
import com.markettwits.time.TimeMapper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val startsCommonModule = module {
    includes(sportSauceStartsCloudModule)
    singleOf(::SportSauceStartsApiBase) bind SportSauceStartsApi::class
    singleOf(::StartsCloudToListMapperBase) bind StartsCloudToListMapper::class
    singleOf(::BaseTimeMapper) bind TimeMapper::class
}