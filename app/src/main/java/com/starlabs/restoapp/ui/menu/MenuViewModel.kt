package com.starlabs.restoapp.ui.menu

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.starlabs.restoapp.helpers.Prefs
import com.starlabs.restoapp.model.MenuItem
import com.starlabs.restoapp.uc.ABMUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(private val abmuc: ABMUC): ViewModel() {

    val menuLiveData = MutableLiveData<List<MenuItem>?>()
    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    lateinit var prefs: Prefs

    fun isAdmin() = prefs.getString("rol") == "admin"
    fun init(context: Context){
        prefs = Prefs(context)
        uiScope.launch {
            try {
                abmuc.getMenuItems()
                    ?.let { menuLiveData.postValue(it) }
                    ?:run { menuLiveData.postValue(null) }
            } catch (e: Exception) {
                menuLiveData.postValue(null)
            }
        }
    }
}