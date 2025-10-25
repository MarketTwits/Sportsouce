package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.start.domain.StartItem

@Composable
internal fun StartRegistrationButton(
    modifier: Modifier = Modifier,
    startStatus: StartItem.StartStatus,
    regLink: String,
    onClickRegistration: () -> Unit,
) {
    if (startStatus.code == 3 || regLink.isNotEmpty()) {
        FloatingActionButton(
            modifier = modifier
                .height(55.dp)
                .fillMaxWidth(),
            onClick = onClickRegistration,
            containerColor = MaterialTheme.colorScheme.secondary
        ) {
            Text(
                modifier = Modifier.padding(2.dp),
                text = "Зарегестрироваться",
                color = MaterialTheme.colorScheme.onSecondary,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                fontSize = 16.sp,
                fontFamily = FontNunito.bold(),
            )
        }
    }
}
