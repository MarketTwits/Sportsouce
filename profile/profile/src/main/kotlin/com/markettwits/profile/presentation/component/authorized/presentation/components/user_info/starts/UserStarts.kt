package com.markettwits.profile.presentation.component.authorized.presentation.components.user_info.starts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.OnBackgroundCard
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.registrations.registrations.domain.StartOrderInfo
import com.markettwits.registrations.registrations_list.StartOrderCard

@Composable
fun UserStarts(
    modifier: Modifier = Modifier,
    starts: List<StartOrderInfo>,
    onClickAll: () -> Unit,
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
            Box(modifier = modifier
                .clip(Shapes.medium)
                .clickable { onClickAll() }
            ) {
                Text(
                    modifier = modifier.padding(horizontal = 10.dp),
                    text = "Показать все",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontFamily = FontNunito.medium,
                    fontSize = 14.sp
                )
            }
        }
        Column {
            val visibleItems = 6
            val moreItems = starts.size - visibleItems
            starts.take(visibleItems).forEach {
                StartOrderCard(start = it) {
                    onClickStart(it)
                }
            }
            if (moreItems > 0) {
                Box(modifier = modifier
                    .padding(10.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(Shapes.medium)
                    .clickable { onClickAll() }
                ) {
                    Text(
                        modifier = modifier.padding(horizontal = 10.dp),
                        text = "Ещё $moreItems регистраций",
                        color = MaterialTheme.colorScheme.tertiary,
                        fontFamily = FontNunito.bold,
                        fontSize = 14.sp
                    )
                }
            }

        }
    }
}