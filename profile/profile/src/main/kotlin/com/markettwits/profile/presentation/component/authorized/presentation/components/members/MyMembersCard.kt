package com.markettwits.profile.presentation.component.authorized.presentation.components.members

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.OnBackgroundCard
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito

@Composable
fun MyMembersCard(modifier: Modifier = Modifier, onClick: () -> Unit) {
    OnBackgroundCard(
        modifier = modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth(), onClick = onClick::invoke
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Мои участники",
                color = MaterialTheme.colorScheme.tertiary,
                fontFamily = FontNunito.bold,
                fontSize = 18.sp
            )
            Box(
                modifier = modifier
                    .clip(Shapes.medium)
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}