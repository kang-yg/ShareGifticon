package com.kyg.sharegifticon

import android.Manifest

object Constants {
    const val category = "카테고리"
    const val deleted = "삭제"
    const val myDateFormat = "yyyyMMdd"
    const val myDateFormatWithSeparator = "yyyy/MM/dd"
    const val barcode = "barcode"
    const val imageUri = "uri"
    const val expirationDate = "expirationDate"
    const val status = "상태"
    const val member = "멤버"
    const val uId = "uId"
    const val name = "이름"
    const val registrant = "registrant"
    const val registrantKey = "registrantKey"
    const val gifticonKey = "key"

    const val permissionRequestCode = 9999
    val permissionList: ArrayList<String> =
        arrayListOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)

    const val channelIdExpirationDate = "ExpirationDate"
    const val channelNameExpirationDate = "ExpirationDate Info"
    const val notificationIdExpirationDate = 0
    const val parcelableItemGifticon = "ItemGifticon"
    const val realmDatabaseName = "AlarmDB"

    const val mime_text = "text/plain"
    const val mime_image = "image/jpg"
    const val image_sffix = ".jpg"
}