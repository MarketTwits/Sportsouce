package di

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.core_ui.dispathcers.DispatchersList
import com.markettwits.random_user.api.service.ContactsServiceApi
import com.markettwits.random_user.di.randomUserNetworkModule
import com.markettwits.random_user.impl.provider.HttpClientProviderImpl
import com.markettwits.random_user.impl.provider.JsonProviderImpl
import com.markettwits.random_user.impl.service.ContactsServiceApiImpl
import data.RandomListUserRepository
import data.RandomListUserRepositoryImpl
import data.cache.CacheDataSource
import data.cache.CacheDataSourceBase
import data.cache.appStorage
import data.mapper.remote.RandomUsersRemoteToDomainMapper
import data.mapper.remote.RandomUsersRemoteToDomainMapperImpl
import data.mapper.time.TimeMapper
import data.mapper.time.TimeMapperBase
import io.github.xxfast.kstore.file.extensions.listStoreOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import okio.Path.Companion.toPath
import org.koin.dsl.module
import presentation.store.ListStoreStoreFactory

val listModule = module {
    includes(randomUserNetworkModule)
    single<CoroutineScope> {
        CoroutineScope(DispatchersList.Base().main())
    }
    single<ListStoreStoreFactory> {
        ListStoreStoreFactory(DefaultStoreFactory())
    }
    single<RandomListUserRepository> {
        RandomListUserRepositoryImpl(
            remoteService = ContactsServiceApiImpl(
                HttpClientProviderImpl(
                    JsonProviderImpl().get(),
                    ContactsServiceApi.RANDOM_USER_BASE_URL
                )
            ),
            remoteToDomainMapper = get(),
            cacheService = get()
        )
    }
    single<CacheDataSource> {
        CacheDataSourceBase(
            cache = listStoreOf("$appStorage/saved.json".toPath())
        )
    }
    single<TimeMapper>{
        TimeMapperBase()
    }
    single<RandomUsersRemoteToDomainMapper> {
        RandomUsersRemoteToDomainMapperImpl(
            timeMapper = get()
        )
    }


}