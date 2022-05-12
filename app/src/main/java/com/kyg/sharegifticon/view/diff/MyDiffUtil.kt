package com.kyg.sharegifticon.view.diff

import androidx.recyclerview.widget.DiffUtil

class MyDiffUtil<T> {
    val defaultDiffUtil = object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T) = oldItem == newItem
        override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem == newItem
    }
}
