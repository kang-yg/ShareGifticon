package com.kyg.sharegifticon.model

data class ItemCategory(
    val categoryName: String,
    val category: Category
): BaseModel

enum class Category {
    CAFE, CONVENIENCE_STORE, ETC
}