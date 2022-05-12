package com.kyg.sharegifticon.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import androidx.lifecycle.MutableLiveData
import com.kyg.sharegifticon.Constants
import com.kyg.sharegifticon.model.GifticonStatus
import com.kyg.sharegifticon.model.ItemGifticon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class GifticonDetailFragmentViewModel @Inject constructor() : BaseViewModel() {
    private val _itemGifticon: MutableLiveData<ItemGifticon> = MutableLiveData()
    val itemGifticon: MutableLiveData<ItemGifticon>
        get() = this._itemGifticon
    lateinit var mBitmap: Bitmap

    fun deleteGifticon(itemGifticon: ItemGifticon) {
        val tempItemGifiticon: ItemGifticon? =
            _itemGifticon.value?.copy().apply { this?.status = GifticonStatus.DELETE }
        itemGifticon.let {
            with(firebaseDatabase.getReference(it.registrantKey)) {
                child(it.itemCategory.toString()).child(it.key).child(Constants.status)
                    .setValue(GifticonStatus.DELETE.toString())

                with(child(Constants.deleted).child(it.key)) {
                    child(Constants.imageUri).setValue(it.imageSrc.toString())
                    child(Constants.barcode).setValue(it.barcode)
                    child(Constants.expirationDate).setValue(it.expirationDate)
                    child(Constants.registrant).setValue(it.registrant)
                    child(Constants.status).setValue(GifticonStatus.DELETE.toString())
                    child(Constants.category).setValue(it.itemCategory)
                    child(Constants.registrantKey).setValue(it.registrantKey)
                    child(Constants.gifticonKey).setValue(it.key)
                }
            }
            tempItemGifiticon?.let {
                insertGifticonToRealmDatabase(tempItemGifiticon)
                _itemGifticon.postValue(tempItemGifiticon)
            }
        }
    }

    fun restoreGifticon(itemGifticon: ItemGifticon) {
        val tempItemGifiticon: ItemGifticon? =
            _itemGifticon.value?.copy().apply { this?.status = GifticonStatus.AVAILABLE }
        itemGifticon.let {
            with(firebaseDatabase.getReference(it.registrantKey)) {
                child(it.itemCategory.toString()).child(it.key).child(Constants.status).setValue(
                    GifticonStatus.AVAILABLE.toString()
                )

                with(child(Constants.deleted).child(it.key)) {
                    removeValue()
                }
            }
            tempItemGifiticon?.let {
                insertGifticonToRealmDatabase(tempItemGifiticon)
                _itemGifticon.postValue(tempItemGifiticon)
            }
        }
    }

    fun setBitMap() {
        _itemGifticon.value?.let { item ->
            val url: URL = stringToURL(item.imageSrc.toString())!!
            val connection: HttpURLConnection?
            try {
                connection = url.openConnection() as HttpURLConnection
                connection.connect()
                val inputStream: InputStream = connection.inputStream
                val bufferedInputStream = BufferedInputStream(inputStream)
                mBitmap =  BitmapFactory.decodeStream(bufferedInputStream)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun stringToURL(string: String): URL? {
        try {
            return URL(string)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return null
    }
}