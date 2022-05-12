package com.kyg.sharegifticon.model.repository

import com.kyg.sharegifticon.model.MyAlarm
import com.kyg.sharegifticon.model.api.FirebaseServiceHelper
import com.kyg.sharegifticon.model.api.RealmDBServiceHelper
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val firebaseServiceHelper: FirebaseServiceHelper,
    private val realmDBServiceHelper: RealmDBServiceHelper
) {
    fun getFirebaseStorage() = firebaseServiceHelper.getFirebaseStorage()
    fun getFirebaseStorageRef() = firebaseServiceHelper.getFirebaseStorageRef()
    fun getFirebaseDatabase() = firebaseServiceHelper.getFirebaseDatabase()
    fun getFirebaseAuth() = firebaseServiceHelper.getFirebaseAuth()
    fun getAuthUI() = firebaseServiceHelper.getAuthUI()

    fun insertGifticon(myAlarm: MyAlarm) = realmDBServiceHelper.insertGifticon(myAlarm)
    fun readGifticon(myAlarm: MyAlarm) = realmDBServiceHelper.readGifticon(myAlarm)
    fun readAllGifticons() = realmDBServiceHelper.readAllGifticons()
    fun deleteGifticon(myAlarm: MyAlarm) = realmDBServiceHelper.deleteGifticon(myAlarm)
    fun deleteAllGifticon() = realmDBServiceHelper.deleteAllGifticon()
}