package com.markettwits.sportsouce.shop.cart.data.cache

import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageListCache
import com.markettwits.cahce.store_wrapper.listStoreOfWrapper
import com.markettwits.sportsouce.shop.cart.domain.ShopItemCart

private const val FILE_NAME = "sportsouce.shop.cart"

internal object ShopCartCache : InStorageListCache<ShopItemCart>(
    listStoreOfWrapper(
        path = InStorageCacheDirectory.path,
        fileName = FILE_NAME
    )
)