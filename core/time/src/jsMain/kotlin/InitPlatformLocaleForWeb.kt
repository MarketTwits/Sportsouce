import com.raedghazal.kotlinx_datetime_ext.Locale
import com.raedghazal.kotlinx_datetime_ext.initPlatformLocales

@JsModule("date-fns/locale/ru")
external object DateFnsLocaleRu

fun InitPlatformLocaleForWeb() {
    Locale.initPlatformLocales(DateFnsLocaleRu)
}