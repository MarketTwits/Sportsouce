package com.markettwits.sportsouce.shop.filter.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.sportsouce.shop.cloud.di.sportSauceShopCloudModule
import com.markettwits.sportsouce.shop.filter.data.ShopFilterRepositoryBase
import com.markettwits.sportsouce.shop.filter.data.mapper.ShopFilterMapper
import com.markettwits.sportsouce.shop.filter.data.mapper.ShopFilterMapperBase
import com.markettwits.sportsouce.shop.filter.data.mapper.ShopProductsCategoriesMapper
import com.markettwits.sportsouce.shop.filter.data.mapper.ShopProductsCategoriesMapperBase
import com.markettwits.sportsouce.shop.filter.domain.ShopFilterRepository
import com.markettwits.sportsouce.shop.filter.presentation.store.ShopFilterStore
import com.markettwits.sportsouce.shop.filter.presentation.store.ShopFilterStoreFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val shopFilterModule = module {
    includes(sportSauceShopCloudModule)
    singleOf(::ShopProductsCategoriesMapperBase) bind ShopProductsCategoriesMapper::class
    singleOf(::ShopFilterRepositoryBase) bind ShopFilterRepository::class
    singleOf(::ShopFilterMapperBase) bind ShopFilterMapper::class
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::ShopFilterStoreFactory)
    single<ShopFilterStore> {
        ShopFilterStoreFactory(DefaultStoreFactory(), get()).create()
    }
}