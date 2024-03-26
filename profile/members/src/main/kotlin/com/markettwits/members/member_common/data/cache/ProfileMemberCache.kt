package com.markettwits.members.member_common.data.cache

import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageSingleCache
import com.markettwits.cahce.store_wrapper.listStoreOfWrapper
import com.markettwits.members.member_common.domain.ProfileMember

private val profileMemberCache =
    listStoreOfWrapper<ProfileMember>(InStorageCacheDirectory.path, "profileMemberCache")

class ProfileMemberCache : InStorageSingleCache<List<ProfileMember>>(profileMemberCache)