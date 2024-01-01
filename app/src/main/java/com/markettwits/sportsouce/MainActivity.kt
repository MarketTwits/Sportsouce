package com.markettwits.sportsouce

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import com.markettwits.core_ui.theme.SportSouceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
          //  SportSouceTheme{
                val root =
                    com.markettwits.root.root.BaseRootComponent(
                        componentContext = defaultComponentContext()
                    )
                com.markettwits.root.root.RootContent(component = root)
          //  }
        }
    }
}
