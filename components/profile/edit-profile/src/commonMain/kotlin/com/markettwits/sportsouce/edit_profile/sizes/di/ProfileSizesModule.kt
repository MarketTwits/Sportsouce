package com.markettwits.sportsouce.edit_profile.sizes.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.sportsouce.auth.service.api.authDataSourceModule
import com.markettwits.sportsouce.edit_profile.sizes.data.ProfileSizesRepositoryBase
import com.markettwits.sportsouce.edit_profile.sizes.domain.ProfileSizesRepository
import com.markettwits.sportsouce.edit_profile.sizes.presentation.store.ProfileSizesStoreFactory
import com.markettwits.sportsouce.profile.cloud.di.sportSauceNetworkProfileModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val profileSizesModule = module {
    includes(sportSauceNetworkProfileModule, authDataSourceModule)
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::ProfileSizesRepositoryBase) bind ProfileSizesRepository::class
    singleOf(::ProfileSizesStoreFactory)
}
