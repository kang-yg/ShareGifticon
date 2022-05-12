package com.kyg.sharegifticon.viewmodel

import com.google.firebase.auth.FirebaseUser
import com.kyg.sharegifticon.model.repository.MainRepository

class DrawerMenuViewModel(
    private val mainActivityViewModel: MainActivityViewModel
) {
    private val _currentUser = mainActivityViewModel.currentUser
    val currentUser: FirebaseUser?
        get() = this._currentUser
}