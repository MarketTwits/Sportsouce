package com.markettwits.sportsouce.shop.catalog.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.sportsouce.shop.cloud.di.sportSauceShopCloudModule
import com.markettwits.sportsouce.shop.catalog.data.ShopCatalogRepositoryBase
import com.markettwits.sportsouce.shop.catalog.domain.ShopCatalogRepository
import com.markettwits.sportsouce.shop.catalog.presentation.store.ShopCatalogStoreFactory
import com.markettwits.sportsouce.shop.domain.mapper.ShopProductsMapper
import com.markettwits.sportsouce.shop.domain.mapper.ShopProductsMapperBase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val shopCatalogModule = module {
    includes(sportSauceShopCloudModule)
    singleOf(::ShopCatalogRepositoryBase) bind ShopCatalogRepository::class
    singleOf(::ShopProductsMapperBase) bind ShopProductsMapper::class
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::ShopCatalogStoreFactory)
}