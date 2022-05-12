package com.kyg.sharegifticon.viewmodel

import androidx.lifecycle.MutableLiveData
import com.kyg.sharegifticon.Constants
import com.kyg.sharegifticon.Utile.setValueEventListener
import com.kyg.sharegifticon.model.FirebaseEventResponse
import com.kyg.sharegifticon.model.ItemGifticon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeletedGifticonFragmentViewModel @Inject constructor() : BaseViewModel() {
    private val _memberKeyList: ArrayList<String> = arrayListOf()
    val memberKeyList: ArrayList<String>
        get() = this._memberKeyList
    private val _itemKeyList: ArrayList<String> = arrayListOf()
    val itemKeyList
        get() = this._itemKeyList
    private val _itemList: MutableLiveData<HashSet<ItemGifticon>> = MutableLiveData()
    val itemList: MutableLiveData<HashSet<ItemGifticon>>
        get() = this._itemList

    fun readDataFromFirebaseDatabase() {
        clearLists()
        CoroutineScope(Dispatchers.IO).launch {
            readMemberFromFirebaseDatabase()
            readDeletedGifticonKey()
            readDeletedGifticon()
        }
    }

    private suspend fun readMemberFromFirebaseDatabase() {
        currentUser?.let { firebaseUser ->
            val result: FirebaseEventResponse =
                firebaseDatabase.getReference(firebaseUser.uid).child(Constants.member)
                    .setValueEventListener()

            when (result) {
                is FirebaseEventResponse.Success -> {
                    _memberKeyList.add(firebaseUser.uid)
                    result.snapshot.value?.let {
                        val memberKeyDataSnapshotHashMap: HashMap<String, String> =
                            it as HashMap<String, String>
                        for (member in memberKeyDataSnapshotHashMap.entries) {
                            _memberKeyList.add(member.key)
                        }
                    }
                }

                is FirebaseEventResponse.Fail -> {

                }
            }
        }
    }

    private suspend fun readDeletedGifticonKey() {
        _memberKeyList.forEach { memberKey ->
            val result: FirebaseEventResponse =
                firebaseDatabase.getReference(memberKey).child(Constants.deleted)
                    .setValueEventListener()

            when (result) {
                is FirebaseEventResponse.Success -> {
                    result.snapshot.value?.let {
                        val itemKeyDataSnapshotHashMap: HashMap<String, String> =
                            it as HashMap<String, String>
                        for (itemKey in itemKeyDataSnapshotHashMap.entries) {
                            _itemKeyList.add(itemKey.key)
                        }
                    }
                }

                is FirebaseEventResponse.Fail -> {

                }
            }
        }
    }

    private suspend fun readDeletedGifticon() {
        val tempItemList: HashSet<ItemGifticon> = hashSetOf()
        _memberKeyList.forEach { memberKey ->
            _itemKeyList.forEach { itemKey ->
                val result: FirebaseEventResponse =
                    firebaseDatabase.getReference(memberKey).child(Constants.deleted)
                        .child(itemKey)
                        .setValueEventListener()

                when (result) {
                    is FirebaseEventResponse.Success -> {
                        result.snapshot.value?.let {
                            val itemDataSnapshot: HashMap<String, String> =
                                it as HashMap<String, String>
                            val tempItem: ItemGifticon = ItemGifticon(key = itemKey)
                            for (item in itemDataSnapshot.entries) {
                                matchItemGifticonValue(tempItem, item)
                            }
                            tempItemList.add(tempItem)
                        }
                    }

                    is FirebaseEventResponse.Fail -> {

                    }
                }
            }
        }
        _itemList.postValue(tempItemList)
    }

    private fun clearLists() {
        _memberKeyList.clear()
        _itemKeyList.clear()
//        _itemList.value?.clear()
    }
}