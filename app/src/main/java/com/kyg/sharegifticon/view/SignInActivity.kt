package com.kyg.sharegifticon.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.kyg.sharegifticon.R
import com.kyg.sharegifticon.databinding.ActivitySigninBinding
import com.kyg.sharegifticon.viewmodel.SignInActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInActivity : ShareGifticonBaseActivity<ActivitySigninBinding>() {
    private val signInActivityViewModel: SignInActivityViewModel by viewModels()
    private lateinit var signInLauncher: ActivityResultLauncher<Intent>
    private val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActivity()
        initBtGoogleSignIn()
        initSignInLauncher()
    }

    private fun initActivity() {
        initDataBinding(R.layout.activity_signin)
    }

    private fun initBtGoogleSignIn() {
        getDataBinding().btGoogleSignIn.setOnClickListener {
            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build()
            signInLauncher.launch(signInIntent)
        }
    }

    private fun initSignInLauncher() {
        signInLauncher =
            registerForActivityResult(FirebaseAuthUIActivityResultContract()) { firebaseAuthResult ->
                this.onSignInResult(firebaseAuthResult)
            }
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            val intent: Intent = Intent(this@SignInActivity, MainActivity::class.java)
            startActivityWithAnimation(intent)
            finish()
        } else {
            showPositiveAlertDialog(
                title = getString(R.string.SIGNIN_ALERT_DIALOG_TITLE),
                message = getString(R.string.SIGNIN_ALERT_DIALOG_MESSAGE)
            ) { dialog, _ -> dialog.cancel() }
        }
    }
}