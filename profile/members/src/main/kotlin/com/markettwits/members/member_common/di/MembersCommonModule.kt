package com.markettwits.members.member_common.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.core_ui.time.BaseTimeMapper
import com.markettwits.core_ui.time.TimeMapper
import com.markettwits.members.member_common.data.ProfileMembersRepository
import com.markettwits.members.member_common.data.ProfileMembersRepositoryBase
import com.markettwits.members.member_common.data.mapper.MembersMapper
import com.markettwits.members.member_common.data.mapper.MembersMapperBase
import com.markettwits.profile.api.authDataSourceModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val membersCommonModule = module {
    includes(sportSouceNetworkModule, authDataSourceModule)
    singleOf(::BaseTimeMapper) bind TimeMapper::class
    singleOf(::MembersMapperBase) bind MembersMapper::class
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::ProfileMembersRepositoryBase) bind ProfileMembersRepository::class
}