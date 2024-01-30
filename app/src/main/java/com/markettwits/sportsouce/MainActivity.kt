package com.markettwits.sportsouce

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import com.markettwits.root.BaseRootComponent
import com.markettwits.root.RootContent
import data.cache.appStorage
import detail.LaunchFeatureImpl
import detail.domain.launchService

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appStorage = filesDir.path
        launchService = LaunchFeatureImpl(context = applicationContext)
        val root = BaseRootComponent(componentContext = defaultComponentContext())
        setContent {
            RootContent(component = root)
        }
    }
}
