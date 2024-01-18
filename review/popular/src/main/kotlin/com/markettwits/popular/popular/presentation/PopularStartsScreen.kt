package com.markettwits.popular.popular.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SyncLock
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material.icons.outlined.SyncLock
import androidx.compose.material.icons.twotone.AccessTime
import androidx.compose.material.icons.twotone.Hardware
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
fun PopularStartsScreen(component: PopularStartsComponent) {
    Scaffold(
        containerColor = Color.White
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Данный раздел все ещё в разработке :)",
                    color = SportSouceColor.SportSouceBlue,
                    textAlign = TextAlign.Center,
                    fontFamily = FontNunito.bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.padding(vertical = 5.dp))
                Icon(
                    modifier = Modifier.size(50.dp),
                    imageVector = Icons.TwoTone.AccessTime,
                    contentDescription = null,
                    tint = SportSouceColor.SportSouceBlue,
                )

            }

        }

    }
}

@Preview
@Composable
private fun PopularStartsScreenPreview() {
    PopularStartsScreen(MockStartsComponent())
}