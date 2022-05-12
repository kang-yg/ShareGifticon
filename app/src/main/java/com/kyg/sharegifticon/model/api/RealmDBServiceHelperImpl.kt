package com.kyg.sharegifticon.model.api

import com.kyg.sharegifticon.Constants
import com.kyg.sharegifticon.model.ItemGifticon
import com.kyg.sharegifticon.model.MyAlarm
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import javax.inject.Inject

class RealmDBServiceHelperImpl @Inject constructor(
    private val realmDatabase: Realm
) : RealmDBServiceHelper {
    override fun insertGifticon(myAlarm: MyAlarm) {
        realmDatabase.executeTransactionAsync {
            it.insertOrUpdate(myAlarm)
        }
    }

    override fun readGifticon(myAlarm: MyAlarm): MyAlarm? =
        realmDatabase.where<MyAlarm>().equalTo(Constants.gifticonKey, myAlarm.key).findFirst()

    override fun readAllGifticons(): RealmResults<MyAlarm> =
        realmDatabase.where<MyAlarm>().findAll()

    override fun deleteGifticon(myAlarm: MyAlarm) {
        realmDatabase.executeTransactionAsync {
            it.where<MyAlarm>().equalTo(Constants.gifticonKey, myAlarm.key).findAll().deleteAllFromRealm()
        }
    }

    override fun deleteAllGifticon() {
        realmDatabase.executeTransactionAsync {
            it.where<MyAlarm>().findAll().deleteAllFromRealm()
        }
    }
}