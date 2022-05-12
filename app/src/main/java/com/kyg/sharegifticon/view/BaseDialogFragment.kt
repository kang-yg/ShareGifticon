package com.kyg.sharegifticon.view

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment

open class BaseDialogFragment<V : ViewDataBinding>(@LayoutRes private val layoutRes: Int) : DialogFragment() {
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

    fun setDialogSize(view: View) {
        val windowManager = view.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val deviceWith: Int = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            val size = Point()
            val display: Display = windowManager.defaultDisplay
            display.getSize(size)
            size.x
        } else {
            windowManager.currentWindowMetrics.bounds.bottom
        }
        val params: WindowManager.LayoutParams? = this.dialog?.window?.attributes
        params?.width = (deviceWith * 1).toInt()
        this.dialog?.window?.attributes = params as WindowManager.LayoutParams
    }
}