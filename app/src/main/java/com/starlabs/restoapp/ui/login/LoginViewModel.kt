package com.starlabs.restoapp.ui.login

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.starlabs.restoapp.helpers.LoadState
import com.starlabs.restoapp.helpers.Prefs
import com.starlabs.restoapp.model.ErrorResponse
import com.starlabs.restoapp.model.User
import com.starlabs.restoapp.uc.LoginUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUC: LoginUC): ViewModel(){

    val userLiveData = MutableLiveData<User?>()
    var loadStateLiveData = MutableLiveData<LoadState>()
    var currentUser: User? = loginUC.getCurrentUser()
    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    lateinit var prefs: Prefs

    fun setPrefs(context: Context){
        prefs = Prefs(context)
    }

    fun isOpenSession(): Boolean {
        val email = prefs.getString("email")
        return if (!email.isNullOrEmpty()){
            getUser(email)
            true
        } else {
            false
        }
    }

    private fun createUser(user: User){
        uiScope.launch {
            loadStateLiveData.postValue(LoadState.Loading)
            try {
                val user = loginUC.createUser(user)
                user?.let {
                    prefs.save("email", it.email?:"")
                    prefs.save("rol", it.rol?:"user")

                    loadStateLiveData.postValue(LoadState.Success)
                    userLiveData.postValue(it)
                }
                    ?:run {
                        val errorUser = User()
                        errorUser.error = ErrorResponse(true, "roto")

                        loadStateLiveData.postValue(LoadState.Error)
                        userLiveData.postValue(errorUser)
                    }

            } catch (e: Exception) {
                user.error = ErrorResponse(true, e.message)

                loadStateLiveData.postValue(LoadState.Error)
                userLiveData.postValue(user)
            }
        }
    }

    fun singInWithCredentials(credential: AuthCredential){
        uiScope.launch {
            try {
                loginUC.signInWithCredentials(credential).addOnCompleteListener {
                        if (it.isSuccessful){
                            val user = User().convert(it.result.user!!)
                            currentUser = user
                            if (user.email.isNullOrEmpty()){
                                createUser(user)
                            }
                            userLiveData.postValue(user)
                        }else {
                            userLiveData.postValue(null)
                        }
                    }
            } catch (e: Exception){
                userLiveData.postValue(null)
            }
        }
    }

    fun getCurrentUser(){
        currentUser = loginUC.getCurrentUser()
    }
    fun getUser(email: String) {
        uiScope.launch {
            loadStateLiveData.postValue(LoadState.Loading)
            try {
                val user = loginUC.getUser(email)
                user?.let {
                    prefs.save("rol", it.rol?:"user")
                    loadStateLiveData.postValue(LoadState.Success)
                }
                    ?:run { loadStateLiveData.postValue(LoadState.Error) }
            } catch (e: Exception) {
                loadStateLiveData.postValue(LoadState.Error)
            }
        }
    }


}