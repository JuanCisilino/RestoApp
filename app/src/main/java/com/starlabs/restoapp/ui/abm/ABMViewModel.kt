package com.starlabs.restoapp.ui.abm

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.starlabs.restoapp.model.MenuItem
import com.starlabs.restoapp.uc.ABMUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ABMViewModel @Inject constructor(private val abmUC: ABMUC): ViewModel() {


    var menuItemLiveData = MutableLiveData<MenuItem?>()
    var saveProductLiveData = MutableLiveData<MenuItem?>()
    var updatedProductLiveData = MutableLiveData<Unit?>()
    var imageProductLiveData = MutableLiveData<Unit?>()
    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var uri : Uri?=null
    var file : File?=null
    private lateinit var menuItems: List<MenuItem>

    fun init(){
        uiScope.launch {
            try {
                abmUC.getMenuItems()
                    ?.let { menuItems = it }
                    ?:run { menuItemLiveData.postValue(null) }
            } catch (e: Exception) {
                menuItemLiveData.postValue(null)
            }
        }
    }

    fun getMenuItem(id: String){
        menuItemLiveData.postValue(menuItems.find { it.itemId == id })
    }

    fun saveMenuItem(menuItem: MenuItem){
        uiScope.launch {
            try {
                abmUC.createItem(menuItem.toItem())
                menuItemLiveData.postValue(menuItem)
            } catch (e: Exception) {
                menuItemLiveData.postValue(null)
            }
        }
    }

    fun saveImage(name: String, uri: Uri){
        uiScope.launch {
            try {
                abmUC.saveToDB(name, uri)
                imageProductLiveData.postValue(Unit)
            } catch (e: Exception) {
                imageProductLiveData.postValue(null)
            }
        }
    }

    fun updateMenuItem(menuItem: MenuItem){
        uiScope.launch {
            try {
                abmUC.createItem(menuItem.toItem())
                menuItemLiveData.postValue(menuItem)
            } catch (e: Exception) {
                menuItemLiveData.postValue(null)
            }
        }
    }
}