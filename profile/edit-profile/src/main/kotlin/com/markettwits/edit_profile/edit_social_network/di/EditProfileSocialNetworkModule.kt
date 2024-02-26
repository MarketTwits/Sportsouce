package com.markettwits.edit_profile.edit_social_network.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.edit_profile.edit_social_network.data.ProfileSocialNetworkRepository
import com.markettwits.edit_profile.edit_social_network.data.ProfileSocialNetworkRepositoryBase
import com.markettwits.edit_profile.edit_social_network.data.mapper.ProfileSocialNetworkCloudMapper
import com.markettwits.edit_profile.edit_social_network.data.mapper.ProfileSocialNetworkCloudMapperBase
import com.markettwits.edit_profile.edit_social_network.domain.handle.ProfileSocialNetworkHandler
import com.markettwits.edit_profile.edit_social_network.domain.handle.ProfileSocialNetworkHandlerBase
import com.markettwits.edit_profile.edit_social_network.domain.interactor.ProfileSocialNetworkInteractor
import com.markettwits.edit_profile.edit_social_network.domain.interactor.ProfileSocialNetworkInteractorBase
import com.markettwits.edit_profile.edit_social_network.presentation.store.EditProfileSocialNetworkStoreFactory
import com.markettwits.profile.di.authDataSourceModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val editProfileSocialNetworkModule = module {
    includes(sportSouceNetworkModule, authDataSourceModule)
    singleOf(::ProfileSocialNetworkHandlerBase) bind ProfileSocialNetworkHandler::class
    singleOf(::ProfileSocialNetworkInteractorBase) bind ProfileSocialNetworkInteractor::class
    singleOf(::EditProfileSocialNetworkStoreFactory)
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::ProfileSocialNetworkRepositoryBase) bind ProfileSocialNetworkRepository::class
    singleOf(::ProfileSocialNetworkCloudMapperBase) bind ProfileSocialNetworkCloudMapper::class
}