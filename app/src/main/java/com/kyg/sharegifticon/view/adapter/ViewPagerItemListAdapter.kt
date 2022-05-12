package com.kyg.sharegifticon.view.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kyg.sharegifticon.R
import com.kyg.sharegifticon.databinding.ViewpagerItemBinding
import com.kyg.sharegifticon.model.ItemGifticon

class ViewPagerItemListAdapter(
    private val itemClickCallBack: (ItemGifticon) -> Unit,
    private val diffUtil: DiffUtil.ItemCallback<ItemGifticon>
) :
    BaseListAdapter<ItemGifticon, ViewpagerItemBinding, ViewPagerItemListAdapter.ViewPagerItemAdapterViewHolder>(
        diffUtil,
        R.layout.viewpager_item
    ) {

    inner class ViewPagerItemAdapterViewHolder(private val viewpagerItemBinding: ViewpagerItemBinding) :
        RecyclerView.ViewHolder(viewpagerItemBinding.root) {
        fun bind(itemGifticon: ItemGifticon) {
            with(viewpagerItemBinding.ivGifticon) {
                Glide.with(this.context).load(itemGifticon.imageSrc).fitCenter().into(this)
                setOnClickListener {
                    itemClickCallBack(itemGifticon)
                }
            }
        }
    }

    override fun returnViewHolder(dataBinding: ViewpagerItemBinding): ViewPagerItemAdapterViewHolder {
        return ViewPagerItemAdapterViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: ViewPagerItemAdapterViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}