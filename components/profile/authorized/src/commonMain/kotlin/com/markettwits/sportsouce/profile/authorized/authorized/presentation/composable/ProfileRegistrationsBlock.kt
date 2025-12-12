package com.markettwits.sportsouce.profile.authorized.authorized.presentation.composable

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.presentation.simple_list.StartOrderSimpleCard

@Composable
internal fun ProfileRegistrationsBlock(
    modifier: Modifier = Modifier,
    registrations: List<StartOrderInfo>,
    onClickRegistration: (StartOrderInfo) -> Unit,
    onClickViewAll: () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        if (registrations.isEmpty()) {
            ProfileRegistrationsEmptyBlock(
                onClickViewAll = onClickViewAll
            )
        } else {
            ProfileRegistrationsContent(
                registrations = registrations.take(3),
                onClickRegistration = onClickRegistration,
                onClickViewAll = onClickViewAll,
                totalCount = registrations.size
            )
        }
    }
}

@Composable
private fun ProfileRegistrationsContent(
    modifier: Modifier = Modifier,
    registrations: List<StartOrderInfo>,
    onClickRegistration: (StartOrderInfo) -> Unit,
    onClickViewAll: () -> Unit,
    totalCount: Int,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(top = 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Мои регистрации",
                fontSize = 18.sp,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.onBackground
            )

            TextButton(
                onClick = onClickViewAll,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (totalCount > 3) "Все $totalCount" else "Показать все",
                    fontSize = 13.sp,
                    fontFamily = FontNunito.medium(),
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
        ) {
            registrations.forEach { registration ->
                Card(
                    modifier = Modifier.width(280.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent,
                    )
                ) {
                    StartOrderSimpleCard(
                        modifier = Modifier.padding(0.dp),
                        start = registration,
                        onItemClick = onClickRegistration
                    )
                }
            }
        }
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun ProfileRegistrationsEmptyBlock(
    modifier: Modifier = Modifier,
    onClickViewAll: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(vertical = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Мои регистрации",
                fontSize = 18.sp,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.onBackground
            )

            TextButton(
                onClick = onClickViewAll,
                enabled = false,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Показать все",
                    fontSize = 13.sp,
                    fontFamily = FontNunito.medium(),
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Нет активных регистраций",
                fontSize = 15.sp,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
            )
            Text(
                text = "Зарегистрируйтесь на старт",
                fontSize = 13.sp,
                fontFamily = FontNunito.regular(),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }
    }
}
