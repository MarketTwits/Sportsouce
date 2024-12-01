package com.markettwits.start.register.presentation.registration.presentation.components.distsance.member

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.start.register.domain.StartStatement
import com.markettwits.start.register.presentation.member.domain.RegistrationMemberValidatorBase

@Composable
internal fun StartRegistrationMemberCard(
    modifier: Modifier = Modifier,
    startStatement: StartStatement,
    onClickStartStatement: (StartStatement) -> Unit
) {

    RegistrationMemberValidatorBase()
        .validateFields(startStatement).fold(onSuccess = {
            UserCardAvailable(
                modifier = modifier.clickable {
                    onClickStartStatement(startStatement)
                },
                name = startStatement.name,
                gender = startStatement.sex,
                birthDate = startStatement.birthday,
                age = startStatement.getSaveAge(),
                city = startStatement.city,
                team = startStatement.team,
                phone = startStatement.phone,
                email = startStatement.email
            )
        }, onFailure = {
           UserCardUnAvailable(
               modifier = modifier.clickable {
                   onClickStartStatement(startStatement)
               }
           )
        }
    )


}

@Composable
internal fun UserCardUnAvailable(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
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

                    // Gender, Birth Date, and Age
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
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                        contentColor = MaterialTheme.colorScheme.secondary
                    ),
                    onClick = { /* Handle edit click */ }) {
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
                text = "Город: ",
                color = MaterialTheme.colorScheme.outline,
                fontFamily = FontNunito.semiBoldBold(),
                fontSize = 14.sp,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Team
            Text(
                text = "Команда: ",
                color = MaterialTheme.colorScheme.outline,
                fontFamily = FontNunito.semiBoldBold(),
                fontSize = 14.sp,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Phone
            Text(
                text = "Телефон: ",
                color = MaterialTheme.colorScheme.outline,
                fontFamily = FontNunito.semiBoldBold(),
                fontSize = 14.sp,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Email
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
    email: String
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
                    onClick = { /* Handle edit click */ }) {
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
private fun EmptyLine(modifier: Modifier = Modifier){
    Box(
        modifier = modifier
            .height(18.dp)
            .fillMaxWidth()
            .clip(Shapes.medium)
            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
    )
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
        .height(1.dp)
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
