package com.starlabs.restoapp.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import com.starlabs.restoapp.helpers.Prefs
import com.starlabs.restoapp.uc.LoginUC
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val loginUC: LoginUC): ViewModel()  {


    lateinit var prefs: Prefs

    fun setPrefs(context: Context){
        prefs = Prefs(context)
    }

    fun getRol(): String?{
        val rol = prefs.getString("rol")
        return rol
    }
    fun signOut(context: Context) {
        Prefs(context).clear()
        loginUC.signOut()
    }


}