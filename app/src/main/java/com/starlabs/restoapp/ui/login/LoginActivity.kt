package com.starlabs.restoapp.ui.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.starlabs.restoapp.databinding.ActivityLoginBinding
import com.starlabs.restoapp.helpers.LoadState
import com.starlabs.restoapp.helpers.LoadingDialog
import com.starlabs.restoapp.helpers.showAlert
import com.starlabs.restoapp.model.User
import com.starlabs.restoapp.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var binding: ActivityLoginBinding
    private var loadingDialog = LoadingDialog()
    private var isOpen = false

    companion object {
        const val GOOGLE_SIGN_IN = 100
        fun start(activity: Activity){
            activity.startActivity(Intent(activity, LoginActivity::class.java))
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.setPrefs(this)
        binding.googleButton.setOnClickListener { setGoogleWidget() }
        subscribeToLiveData()
        checkSession()
    }

    private fun subscribeToLiveData() {
        viewModel.userLiveData.observe(this) { handleUser(it) }
        viewModel.loadStateLiveData.observe(this) { handleLoadingState(it) }
    }

    private fun handleUser(user: User?) {
        user?.let { MainActivity.start(this) }
            ?:run { showAlert() }
    }

    private fun handleLoadingState(state: LoadState) {
        when (state) {
            LoadState.Loading -> { loadingDialog.show(supportFragmentManager) }
            LoadState.Success -> {
                if (isOpen) MainActivity.start(this)
                isOpen = false
                loadingDialog.dismiss() }
            else -> { loadingDialog.dismiss() }
        }
    }
    private fun checkSession() {
        viewModel.isOpenSession()
        isOpen = true
    }

    private fun setGoogleWidget() {
        val googleConfig = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("352494981664-qhmp6tvk5nf82mnfup3oghsjhhlc5rpd.apps.googleusercontent.com")
            .requestEmail()
            .build()
        val googleClient = GoogleSignIn.getClient(this, googleConfig)
        googleClient.signOut()
        startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = task.getResult(ApiException::class.java)
                account?.let { account ->
                    viewModel.singInWithCredentials(GoogleAuthProvider.getCredential(account.idToken, null))
                }
            }catch (e: ApiException){
                showAlert()
            }
        }
    }
}