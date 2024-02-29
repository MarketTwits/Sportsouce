package com.markettwits.profile.di

import com.markettwits.edit_profile.edit_profile.di.editProfileModule
import com.markettwits.edit_profile.edit_social_network.di.editProfileSocialNetworkModule
import com.markettwits.members.member_edit.di.memberEditModule
import com.markettwits.profile.data.BaseProfileDataSource
import com.markettwits.profile.data.ProfileDataSource
import com.markettwits.profile.domain.UserUseCase
import com.markettwits.profile.domain.UserUseCaseBase
import com.markettwits.profile.presentation.component.authorized.di.authorizedProfileModule
import com.markettwits.registrations.root_registrations.di.userStartRegistrationModule
import com.markettwits.start.di.startModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val rootProfileModule = module {
    includes(
        editProfileModule, authDataSourceModule, authorizedProfileModule,
        startModule,
        editProfileSocialNetworkModule,
        userStartRegistrationModule,
        memberEditModule
    )
    singleOf(::BaseProfileDataSource) bind ProfileDataSource::class
    singleOf(::UserUseCaseBase) bind UserUseCase::class
}
