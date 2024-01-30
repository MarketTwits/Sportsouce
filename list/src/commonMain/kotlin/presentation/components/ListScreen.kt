package presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import presentation.component.ListComponent
import presentation.store.ListStore

@Composable
fun ListScreen(component: ListComponent) {
    val state by component.state.collectAsState()
    ListScreenContent(
        state = state,
        onClickRefresh = {
            component.obtainEvent(ListStore.Intent.Launch(true))
        },
        onClickItem = {
            component.obtainEvent(ListStore.Intent.OnClickItem(it))
        }
    )
}