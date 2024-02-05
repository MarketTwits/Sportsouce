package com.markettwits.sportsouce.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import com.markettwits.root.root.BaseRootComponent
import com.markettwits.root.root.RootContent


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = BaseRootComponent(componentContext = defaultComponentContext())
        setContent {
            RootContent(component = root)
        }
    }
}