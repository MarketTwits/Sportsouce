package com.markettwits.sportsouce.club.dashboard.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
internal fun ShimmerMoreSection() {
    Column {
        Text(
            text = "Еще",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 16.dp),
            fontFamily = FontNunito.extraBold()
        )
        Spacer(modifier = Modifier.height(16.dp))
        ClubShimmerInfoCard()
        Spacer(modifier = Modifier.height(16.dp))
        ClubShimmerInfoCard()
    }
}