package com.markettwits.shop.item.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cloud_shop.di.sportSauceShopCloudModule
import com.markettwits.shop.item.data.ShopItemRepositoryBase
import com.markettwits.shop.item.domain.ShopItemRepository
import com.markettwits.shop.item.domain.mapper.ShopPageItemMapper
import com.markettwits.shop.item.domain.mapper.ShopPageItemMapperBase
import com.markettwits.shop.item.presentation.store.ShopItemPageStoreFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val shopItemPageModule = module {
    includes(sportSauceShopCloudModule)
    singleOf(::ShopItemRepositoryBase) bind ShopItemRepository::class
    singleOf(::ShopPageItemMapperBase) bind ShopPageItemMapper::class
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::ShopItemPageStoreFactory)
}