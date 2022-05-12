package com.kyg.sharegifticon.view

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.google.android.material.datepicker.MaterialDatePicker
import com.kyg.sharegifticon.Constants
import com.kyg.sharegifticon.R
import com.kyg.sharegifticon.databinding.ActivityAddGifticonBinding
import com.kyg.sharegifticon.model.Category
import com.kyg.sharegifticon.viewmodel.AddGifticonActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class AddGigticonActivity : ShareGifticonBaseActivity<ActivityAddGifticonBinding>() {
    private val addGifticonActivityViewModel: AddGifticonActivityViewModel by viewModels()
    private lateinit var SAFLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActivity()
        initSelectCategory()
        initIvAddImage()
        initEtAddBarcodeNumber()
        initBtDatePicker()
        initBtAddGifticon()
        observeUploadProgress()
    }

    private fun initActivity() {
        initDataBinding(R.layout.activity_add_gifticon)
        getDataBinding().addGifticonActivityViewModel = this.addGifticonActivityViewModel
        initSAFLauncher()
    }

    private fun initIvAddImage() {
        with(getDataBinding().ivAddImage) {
            setOnClickListener {
                if (isPermissionGranted(Constants.permissionList)) {
                    openSAF()
                }
            }
            addGifticonActivityViewModel.selectedImage.observe(this@AddGigticonActivity) {
                Glide.with(this).load(addGifticonActivityViewModel.selectedImage.value).into(this)
            }
        }
    }

    private fun observeUploadProgress() {
        addGifticonActivityViewModel.uploadProgress.observe(this@AddGigticonActivity) {
            showToast(getString(R.string.PROGRESS).plus(String.format("%.0f", it)).plus("%"))
        }
    }

    private fun initEtAddBarcodeNumber() {
        val etAddBarcodeNumber = getDataBinding().etAddBarcodeNumber
        etAddBarcodeNumber.setOnEditorActionListener(object :
            TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    val imm: InputMethodManager =
                        this@AddGigticonActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(etAddBarcodeNumber.windowToken, 0)
                    etAddBarcodeNumber.clearFocus()
                    return true
                }

                return false
            }
        })

        getDataBinding().etAddBarcodeNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                addGifticonActivityViewModel.enteredBarcode = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun initBtDatePicker() {
        getDataBinding().btDatePicker.setOnClickListener {
            showDatePicker()
        }
    }

    private fun initSelectCategory() {
        val items = arrayListOf<String>().apply {
            add(getString(R.string.CATEGORY_CAFE))
            add(getString(R.string.CATEGORY_CONVENIENCE_STORE))
            add(getString(R.string.CATEGORY_ETC))
        }
        val adapter = ArrayAdapter(this, R.layout.expose_dropdown_item, items)
        getDataBinding().tvSelectCategory.setAdapter(adapter)
        (getDataBinding().selectCategory.editText as AutoCompleteTextView).setOnItemClickListener { _, _, position, _ ->
            addGifticonActivityViewModel.selectedCategory =
                when (adapter.getItem(position).toString()) {
                    getString(R.string.CATEGORY_CAFE) -> {
                        Category.CAFE.toString()
                    }
                    getString(R.string.CATEGORY_CONVENIENCE_STORE) -> {
                        Category.CONVENIENCE_STORE.toString()
                    }
                    getString(R.string.CATEGORY_ETC) -> {
                        Category.ETC.toString()
                    }
                    else -> {
                        Category.ETC.toString()
                    }
                }
        }
    }

    private fun initBtAddGifticon() {
        getDataBinding().btAddGifticon.setOnClickListener {
            with(addGifticonActivityViewModel) {
                if (inputCheck()) {
                    btAddGifticonClicked()
                } else {
                    showToast(getString(R.string.VALIDATION_FAIL))
                }
            }
        }
    }

    private fun showDatePicker() {
        val defaultSelection: Long = MaterialDatePicker.todayInUtcMilliseconds()
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.PICK_EXPIRATION_DATE))
            .setSelection(defaultSelection)
            .build().apply {
                addOnPositiveButtonClickListener {
                    var dateFormat = SimpleDateFormat(Constants.myDateFormat)
                    val selectedDate = dateFormat.format(it)
                    addGifticonActivityViewModel.selectedDate = selectedDate
                    dateFormat = SimpleDateFormat(Constants.myDateFormatWithSeparator)
                    getDataBinding().btDatePicker.text = dateFormat.format(it)
                }
            }
        datePicker.show(supportFragmentManager, "")
    }

    private fun openSAF() {
        val intent: Intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        SAFLauncher.launch(intent)
    }

    private fun initSAFLauncher() {
        SAFLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
                if (activityResult.resultCode == RESULT_OK) {
                    activityResult.data?.let { data ->
                        addGifticonActivityViewModel.selectedImage.postValue(data.data)
                    }
                }
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.permissionRequestCode) {
            if (grantResults.isNotEmpty()) {
                for (permissionIndex in permissions.indices) {
                    if (grantResults[permissionIndex] == PackageManager.PERMISSION_GRANTED) {
                        // 권한이 승인된 경우
                    } else if (grantResults[permissionIndex] == PackageManager.PERMISSION_DENIED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                permissions[permissionIndex]
                            )
                        ) {
                            // 권한이 거절된 경우
                        } else {
                            // 다시 묻지 않음을 선택하거나 기기 정책으로 인해 앱이 해당 권한을 갖지 못하도록 금지했을 경우
                            showPositiveAlertDialog(
                                title = getString(R.string.PERMISSION_ALERT_DIALOG_TITLE),
                                message = getString(R.string.PERMISSION_ALERT_DIALOG_MESSAGE)
                            ) { _, _ -> super.moveToSetting() }
                        }
                    }
                }
            }
        }
    }
}