import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.essenty.lifecycle.stop
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.selfupdater.api.SelfUpdaterApi
import com.markettwits.selfupdater.components.selft_update.component.SelfUpdateComponentBase
import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStoreFactory
import com.markettwits.selfupdater.models.SelfUpdateResult
import com.markettwits.sportsouce.bottom_bar.component.storage.BottomBarStorageImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class UpdateScreenNavigationTest {
    @Test
    fun everyUpdateEntryHidesNavigationAndRestoresItOnExit() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val lifecycle = LifecycleRegistry()
        try {
            val api = object : SelfUpdaterApi {
                override suspend fun startCheckUpdate(manual: Boolean) = SelfUpdateResult.NoUpdates
                override fun getInProgressState() = MutableStateFlow(false)
                override fun getInstallSourceName() = "GitHub"
                override fun isSelfUpdateCanManualCheck() = true
            }
            BottomBarStorageImpl.show()
            SelfUpdateComponentBase(DefaultComponentContext(lifecycle), SelfUpdateStoreFactory(DefaultStoreFactory(), api), null, {})
            lifecycle.resume(); runCurrent()
            assertFalse(BottomBarStorageImpl.state.value.showTopBar)
            lifecycle.stop(); runCurrent()
            assertTrue(BottomBarStorageImpl.state.value.showTopBar)
        } finally {
            lifecycle.destroy(); BottomBarStorageImpl.show(); Dispatchers.resetMain()
        }
    }
}
