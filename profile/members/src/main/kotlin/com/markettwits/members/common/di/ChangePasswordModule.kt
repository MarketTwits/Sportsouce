package com.markettwits.members.common.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.core_ui.time.BaseTimeMapper
import com.markettwits.core_ui.time.TimeMapper
import com.markettwits.members.common.data.ProfileMembersRepositoryBase
import com.markettwits.members.common.data.mapper.MembersMapper
import com.markettwits.members.common.data.mapper.MembersMapperBase
import com.markettwits.members.members_list.domain.MembersListUseCaseBase
import com.markettwits.members.members_list.presentation.store.store.MembersListStoreFactory
import com.markettwits.profile.di.authDataSourceModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val membersModule = module {
    includes(sportSouceNetworkModule, authDataSourceModule)

    singleOf(::BaseTimeMapper) bind TimeMapper::class
    singleOf(::MembersMapperBase) bind MembersMapper::class
    singleOf(::DefaultStoreFactory) bind StoreFactory::class

    single<MembersListStoreFactory> {
        MembersListStoreFactory(
            storeFactory = get(),
            useCase = MembersListUseCaseBase(
                ProfileMembersRepositoryBase(
                    service = get(),
                    auth = get(),
                    mapper = get()
                )
            )
        )
    }


}