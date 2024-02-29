package com.markettwits.members.member_edit.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.members.member_common.di.membersCommonModule
import com.markettwits.members.member_edit.domain.MemberAddUseCase
import com.markettwits.members.member_edit.domain.MemberAddUseCaseBase
import com.markettwits.members.member_edit.domain.MemberEditUseCase
import com.markettwits.members.member_edit.domain.MemberEditUseCaseBase
import com.markettwits.members.member_edit.presentation.store.MemberEditStoreFactory
import com.markettwits.teams_city.di.teamsCityModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val memberEditModule = module {
    includes(sportSouceNetworkModule, teamsCityModule, membersCommonModule)
    singleOf(::MemberEditStoreFactory)
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::MemberEditUseCaseBase) bind MemberEditUseCase::class
    singleOf(::MemberAddUseCaseBase) bind MemberAddUseCase::class
}