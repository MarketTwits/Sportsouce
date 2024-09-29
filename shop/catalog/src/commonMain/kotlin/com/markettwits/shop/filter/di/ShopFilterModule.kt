package com.markettwits.shop.filter.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cloud_shop.di.sportSauceShopCloudModule
import com.markettwits.shop.catalog.domain.mapper.ShopProductsCategoriesMapper
import com.markettwits.shop.catalog.domain.mapper.ShopProductsCategoriesMapperBase
import com.markettwits.shop.catalog.domain.mapper.ShopProductsMapper
import com.markettwits.shop.catalog.domain.mapper.ShopProductsMapperBase
import com.markettwits.shop.filter.data.ShopFilterRepositoryBase
import com.markettwits.shop.filter.domain.ShopFilterRepository
import com.markettwits.shop.filter.domain.mapper.ShopFilterMapper
import com.markettwits.shop.filter.domain.mapper.ShopFilterMapperBase
import com.markettwits.shop.filter.presentation.store.ShopFilterStoreFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val shopFilterModule = module {
    includes(sportSauceShopCloudModule)
    singleOf(::ShopProductsCategoriesMapperBase) bind ShopProductsCategoriesMapper::class
    singleOf(::ShopFilterRepositoryBase) bind ShopFilterRepository::class
    singleOf(::ShopFilterMapperBase) bind ShopFilterMapper::class
    singleOf(::ShopProductsMapperBase) bind ShopProductsMapper::class
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::ShopFilterStoreFactory)
}