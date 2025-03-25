package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.extensions.noRippleClickable
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.start.domain.StartItem
import com.markettwits.sportsouce.start.cloud.model.start.fields.DistinctDistance

@Composable
internal fun StartRegistrationPanel(
    modifier: Modifier,
    distance: List<DistinctDistance>,
    startStatus: StartItem.StartStatus,
    regLink: String,
    onClickRegistration: () -> Unit,
) {
    if (distance.isNotEmpty() && startStatus.code == 3 || regLink.isNotEmpty() && startStatus.code == 3) {
        Box(
            modifier = modifier
                .noRippleClickable {
                    onClickRegistration()
                }
                .fillMaxWidth()

        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(76.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                shape = Shapes.medium,
            ) {
                Spacer(modifier = Modifier.height(50.dp))
                if (distance.isNotEmpty()) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Дистанции: ${distance.joinToString(", ") { it.name } ?: ""}",
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 12.sp,
                        fontFamily = FontNunito.regular(),
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                    )
                } else {
                    Spacer(modifier = Modifier.height(12.dp))
                }

            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = {
                    onClickRegistration()
                },
                elevation = ButtonDefaults.buttonElevation(2.dp, 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                shape = Shapes.medium,
            ) {
                Text(
                    text = "Зарегистрироваться",
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontSize = 16.sp,
                    fontFamily = FontNunito.bold(),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}