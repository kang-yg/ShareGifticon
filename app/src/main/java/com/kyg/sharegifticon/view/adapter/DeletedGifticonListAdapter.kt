package com.kyg.sharegifticon.view.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kyg.sharegifticon.R
import com.kyg.sharegifticon.databinding.DeletedGifticonItemBinding
import com.kyg.sharegifticon.model.ItemGifticon

class DeletedGifticonListAdapter(
    private val itemClickCallBack: (ItemGifticon) -> Unit,
    private val diffUtil: DiffUtil.ItemCallback<ItemGifticon>
) :
    BaseListAdapter<ItemGifticon, DeletedGifticonItemBinding, DeletedGifticonListAdapter.DeletedGifticonAdapterViewHolder>(
        diffUtil,
        R.layout.deleted_gifticon_item
    ) {
    inner class DeletedGifticonAdapterViewHolder(private val deletedGifticonItemBinding: DeletedGifticonItemBinding) :
        RecyclerView.ViewHolder(deletedGifticonItemBinding.root) {
        fun bind(itemGifticon: ItemGifticon) {
            with(deletedGifticonItemBinding.ivDeletedGifticon) {
                Glide.with(this.context).load(itemGifticon.imageSrc).fitCenter().into(this)
            }
            deletedGifticonItemBinding.tvDeletedGifticonRegistrant.text = itemGifticon.registrant

            with(deletedGifticonItemBinding.tvDeletedGifticonExpirationDate) {
                val expirationDate =
                    String.format(
                        "%s: ~%s",
                        this.context.getString(R.string.EXPIRATION_DATE),
                        itemGifticon.expirationDate
                    )
                text = expirationDate
            }
            deletedGifticonItemBinding.cvDeletedGifticon.setOnClickListener {
                itemClickCallBack(itemGifticon)
            }
        }
    }

    override fun returnViewHolder(dataBinding: DeletedGifticonItemBinding): DeletedGifticonAdapterViewHolder {
        return DeletedGifticonAdapterViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: DeletedGifticonAdapterViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}