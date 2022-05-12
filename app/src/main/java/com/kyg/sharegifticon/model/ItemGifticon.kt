package com.kyg.sharegifticon.model

import android.net.Uri
import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemGifticon(
    var key: String,
    var imageSrc: Uri = Uri.EMPTY,
    var barcode: String = "",
    var itemCategory: Category = Category.ETC,
    var registrant: String = "",
    var registrantKey: String = "",
    var expirationDate: String = "",
    var status: GifticonStatus = GifticonStatus.AVAILABLE,
): Parcelable, BaseModel

enum class GifticonStatus {
    AVAILABLE, EXPIRATION, USE_COMPLETED, DELETE
}

