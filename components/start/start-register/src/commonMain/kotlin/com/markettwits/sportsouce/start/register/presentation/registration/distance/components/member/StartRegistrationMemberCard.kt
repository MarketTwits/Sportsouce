package com.markettwits.sportsouce.start.register.presentation.registration.distance.components.member

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.extensions.noRippleClickable
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.start.register.domain.StartStatement
import com.markettwits.sportsouce.start.register.presentation.registration.member.domain.RegistrationMemberValidatorBase

@Composable
internal fun StartRegistrationMemberCard(
    modifier: Modifier = Modifier,
    startStatement: StartStatement,
    isValidationError: Boolean = false,
    validationAttemptTick: Int = 0,
    onClickStartStatement: (StartStatement) -> Unit,
) {

    RegistrationMemberValidatorBase()
        .validateFields(startStatement).fold(onSuccess = {
            UserCardAvailable(
                modifier = modifier.noRippleClickable {
                    onClickStartStatement(startStatement)
                },
                name = startStatement.name,
                gender = startStatement.sex,
                birthDate = startStatement.birthday,
                age = startStatement.getSaveAge(),
                city = startStatement.city,
                team = startStatement.team,
                phone = startStatement.phone,
                email = startStatement.email,
                onClick = { onClickStartStatement(startStatement) }
            )
        }, onFailure = {
            UserCardUnAvailable(
                modifier = modifier.noRippleClickable {
                    onClickStartStatement(startStatement)
                },
                isValidationError = isValidationError,
                validationAttemptTick = validationAttemptTick,
                onClick = { onClickStartStatement(startStatement) }
            )
        }
        )
}

@Composable
internal fun UserCardUnAvailable(
    modifier: Modifier = Modifier,
    isValidationError: Boolean = false,
    validationAttemptTick: Int = 0,
    onClick: () -> Unit,
) {
    val pulseTransition = rememberInfiniteTransition(label = "invalid_member_pulse")
    val pulseScale = pulseTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900),
            repeatMode = RepeatMode.Reverse
        ),
        label = "invalid_member_pulse_scale"
    )
    val shakeOffset = remember { Animatable(0f) }
    LaunchedEffect(validationAttemptTick, isValidationError) {
        if (isValidationError && validationAttemptTick > 0) {
            shakeOffset.snapTo(0f)
            shakeOffset.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = 520
                    -16f at 60
                    16f at 120
                    -14f at 180
                    14f at 240
                    -10f at 300
                    10f at 360
                    -6f at 420
                    0f at 520
                }
            )
        }
    }

    val borderColor =
        if (isValidationError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline
    val editContainerColor =
        if (isValidationError) MaterialTheme.colorScheme.error.copy(alpha = 0.20f)
        else MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
    val editContentColor =
        if (isValidationError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .offset(x = shakeOffset.value.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.elevatedCardElevation(2.dp),
            border = BorderStroke(1.dp, borderColor),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Имя участника",
                            color = MaterialTheme.colorScheme.outline,
                            fontFamily = FontNunito.bold(),
                            fontSize = 18.sp,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Пол",
                                color = MaterialTheme.colorScheme.outline,
                                fontFamily = FontNunito.semiBoldBold(),
                                fontSize = 14.sp,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Dot()
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Дата рождения",
                                color = MaterialTheme.colorScheme.outline,
                                fontFamily = FontNunito.semiBoldBold(),
                                fontSize = 14.sp,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Dot()
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Возраст",
                                color = MaterialTheme.colorScheme.outline,
                                fontFamily = FontNunito.semiBoldBold(),
                                fontSize = 14.sp,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    IconButton(
                        modifier = Modifier.graphicsLayer {
                            scaleX = pulseScale.value
                            scaleY = pulseScale.value
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = editContainerColor,
                            contentColor = editContentColor
                        ),
                        onClick = onClick
                    ) {
                        Icon(
                            modifier = Modifier.padding(10.dp),
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                DashedLine(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Город: ",
                    color = MaterialTheme.colorScheme.outline,
                    fontFamily = FontNunito.semiBoldBold(),
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Команда: ",
                    color = MaterialTheme.colorScheme.outline,
                    fontFamily = FontNunito.semiBoldBold(),
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Телефон: ",
                    color = MaterialTheme.colorScheme.outline,
                    fontFamily = FontNunito.semiBoldBold(),
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Почта: ",
                    color = MaterialTheme.colorScheme.outline,
                    fontFamily = FontNunito.semiBoldBold(),
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
internal fun UserCardAvailable(
    modifier: Modifier = Modifier,
    name: String,
    gender: String,
    birthDate: String,
    age: Int,
    city: String,
    team: String,
    phone: String,
    email: String,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(6.dp),
        border = BorderStroke(4.dp, MaterialTheme.colorScheme.secondary),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = name,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = FontNunito.bold(),
                        fontSize = 18.sp,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Gender, Birth Date, and Age
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = gender,
                            color = MaterialTheme.colorScheme.outline,
                            fontFamily = FontNunito.semiBoldBold(),
                            fontSize = 14.sp,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Dot()
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = birthDate,
                            color = MaterialTheme.colorScheme.outline,
                            fontFamily = FontNunito.semiBoldBold(),
                            fontSize = 14.sp,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Dot()
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "$age лет",
                            color = MaterialTheme.colorScheme.outline,
                            fontFamily = FontNunito.semiBoldBold(),
                            fontSize = 14.sp,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                        contentColor = MaterialTheme.colorScheme.secondary
                    ),
                    onClick = onClick
                ) {
                    Icon(
                        modifier = Modifier.padding(10.dp),
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                    )
                }
            }
            // Name

            Spacer(modifier = Modifier.height(8.dp))
            DashedLine(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(8.dp))

            // City
            Text(
                text = "Город: $city",
                color = MaterialTheme.colorScheme.outline,
                fontFamily = FontNunito.semiBoldBold(),
                fontSize = 14.sp,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Team
            Text(
                text = "Команда: $team",
                color = MaterialTheme.colorScheme.outline,
                fontFamily = FontNunito.semiBoldBold(),
                fontSize = 14.sp,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Phone
            Text(
                text = "Телефон: $phone",
                color = MaterialTheme.colorScheme.outline,
                fontFamily = FontNunito.semiBoldBold(),
                fontSize = 14.sp,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Email
            Text(
                text = "Почта: $email",
                color = MaterialTheme.colorScheme.outline,
                fontFamily = FontNunito.semiBoldBold(),
                fontSize = 14.sp,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun Dot() {
    Box(
        modifier = Modifier
            .size(6.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.tertiary)
    )
}

@Composable
private fun DashedLine(
    color: Color = Color.Gray,
    lineLength: Float = 10f,
    spaceLength: Float = 10f,
    lineThickness: Float = 1f,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(1.dp),
) {
    Canvas(modifier = modifier) {
        var startX = 0f
        while (startX < size.width) {
            val endX = (startX + lineLength).coerceAtMost(size.width)
            drawLine(
                color = color,
                start = Offset(startX, size.height / 2),
                end = Offset(endX, size.height / 2),
                strokeWidth = lineThickness
            )
            startX += lineLength + spaceLength
        }
    }
}
