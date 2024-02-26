package com.markettwits.core_ui.components.top_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito

@Composable
fun TopBarBase(modifier: Modifier = Modifier, title: String, goBack: () -> Unit) {
    Box(
        modifier
            .background(MaterialTheme.colorScheme.primary)
            .padding(start = 5.dp, end = 8.dp)
            .padding(vertical = 10.dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier
                .align(Alignment.CenterStart)
                .clip(Shapes.medium)
                .clickable {
                    goBack()
                }
        )
        {
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(10.dp),
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "back",
                tint = MaterialTheme.colorScheme.tertiary,
            )
        }

        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(start = 30.dp),
            text = title,
            color = MaterialTheme.colorScheme.tertiary,
            fontFamily = FontNunito.bold,
            fontSize = 18.sp
        )
    }
}

@Preview
@Composable
private fun MyProfileTopBarPreview() {
    TopBarBase(title = "Мой профиль") {}
}