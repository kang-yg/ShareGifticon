package com.kyg.sharegifticon.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment

open class BaseDialogFragment<V : ViewDataBinding>(@LayoutRes private val layoutRes: Int) :
    DialogFragment() {
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

    fun setDialogSize() {
        val params = dialog?.window?.attributes.apply {
            this?.width = WindowManager.LayoutParams.MATCH_PARENT
            this?.height = WindowManager.LayoutParams.WRAP_CONTENT
        }
        dialog?.window?.attributes = params
    }
}