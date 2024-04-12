package com.markettwits.profile.root

import com.markettwits.edit_profile.edit_social_network.di.editProfileSocialNetworkModule
import com.markettwits.members.member_add_edit.di.memberAddAndEditModule
import com.markettwits.profile.api.authDataSourceModule
import com.markettwits.profile.authorized.di.authorizedProfileModule
import com.markettwits.registrations.root_registrations.di.userStartRegistrationModule
import com.markettwits.start.di.startModule
import org.koin.dsl.module

internal val rootProfileModule = module {
    includes(
        authDataSourceModule,
        authorizedProfileModule,
        startModule,
        editProfileSocialNetworkModule,
        userStartRegistrationModule,
        memberAddAndEditModule,
    )
}
