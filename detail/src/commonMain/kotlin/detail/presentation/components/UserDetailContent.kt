package detail.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.random_user.RandomUser
import detail.presentation.components.detail.PersonLoaded

@Composable
fun UserDetailContent(
    modifier: Modifier = Modifier,
    item: RandomUser,
    pop: () -> Unit,
    onClickEmail: (String) -> Unit,
    onClickMap: (String) -> Unit,
    onClickGeoMap: (String) -> Unit,
    onClickPhone: (String) -> Unit
) {
    UserDetailSurface {
        Column {
            UserDetailList(
                modifier = modifier.padding(10.dp),
                user = item,
                onClickEmail = onClickEmail,
                onClickMap = onClickMap,
                onClickGeoMap = onClickGeoMap,
                onClickPhone = onClickPhone
            )
        }
        BackFloatingActionButton(
            back = {
                pop()
            }
        )
    }
}