package com.kyg.sharegifticon.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class MyAlarm() : RealmObject() {
    @PrimaryKey
    var key: String = ""
    var expirationDate: String = ""

    constructor(key: String, expirationDate: String): this() {
        this.key = key
        this.expirationDate = expirationDate
    }
}