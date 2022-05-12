package com.kyg.sharegifticon.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kyg.sharegifticon.R
import com.kyg.sharegifticon.databinding.MemberItemBinding
import com.kyg.sharegifticon.model.Member

class MemberListAdapter:
    ListAdapter<Member, MemberListAdapter.MemberListItemViewHolder>(diffUtil) {
    inner class MemberListItemViewHolder(private val memberItemBinding: MemberItemBinding) :
        RecyclerView.ViewHolder(memberItemBinding.root) {
        fun bind(memberItem: Member) {
            with(memberItemBinding) {
                tvMemberUId.text = memberItem.uId
                tvMemberName.text = memberItem.uName
                Glide.with(memberItemBinding.root.context)
                    .load(memberItem.uPhotoUrl).placeholder(R.drawable.ic_baseline_person_24)
                    .fitCenter()
                    .into(ivMemberPhoto)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberListItemViewHolder {
        return MemberListItemViewHolder(
            MemberItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MemberListItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Member>() {
            override fun areItemsTheSame(oldItem: Member, newItem: Member) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Member, newItem: Member) =
                oldItem == newItem
        }
    }
}