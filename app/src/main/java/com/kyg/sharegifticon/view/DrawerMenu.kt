package com.kyg.sharegifticon.view

import android.content.Intent
import com.kyg.sharegifticon.Constants
import com.kyg.sharegifticon.R
import com.kyg.sharegifticon.databinding.NavigationBinding
import com.kyg.sharegifticon.model.repository.MainRepository
import com.kyg.sharegifticon.viewmodel.DrawerMenuViewModel
import com.kyg.sharegifticon.viewmodel.MainActivityViewModel

class DrawerMenu(
    private val activity: MainActivity,
    private val mainActivityViewModel: MainActivityViewModel
) {
    private val drawerMenuViewModel: DrawerMenuViewModel = DrawerMenuViewModel(mainActivityViewModel)
    val deletedGifticonFragment: DeletedGifticonFragment = DeletedGifticonFragment()

    fun ivMenuCloseClick() {
        activity.getDataBinding().dlMain.close()
    }

    fun tvMenuAddGifticonClick() {
        with(activity) {
            val intent: Intent = Intent(this, AddGigticonActivity::class.java)
            startActivityWithAnimation(intent)
        }
    }

    fun tvManageMemberClick() {
        with(activity) {
            val intent: Intent = Intent(this, ManageMemberActivity::class.java)
            startActivityWithAnimation(intent)
        }
    }

    fun tvMenuLogoutClick() {
        mainActivityViewModel.firebaseAuthUI.signOut(activity).addOnCompleteListener {
            val intent: Intent = Intent(activity, SignInActivity::class.java)
            activity.startActivityWithAnimation(intent)
            activity.finish()
        }
    }

    fun tvSendMyUidClick() {
        drawerMenuViewModel.currentUser?.let { firebaseUser ->
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, firebaseUser.uid)
                type = Constants.mime_text
            }
            val shareIntent: Intent = Intent.createChooser(sendIntent, null)
            activity.startActivity(shareIntent)
        }
    }

    fun tvDeletedGifticonClick() {
        with(activity) {
            supportFragmentManager.beginTransaction().add(R.id.flMain, deletedGifticonFragment)
                .addToBackStack(null).commit()
            getDataBinding().dlMain.close()
        }
    }

    fun ivManageAlarmClick() {
        ManageAlarmDialogFragment(activity, mainActivityViewModel).show(
            activity.supportFragmentManager,
            ""
        )
    }

    init {
        activity.getDataBinding().mainNavigation.drawerMenu = this
    }
}