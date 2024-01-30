package data

import com.markettwits.random_user.RandomUser
import com.markettwits.random_user.api.service.ContactsServiceApi
import data.cache.CacheDataSource
import data.mapper.remote.RandomUsersRemoteToDomainMapper

class RandomListUserRepositoryImpl(
    private val cacheService: CacheDataSource,
    private val remoteService: ContactsServiceApi,
    private val remoteToDomainMapper: RandomUsersRemoteToDomainMapper,
) : RandomListUserRepository {
    override suspend fun randomUser(forced: Boolean): Result<List<RandomUser>> {
        val result = runCatching {
            if (forced)
                launch()
            else
                cacheService.get().ifEmpty { launch() }
        }.onSuccess { new ->
            cacheService.update(new)
        }
        val cache = cacheService.get()
        return if (result.isFailure && cache.isNotEmpty()
        ) Result.success(cache) else
            result
    }

    override suspend fun launch(): List<RandomUser> {
        val response = remoteService.contacts()
        return remoteToDomainMapper.map(response)
    }
}