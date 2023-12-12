package com.markettwits.sportsouce

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.defaultComponentContext
import com.markettwits.sportsouce.ui.theme.SportsouceTheme
import com.markettwits.root.root.RootComponent
import com.markettwits.root.root.RootContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val root =
                com.markettwits.root.root.RootComponent(
                    componentContext = defaultComponentContext()
                )
            com.markettwits.root.root.RootContent(component = root)
        }
    }
}
