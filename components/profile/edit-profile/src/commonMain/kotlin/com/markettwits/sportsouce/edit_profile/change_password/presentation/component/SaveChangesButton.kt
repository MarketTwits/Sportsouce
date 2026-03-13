package com.markettwits.sportsouce.edit_profile.change_password.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes

@Composable
fun SaveChangesButton(
    modifier: Modifier = Modifier,
    loading: Boolean,
    enabled: Boolean = !loading,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth(),
        enabled = enabled,
        onClick = {
            onClick()
        },
        shape = Shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
        )
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(25.dp),
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            Text(
                text = "Сохранить изменения",
                color = MaterialTheme.colorScheme.primary,
                fontFamily = FontNunito.bold(),
            )
        }
    }
}
