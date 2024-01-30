package data.cache

import com.markettwits.random_user.RandomUser
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.extensions.getOrEmpty

class CacheDataSourceBase(private val cache: KStore<List<RandomUser>>) : CacheDataSource {
    override suspend fun update(items: List<RandomUser>){
        cache.set(items)
    }
    override suspend fun get(): List<RandomUser> =  cache.getOrEmpty()
}