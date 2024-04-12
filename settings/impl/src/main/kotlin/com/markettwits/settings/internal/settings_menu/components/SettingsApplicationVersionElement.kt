package com.markettwits.settings.internal.settings_menu.components

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.R
import com.markettwits.core_ui.components.OnBackgroundCard
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.LocalDarkOrLightTheme


@Composable
fun SettingsApplicationVersionElement(
    modifier: Modifier = Modifier,
) {
    val image =
        if (LocalDarkOrLightTheme.current)
            R.drawable.ic_launcher_black_foreground
        else
            R.drawable.ic_launcher_light_foreground
    val context = LocalContext.current

    val versionName: String = context.packageManager
        .getPackageInfo(context.packageName, 0).versionName

    val versionNumber: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        context.packageManager
            .getPackageInfo(context.packageName, 0).longVersionCode.toString()
    } else {
        context.packageManager
            .getPackageInfo(context.packageName, 0).versionCode.toString()
    }

    OnBackgroundCard(modifier = modifier) {
        Row(
            modifier = modifier
                .padding(4.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(70.dp),
                painter = painterResource(id = image),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Column {
                Text(
                    text = "Спорт Союз",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontFamily = FontNunito.semiBoldBold,
                )
                Text(
                    text = "Версия $versionName Сборка $versionNumber",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.outline,
                    fontFamily = FontNunito.semiBoldBold,
                )
            }
        }
    }
}