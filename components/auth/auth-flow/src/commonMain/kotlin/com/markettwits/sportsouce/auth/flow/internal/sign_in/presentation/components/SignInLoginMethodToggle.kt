import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.sportsouce.auth.flow.internal.sign_in.domain.LoginMethod

@Composable
internal fun SignInLoginMethodToggle(
    modifier: Modifier = Modifier,
    currentLoginMethod: LoginMethod,
    onLoginMethodChange: (LoginMethod) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(9.dp)
            .shadow(2.dp, shape = Shapes.medium),
        shape = Shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LoginMethod.entries.forEach { method ->
                when (method) {
                    LoginMethod.PASSWORD -> SelectorOption(
                        modifier = Modifier
                            .padding(4.dp)
                            .weight(1f),
                        text = "Пароль",
                        isSelected = currentLoginMethod == LoginMethod.PASSWORD,
                        onClick = { onLoginMethodChange(LoginMethod.PASSWORD) }
                    )

                    LoginMethod.SMS -> SelectorOption(
                        modifier = Modifier
                            .padding(4.dp)
                            .weight(1f),
                        text = "SMS",
                        isSelected = currentLoginMethod == LoginMethod.SMS,
                        onClick = { onLoginMethodChange(LoginMethod.SMS) },
                        badge = {
                            BetaBadge()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SelectorOption(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    badge: (@Composable () -> Unit)? = null,
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.tertiary
        else MaterialTheme.colorScheme.background
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.onTertiary
        else MaterialTheme.colorScheme.outline
    )
    val padding by animateDpAsState(
        targetValue = if (isSelected) 12.dp else 8.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    Box(
        modifier = modifier
            .clip(Shapes.medium)
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = padding, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                color = textColor,
                fontFamily = FontNunito.semiBoldBold(),
                fontSize = 18.sp
            )
            if (badge != null) {
                Spacer(Modifier.width(6.dp))
                badge()
            }
        }
    }
}

@Composable
fun BetaBadge() {
    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .background(Color(0xFFFF9800))
            .padding(horizontal = 6.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Beta",
            color = Color.White,
            style = MaterialTheme.typography.labelSmall
        )
    }
}
