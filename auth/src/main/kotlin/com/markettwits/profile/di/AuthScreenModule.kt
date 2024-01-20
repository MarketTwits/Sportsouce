package com.markettwits.profile.di

import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.profile.presentation.sign_in.SignInInstanceKeeper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authScreenModule = module{
    includes(sportSouceNetworkModule, authDataSourceModule)
    singleOf(::SignInInstanceKeeper)
}