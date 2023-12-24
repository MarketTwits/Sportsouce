package com.markettwits.profile.presentation

interface ProfileLaunchPolicy {
    object Base : ProfileLaunchPolicy
    object Update : ProfileLaunchPolicy
}