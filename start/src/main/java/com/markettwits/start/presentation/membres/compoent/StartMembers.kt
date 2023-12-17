package com.markettwits.start.presentation.membres.compoent

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.start.presentation.membres.StartMembersUi

@Composable
fun StartMembers(modifier: Modifier = Modifier, members: List<StartMembersUi>) {
    Box(modifier = modifier) {
        Column(
            modifier = modifier
                .height(1000.dp),
        ) {
            Text(
                text = "Количество участников (команд): ${members.size}",
                modifier = Modifier.padding(start = 30.dp),
                color = SportSouceColor.SportSouceBlue,
                fontFamily = FontNunito.bold,
                fontSize = 16.sp
            )
            TestTable(members)
        }

    }
}