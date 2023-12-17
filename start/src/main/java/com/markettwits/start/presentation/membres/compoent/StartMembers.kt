package com.markettwits.start.presentation.membres.compoent

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.start.presentation.membres.StartMembersUi

@Composable
fun StartMembers(modifier: Modifier = Modifier, members: List<StartMembersUi>) {
    Box(modifier = modifier) {
        LazyColumn(
            modifier = modifier
                .height(1000.dp),
        ) {

            item {
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "Участник", color = SportSouceColor.SportSouceBlue)
                    Text(text = "Дистанция", color = SportSouceColor.SportSouceBlue)
                    Text(text = "Команда", color = SportSouceColor.SportSouceBlue)
                    Text(text = "Группа", color = SportSouceColor.SportSouceBlue)
                    Text(text = "Город", color = SportSouceColor.SportSouceBlue)
                }
            }
            items(members) { member ->
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                ) {
                    Text(text = member.member, color = SportSouceColor.SportSouceBlue)
                    Text(text = member.distance, color = SportSouceColor.SportSouceBlue)
                    Text(text = member.team, color = SportSouceColor.SportSouceBlue)
                    Text(text = member.group, color = SportSouceColor.SportSouceBlue)
                    Text(text = member.city, color = SportSouceColor.SportSouceBlue)
                }
            }
        }

    }
}