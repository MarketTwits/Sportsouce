package com.markettwits.shop.cart.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cahce.InStorageListCache
import com.markettwits.cloud_shop.di.sportSauceShopCloudModule
import com.markettwits.profile.api.authDataSourceModule
import com.markettwits.shop.cart.data.cache.ShopCartCache
import com.markettwits.shop.cart.data.ShopCartRepositoryBase
import com.markettwits.shop.cart.domain.ShopCartRepository
import com.markettwits.shop.cart.presentation.cart.store.ShopCartStoreFactory
import com.markettwits.shop.cart.presentation.catalog.store.ShopCartCatalogStoreFactory
import com.markettwits.shop.cart.presentation.page.store.ShopCartPageStoreFactory
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
