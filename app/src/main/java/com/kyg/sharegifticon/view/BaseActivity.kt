package com.kyg.sharegifticon.view

import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.kyg.sharegifticon.Constants

open class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {
    private lateinit var mDataBinding: T

    fun initDataBinding(@LayoutRes layoutRes: Int) {
        this.mDataBinding = DataBindingUtil.setContentView(this, layoutRes)
    }

    fun getDataBinding() = this.mDataBinding

    fun isPermissionGranted(permissions: ArrayList<String>): Boolean  {
        if (!hasPermissions(this, permissions)) {
            // 권한 없는 경우
            ActivityCompat.requestPermissions(
                this,
                permissions.toTypedArray(),
                Constants.permissionRequestCode
            )
            return false
        }
        return true
    }

    private fun hasPermissions(
        context: Context,
        permissions: ArrayList<String>
    ): Boolean = permissions.all { permission ->
        ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }
}