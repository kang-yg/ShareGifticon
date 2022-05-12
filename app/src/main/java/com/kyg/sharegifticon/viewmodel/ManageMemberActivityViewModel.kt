package com.kyg.sharegifticon.viewmodel

import androidx.lifecycle.MutableLiveData
import com.kyg.sharegifticon.Constants
import com.kyg.sharegifticon.Utile.setValueEventListener
import com.kyg.sharegifticon.model.FirebaseEventResponse
import com.kyg.sharegifticon.model.Member
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageMemberActivityViewModel @Inject constructor() : BaseViewModel() {
    private val _memberList: MutableLiveData<ArrayList<Member>> = MutableLiveData()
    val memberList: MutableLiveData<ArrayList<Member>>
        get() = _memberList

    fun writeFirebaseDatabaseMemberInfo(name: String, uId: String) {
        currentUser?.let {
            val ref = firebaseDatabase.getReference(it.uid).child(Constants.member).child(uId)
            ref.child(Constants.name).setValue(name)
            ref.child(Constants.uId).setValue(uId)
        }
    }

    fun readMember() {
        CoroutineScope(Dispatchers.Main).launch {
            readFirebaseDatabaseMemberInfo()
        }
    }

    private suspend fun readFirebaseDatabaseMemberInfo() {
        val tempMemberList: ArrayList<Member> = arrayListOf()
        currentUser?.let { firebaseUser ->
            val result: FirebaseEventResponse =
                firebaseDatabase.getReference(firebaseUser.uid).child(Constants.member)
                    .setValueEventListener()
            when (result) {
                is FirebaseEventResponse.Success -> {
                    result.snapshot.value?.let {
                        val memberKeyDataSnapshotHashMap: HashMap<String, String> =
                            it as HashMap<String, String>
                        for (memberKey in memberKeyDataSnapshotHashMap.entries) {
                            val tempMember: Member = Member()
                            val memberInfoDataSnapshotHashMap: HashMap<String, String> =
                                memberKey.value as HashMap<String, String>
                            for (member in memberInfoDataSnapshotHashMap) {
                                matchItemMemberValue(tempMember, member)
                            }
                            tempMemberList.add(tempMember)
                        }
                    }
                    _memberList.postValue(tempMemberList)
                }

                is FirebaseEventResponse.Fail -> {

                }
            }
        }
    }

    private fun matchItemMemberValue(
        member: Member,
        entries: MutableMap.MutableEntry<String, String>
    ) {
        when (entries.key) {
            Constants.uId -> {
                member.uId = entries.value
            }

            Constants.name -> {
                member.uName = entries.value
            }
        }
    }

    fun removeMember(member: Member) {
        currentUser?.let {
            firebaseDatabase.getReference(it.uid).child(Constants.member).child(member.uId)
                .removeValue()
        }
    }
}