package com.kyg.sharegifticon.model.api

import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

interface FirebaseServiceHelper {
    fun getFirebaseStorage(): FirebaseStorage
    fun getFirebaseStorageRef(): StorageReference
    fun getFirebaseDatabase(): FirebaseDatabase
    fun getFirebaseAuth(): FirebaseAuth
    fun getAuthUI(): AuthUI
}