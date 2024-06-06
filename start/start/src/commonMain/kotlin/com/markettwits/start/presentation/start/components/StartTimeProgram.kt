package com.markettwits.start.presentation.start.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.start.domain.StartItem

@Composable
internal fun StartTimeProgram(
    modifier: Modifier = Modifier,
    startTimes: StartItem.StartTimes
) {
    StartContentBasePanel(
        modifier = modifier,
        label = "Программа соревнований",
    ) {
        Column(modifier = Modifier.padding(bottom = 10.dp)) {
            StartTimeItemRowInfo(
                modifier = Modifier.padding(horizontal = 10.dp),
                title = "Начало регистрации",
                value = startTimes.beginningRegistry
            )
            StartTimeItemRowInfo(
                modifier = Modifier.padding(horizontal = 10.dp),
                title = "Конец регистрации",
                value = startTimes.endRegistry
            )
            StartTimeItemRowInfo(
                modifier = Modifier.padding(horizontal = 10.dp),
                title = "Начало старта",
                value = startTimes.beginningStart
            )
            StartTimeItemRowInfo(
                modifier = Modifier.padding(horizontal = 10.dp),
                title = "Конец старта",
                value = startTimes.endStart
            )
        }

    }
}

@Composable
private fun StartTimeItemRowInfo(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    isHtml: Boolean = false,
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier,
            textAlign = TextAlign.Start,
            text = "$title:",
            fontSize = 14.sp,
            overflow = TextOverflow.Ellipsis,
            fontFamily = FontNunito.semiBoldBold(),
            color = MaterialTheme.colorScheme.onPrimary
        )
        (if (isHtml) {
            HtmlText(
                modifier = Modifier,
                textAlign = TextAlign.Start,
                text = value,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp,
                lineHeight = 12.sp,
                fontFamily = FontNunito.semiBoldBold(),
                color = MaterialTheme.colorScheme.outline
            )
        } else {
            Text(
                modifier = Modifier,
                textAlign = TextAlign.Start,
                text = value,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp,
                fontFamily = FontNunito.semiBoldBold(),
                color = MaterialTheme.colorScheme.outline
            )
        })
    }
}