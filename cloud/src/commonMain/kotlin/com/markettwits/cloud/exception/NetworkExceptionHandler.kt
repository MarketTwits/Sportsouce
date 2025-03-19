package com.markettwits.cloud.exception


//suspend fun networkExceptionHandler(exception: Throwable): Exception = when (exception) {
//        is AuthException -> Exception(exception.message)
//      //  is UnknownHostException -> Exception("Проблема интернет соединения")
//        is HttpRequestTimeoutException -> Exception("Неустойчивое интернет соединение")
//       // is SocketTimeoutException -> Exception("Ошибка обработки запроса")
//        is ResponseException -> Exception(exception.response.body<ErrorResponse>().message)
//        is SerializationException -> Exception("Ошибка обработки данных")
//        else -> Exception(exception.message.toString())
//    }
//
//fun Throwable.isNetworkConnectionError(): Boolean {
//    return when (this) {
////        is ConnectException,
////        is SocketException,
////        is SocketTimeoutException,
////        is TimeoutException,
////        is UnknownHostException -> true
//        else -> false
//    }
//}
//fun Throwable.isResponseException(): Boolean =
//    (this is ResponseException || this is SerializationException)

