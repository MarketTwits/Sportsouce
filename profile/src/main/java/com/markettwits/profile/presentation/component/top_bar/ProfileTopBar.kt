package com.markettwits.profile.presentation.component.top_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Architecture
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
fun ProfileTopBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .padding(start = 5.dp, end = 8.dp)
            .padding(vertical = 10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(Shapes.large)
                    .background(Color.Gray)
            )
            Text(
                text = "Кулаков Данилл",
                color = SportSouceColor.SportSouceBlue,
                fontFamily = FontNunito.bold,
                modifier = Modifier.padding(10.dp)
            )
        }
        Icon(
            imageVector = Icons.Default.Architecture,
            contentDescription = "bell",
            tint = SportSouceColor.SportSouceBlue
        )

    }
}

@Composable
@Preview(showBackground = true)
private fun ProfileTopBarPreview() {
    ProfileTopBar()
}