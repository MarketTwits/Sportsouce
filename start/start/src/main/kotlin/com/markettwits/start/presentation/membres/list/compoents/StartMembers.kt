package com.markettwits.start.presentation.membres.list.compoents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.start.presentation.membres.list.StartMembersUi

@Composable
fun StartMembers(modifier: Modifier = Modifier, members: List<StartMembersUi>) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "Количество участников (команд): ${members.size}",
            modifier = Modifier.padding(start = 15.dp),
            color = MaterialTheme.colorScheme.tertiary,
            fontFamily = FontNunito.bold(),
            fontSize = 16.sp
        )
        TestTable(members)
    }
}