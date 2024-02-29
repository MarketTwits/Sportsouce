package com.markettwits.members.member_detail.di

import com.markettwits.members.member_common.di.membersCommonModule
import com.markettwits.members.member_detail.domain.MemberDetailUseCase
import com.markettwits.members.member_detail.domain.MemberDetailUseCaseBase
import com.markettwits.members.member_detail.presentation.store.MemberDetailStoreFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val memberDetailModule = module {
    includes(membersCommonModule)
    singleOf(::MemberDetailUseCaseBase) bind MemberDetailUseCase::class
    singleOf(::MemberDetailStoreFactory)
}