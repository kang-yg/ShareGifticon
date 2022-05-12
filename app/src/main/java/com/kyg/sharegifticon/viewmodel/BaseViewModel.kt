package com.kyg.sharegifticon.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.kyg.sharegifticon.Constants
import com.kyg.sharegifticon.model.*
import com.kyg.sharegifticon.model.repository.MainRepository
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {
    @Inject
    lateinit var mainRepository: MainRepository

    val firebaseStorage by lazy { mainRepository.getFirebaseStorage() }
    val firebaseStorageRef by lazy { mainRepository.getFirebaseStorageRef() }
    val firebaseDatabase by lazy { mainRepository.getFirebaseDatabase() }
    private val firebaseAuth by lazy { mainRepository.getFirebaseAuth() }
    val firebaseAuthUI by lazy { mainRepository.getAuthUI() }

    val currentUser by lazy { firebaseAuth.currentUser }

    fun matchItemGifticonValue(
        itemGifticon: ItemGifticon,
        entries: MutableMap.MutableEntry<String, String>
    ) {
        when (entries.key) {
            Constants.barcode -> {
                itemGifticon.barcode = entries.value
            }
            Constants.imageUri -> {
                itemGifticon.imageSrc = Uri.parse(entries.value)
            }
            Constants.expirationDate -> {
                itemGifticon.expirationDate = entries.value
            }
            Constants.registrant -> {
                itemGifticon.registrant = entries.value
            }
            Constants.status -> {
                itemGifticon.status = setGifticonStatusEnum(entries.value)
            }
            Constants.registrantKey -> {
                itemGifticon.registrantKey = entries.value
            }
            Constants.gifticonKey -> {
                itemGifticon.key = entries.value
            }
            Constants.category -> {
                itemGifticon.itemCategory = setCategoryEnum(entries.value)
            }
        }
    }

    private fun setGifticonStatusEnum(str: String): GifticonStatus {
        return when (str) {
            GifticonStatus.AVAILABLE.toString() -> {
                GifticonStatus.AVAILABLE
            }

            GifticonStatus.EXPIRATION.toString() -> {
                GifticonStatus.EXPIRATION
            }

            GifticonStatus.USE_COMPLETED.toString() -> {
                GifticonStatus.USE_COMPLETED
            }

            GifticonStatus.DELETE.toString() -> {
                GifticonStatus.DELETE
            }
            else -> {
                GifticonStatus.AVAILABLE
            }
        }
    }

    private fun setCategoryEnum(str: String): Category {
        return when (str) {
            Category.CAFE.toString() -> {
                Category.CAFE
            }
            Category.CONVENIENCE_STORE.toString() -> {
                Category.CONVENIENCE_STORE
            }
            Category.ETC.toString() -> {
                Category.ETC
            }
            else -> {
                Category.ETC
            }
        }
    }

    fun insertGifticonToRealmDatabase(itemGifticon: ItemGifticon) {
        with(itemGifticon) {
            val tempMyAlarm = MyAlarm(key, expirationDate)
            if (status == GifticonStatus.AVAILABLE) {
                mainRepository.insertGifticon(tempMyAlarm)
            } else {
                mainRepository.deleteGifticon(tempMyAlarm)
            }
        }
    }

    companion object {
        private val _itemCategoryList: ArrayList<ItemCategory> = arrayListOf()
        val itemCategoryList: ArrayList<ItemCategory>
            get() = _itemCategoryList
    }
}