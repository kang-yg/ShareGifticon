package com.kyg.sharegifticon

import android.content.Context
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.kyg.sharegifticon.model.FirebaseEventResponse
import com.kyg.sharegifticon.model.ItemGifticon
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object Utile {
    fun Int.toPx(context: Context): Int = (this * context.resources.displayMetrics.density).toInt()

    suspend fun DatabaseReference.setValueEventListener(): FirebaseEventResponse =
        suspendCoroutine<FirebaseEventResponse> { continuation ->
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    continuation.resume(FirebaseEventResponse.Success(snapshot))
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resume(FirebaseEventResponse.Fail(error))
                }
            }
            addListenerForSingleValueEvent(valueEventListener)
        }

    fun HashSet<ItemGifticon>.getSameItem(itemGifticon: ItemGifticon): ItemGifticon? {
        var result: ItemGifticon? = null
        this.forEach { item ->
            if (item.key == itemGifticon.key) {
                result = item.copy()
            }
        }
        return result
    }
}