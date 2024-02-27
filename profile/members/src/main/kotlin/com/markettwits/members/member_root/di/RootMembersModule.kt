package com.markettwits.members.member_root.di

import com.markettwits.members.common.di.membersModule
import com.markettwits.members.member_edit.di.memberEditModule
import org.koin.dsl.module

val rootMembersModule = module {
    includes(memberEditModule, membersModule)
}