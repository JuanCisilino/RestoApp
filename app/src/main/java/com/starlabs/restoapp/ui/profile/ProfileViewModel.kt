package com.starlabs.restoapp.ui.profile

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.starlabs.restoapp.helpers.LoadState
import com.starlabs.restoapp.helpers.Prefs
import com.starlabs.restoapp.model.User
import com.starlabs.restoapp.uc.LoginUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val loginUC: LoginUC): ViewModel() {

    val userLiveData = MutableLiveData<User?>()
    val loadStateLiveData = MutableLiveData<LoadState>()
    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private lateinit var prefs: Prefs

    fun setPrefs(context: Context){
        prefs = Prefs(context)
    }

    fun getUser() {
        val email = prefs.getString("email")
        if (!email.isNullOrEmpty()){
            newGetUser(email)
        } else {
            userLiveData.postValue(null)
        }
    }

    private fun getUser(email: String) {
        uiScope.launch {
            loadStateLiveData.postValue(LoadState.Loading)
            try {
                val user = loginUC.getUser(email)
                user?.let {
                    userLiveData.postValue(it)
                    loadStateLiveData.postValue(LoadState.Success)
                }
                    ?:run { loadStateLiveData.postValue(LoadState.Error) }
            } catch (e: Exception) {
                loadStateLiveData.postValue(LoadState.Error)
            }
        }
    }

    private fun newGetUser(email: String) {
        uiScope.launch {
            try {
                loginUC.newGetUser(email).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val user = it.result.toObject(User::class.java)
                        loginUC.setCurrentUser(user!!)
                        userLiveData.postValue(user)
                        loadStateLiveData.postValue(LoadState.Success)
                    } else {
                        loadStateLiveData.postValue(LoadState.Error)
                    }
                }
            } catch (e: Exception) {
                loadStateLiveData.postValue(LoadState.Error)
            }
        }
    }
    fun signOut() {
        prefs.clear()
        loginUC.signOut()
    }
}