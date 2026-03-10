package com.markettwits.core.errors.impl.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.core.errors.api.throwable.mapToString
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
internal fun SauceThrowableMediumContent(
    modifier: Modifier = Modifier,
    sauceError: SauceError,
    onClickRetry: (() -> Unit)? = null,
) {
    val localUriHandler = LocalUriHandler.current

    Card(
        modifier = modifier
            .padding(24.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        MaterialTheme.colorScheme.tertiaryContainer,
                        RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(36.dp),
                    imageVector = sauceError.mapToMaterialIcon(),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }

            Spacer(modifier = Modifier.padding(top = 16.dp))

            Text(
                text = "Возникла ошибка",
                color = MaterialTheme.colorScheme.tertiary,
                fontFamily = FontNunito.bold(),
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.padding(top = 8.dp))

            Text(
                text = sauceError.mapToString(),
                color = MaterialTheme.colorScheme.outline,
                fontFamily = FontNunito.regular(),
                fontSize = 13.sp,
                lineHeight = 18.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.padding(top = 20.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (onClickRetry != null) {
                    OutlinedButton(
                        onClick = onClickRetry,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                        )
                    ) {
                        Text(
                            text = "Повторить попытку",
                            fontFamily = FontNunito.bold(),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onTertiary
                        )
                    }
                }

                TextButton(
                    onClick = {
                        localUriHandler.openUri("https://t.me/sportsoyuznsk")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Сообщить об ошибке",
                        fontFamily = FontNunito.medium(),
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}

private fun SauceError.mapToMaterialIcon(): ImageVector =
    when (this) {
        is SauceError.Connection -> Icons.Default.CloudOff
        is SauceError.Empty -> Icons.Default.SearchOff
        is SauceError.General -> Icons.Default.Error
        is SauceError.JsonConverter -> Icons.Default.Warning
        is SauceError.NotFound -> Icons.Default.QuestionMark
        is SauceError.WrongRequest -> Icons.Default.Error
    }