package com.markettwits.profile.internal.sign_in.domain

/** convert phone in +7 (xxx) xxx-xx-xx to 7xxxxxxxxxx */
fun String.clearPhone(): String = replace(Regex("\\D"), "")