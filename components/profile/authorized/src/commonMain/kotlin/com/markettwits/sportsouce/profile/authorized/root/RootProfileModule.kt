package com.markettwits.sportsouce.profile.authorized.root

import com.markettwits.sportsouce.auth.service.api.authDataSourceModule
import com.markettwits.sportsouce.edit_profile.social_network.di.editProfileSocialNetworkModule
import com.markettwits.sportsouce.profile.authorized.authorized.di.authorizedProfileModule
import com.markettwits.sportsouce.profile.members.member_add_edit.di.memberAddAndEditModule
import com.markettwits.sportsouce.profile.registrations.presentation.root.di.userStartRegistrationModule
import com.markettwits.sportsouce.shop.orders.di.shopUserOrdersModule
import com.markettwits.sportsouce.start.di.startModule
import com.markettwits.sportsouce.starts.favorites.di.startsFavoritesModule
import org.koin.dsl.module
import com.markettwits.sportsouce.profile.authorized.receipts.ReceiptsRepository
import com.markettwits.sportsouce.profile.authorized.receipts.ReceiptsRepositoryBase

internal val rootProfileModule = module {
    factory<ReceiptsRepository> { ReceiptsRepositoryBase(get(), get()) }
    includes(
        authDataSourceModule,
        authorizedProfileModule,
        startsFavoritesModule,
        startModule,
        editProfileSocialNetworkModule,
        userStartRegistrationModule,
        memberAddAndEditModule,
        shopUserOrdersModule
    )
}
