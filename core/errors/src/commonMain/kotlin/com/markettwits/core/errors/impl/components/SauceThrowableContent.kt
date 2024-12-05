package com.markettwits.core.errors.impl.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.core.errors.api.throwable.mapToString
import com.markettwits.core.errors.impl.supcomponents.SauceThrowableRetryButtonLarge
import com.markettwits.core.errors.impl.supcomponents.mapSauceErrorImage
import com.markettwits.core_ui.items.base_extensions.noRippleClickable
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
internal fun SauceThrowableContent(
    modifier: Modifier = Modifier,
    sauceError: SauceError,
    onClickRetry: (() -> Unit)? = null,
) {

    val localUriHandler = LocalUriHandler.current

    Column(
        modifier = modifier
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier
                .padding(6.dp)
                .size(100.dp),
            imageVector = sauceError.mapSauceErrorImage(),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.secondary
        )
        Text(
            modifier = Modifier.padding(6.dp),
            text = "Что то пошло не так :(",
            color = MaterialTheme.colorScheme.tertiary,
            textAlign = TextAlign.Center,
            fontFamily = FontNunito.bold(),
            fontSize = 20.sp
        )
        Text(
            modifier = Modifier.padding(6.dp),
            text = sauceError.mapToString(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.outline,
            fontFamily = FontNunito.regular(),
            fontSize = 14.sp
        )
        if(onClickRetry != null){
            SauceThrowableRetryButtonLarge(
                modifier = Modifier
                    .padding(8.dp)
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally),
                onClickRetry = onClickRetry
            )
        }
        Text(
            modifier = Modifier
                .padding(8.dp)
                .noRippleClickable {
                    localUriHandler.openUri("https://t.me/sportsoyuznsk")
            },
            text = "Сообщить об ошибке",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.tertiary,
            fontFamily = FontNunito.regular(),
            fontSize = 14.sp
        )
    }
}

