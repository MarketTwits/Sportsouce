package com.markettwits.sportsouce.profile.members.member_common.data.cache

import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageSingleCache
import com.markettwits.cahce.store_wrapper.listStoreOfWrapper
import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember

private const val FILE_NAME = "sportsouce.profile.members"

private val profileMemberCache =
    listStoreOfWrapper<ProfileMember>(InStorageCacheDirectory.path, FILE_NAME)

class ProfileMemberCache : InStorageSingleCache<List<ProfileMember>>(profileMemberCache)