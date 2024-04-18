package com.markettwits.sportsouce.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageFileDirectory
import com.markettwits.root.root.RootComponentBase
import com.markettwits.root.root.RootContent
import com.markettwits.theme.theme.SportSauceTheme
import com.markettwits.theme.theme.component.ThemeComponentBase
import java.io.File


fun main() {
    //overrideSchedulers(main = Dispatchers.Main::asScheduler)

    InStorageCacheDirectory.path = File(System.getProperty("java.io.tmpdir")).absolutePath
    InStorageFileDirectory.path = File(System.getProperty("java.io.tmpdir")).absolutePath

    val lifecycle = LifecycleRegistry()
    val root =
        runOnUiThread {
            RootComponentBase(
                DefaultComponentContext(lifecycle)
            )
        }
    val theme = runOnUiThread {
        ThemeComponentBase(DefaultComponentContext(lifecycle))
    }

    application {
        val windowState = rememberWindowState()

        LifecycleController(lifecycle, windowState)

        var isCloseRequested by remember { mutableStateOf(false) }

        Window(
            onCloseRequest = { isCloseRequested = true },
            state = windowState,
            title = "Decompose Sample"
        ) {
            SportSauceTheme(theme) {
                RootContent(root)
            }

            if (isCloseRequested) {
                SaveStateDialog(
                    onSaveState = { //TODO  add save state
                    },
                    onExitApplication = ::exitApplication,
                    onDismiss = { isCloseRequested = false },
                )
            }
        }
    }
}

@Composable
private fun SaveStateDialog(
    onSaveState: () -> Unit,
    onExitApplication: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        buttons = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(onClick = onDismiss) {
                    Text(text = "Cancel")
                }

                TextButton(onClick = onExitApplication) {
                    Text(text = "No")
                }

                TextButton(
                    onClick = {
                        onSaveState()
                        onExitApplication()
                    }
                ) {
                    Text(text = "Yes")
                }
            }
        },
        title = { Text(text = "Decompose Sample") },
        text = { Text(text = "Do you want to save the application's state?") },
        modifier = Modifier.width(400.dp),
    )
}