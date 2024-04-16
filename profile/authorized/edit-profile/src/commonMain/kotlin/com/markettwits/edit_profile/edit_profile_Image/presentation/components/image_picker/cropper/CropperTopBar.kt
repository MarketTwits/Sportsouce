package com.markettwits.edit_profile.edit_profile_Image.presentation.components.image_picker.cropper

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//internal fun CropperTopBar(state: CropState) {
//    TopAppBar(
//        colors = TopAppBarDefaults.topAppBarColors(
//            containerColor = MaterialTheme.colorScheme.primary,
//            actionIconContentColor = MaterialTheme.colorScheme.tertiary,
//            navigationIconContentColor = MaterialTheme.colorScheme.tertiary
//        ),
//        title = {
//            Text(
//                text = "Изменить фото профиля",
//                fontFamily = FontNunito.bold(),
//                fontSize = 16.sp,
//                color = MaterialTheme.colorScheme.tertiary
//            )
//        },
//        navigationIcon = {
//            IconButton(onClick = { state.done(accept = false) }) {
//                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
//            }
//        },
//        actions = {
//            IconButton(onClick = { state.reset() }) {
//                Icon(Icons.Default.Restore, null)
//            }
//            IconButton(onClick = { state.done(accept = true) }, enabled = !state.accepted) {
//                Icon(Icons.Default.Done, null)
//            }
//        }
//    )
//}
