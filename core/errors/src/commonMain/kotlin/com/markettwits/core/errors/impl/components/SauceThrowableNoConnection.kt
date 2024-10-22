package com.markettwits.core.errors.impl.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.markettwits.core_ui.items.components.buttons.BackFloatingActionButton
import com.markettwits.core_ui.items.image.exception.WarningYellowIc
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
internal fun SauceThrowableScreen(
    modifier: Modifier = Modifier,
    throwable : SauceError,
    onClickRetry: () -> Unit,
    onClickGoBack :  (() -> Unit?)? = null
) {

    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        if (onClickGoBack != null) {
            BackFloatingActionButton(back = onClickRetry)
        }
        Column(
            modifier = modifier
                .align(Alignment.Center)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = modifier.size(100.dp),
                imageVector = WarningYellowIc,
                contentDescription = ""
            )
            Text(
                modifier = modifier,
                text = "Что то пошло не так :(",
                color = MaterialTheme.colorScheme.tertiary,
                textAlign = TextAlign.Center,
                fontFamily = FontNunito.bold(),
                fontSize = 18.sp
            )
            Text(
                modifier = modifier,
                text = throwable.mapToString(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.tertiary,
                fontFamily = FontNunito.regular(),
                fontSize = 14.sp
            )
            SauceThrowableRetryButton(
                modifier = modifier.align(Alignment.CenterHorizontally),
                onClick = onClickRetry
            )
        }
    }
}