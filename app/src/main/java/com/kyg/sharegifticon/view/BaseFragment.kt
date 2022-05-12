package com.kyg.sharegifticon.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.kyg.sharegifticon.R

open class BaseFragment<V: ViewDataBinding>(@LayoutRes private val layoutRes: Int): Fragment() {
    private lateinit var mDataBinding: V

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDataBinding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        return mDataBinding.root
    }

    fun getDataBinding() = this.mDataBinding
}