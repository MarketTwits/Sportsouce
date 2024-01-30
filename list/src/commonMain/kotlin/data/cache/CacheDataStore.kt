package data.cache

import com.markettwits.random_user.RandomUser
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.extensions.listStoreOf
import okio.Path.Companion.toPath

//val store: KStore<List<RandomUser>> = listStoreOf("$appStorage/saved.json".toPath())