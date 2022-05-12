package com.kyg.sharegifticon.model.api

import com.kyg.sharegifticon.model.ItemGifticon
import com.kyg.sharegifticon.model.MyAlarm
import io.realm.RealmResults

interface RealmDBServiceHelper {
    fun insertGifticon(myAlarm: MyAlarm)
    fun readGifticon(myAlarm: MyAlarm): MyAlarm?
    fun readAllGifticons(): RealmResults<MyAlarm>
    fun deleteGifticon(myAlarm: MyAlarm)
    fun deleteAllGifticon()
}