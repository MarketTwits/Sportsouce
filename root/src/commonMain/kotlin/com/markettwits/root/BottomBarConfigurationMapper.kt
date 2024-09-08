package com.markettwits.root

import com.markettwits.bottom_bar.model.Configuration

internal object BottomBarConfigurationMapper : Configuration.Mapper<RootComponent.Configuration> {

    override fun map(configuration: Configuration): RootComponent.Configuration =
        when (configuration) {
            is Configuration.Home -> RootComponent.Configuration.Starts
            is Configuration.Profile -> RootComponent.Configuration.Profile
            is Configuration.Review -> RootComponent.Configuration.Review
        }

    override fun map(configuration: RootComponent.Configuration): Configuration =
        when (configuration) {
            is RootComponent.Configuration.Profile -> Configuration.Profile
            is RootComponent.Configuration.Review -> Configuration.Review
            is RootComponent.Configuration.Starts -> Configuration.Home
        }
}