import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.mvikotlin.core.utils.setMainThreadId
import com.markettwits.root.BaseRootComponent
import com.markettwits.root.RootContent
import data.cache.appStorage
import java.io.File
import javax.swing.SwingUtilities

fun main() {
    val rootComponent = invokeOnAwtSync {
        setMainThreadId(Thread.currentThread().id)

        val lifecycle = LifecycleRegistry()
        appStorage = File(System.getProperty("java.io.tmpdir")).absolutePath

        val rootComponent = BaseRootComponent(
            componentContext = DefaultComponentContext(lifecycle = lifecycle),
        )

        lifecycle.resume()

        rootComponent
    }
    application {
        Window(
            title = "Shift",
            onCloseRequest = ::exitApplication
        ) {
            RootContent(component = rootComponent)
        }
    }
}

fun <T> invokeOnAwtSync(block: () -> T): T {
    var result: T? = null
    SwingUtilities.invokeAndWait { result = block() }

    @Suppress("UNCHECKED_CAST")
    return result as T
}
