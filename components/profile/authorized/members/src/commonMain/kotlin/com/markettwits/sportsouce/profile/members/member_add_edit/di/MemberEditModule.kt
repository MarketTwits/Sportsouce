package com.markettwits.sportsouce.profile.members.member_add_edit.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.sportsouce.profile.members.member_add_edit.domain.add.MemberAddUseCase
import com.markettwits.sportsouce.profile.members.member_add_edit.domain.add.MemberAddUseCaseBase
import com.markettwits.sportsouce.profile.members.member_add_edit.domain.edit.MemberEditUseCase
import com.markettwits.sportsouce.profile.members.member_add_edit.domain.edit.MemberEditUseCaseBase
import com.markettwits.sportsouce.profile.members.member_add_edit.domain.validate.AddOrEditMemberValidator
import com.markettwits.sportsouce.profile.members.member_add_edit.domain.validate.AddOrEditMemberValidatorBase
import com.markettwits.sportsouce.profile.members.member_add_edit.presentation.store.MemberEditStoreFactory
import com.markettwits.sportsouce.profile.members.member_common.di.membersCommonModule
import com.markettwits.sportsouce.teams_city.di.teamsCityModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val memberAddAndEditModule = module {
    includes(teamsCityModule, membersCommonModule)
    singleOf(::MemberEditStoreFactory)
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::MemberEditUseCaseBase) bind MemberEditUseCase::class
    singleOf(::MemberAddUseCaseBase) bind MemberAddUseCase::class
    singleOf(::AddOrEditMemberValidatorBase) bind AddOrEditMemberValidator::class
}