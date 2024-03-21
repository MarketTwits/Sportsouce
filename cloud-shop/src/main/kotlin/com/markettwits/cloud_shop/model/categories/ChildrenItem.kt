package com.markettwits.cloud_shop.model.categories

import kotlinx.serialization.Serializable

@Serializable
data class ChildrenItem(
    val children: List<ChildrenItem>,
    val description: String,
    val has_child: Int,
    val id: Int,
    val level: Int,
    val parent_id: Int,
    val slug: String,
    val title: String
)