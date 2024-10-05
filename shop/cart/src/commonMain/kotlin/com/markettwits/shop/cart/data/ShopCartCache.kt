package com.markettwits.shop.cart.data

import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageListCache
import com.markettwits.cahce.InStorageSingleCache
import com.markettwits.cahce.store_wrapper.listStoreOfWrapper
import com.markettwits.cahce.store_wrapper.storeOfWrapper
import com.markettwits.shop.cart.domain.ShopItemCart

internal object ShopCartCache : InStorageListCache<ShopItemCart>(
    listStoreOfWrapper(
        path = InStorageCacheDirectory.path,
        fileName = "shopCart"
    )
)