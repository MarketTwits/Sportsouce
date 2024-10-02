package com.markettwits.shop.catalog.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cloud_shop.di.sportSauceShopCloudModule
import com.markettwits.shop.catalog.data.ShopCatalogRepositoryBase
import com.markettwits.shop.catalog.domain.ShopCatalogRepository
import com.markettwits.shop.catalog.domain.mapper.ShopProductsMapper
import com.markettwits.shop.catalog.domain.mapper.ShopProductsMapperBase
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStoreFactory
import com.markettwits.shop.filter.domain.mapper.ShopProductsCategoriesMapper
import com.markettwits.shop.filter.domain.mapper.ShopProductsCategoriesMapperBase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val shopCatalogModule = module {
    includes(sportSauceShopCloudModule)
    singleOf(::ShopCatalogRepositoryBase) bind ShopCatalogRepository::class
    singleOf(::ShopProductsMapperBase) bind ShopProductsMapper::class
    singleOf(::ShopProductsCategoriesMapperBase) bind ShopProductsCategoriesMapper::class
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::ShopCatalogStoreFactory)
}