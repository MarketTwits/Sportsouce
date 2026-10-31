import com.markettwits.selfupdater.api.SelfUpdaterApi
import com.markettwits.selfupdater.api.SelfUpdaterSourceApi
import com.markettwits.selfupdater.components.selft_update.di.selfUpdateComponentModule
import com.markettwits.selfupdater.thirdparty.api.SelfUpdaterDesktop
import org.koin.dsl.koinApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import kotlin.test.Test
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class DesktopUpdaterWiringTest {
    @Test
    fun featureModuleResolvesDesktopProviderAndFacade() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        val app = koinApplication { modules(selfUpdateComponentModule) }
        try {
            assertIs<SelfUpdaterDesktop>(app.koin.get<SelfUpdaterSourceApi>())
            assertIs<SelfUpdaterApi>(app.koin.get<SelfUpdaterApi>())
        } finally { app.close(); Dispatchers.resetMain() }
    }
}
