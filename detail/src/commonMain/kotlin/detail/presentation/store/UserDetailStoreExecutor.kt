package detail.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import detail.domain.LaunchFeature
import detail.presentation.store.UserDetailStore.Intent
import detail.presentation.store.UserDetailStore.Label
import detail.presentation.store.UserDetailStore.State
import detail.presentation.store.UserDetailStore.Message

class UserDetailStoreExecutor(private val feature: LaunchFeature) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.OnBackClicked -> publish(Label.OnBackClicked)
            is Intent.OnClickEmail -> feature.sendEmail(intent.email)
            is Intent.OnClickGeoMap -> feature.openGeoOnMap(intent.latitude)
            is Intent.OnClickMap -> feature.openMap(intent.coordinates)
            is Intent.OnClickPhone -> feature.openPhone(intent.phone)
        }
    }
}
