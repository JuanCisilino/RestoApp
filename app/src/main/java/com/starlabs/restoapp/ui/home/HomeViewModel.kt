package com.starlabs.restoapp.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.starlabs.restoapp.helpers.Prefs
import dagger.hilt.android.lifecycle.HiltViewModel

class HomeViewModel : ViewModel() {

    lateinit var prefs: Prefs

    fun setPrefs(context: Context){
        prefs = Prefs(context)
    }

    fun getRol() = prefs.getString("rol")
}