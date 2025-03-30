package com.markettwits.sportsouce.profile.members.members_list.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.sportsouce.profile.members.member_common.di.membersCommonModule
import com.markettwits.sportsouce.profile.members.members_list.domain.MembersListUseCase
import com.markettwits.sportsouce.profile.members.members_list.domain.MembersListUseCaseBase
import com.markettwits.sportsouce.profile.members.members_list.presentation.store.store.MembersListStoreFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val membersListModule = module {
    includes(membersCommonModule)
    singleOf(::MembersListStoreFactory)
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::MembersListUseCaseBase) bind MembersListUseCase::class
}