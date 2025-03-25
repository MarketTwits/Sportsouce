package com.markettwits.sportsouce.start.support.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.intentActionModule
import com.markettwits.sportsouce.start.cloud.di.sportSauceStartNetworkModule
import com.markettwits.sportsouce.start.support.data.StartSupportRepository
import com.markettwits.sportsouce.start.support.data.StartSupportRepositoryBase
import com.markettwits.sportsouce.start.support.data.mapper.StartSupportMapper
import com.markettwits.sportsouce.start.support.data.mapper.StartSupportMapperBase
import com.markettwits.sportsouce.start.support.domain.StartSupportUseCase
import com.markettwits.sportsouce.start.support.domain.StartSupportUseCaseBase
import com.markettwits.sportsouce.start.support.presentation.store.StartSupportStoreFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val startSupportModule = module {
    includes(sportSauceStartNetworkModule, intentActionModule)
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::StartSupportStoreFactory)
    singleOf(::StartSupportUseCaseBase) bind StartSupportUseCase::class
    singleOf(::StartSupportRepositoryBase) bind StartSupportRepository::class
    singleOf(::StartSupportMapperBase) bind StartSupportMapper::class
}
