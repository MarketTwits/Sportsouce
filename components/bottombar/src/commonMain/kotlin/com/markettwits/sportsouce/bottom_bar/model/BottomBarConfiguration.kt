package com.markettwits.sportsouce.bottom_bar.model

sealed interface BottomBarConfiguration {

    data object Home : BottomBarConfiguration

    data object Review : BottomBarConfiguration

    data object Profile : BottomBarConfiguration

    interface Mapper<T> {

        fun map(bottomBarConfiguration: BottomBarConfiguration): T

        fun map(configuration: T): BottomBarConfiguration

    }
}

