package detail.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import detail.presentation.UserDetailComponent
import detail.presentation.store.UserDetailStore


@Composable
fun UserDetailScreen(component: UserDetailComponent) {
    val state by component.state.collectAsState()
    UserDetailContent(
        item = state.item,
        pop = {
            component.obtainEvent(UserDetailStore.Intent.OnBackClicked)
        },
        onClickEmail = {
            component.obtainEvent(UserDetailStore.Intent.OnClickEmail(it))
        },
        onClickGeoMap = {
            component.obtainEvent(UserDetailStore.Intent.OnClickGeoMap(it))
        },
        onClickMap = {
            component.obtainEvent(UserDetailStore.Intent.OnClickMap(it))
        },
        onClickPhone = {
            component.obtainEvent(UserDetailStore.Intent.OnClickPhone(it))
        }
    )
}