package com.markettwits.start_support.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.start_support.data.StartSupportRepository
import com.markettwits.start_support.data.StartSupportRepositoryBase
import com.markettwits.start_support.data.mapper.StartSupportMapper
import com.markettwits.start_support.data.mapper.StartSupportMapperBase
import com.markettwits.start_support.domain.StartSupportUseCase
import com.markettwits.start_support.domain.StartSupportUseCaseBase
import com.markettwits.start_support.presentation.store.StartSupportStoreFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val startSupportModule = module {
    includes(sportSouceNetworkModule)
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::StartSupportStoreFactory)
    singleOf(::StartSupportUseCaseBase) bind StartSupportUseCase::class
    singleOf(::StartSupportRepositoryBase) bind StartSupportRepository::class
    singleOf(::StartSupportMapperBase) bind StartSupportMapper::class
}
