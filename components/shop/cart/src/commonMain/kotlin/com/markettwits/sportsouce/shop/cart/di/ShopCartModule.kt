package com.markettwits.sportsouce.shop.cart.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cahce.InStorageListCache
import com.markettwits.sportsouce.auth.service.api.authDataSourceModule
import com.markettwits.sportsouce.shop.cart.data.ShopCartRepositoryBase
import com.markettwits.sportsouce.shop.cart.data.cache.ShopCartCache
import com.markettwits.sportsouce.shop.cart.domain.ShopCartRepository
import com.markettwits.sportsouce.shop.cart.presentation.cart.store.ShopCartStoreFactory
import com.markettwits.sportsouce.shop.cart.presentation.catalog.store.ShopCartCatalogStoreFactory
import com.markettwits.sportsouce.shop.cart.presentation.page.store.ShopCartPageStoreFactory
import com.markettwits.sportsouce.shop.cloud.di.sportSauceShopCloudModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val shopCartModule = module {
    includes(sportSauceShopCloudModule, authDataSourceModule)
    singleOf(::ShopCartRepositoryBase) bind ShopCartRepository::class
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    single { ShopCartCatalogStoreFactory(get()) }
    single { ShopCartCache } bind InStorageListCache::class
    single { ShopCartPageStoreFactory(get()) }
    single { ShopCartStoreFactory(get(),get()) }
}
