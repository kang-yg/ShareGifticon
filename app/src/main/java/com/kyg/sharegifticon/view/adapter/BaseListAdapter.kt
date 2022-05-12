package com.kyg.sharegifticon.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kyg.sharegifticon.model.BaseModel

abstract class BaseListAdapter<I: BaseModel, T : ViewDataBinding, VH : RecyclerView.ViewHolder>(
    diffUtil: DiffUtil.ItemCallback<I>,
    @LayoutRes private val layoutRes: Int
) : ListAdapter<I, VH>(diffUtil) {
    private lateinit var mDataBinding: T
    abstract fun returnViewHolder(dataBinding: T): VH
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        mDataBinding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutRes, parent, false)
        return returnViewHolder(mDataBinding)
    }
}