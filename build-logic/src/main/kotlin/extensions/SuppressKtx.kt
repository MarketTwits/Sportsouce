package extensions

fun suppressExperimentalCoroutinesApi() = listOf(
    "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
)