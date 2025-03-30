package com.markettwits.sportsouce.profile.members.member_common.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cahce.Cache
import com.markettwits.cahce.execute.list.ExecuteListWithCache
import com.markettwits.cahce.execute.list.ExecuteListWithCacheBase
import com.markettwits.core.time.BaseTimeMapper
import com.markettwits.core.time.TimeMapper
import com.markettwits.sportsouce.auth.service.api.authDataSourceModule
import com.markettwits.sportsouce.profile.cloud.di.sportSauceNetworkProfileModule
import com.markettwits.sportsouce.profile.members.member_common.data.ProfileMembersRepository
import com.markettwits.sportsouce.profile.members.member_common.data.ProfileMembersRepositoryBase
import com.markettwits.sportsouce.profile.members.member_common.data.cache.ProfileMemberCache
import com.markettwits.sportsouce.profile.members.member_common.data.mapper.MembersMapper
import com.markettwits.sportsouce.profile.members.member_common.data.mapper.MembersMapperBase
import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val membersCommonModule = module {
    includes(sportSauceNetworkProfileModule, authDataSourceModule)
    singleOf(::BaseTimeMapper) bind TimeMapper::class
    singleOf(::MembersMapperBase) bind MembersMapper::class
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::ProfileMembersRepositoryBase) bind ProfileMembersRepository::class
    singleOf(::ExecuteListWithCacheBase) bind ExecuteListWithCache::class
    single<Cache<List<ProfileMember>>> {
        ProfileMemberCache()
    }
}