package com.kyg.sharegifticon.viewmodel

import androidx.lifecycle.MutableLiveData
import com.kyg.sharegifticon.Constants
import com.kyg.sharegifticon.Utile.setValueEventListener
import com.kyg.sharegifticon.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.CopyOnWriteArrayList

class BottomSheetItemViewModel(
    private val mainActivityViewModel: MainActivityViewModel
) {
    private val currentUser = mainActivityViewModel.currentUser
    private val firebaseDatabase = mainActivityViewModel.firebaseDatabase
    private lateinit var selectedCategory: String
    private val _memberKeyList: CopyOnWriteArrayList<String> = CopyOnWriteArrayList()
    val memberKeyList: CopyOnWriteArrayList<String>
        get() = this._memberKeyList
    private val _itemKeyList: CopyOnWriteArrayList<String> = CopyOnWriteArrayList()
    val itemKeyList: CopyOnWriteArrayList<String>
        get() = this._itemKeyList
    private val _itemList: MutableLiveData<HashSet<ItemGifticon>> = MutableLiveData()
    val itemList: MutableLiveData<HashSet<ItemGifticon>>
        get() = this._itemList

    fun setStorageRef(itemCategory: ItemCategory) {
        selectedCategory = itemCategory.category.toString()
    }

    fun readDataFromFirebaseDatabase() {
        _itemList.value = null
        CoroutineScope(Dispatchers.Main).launch {
            readMemberFromFirebaseDatabase()
            readGifticonKeysFromFirebaseDatabase()
            readGifticonsFromFirebaseDatabase()
        }
    }

    private suspend fun readMemberFromFirebaseDatabase() {
        _memberKeyList.clear()
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

    private suspend fun readGifticonKeysFromFirebaseDatabase() {
        _itemKeyList.clear()
        _memberKeyList.forEach { key ->
            val result: FirebaseEventResponse =
                firebaseDatabase.getReference(key).child(selectedCategory)
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

    private suspend fun readGifticonsFromFirebaseDatabase() {
        val tempItemList: HashSet<ItemGifticon> = hashSetOf()
        _memberKeyList.forEach { memberKey ->
            _itemKeyList.forEach { itemKey ->
                val result: FirebaseEventResponse =
                    firebaseDatabase.getReference(memberKey).child(selectedCategory)
                        .child(itemKey).setValueEventListener()

                when (result) {
                    is FirebaseEventResponse.Success -> {
                        result.snapshot.value?.let {
                            val itemDataSnapshotHashMap: HashMap<String, String> =
                                it as HashMap<String, String>
                            val tempItem = ItemGifticon(key = itemKey)
                            for (item in itemDataSnapshotHashMap.entries) {
                                mainActivityViewModel.matchItemGifticonValue(tempItem, item)
                            }
                            mainActivityViewModel.insertGifticonToRealmDatabase(tempItem)
                            if (tempItem.status == GifticonStatus.AVAILABLE) {
                                tempItemList.add(tempItem)
                            }
                        }
                    }

                    is FirebaseEventResponse.Fail -> {

                    }
                }
            }
        }
        _itemList.postValue(tempItemList)
    }

/*    fun insertGifticonToRealmDatabase(itemGifticon: ItemGifticon) {
        with(itemGifticon) {
            val mainRepository = mainActivityViewModel.mainRepository
            val tempMyAlarm = MyAlarm(key, expirationDate)
            if (status == GifticonStatus.AVAILABLE) {
                mainRepository.insertGifticon(tempMyAlarm)
            } else {
                mainRepository.deleteGifticon(tempMyAlarm)
            }
        }
    }*/
}