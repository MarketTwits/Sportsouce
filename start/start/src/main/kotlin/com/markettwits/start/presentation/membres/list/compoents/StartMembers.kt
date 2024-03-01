package com.markettwits.start.presentation.membres.list.compoents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.start.presentation.membres.list.StartMembersUi

@Composable
fun StartMembers(modifier: Modifier = Modifier, members: List<StartMembersUi>) {
    Box(modifier = modifier) {
        Column(
            modifier = modifier
                .height(1000.dp),
        ) {
            Text(
                text = "Количество участников (команд): ${members.size}",
                modifier = Modifier.padding(start = 15.dp),
                color = MaterialTheme.colorScheme.tertiary,
                fontFamily = FontNunito.bold,
                fontSize = 16.sp
            )
            TestTable(members)
        }
    }
}