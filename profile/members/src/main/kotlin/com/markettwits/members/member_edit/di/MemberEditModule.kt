package com.markettwits.members.member_edit.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.members.common.di.membersModule
import com.markettwits.members.member_add.domain.MemberAddUseCase
import com.markettwits.members.member_add.domain.MemberAddUseCaseBase
import com.markettwits.members.member_edit.domain.MemberEditUseCase
import com.markettwits.members.member_edit.domain.MemberEditUseCaseBase
import com.markettwits.members.member_edit.presentation.store.MemberEditStoreFactory
import com.markettwits.teams_city.di.teamsCityModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val memberEditModule = module {
    includes(sportSouceNetworkModule, teamsCityModule, membersModule)
    singleOf(::MemberEditStoreFactory)
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::MemberEditUseCaseBase) bind MemberEditUseCase::class
    singleOf(::MemberAddUseCaseBase) bind MemberAddUseCase::class
}