package com.markettwits.start.presentation.membres.filter_screen.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.start.presentation.common.OnClick
import com.markettwits.start.presentation.membres.filter_screen.MembersFilterGroup
import com.markettwits.start.presentation.membres.filter_screen.MembersFilterItem
import com.markettwits.start.presentation.membres.filter_screen.StartMembersFilterScreen

@Composable
fun StartMemberFilterItemBase(modifier: Modifier = Modifier, item: String, onClick: OnClick) {
    Card(
        modifier = modifier
            .padding(10.dp)
            .clickable { onClick() },
        shape = Shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Text(
            modifier = Modifier
                .padding(10.dp),
            text = item,
            color = MaterialTheme.colorScheme.outline,
            maxLines = 1,
            fontFamily = FontNunito.regular(),
            fontSize = 12.sp
        )
    }
}

@Composable
fun StartMemberFilterItemSelected(modifier: Modifier = Modifier, item: String, onClick: OnClick) {
    Card(
        modifier = modifier
            .padding(10.dp)
            .clickable { onClick() },
        shape = Shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
    ) {
        Text(
            modifier = Modifier
                .padding(10.dp),
            text = item,
            color = MaterialTheme.colorScheme.onSecondary,
            maxLines = 1,
            fontFamily = FontNunito.regular(),
            fontSize = 12.sp
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StartMembersSortWrapper2(
    members: List<MembersFilterGroup>,
    component: StartMembersFilterScreen
) {
    members.forEachIndexed { groupIndex, item ->
        Column(
            modifier = Modifier.padding(10.dp),
        ) {
            Text(
                text = item.title,
                color = MaterialTheme.colorScheme.tertiary,
                fontFamily = FontNunito.bold(),
                fontSize = 14.sp
            )
            FlowRow(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                maxItemsInEachRow = 12
            ) {
                item.items.forEachIndexed { index, item ->
                    if (item is MembersFilterItem.Base) {
                        StartMemberFilterItemBase(item = item.title) {
                            component.toggleFilterItemState(groupIndex, index)
                        }
                    } else {
                        StartMemberFilterItemSelected(item = item.title) {
                            component.toggleFilterItemState(groupIndex, index)
                        }
                    }
                }
            }
//            LazyRow(
//                content = {
//
//                    itemsIndexed(item.items) { index, item ->
//                        if (item is MembersFilterItem.Base) {
//                            StartMemberFilterItemBase(item = item.title) {
//                                component.toggleFilterItemState(groupIndex, index)
//                            }
//                        } else {
//                            StartMemberFilterItemSelected(item = item.title) {
//                                component.toggleFilterItemState(groupIndex, index)
//                            }
//                        }
//                    }
//                }
//            )
        }
    }
}
