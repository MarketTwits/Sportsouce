package com.markettwits.news_list.presentation.components

import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.news.R
import com.markettwits.news_list.domain.NewsInfo
import de.charlex.compose.material3.HtmlText

@Composable
fun NewsItemCard(modifier: Modifier = Modifier, newsInfo: NewsInfo, onCLick: (NewsInfo) -> Unit) {
    val imageModifier = Modifier.background(
        brush = Brush.verticalGradient(
            colors = listOf(
                Color.Transparent,
                Color.Black.copy(alpha = .55f),
            ),
        )
    )
    val imageModifierError = Modifier.background(
        brush = Brush.verticalGradient(
            colors = listOf(
                SportSouceColor.VeryLighBlue.copy(alpha = .35f),
                SportSouceColor.SportSouceLighBlue.copy(alpha = .65f),
            ),
        )
    )
    Box(
        modifier = modifier
            .padding(10.dp)
            .clip(Shapes.medium)
            .size(width = 270.dp, height = 160.dp)
            .clickable {
                onCLick(newsInfo)
            }

    ) {
        SubcomposeAsyncImage(
            model = newsInfo.imageUrl,
            contentDescription = "",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxSize(),
            error = {
                Box(
                    modifier = imageModifierError
                        .fillMaxSize()
                )
            },
            success = {
                SubcomposeAsyncImageContent()
            }
        )
        Box(modifier = imageModifier.fillMaxSize())
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(horizontal = 15.dp)
        ) {
            Text(
                overflow = TextOverflow.Ellipsis,
                text = newsInfo.title,
                lineHeight = 10.sp,
                maxLines = 1,
                fontSize = 12.sp,
                fontFamily = FontNunito.bold,
                color = Color.White,
            )
            HtmlText(
                overflow = TextOverflow.Ellipsis,
                text = newsInfo.fullDescription,
                maxLines = 2,
                lineHeight = 10.sp,
                fontSize = 10.sp,
                fontFamily = FontNunito.regular,
                color = Color.White,
                colorMapping = mapOf(Color.White to Color.White)
            )
        }
    }
}

@Preview
@Composable
fun NewsItemCardPreview() {
    NewsItemCard(
        newsInfo = NewsInfo(
            id = 0,
            title = "Test title",
            shortDescription = "Test title",
            fullDescription = "",
            imageUrl = "",
            createData = "18 marth 2023 year",
            emptyList()
        )
    ) {}
}