package com.kyg.sharegifticon.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kyg.sharegifticon.databinding.ItemCategoryBinding
import com.kyg.sharegifticon.model.ItemCategory

class ItemCategoryAdapter(private val clickCallback: (ItemCategory) -> Unit) :
    ListAdapter<ItemCategory, ItemCategoryAdapter.ItemCategoryViewHolder>(diffUtil) {
    inner class ItemCategoryViewHolder(private val itemCategoryBinding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(itemCategoryBinding.root) {
        fun bind(itemCategory: ItemCategory) {
            with(itemCategoryBinding.tvItemCategory) {
                text = itemCategory.categoryName
                setOnClickListener {
                    clickCallback(itemCategory)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCategoryViewHolder {
        return ItemCategoryViewHolder(
            ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemCategoryViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ItemCategory>() {
            override fun areItemsTheSame(oldItem: ItemCategory, newItem: ItemCategory): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ItemCategory, newItem: ItemCategory): Boolean {
                return oldItem == newItem
            }

        }
    }
}