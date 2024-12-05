package com.markettwits.core.errors.impl.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.core.errors.api.throwable.mapToString
import com.markettwits.core.errors.impl.supcomponents.mapSauceErrorImage
import com.markettwits.core_ui.items.base_extensions.noRippleClickable
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
internal fun SauceThrowableSimpleContent(
    modifier: Modifier = Modifier,
    sauceError: SauceError,
    onClickRetry: (() -> Unit)? = null,
){
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = sauceError.mapSauceErrorImage(),
                contentDescription = sauceError.mapToString(),
                tint = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = sauceError.mapToString(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.outline,
                fontFamily = FontNunito.regular(),
                fontSize = 14.sp
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        if (onClickRetry != null) {
            Text(
                modifier = Modifier.noRippleClickable {
                    onClickRetry()
                },
                text = "Повторить",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.secondary,
                fontFamily = FontNunito.regular(),
                fontSize = 14.sp
            )
        }
    }
}