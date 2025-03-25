package com.markettwits.sportsouce.shop.orders.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.core.time.BaseTimeMapper
import com.markettwits.core.time.TimeMapper
import com.markettwits.crashlitics.api.di.crashlyticsModule
import com.markettwits.sportsouce.auth.service.api.authDataSourceModule
import com.markettwits.sportsouce.shop.cloud.di.sportSauceShopCloudModule
import com.markettwits.sportsouce.shop.orders.data.ShopUserOrdersRepositoryBase
import com.markettwits.sportsouce.shop.orders.data.mapper.ShopUserOrdersMapper
import com.markettwits.sportsouce.shop.orders.domain.ShopUserOrdersRepository
import com.markettwits.sportsouce.shop.orders.presentation.store.ShopUserOrdersStoreFactory
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