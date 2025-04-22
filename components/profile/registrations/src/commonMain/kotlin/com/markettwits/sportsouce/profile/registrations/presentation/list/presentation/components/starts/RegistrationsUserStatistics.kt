package com.markettwits.sportsouce.profile.registrations.presentation.list.presentation.components.starts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
internal fun RegistrationsUserStatistics(
    modifier: Modifier = Modifier,
    userImageUrl: String,
    userName: String,
    registrationsCount: Int
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            SubcomposeAsyncImage(
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(90.dp),
                model = imageRequestCrossfade(model = userImageUrl),
                contentDescription = "",
                success = {
                    SubcomposeAsyncImageContent(
                        modifier = modifier,
                        painter = it.painter
                    )
                },
                error = {
                    Box(modifier = modifier.background(MaterialTheme.colorScheme.tertiaryContainer)) {
                        Icon(
                            modifier = modifier
                                .size(50.dp)
                                .padding(10.dp)
                                .align(Alignment.Center),
                            imageVector = Icons.Default.Person,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            )
            Spacer(Modifier.width(8.dp))
            Column {
                Text(
                    text = userName,
                    fontFamily = FontNunito.bold(),
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = if (registrationsCount == 0)
                        "Нет регистраций"
                    else
                        "$registrationsCount регистраций",
                    fontFamily = FontNunito.medium(),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            Spacer(Modifier.width(8.dp))
        }
    }
}
