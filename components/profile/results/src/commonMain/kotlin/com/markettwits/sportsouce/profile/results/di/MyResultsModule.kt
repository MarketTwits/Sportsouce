package com.markettwits.sportsouce.profile.results.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.core_cloud.provider.*
import com.markettwits.sportsouce.auth.service.api.authDataSourceModule
import com.markettwits.sportsouce.auth.service.api.AuthDataSource
import com.markettwits.sportsouce.profile.results.data.*
import com.markettwits.sportsouce.profile.results.domain.MyResultsRepository
import com.markettwits.sportsouce.profile.results.domain.ResultsActionsRepository
import com.markettwits.sportsouce.profile.results.presentation.store.MyResultsStoreFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val myResultsModule = module {
    includes(authDataSourceModule)
    single { MyResultsApi(HttpClientProviderBase(JsonProviderBase().provide(), SportSauceBaseUrl)) }
    single { ResultsActionsApi(HttpClientProviderBase(JsonProviderBase().provide(), SportSauceBaseUrl)) }
    single<ResultsActionsRepository> { ResultsActionsRepositoryBase(get(), get<AuthDataSource>()) }
    singleOf(::MyResultsRepositoryBase) bind MyResultsRepository::class
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::MyResultsStoreFactory)
}
