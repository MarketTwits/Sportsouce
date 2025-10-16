package com.markettwits.sportsouce.club.info.presentation.components.plan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.sportsouce.club.info.presentation.components.common.SubscribeGradientButton


@Composable
internal fun ClubPlanContent(
    onClickSubscribe: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(start = 10.dp),
        ) {
            PlanStep(
                stepNumber = 1,
                text = "Записаться и прийти на пробную тренировку",
                isLast = false,
                hasGradientBackground = false
            )

            PlanStep(
                stepNumber = 2,
                text = "После тренировки поможем выбрать абонемент и нужное количество тренировок",
                isLast = false,
                hasGradientBackground = false
            )

            PlanStep(
                stepNumber = 3,
                text = "Выберем цель. Если необходимо можно добавить индивидуальный план",
                isLast = false,
                hasGradientBackground = false
            )

            PlanStep(
                stepNumber = 4,
                text = "Получаем удовольствие от тренировок, общения в команде, и эффективно достигаем результата",
                isLast = true,
                hasGradientBackground = true
            )
        }



        Spacer(modifier = Modifier.height(32.dp))

        SubscribeGradientButton(onClick = onClickSubscribe)

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun PlanStep(
    stepNumber: Int,
    text: String,
    isLast: Boolean,
    hasGradientBackground: Boolean = false,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        brush = if (hasGradientBackground) {
                            androidx.compose.ui.graphics.Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF5AE4C0),
                                    Color(0xFF70BFF5)
                                )
                            )
                        } else {
                            androidx.compose.ui.graphics.Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF42A5F5),
                                    Color(0xFF42A5F5)
                                )
                            )
                        },
                        shape = androidx.compose.foundation.shape.CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stepNumber.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }

            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(60.dp)
                        .background(MaterialTheme.colorScheme.secondary)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = text,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .weight(1f)
                .padding(top = 4.dp)
        )
    }

    if (!isLast) {
        Spacer(modifier = Modifier.height(8.dp))
    }
}
