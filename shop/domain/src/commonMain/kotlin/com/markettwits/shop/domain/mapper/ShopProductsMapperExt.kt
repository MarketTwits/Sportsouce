package com.markettwits.shop.domain.mapper

//import java.text.NumberFormat
//import java.util.Locale

fun mapCloudPriceToDouble(price : Int) : String =
    formatPrice(price / 100)

fun mapCloudPriceToDouble(price : String) : String =
    formatPrice(price.toInt() / 100)


private fun formatPrice(price: Int): String {
   // val formatter = NumberFormat.getInstance(Locale("ru", "RU"))
  //  return formatter.format(price)
    return ""
}