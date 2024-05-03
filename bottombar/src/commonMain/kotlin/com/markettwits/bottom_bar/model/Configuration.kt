package com.markettwits.bottom_bar.model

sealed interface Configuration {
    data object Home : Configuration
    data object Review : Configuration
    data object Profile : Configuration

    interface Mapper<T> {
        fun map(configuration: Configuration): T
        fun map(configuration: T): Configuration
    }
}

