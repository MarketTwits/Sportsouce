package com.markettwits.start.presentation.membres.filter_screen.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.start.presentation.common.OnClick
import com.markettwits.start.presentation.membres.filter_screen.MembersFilterGroup
import com.markettwits.start.presentation.membres.filter_screen.MembersFilterItem
import com.markettwits.start.presentation.membres.filter_screen.StartMembersFilterScreen
import com.markettwits.start.presentation.membres.list.StartMembersUi

@Composable
fun StartMembersSortItemRow(
    modifier: Modifier = Modifier,
    title: String,
    groups: List<MembersFilterItem>
) {
    Column(
        modifier = modifier.padding(10.dp),
    ) {
        Text(
            text = title,
            color = SportSouceColor.SportSouceBlue,
            fontFamily = FontNunito.bold,
            fontSize = 14.sp
        )
        LazyRow(
            content = {
                items(groups) { item ->
                    if (item is MembersFilterItem.Base) {
                        StartMemberFilterItemBase(item = item.title) {

                        }
                    } else {
                        StartMemberFilterItemSelected(item = item.title) {}
                    }

                }
            }
        )
    }
}

@Composable
fun StartMemberFilterItemBase(modifier: Modifier = Modifier, item: String, onClick: OnClick) {
    Card(
        modifier = modifier
            .padding(10.dp)
            .clickable { onClick() },
        shape = Shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Text(
            modifier = Modifier
                .padding(10.dp),
            text = item,
            color = Color.Gray,
            maxLines = 1,
            fontFamily = FontNunito.regular,
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
        colors = CardDefaults.cardColors(containerColor = SportSouceColor.SportSouceLighBlue),
    ) {
        Text(
            modifier = Modifier
                .padding(10.dp),
            text = item,
            color = Color.White,
            maxLines = 1,
            fontFamily = FontNunito.regular,
            fontSize = 12.sp
        )
    }
}

@Composable
fun StartMembersSortWrapper(members: List<StartMembersUi>) {
    val categories = mapOf(
        "Distances" to StartMembersUi::distance,
        "Teams" to StartMembersUi::team,
        "City" to StartMembersUi::city,
        "Groups" to StartMembersUi::group
    )


//    for ((category, keyExtractor) in categories) {
//        val duplicates = findDuplicates(members, keyExtractor)
//        StartMembersSortItemRow(title = category, groups = duplicates)
//    }
}

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
                color = SportSouceColor.SportSouceBlue,
                fontFamily = FontNunito.bold,
                fontSize = 14.sp
            )
            LazyRow(
                content = {

                    itemsIndexed(item.items) { index, item ->
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
            )
        }

//
//        StartMembersSortItemRow(title = item.title, groups = item.items)
    }
}

fun <T> findDuplicates(items: List<StartMembersUi>, keyExtractor: (StartMembersUi) -> T): List<T> {
    return items.groupBy(keyExtractor)
        .filter { it.value.isNotEmpty() }
        .map { it.key }
}

@Preview(showBackground = true)
@Composable
private fun StartMembersSortItemRowPreview() {
//    StartMembersSortItemRow(
//        title = "Дистанция",
//        groups = listOf(
//            "5км",
//            "Детский забег",
//            "Детский забег",
//            "Детский забег",
//            "Детский забег",
//            "Детский забег",
//            "Детский забег"
//        )
//    )
}