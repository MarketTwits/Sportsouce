package com.markettwits.sportsouce.app.desktop

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.badoo.reaktive.coroutinesinterop.asScheduler
import com.badoo.reaktive.scheduler.overrideSchedulers
import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageFileDirectory
import com.markettwits.core.theme.SportSauceTheme
import com.markettwits.core.theme.component.ThemeComponentBase
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.image.DefaultImages
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.LocalDarkOrLightTheme
import com.markettwits.initKoin
import com.markettwits.sportsouce.root.RootComponentBase
import com.markettwits.sportsouce.root.RootContent
import kotlinx.coroutines.Dispatchers
import java.io.File
import java.util.*

fun main() {
    Locale.setDefault(Locale("ru", "RUS"))

    overrideSchedulers(main = Dispatchers.Main::asScheduler)

    InStorageCacheDirectory.path = File(System.getProperty("java.io.tmpdir")).absolutePath
    InStorageFileDirectory.path = File(System.getProperty("java.io.tmpdir")).absolutePath

    val lifecycle = LifecycleRegistry()
    val context = DefaultComponentContext(lifecycle)
    initKoin()
    application {
        val windowState = rememberWindowState(
            width = 1280.dp,
            height = 860.dp,
            position = WindowPosition.Aligned(Alignment.Center)
        )
        LifecycleController(lifecycle, windowState)

        var root by remember { mutableStateOf<RootComponentBase?>(null) }
        var theme by remember { mutableStateOf<ThemeComponentBase?>(null) }
        var startupError by remember { mutableStateOf<Throwable?>(null) }

        LaunchedEffect(Unit) {
            try {
                theme = ThemeComponentBase(context)
                root = RootComponentBase(context)
            } catch (throwable: Throwable) {
                startupError = throwable
                throwable.printStackTrace()
            }
        }

        Window(
            state = windowState,
            title = "Спорт Союз",
            icon = DefaultImages.SportSauceLightLogo(),
            onCloseRequest = { exitApplication() }
        ) {
            when {
                startupError != null && theme != null -> SportSauceTheme(theme!!) {
                    StartupErrorContent(startupError!!, themeReady = true)
                }

                startupError != null -> StartupErrorContent(startupError!!, themeReady = false)
                root == null || theme == null -> StartupLoadingContent()
                else -> SportSauceTheme(theme!!) {
                    RootContent(root!!)
                }
            }
        }
    }
}

@Composable
private fun StartupLoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(42.dp))
    }
}

@Composable
private fun StartupErrorContent(
    throwable: Throwable,
    themeReady: Boolean,
) {
    val clipboardManager = LocalClipboardManager.current
    val stackTrace = remember(throwable) { throwable.stackTraceToString() }
    val errorText = remember(stackTrace) {
        buildString {
            appendLine("Не удалось инициализировать desktop-приложение.")
            appendLine()
            append(stackTrace)
        }
    }
    val scrollState = rememberScrollState()
    val logo = when {
        themeReady && LocalDarkOrLightTheme.current -> DefaultImages.SportSauceDarkLogo()
        else -> DefaultImages.SportSauceLightLogo()
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            OnBackgroundCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier.size(width = 180.dp, height = 72.dp),
                        painter = logo,
                        contentDescription = "SportSauce logo",
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Ошибка запуска",
                        style = MaterialTheme.typography.headlineSmall,
                        fontFamily = FontNunito.bold(),
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Приложение не завершает работу, чтобы можно было просмотреть и скопировать стек ошибки.",
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = FontNunito.regular(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    FilledTonalButton(
                        onClick = { clipboardManager.setText(AnnotatedString(errorText)) }
                    ) {
                        Text(
                            text = "Скопировать ошибку",
                            fontFamily = FontNunito.bold()
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false),
                        tonalElevation = 1.dp,
                        shape = MaterialTheme.shapes.medium,
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        SelectionContainer {
                            Text(
                                text = errorText,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .verticalScroll(scrollState)
                                    .padding(16.dp),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 13.sp,
                                    fontFamily = FontFamily.Monospace
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}
