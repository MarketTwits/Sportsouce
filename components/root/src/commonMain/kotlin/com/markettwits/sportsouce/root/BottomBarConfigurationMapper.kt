package com.markettwits.sportsouce.root

import com.markettwits.sportsouce.bottom_bar.model.BottomBarConfiguration

internal object BottomBarConfigurationMapper : BottomBarConfiguration.Mapper<RootComponent.Configuration> {

    override fun map(bottomBarConfiguration: BottomBarConfiguration): RootComponent.Configuration =
        when (bottomBarConfiguration) {
            is BottomBarConfiguration.Home -> RootComponent.Configuration.Starts
            is BottomBarConfiguration.Profile -> RootComponent.Configuration.Profile
            is BottomBarConfiguration.Review -> RootComponent.Configuration.Review
        }

    override fun map(configuration: RootComponent.Configuration): BottomBarConfiguration =
        when (configuration) {
            is RootComponent.Configuration.Profile -> BottomBarConfiguration.Profile
            is RootComponent.Configuration.Review -> BottomBarConfiguration.Review
            is RootComponent.Configuration.Starts -> BottomBarConfiguration.Home
        }
}