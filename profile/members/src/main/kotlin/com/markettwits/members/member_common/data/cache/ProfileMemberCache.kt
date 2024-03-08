package com.markettwits.members.member_common.data.cache

import com.markettwits.cahce.InStorageCache
import com.markettwits.cahce.InStorageSingleCache
import com.markettwits.cahce.store.listStoreOfWrapper
import com.markettwits.members.member_common.domain.ProfileMember

private val profileMemberCache =
    listStoreOfWrapper<ProfileMember>(InStorageCache.path, "profileMemberCache")

class ProfileMemberCache : InStorageSingleCache<List<ProfileMember>>(profileMemberCache)