package com.kyg.sharegifticon.model.di

import android.content.Context
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.kyg.sharegifticon.Constants
import com.kyg.sharegifticon.model.repository.RealmDBSMigration
import com.kyg.sharegifticon.model.api.FirebaseServiceHelper
import com.kyg.sharegifticon.model.api.FirebaseServiceHelperImpl
import com.kyg.sharegifticon.model.api.RealmDBServiceHelper
import com.kyg.sharegifticon.model.api.RealmDBServiceHelperImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFirebase(): Firebase = Firebase

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideAuthUI(): AuthUI = AuthUI.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseServiceHelper(firebaseServiceHelper: FirebaseServiceHelperImpl): FirebaseServiceHelper =
        firebaseServiceHelper

    @Singleton
    @Provides
    fun provideRealmDatabase(@ApplicationContext context: Context): Realm {
        Realm.init(context)
        val realmConfiguration =
            RealmConfiguration.Builder().name(Constants.realmDatabaseName)
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(0)
                .migration(RealmDBSMigration())
                .build()
        Realm.setDefaultConfiguration(realmConfiguration)
        return Realm.getDefaultInstance()
    }

    @Singleton
    @Provides
    fun provideRealmDatabaseHelper(realmDBServiceHelper: RealmDBServiceHelperImpl): RealmDBServiceHelper =
        realmDBServiceHelper
}