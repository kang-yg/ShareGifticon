package com.kyg.sharegifticon.model.repository

import io.realm.DynamicRealm
import io.realm.RealmMigration
import io.realm.RealmSchema

class RealmDBSMigration: RealmMigration {
    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        val schema: RealmSchema = realm.schema
    }
}