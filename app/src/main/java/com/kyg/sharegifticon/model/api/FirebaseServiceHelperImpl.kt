package com.kyg.sharegifticon.model.api

import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import javax.inject.Inject

class FirebaseServiceHelperImpl @Inject constructor(
    private val firebase: Firebase,
    private val firebaseAuth: FirebaseAuth,
    private val authUI: AuthUI
) : FirebaseServiceHelper {
    override fun getFirebaseStorage(): FirebaseStorage = firebase.storage
    override fun getFirebaseStorageRef(): StorageReference = firebase.storage.reference
    override fun getFirebaseDatabase(): FirebaseDatabase = firebase.database
    override fun getFirebaseAuth(): FirebaseAuth = firebaseAuth
    override fun getAuthUI(): AuthUI = authUI
}