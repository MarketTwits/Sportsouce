package com.markettwits.sportsouce.shop.order.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.intentActionModule
import com.markettwits.sportsouce.auth.service.api.authDataSourceModule
import com.markettwits.sportsouce.shop.cart.di.shopCartModule
import com.markettwits.sportsouce.shop.cloud.di.sportSauceShopCloudModule
import com.markettwits.sportsouce.shop.order.data.ShopOrderRepositoryBase
import com.markettwits.sportsouce.shop.order.domain.ShopOrderRepository
import com.markettwits.sportsouce.shop.order.presentation.store.ShopCreateOrderStoreFactory
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