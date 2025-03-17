package com.markettwits.shop.orders.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cloud_shop.di.sportSauceShopCloudModule
import com.markettwits.crashlitics.api.di.crashlyticsModule
import com.markettwits.profile.api.authDataSourceModule
import com.markettwits.shop.orders.data.ShopUserOrdersRepositoryBase
import com.markettwits.shop.orders.data.mapper.ShopUserOrdersMapper
import com.markettwits.shop.orders.domain.ShopUserOrdersRepository
import com.markettwits.shop.orders.presentation.store.ShopUserOrdersStoreFactory
import com.markettwits.time.BaseTimeMapper
import com.markettwits.time.TimeMapper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val shopUserOrdersModule = module {
    includes(authDataSourceModule, sportSauceShopCloudModule,crashlyticsModule)

    singleOf(::ShopUserOrdersRepositoryBase) bind ShopUserOrdersRepository::class
    singleOf(::ShopUserOrdersStoreFactory)
    singleOf(::ShopUserOrdersMapper)
    singleOf(::BaseTimeMapper) bind TimeMapper::class
    singleOf(::DefaultStoreFactory) bind StoreFactory::class

}