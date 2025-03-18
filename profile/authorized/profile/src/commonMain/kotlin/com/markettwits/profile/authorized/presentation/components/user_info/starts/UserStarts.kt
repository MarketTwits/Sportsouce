package com.markettwits.profile.authorized.presentation.components.user_info.starts

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
import com.markettwits.core_ui.items.extensions.noRippleClickable
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.registrations.list.domain.StartOrderInfo
import com.markettwits.registrations.simple_list.StartOrderSimpleCard

@Composable
fun UserStarts(
    modifier: Modifier = Modifier,
    starts: List<StartOrderInfo>,
    onClickAll: () -> Unit,
    onClickStart: (StartOrderInfo) -> Unit,
) {
    OnBackgroundCard(modifier = modifier.padding(top = 14.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Мои старты",
                color = MaterialTheme.colorScheme.tertiary,
                fontFamily = FontNunito.bold(),
                fontSize = 18.sp
            )
            Box(modifier = modifier
                .clip(Shapes.medium)
                .noRippleClickable { onClickAll() }
            ) {
                Text(
                    modifier = modifier.padding(horizontal = 10.dp),
                    text = "Показать все",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontFamily = FontNunito.medium(),
                    fontSize = 14.sp
                )
            }
        }
        Column {
            val visibleItems = 5
            val moreItems = starts.size - visibleItems
            starts.take(visibleItems).forEach {
                StartOrderSimpleCard(start = it) {
                    onClickStart(it)
                }
            }
            if (moreItems > 0) {
                Box(modifier = modifier
                    .padding(10.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(Shapes.medium)
                    .noRippleClickable { onClickAll() }
                ) {
                    Text(
                        modifier = modifier.padding(horizontal = 10.dp),
                        text = "Ещё $moreItems регистраций",
                        color = MaterialTheme.colorScheme.tertiary,
                        fontFamily = FontNunito.bold(),
                        fontSize = 14.sp
                    )
                }
            }

        }
    }
}