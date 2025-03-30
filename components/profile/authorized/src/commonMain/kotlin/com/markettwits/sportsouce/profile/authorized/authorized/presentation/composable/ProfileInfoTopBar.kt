package com.markettwits.sportsouce.profile.authorized.authorized.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
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
import com.markettwits.core_ui.items.extensions.noRippleClickable
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes

@Composable
internal fun ProfileInfoTopBar(
    modifier: Modifier = Modifier,
    imageUrl: String,
    userName: String,
    onClickImage: (String) -> Unit,
    onClickEditProfile: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(12.dp)
            .fillMaxWidth()
            .noRippleClickable(onClick = onClickEditProfile),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SubcomposeAsyncImage(
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(RoundedCornerShape(100.dp))
                .size(70.dp),
            model = imageRequestCrossfade(model = imageUrl),
            contentDescription = "",
            success = {
                SubcomposeAsyncImageContent(
                    modifier = modifier
                        .clickable {
                            onClickImage(imageUrl)
                        },
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
        Text(
            text = userName,
            fontFamily = FontNunito.bold(),
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(Modifier.width(8.dp))
        Box(
            modifier = Modifier.clip(Shapes.medium)
        ) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "go to edit profile",
                tint = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}