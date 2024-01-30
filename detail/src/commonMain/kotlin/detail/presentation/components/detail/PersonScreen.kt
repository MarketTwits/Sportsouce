package detail.presentation.components.detail

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.markettwits.random_user.RandomUser
import com.seiko.imageloader.rememberImagePainter
import detail.presentation.components.BackFloatingActionButton


@Composable
private fun PersonError() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(Icons.Filled.Refresh, null)
        Text(text = "Ошибка в загрузке")

    }
}

@Composable
fun PersonLoaded(
    modifier: Modifier = Modifier,
    user: RandomUser,
    pop: () -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        val painter = rememberImagePainter(user.picture)
        Image(
            painter = painter,
            contentScale = ContentScale.FillWidth,
            colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(3f) }),
            modifier = Modifier
                .widthIn(max = 800.dp)
                .fillMaxWidth(.9f)
                .wrapContentHeight(Alignment.Top, true)
                .scale(1f, 2.0f)
                .blur(70.dp, BlurredEdgeTreatment.Unbounded)
                .alpha(.5f),
            contentDescription = "image",
        )

        Column {
            Row(
                horizontalArrangement = Arrangement.Start,
            ) {
                BackFloatingActionButton(
                    back = pop::invoke
                )
            }
            LazyColumn(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                item {
                    val painter = rememberImagePainter(user.picture)
                    Image(
                        painter = painter,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .size(220.dp)
                            .border(
                                BorderStroke(2.dp, Color.LightGray),
                                RoundedCornerShape(20.dp)
                            ),
                        contentDescription = "image",
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item {
                    Text(
                        user.name.last,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item {
                    Text(
                        "Birthday: ${user.ageParam.date}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item {
                    Text(
                        user.email,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = modifier.clickable(
                            onClick = {
                                //  context.sendEmail(userData.email)
                            }
                        )
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item {
                    Text(
                        "Location: ${user.location.city}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = modifier
                            .clickable(
                                onClick = {
                                    // context.openLocationOnMap(userData.location)
                                }
                            )
                            .widthIn(0.dp, 300.dp),
                        textAlign = TextAlign.Center,
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item {
                    Text(
                        "Geo: ${user.location.coordinates.latitude}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = modifier.clickable(
                            onClick = {
                                // context.openGeoOnMap(userData.geo)
                            }
                        )
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item {
                    Text(
                        "Phone: ${user.phone}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = modifier.clickable(
                            onClick = {
                                // context.callPhone(userData.phone)
                            }
                        )
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

    }
}

@Composable
private fun PersonLoading() {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val colorAnim by infiniteTransition.animateColor(
        initialValue = Color.LightGray,
        targetValue = Color.Gray,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    val settings = Modifier
        .background(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    colorAnim,
                    colorAnim,
                    MaterialTheme.colorScheme.background,
                )
            )
        )
        .graphicsLayer {
            clip = true // don't forget this
            shape = CircleShape
        }
    LazyColumn(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Box(
                modifier = settings
                    .size(120.dp)

            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            Box(
                modifier = settings
                    .height(22.dp)
                    .width(180.dp)
            )

        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Box(
                modifier = settings
                    .height(22.dp)
                    .width(160.dp)
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Box(
                modifier = settings
                    .height(22.dp)
                    .width(180.dp)
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
