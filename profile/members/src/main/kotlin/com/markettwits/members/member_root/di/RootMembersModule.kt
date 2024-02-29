package com.markettwits.members.member_root.di

import com.markettwits.members.member_common.di.membersCommonModule
import com.markettwits.members.member_detail.di.memberDetailModule
import com.markettwits.members.member_edit.di.memberEditModule
import com.markettwits.members.members_list.di.membersListModule
import org.koin.dsl.module

val rootMembersModule = module {
    includes(memberEditModule, membersCommonModule, memberDetailModule, membersListModule)
}