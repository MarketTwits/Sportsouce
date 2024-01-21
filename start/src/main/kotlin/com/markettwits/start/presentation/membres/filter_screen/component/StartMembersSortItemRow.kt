package com.markettwits.start.presentation.membres.filter_screen.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor
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
    }
}
