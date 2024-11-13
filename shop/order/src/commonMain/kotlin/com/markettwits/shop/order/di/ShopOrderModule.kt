package com.markettwits.shop.order.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cloud_shop.di.sportSauceShopCloudModule
import com.markettwits.intentActionModule
import com.markettwits.profile.api.authDataSourceModule
import com.markettwits.shop.cart.di.shopCartModule
import com.markettwits.shop.order.data.ShopOrderRepositoryBase
import com.markettwits.shop.order.domain.ShopOrderRepository
import com.markettwits.shop.order.presentation.store.ShopCreateOrderStoreFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val shopOrderModule = module {
    includes(
        authDataSourceModule,
        sportSauceShopCloudModule,
        shopCartModule,
        intentActionModule,
    )
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::ShopOrderRepositoryBase) bind ShopOrderRepository::class
    singleOf(::ShopCreateOrderStoreFactory)
}