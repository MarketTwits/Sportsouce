package com.markettwits.start_search.search.presentation.components.publish

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito


@Composable
fun StartsSearchBarPublic(modifier: Modifier = Modifier) {

    Card(
        modifier = Modifier
            .padding(10.dp)
            .shadow(elevation = 2.dp, shape = Shapes.large)
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = Shapes.large,
            )
            .clip(Shapes.large),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
    ) {
        Row(
            modifier = modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Поиск старта",
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.padding(horizontal = 5.dp))
            Text(
                text = "Поиск старта",
                fontSize = 16.sp,
                fontFamily = FontNunito.bold,
                color = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StartsSearchBarPreview() {
    StartsSearchBarPublic()
}