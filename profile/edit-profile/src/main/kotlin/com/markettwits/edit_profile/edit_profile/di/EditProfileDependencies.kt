package com.markettwits.edit_profile.edit_profile.di

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.profile.data.AuthDataSource

interface EditProfileDependencies {
    val sportsouceApi : SportsouceApi
    val authDataSource : AuthDataSource
}