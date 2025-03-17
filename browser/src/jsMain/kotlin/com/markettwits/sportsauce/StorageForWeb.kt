package com.markettwits.sportsauce

import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageFileDirectory

fun InitStorageForWeb() {
    InStorageCacheDirectory.path = "data/com.markettwits.sportsauce"
    InStorageFileDirectory.path = "cache/com.markettwits.sportsauce"
}