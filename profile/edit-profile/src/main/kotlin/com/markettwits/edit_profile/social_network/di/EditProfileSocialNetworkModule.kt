package com.markettwits.edit_profile.social_network.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.edit_profile.social_network.data.ProfileSocialNetworkCloudMapperBase
import com.markettwits.edit_profile.social_network.data.ProfileSocialNetworkRepositoryBase
import com.markettwits.edit_profile.social_network.domain.ProfileSocialNetworkCloudMapper
import com.markettwits.edit_profile.social_network.domain.ProfileSocialNetworkRepository
import com.markettwits.edit_profile.social_network.presentation.store.EditProfileSocialNetworkStoreFactory
import com.markettwits.profile.di.authDataSourceModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val editProfileSocialNetworkModule = module {
    includes(sportSouceNetworkModule, authDataSourceModule)
    singleOf(::EditProfileSocialNetworkStoreFactory)
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::ProfileSocialNetworkRepositoryBase) bind ProfileSocialNetworkRepository::class
    singleOf(::ProfileSocialNetworkCloudMapperBase) bind ProfileSocialNetworkCloudMapper::class
}