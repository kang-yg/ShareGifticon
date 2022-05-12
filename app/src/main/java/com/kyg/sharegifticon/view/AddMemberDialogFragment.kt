package com.kyg.sharegifticon.view

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.kyg.sharegifticon.R
import com.kyg.sharegifticon.databinding.DialogAddMemberBinding

class AddMemberDialogFragment(private val callback: (String, String) -> Unit) :
    BaseDialogFragment<DialogAddMemberBinding>(R.layout.dialog_add_member) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDialogSize()
        initEtAddMemberDialogName()
        initEtAddMemberDialogUid()
        initBtAddMemberConfirm(callback)
        initBtAddMemberCancel()
    }

    private fun initEtAddMemberDialogName() {
        val etAddMemberDialogName = getDataBinding().etAddMemberDialogName
        etAddMemberDialogName.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    etAddMemberDialogName.clearFocus()
                    getDataBinding().etAddMemberDialogUid.requestFocus()
                    return true
                }
                return false
            }
        })
    }

    private fun initEtAddMemberDialogUid() {
        val etAddMemberDialogUid = getDataBinding().etAddMemberDialogUid
        etAddMemberDialogUid.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    val imm: InputMethodManager =
                        this@AddMemberDialogFragment.activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(etAddMemberDialogUid.windowToken, 0)
                    etAddMemberDialogUid.clearFocus()
                    return true
                }
                return false
            }
        })
    }

    private fun initBtAddMemberConfirm(callback: (String, String) -> Unit) {
        getDataBinding().btAddMemberConfirm.setOnClickListener {
            with(getDataBinding()) {
                callback(
                    this.etAddMemberDialogName.text.toString(),
                    this.etAddMemberDialogUid.text.toString()
                )
            }
            this.dialog?.dismiss()
        }
    }

    private fun initBtAddMemberCancel() {
        getDataBinding().btAddMemberCancel.setOnClickListener {
            this.dialog?.dismiss()
        }
    }
}