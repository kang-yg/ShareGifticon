package com.kyg.sharegifticon.model

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

sealed class FirebaseEventResponse {
    data class Success(val snapshot: DataSnapshot): FirebaseEventResponse()
    data class Fail(val error: DatabaseError): FirebaseEventResponse()
}