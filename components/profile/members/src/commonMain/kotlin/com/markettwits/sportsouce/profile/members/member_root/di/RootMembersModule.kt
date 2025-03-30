package com.markettwits.sportsouce.profile.members.member_root.di

import com.markettwits.sportsouce.profile.members.member_add_edit.di.memberAddAndEditModule
import com.markettwits.sportsouce.profile.members.member_common.di.membersCommonModule
import com.markettwits.sportsouce.profile.members.member_detail.di.memberDetailModule
import com.markettwits.sportsouce.profile.members.members_list.di.membersListModule
import org.koin.dsl.module

val rootMembersModule = module {
    includes(memberAddAndEditModule, membersCommonModule, memberDetailModule, membersListModule)
}