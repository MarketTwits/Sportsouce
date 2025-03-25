package com.markettwits.sportsouce.shop.item.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.intentActionModule
import com.markettwits.sportsouce.shop.cloud.di.sportSauceShopCloudModule
import com.markettwits.sportsouce.shop.item.data.ShopItemRepositoryBase
import com.markettwits.sportsouce.shop.item.data.cache.ShopItemLocalCache
import com.markettwits.sportsouce.shop.item.data.mapper.ShopPageItemMapper
import com.markettwits.sportsouce.shop.item.data.mapper.ShopPageItemMapperBase
import com.markettwits.sportsouce.shop.item.domain.ShopItemRepository
import com.markettwits.sportsouce.shop.item.presentation.store.ShopItemPageStoreFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val shopItemPageModule = module {
    includes(sportSauceShopCloudModule, intentActionModule)
    singleOf(::ShopItemRepositoryBase) bind ShopItemRepository::class
    singleOf(::ShopPageItemMapperBase) bind ShopPageItemMapper::class
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::ShopItemLocalCache)
    singleOf(::ShopItemPageStoreFactory)
}