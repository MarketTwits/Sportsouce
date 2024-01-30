package presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun FailedScreen(
    modifier: Modifier = Modifier.padding(5.dp),
    title: String = "Something wrong...",
    message: String = "An error occurred while uploading the data",
    onClickRetry: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .align(Alignment.Center)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = modifier,
                text = title,
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )
            Text(
                modifier = modifier,
                text = message,
                textAlign = TextAlign.Center,
                fontSize = 12.sp
            )
            Row(modifier = modifier) {
                OutlinedButton(
                    modifier = modifier.fillMaxWidth(),
                    onClick = {
                        onClickRetry()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text(
                        text = "Retry",
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}