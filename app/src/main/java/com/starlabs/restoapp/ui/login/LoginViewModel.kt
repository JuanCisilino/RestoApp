package com.starlabs.restoapp.ui.login

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthCredential
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
    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    lateinit var prefs: Prefs

    fun setPrefs(context: Context){
        prefs = Prefs(context)
    }

    fun isOpenSession() {
        val email = prefs.getString("email")
        email?.let { getUser(it) }
    }

    private fun createUser(newuser: User){
        uiScope.launch {
            loadStateLiveData.postValue(LoadState.Loading)
            try {
                val user = loginUC.createUser(newuser)
                user?.let {
                    saveUserPrefs(user)
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
                newuser.error = ErrorResponse(true, e.message)

                loadStateLiveData.postValue(LoadState.Error)
                userLiveData.postValue(newuser)
            }
        }
    }

    private fun saveUserPrefs(user: User){
        prefs.save("email", user.email?:"")
        prefs.save("rol", user.rol?:"user")
    }

    fun singInWithCredentials(credential: AuthCredential){
        uiScope.launch {
            try {
                loginUC.signInWithCredentials(credential).addOnCompleteListener {
                        if (it.isSuccessful){
                            val user = User().convert(it.result.user!!)
                            if (user.rol.isNullOrEmpty()){
                                createUser(user)
                            } else {
                                getUser(user.email!!)
                            }
                        }else {
                            userLiveData.postValue(null)
                        }
                    }
            } catch (e: Exception){
                userLiveData.postValue(null)
            }
        }
    }

    fun getCurrentUser() = loginUC.getCurrentUser()

    fun getUser(email: String) {
        uiScope.launch {
            loadStateLiveData.postValue(LoadState.Loading)
            try {
                loginUC.newGetUser(email).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val user = it.result.toObject(User::class.java)
                        user
                            ?.let {
                                saveUserPrefs(user)
                                loginUC.setCurrentUser(user)
                                loadStateLiveData.postValue(LoadState.Success)
                            }
                            ?:run { loadStateLiveData.postValue(LoadState.Error) }

                    } else {
                        loadStateLiveData.postValue(LoadState.Error)
                    }
                }
            } catch (e: Exception) {
                loadStateLiveData.postValue(LoadState.Error)
            }
        }
    }


}