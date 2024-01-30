package data.cache

import com.markettwits.random_user.RandomUser

interface CacheDataSource {
    suspend fun update(items : List<RandomUser>)
    suspend fun get() : List<RandomUser>
}