package com.kyg.sharegifticon.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kyg.sharegifticon.R
import com.kyg.sharegifticon.databinding.ActivityManageMemberBinding
import com.kyg.sharegifticon.view.adapter.MemberListAdapter
import com.kyg.sharegifticon.view.helper.MemberListItemTouchHelperCallback
import com.kyg.sharegifticon.viewmodel.ManageMemberActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ManageMemberActivity : ShareGifticonBaseActivity<ActivityManageMemberBinding>() {
    private val manageMemberActivityViewModel: ManageMemberActivityViewModel by viewModels()
    private val memberListAdapter: MemberListAdapter = MemberListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActivity()
        initToolbar()
        initRvMember()
        observeMemberList()
        readMember()
    }

    private fun initActivity() {
        initDataBinding(R.layout.activity_manage_member)
    }

    private fun initToolbar() {
        getDataBinding().manageMemberAppbar.ivToolbarAddMember.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                AddMemberDialogFragment { name, uID ->
                    if (name != "" && uID != "") {
                        manageMemberActivityViewModel.writeFirebaseDatabaseMemberInfo(name, uID)
                        CoroutineScope(Dispatchers.Main).launch {
                            readMember()
                        }
                    } else {
                        showToast(getString(R.string.VALIDATION_FAIL))
                    }
                }.apply {
                    isCancelable = false
                }.show(supportFragmentManager, "")
            }
        }
    }

    private fun readMember() {
        playLottie()
        manageMemberActivityViewModel.readMember()
    }

    private fun playLottie() {
        with(getDataBinding().memberLottie) {
            playAnimation()
            visibility = View.VISIBLE
        }
    }

    private fun pauseLottie() {
        with(getDataBinding().memberLottie) {
            pauseAnimation()
            visibility = View.GONE
        }
    }

    private fun initRvMember() {
        val memberListItemTouchHelperCallback = object : MemberListItemTouchHelperCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val removeTargetMember = memberListAdapter.currentList[position]
                manageMemberActivityViewModel.removeMember(removeTargetMember)
                CoroutineScope(Dispatchers.Main).launch {
                    readMember()
                }
            }
        }
        with(getDataBinding().rvMember) {
            adapter = memberListAdapter
            layoutManager = LinearLayoutManager(this@ManageMemberActivity).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            ItemTouchHelper(memberListItemTouchHelperCallback).attachToRecyclerView(this)
        }
    }

    private fun observeMemberList() {
        manageMemberActivityViewModel.memberList.observe(this@ManageMemberActivity) {
            if (it.isNullOrEmpty()) {
                getDataBinding().ivEmpty.visibility = View.VISIBLE
                getDataBinding().rvMember.visibility = View.GONE
            } else {
                getDataBinding().ivEmpty.visibility = View.GONE
                getDataBinding().rvMember.visibility = View.VISIBLE
                memberListAdapter.submitList(it)
            }
            pauseLottie()
        }
    }
}