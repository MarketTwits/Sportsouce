package com.markettwits.profile.presentation.component.authorized.presentation.components.user_info.starts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.OnBackgroundCard
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.registrations.registrations.domain.StartOrderInfo
import com.markettwits.registrations.registrations_list.profile.StartOrderCard
import com.markettwits.starts_common.domain.StartsListItem

@Composable
fun UserStarts(
    modifier: Modifier = Modifier,
    starts: List<StartOrderInfo>,
    onClick: (Int) -> Unit,
    onClickStart: (StartOrderInfo) -> Unit,
) {
    OnBackgroundCard(modifier = modifier.padding(top = 10.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Мои старты",
                color = MaterialTheme.colorScheme.tertiary,
                fontFamily = FontNunito.bold,
                fontSize = 18.sp
            )
            Text(
                modifier = modifier.padding(horizontal = 10.dp),
                text = "Показать все",
                color = MaterialTheme.colorScheme.tertiary,
                fontFamily = FontNunito.regular,
                fontSize = 14.sp
            )
        }
        Column {
            starts.take(5).forEach {
                StartOrderCard(start = it) {
                    onClickStart(it)
                }
            }
        }
//        LazyRow(modifier = modifier) {
//            items(starts, key = { it.id }) {
//                StartCardSimpleAuth(
//                    start = it, onItemClick = {id ->
//                        onClickStart(it)
//                       // onClick(it.id)
//                    })
//            }
//        }
    }
}

private val testData = listOf(
    StartsListItem(
        id = 1,
        name = "Start 1",
        image = "Image 1",
        date = "2024-01-01",
        statusCode = StartsListItem.StatusCode(id = 200, message = "Success"),
        place = "Place 1",
        distance = "10km"
    ),
    StartsListItem(
        id = 2,
        name = "Start 2",
        image = "Image 2",
        date = "2024-02-01",
        statusCode = StartsListItem.StatusCode(id = 200, message = "Success"),
        place = "Place 2",
        distance = "15km"
    ),
    // Добавьте больше объектов по аналогии, если это необходимо
)